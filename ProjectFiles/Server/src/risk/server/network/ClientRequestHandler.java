package risk.server.network;

import risk.common.entities.Continent;
import risk.common.entities.Country;
import risk.common.entities.Player;
import risk.common.entities.UnitCard;
import risk.common.interfaces.RiskInterface;
import risk.common.exceptions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

/**
 * Handles incoming client requests and manages communication between clients and the game server.
 * Implements the Runnable interface to run in a separate thread for concurrent handling of client requests.
 */
public class ClientRequestHandler implements Runnable{
    private RiskInterface risk;
    private GameServer server;
    private boolean stop = false;
    private boolean isRunning;
    private Player player = null;
    private UUID clientID;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructs a ClientRequestHandler2 object.
     * Initializes the handler with the provided socket, RiskInterface, and GameServer2 instances.
     *
     * @param socket The socket connected to the client.
     * @param risk The RiskInterface instance managing game logic.
     * @param server The GameServer2 instance handling server operations.
     */
    public ClientRequestHandler(Socket socket, RiskInterface risk, GameServer server){
        this.risk =risk;
        this.socket = socket;
        this.server = server;
        this.isRunning = false;
        this.clientID = UUID.randomUUID();
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            //stopListening();
        }
    }

    /**
     * Retrieves the unique client ID assigned to this handler.
     *
     * @return The UUID representing the client ID.
     */
    public UUID getClientID(){
        return clientID;
    }

    /**
     * Retrieves the Player associated with this client handler.
     *
     * @return The Player object associated with the client.
     */
    public Player getPlayer(){
        return this.player;
    }

    /**
     * Stops listening to client requests and closes associated resources.
     */
    public void stopListening(){
        isRunning = false;
        try{
            if (in != null) in.close();
            if(out != null) out.close();
            if(socket != null) socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Handles client requests on a separate thread.
     * Listens for incoming messages from the client, processes them, and handles disconnection requests.
     *
     * Note: This method runs continuously until the client disconnects.
     */

    @Override
    public void run(){
        System.out.println("client request handler is running");
        System.out.println("ClientRequestHandler2 is running on thread: " + Thread.currentThread().getId());
        try{
            String message ;
            while ((message = in.readLine()) != null) {
                if(message.equals("disconnect")){
                    System.out.println("Client disconnected");
                    server.removeClient(this);
                    break;
                }
                System.out.println("From Client :" + message);
                handleClientMessage(message);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            stopListening();
        }
    }

    /**
     * Stops the listener.
     *
     * @return true indicating that the listener has been stopped.
     */
    private boolean stop(){
        System.out.println("listener stopped");
        return stop=true;
    }

    /**
     * Starts the listener.
     *
     * @return false indicating that the listener has been started.
     */
    private boolean start(){
        System.out.println("listener started");
        return stop=false;
    }

    /**
     * Handles messages received from the client and processes corresponding commands.
     *
     * @param message The message received from the client.
     */
    private  void handleClientMessage(String message) {
        String[] tokens = message.split(" ");
        String command = tokens[0];
        System.out.println("Command " + command);

        switch (command){
            case "addPlayer":
                if (tokens.length > 2){
                    String name = tokens[1];
                    String color = tokens[2];
                    addPlayer(name, color);
                    server.broadcastMessage("UPDATE_PLAYERS:");
                }else{
                    out.println("Player not found");
                }
                break;

            case "removePlayer":
                if (tokens.length > 1) {
                    String name = tokens[1];
                    removePlayer(name);
                    //server.broadcastMessage("playerRemoved");
                } else {
                    out.println("Player not found");
                    System.out.println("Player not found");
                }
                break;

            case "getPlayers":
                playerList();
                break;

            case "startGame":
                try{
                    int playerId = Integer.parseInt(in.readLine());
                    server.addIsReady(playerId);
                    System.out.println(server.isReady(playerId));
                }catch (Exception e){
                    System.out.println("Error starting game");
                }
                boolean allReady = false;
                for (int i=0; i<risk.getPlayers().size(); i++){
                    if (server.getIsReady()[i]){
                        allReady = true;
                        System.out.println("Player " + i + " is ready");
                    }else{
                        allReady = false;
                        System.out.println("Player " + i + " is not ready");
                        break;
                    }
                }
                System.out.println("All ready: " + allReady);

                if (allReady){
                    risk.startGame();
                    //out.println("GAME_STARTED:Game started");
                    server.broadcastMessage("GAME_STARTED:");
                    sendCountryData();
                    removeAllPlayersReady();
                    sendPlayerOnTurn();
                    allReady = false;
                }


                break;

            case "makeAttack":
                if (tokens.length == 5){
                    String fromCountry = tokens[1];
                    String toCountry   = tokens[2];
                    String attacker    = tokens[3];
                    int numberOfUnits  = Integer.parseInt(tokens[4]);
                    try {
                        int defender = risk.getCountryByName(toCountry).getPlayer().getId();
                        server.broadcastMessage("GETTING_ATTACKED: " + fromCountry + " " + toCountry + " " + attacker + " " + numberOfUnits + " " + defender);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //makeAttack(fromCountry, toCountry, attacker, numberOfUnits);
                }
                break;

            case "sendCountries":
                sendPlayerCountries(tokens[1]);
                break;

            case "sendCountryData":
                System.out.println("Sending country data");
                sendCountryData();
                break;

            case "sendMissionCard":
                sendMissionCard(tokens[1]);
                break;

            case "sendUnitCards":
                sendUnitCards(tokens[1]);
                break;

            case "addPlayerUnitsPerRound":
                if (this.player.getName().equals(risk.getPlayerOnTurn().getName())) {
                    risk.addPlayerUnitsPerRound(risk.getPlayerOnTurn());
                    out.println("ADD_PLAYER_UNITS:");
                    out.println(risk.getPlayerOnTurn().getNumberOfUnits());
                }
                System.out.println(risk.getPlayerOnTurn().getNumberOfUnits());
                break;

            case "addPlayerUnitsToCountry":
                try {
                    risk.addPlayerUnitsToCountry(tokens[1], tokens[2], Integer.parseInt(tokens[3]));
                    risk.distributeStartingUnits();
                    out.println("ADD_PLAYER_UNITS_TO_COUNTRY: Success" + " " + risk.getPlayerOnTurn().getNumberOfUnits());
                    Thread.sleep(1000);
                    sendCountryData();
                } catch (Exception e) {
                    out.println("ADD_PLAYER_UNITS_TO_COUNTRY: An error occurred");
                }
                break;

            case "moveUnits" :
                try{
                    System.out.println(risk.moveUnits(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4])));
                    out.println("MOVE_UNITS: " + tokens[1]  + tokens[2]  + tokens[3]  + tokens[4]);
                    Thread.sleep(1000);
                    sendCountryData();
                } catch (Exception e) {
                    out.println("MOVE_UNITS: An error occurred");
                }
                break;

            default:
                out.println("command not found");
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message){
        out.println(message);
    }

    /**
     * Adds a new player to the game.
     *
     * @param name The name of the player to add.
     * @param color The color associated with the player.
     */
    public void addPlayer(String name, String color){
        Player player = null;
        try {
            player = risk.addPlayer(name, server.playerID, color);
            this.player = player;
            System.out.println("Server: Player created");
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.playerID++;
        if (player != null){
            server.addPlayer(player);
            out.println("PLAYER_ADDED:" + " " + name + " " + player.getId() + " " + color);    //send "successfull" message to client
            System.out.println("PLAYER_ADDED:" + " " + name + " " + player.getId() + " " + color);
//            out.println(name);
//            out.println(player.getId()); //send player id to client to keep track of their player id
//            out.println(color);
            System.out.println("Player ID sent to client");
        }else{
            out.println("PLAYER_NOT_ADDED:Player not added");
        }
    }

    /**
     * Removes a player from the game.
     *
     * @param name The name of the player to remove.
     */
    public void removePlayer(String name){
        Player player = risk.getPlayerByName(name);
        if (player != null) {
            risk.removePlayer(name);
            out.println("playerRemoved");
            System.out.println("playerRemoved");
        } else {
            out.println("Player not found");
            System.out.println("Player not found");
        }
    }

    /**
     * Sends country objects associated with a player to the client.
     *
     * @param name The name of the player for which country objects are sent.
     */
    public void sendPlayerCountries(String name){
        for (Player player : risk.getPlayers()){
            if (player.getName().equals(name)){
                out.println("COUNTRY_OBJECTS:");
                out.println(player.getCountries().size());
                for (Country country : player.getCountries()){
                    out.println(country.getName());
                    out.println(country.getShortName());
                    out.println(country.getUnits());

                }
            }
        }
    }

    /**
     * Initiates an attack from one country to another.
     *
     * @param fromCountry The name of the attacking country.
     * @param toCountry The name of the defending country.
     * @param attacker The name of the player initiating the attack.
     * @param numberOfUnits The number of units used in the attack.
     */
    public void makeAttack(String fromCountry, String toCountry, String attacker, int numberOfUnits){
        System.out.println("now in makeAttack method CRH");

        if (fromCountry == null || toCountry == null || attacker == null || numberOfUnits <= 0){
            out.println("Invalid input parameters for makeAttack");
            return;
        }
        try {
            Country _fromCountry = risk.getCountryByName(fromCountry);
            Country _toCountry = risk.getCountryByName(toCountry);
            Player _attackingPlayer = risk.getCountryByName(attacker).getPlayer();
            Player _defender = _toCountry.getPlayer();
            int _defenderUnits = _toCountry.getUnits();

            System.out.println("_fromCountry : " + _fromCountry);
            System.out.println("_toCountry : " + _toCountry);
            System.out.println("_attackingPlayer : " + _attackingPlayer.getName());
            System.out.println("_defender : " + _defender.getName());
            System.out.println("_defenerUnits : " + _defenderUnits);

            //risk.makeAttack(fromCountry, toCountry, attacker, numberOfUnits);

            if (_defender != null){
                String defenderName = _defender.getName();
                //Notify client about the attack
                String attackMessage = "attackNotification " + fromCountry + " " + toCountry + " " + attacker + " " + defenderName + " " + numberOfUnits;
                server.broadcastMessage(attackMessage);
                out.println("Attack successful");
            }else{
                out.println("No Defender found in country !" + toCountry);
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
            out.println("IllegalStateException occured: " + e.getMessage());
        } catch (CountryNotFoundException e) {
            e.printStackTrace();
            out.println("CountryNotFoundException  occured: " + e.getMessage());
        } catch (Exception  e) {
            e.printStackTrace();
            out.println("An unexpected error occured: " + e.getMessage());
        }
    }

    /**
     * Sets a player as ready.
     *
     * @param name The name of the player to set as ready.
     */
    private void setPlayerReady(String name){
        for (Player player : risk.getPlayers()){
            if (player.getName().equals(name)){
                server.getIsReady()[player.getId()] = true;
            }
        }
    }

    /**
     * Resets the readiness status of all players.
     * Sets all players as not ready.
     */
    private void removeAllPlayersReady(){
        for (int i = 0; i < server.getIsReady().length; i++){
            server.getIsReady()[i] = false;
        }
    }

    /**
     * Checks if all players are ready.
     * Prints the readiness status of each player and whether all players are ready.
     */
    private void checkAllPlayersReady(){
        boolean allReady = false;
        for (int i=0; i<risk.getPlayers().size(); i++){
            if (server.getIsReady()[i]){
                allReady = true;
                System.out.println("Player " + i + " is ready");
            }else{
                allReady = false;
                System.out.println("Player " + i + " is not ready");
                break;
            }
        }
        System.out.println("All ready: " + allReady);
    }

    /**
     * Sends country data to all connected clients.
     * Sends information about each country, including its name, owner, units, continents, and neighbors.
     */
    public void sendCountryData(){
        server.broadcastMessage("COUNTRY_DATA:");
        server.broadcastMessage(String.valueOf(risk.getCountries().size()));            //Send the number of countries
        //System.out.println(risk.getCountries().size());
        for (Country country : risk.getCountries()){        //Send the country data
            server.broadcastMessage(country.getName());
            // System.out.println(country.getName());
            server.broadcastMessage(country.getPlayer().getName());
            // System.out.println(country.getPlayer().getName());
            server.broadcastMessage(String.valueOf(country.getUnits()));
            //System.out.println(country.getUnits());
            for (Continent continent : risk.getContinents()){           //Send the continent data
                if (continent.getCountries().contains(country)){
                    server.broadcastMessage(continent.getName());
                    // System.out.println("Listing Continents" + continent.getName());
                }
            }
            server.broadcastMessage(String.valueOf(country.getNeighbors().size()));             //Send the number of neighbors
            //System.out.println(country.getNeighbors().size());
            for (Country neighbor : country.getNeighbors()){
                server.broadcastMessage(neighbor.getName());
                // System.out.println(neighbor.getName());
            }
        }

    }

    /**
     * Sends a mission card description to the specified player.
     *
     * @param player The name of the player to send the mission card description.
     */
    public void sendMissionCard(String player){
        if (risk.getPlayerByName(player) != null) {
            out.println(risk.getPlayerByName(player).getMissionCard().getDescription());
        }
    }

    /**
     * Sends unit cards to the specified player.
     *
     * @param player The name of the player to send unit cards.
     */
    public void sendUnitCards(String player) {
        if (risk.getPlayerByName(player) != null) {
            out.println(risk.getPlayerByName(player).getUnitCards().size());
            for (UnitCard card : risk.getPlayerByName(player).getUnitCards()) {
                out.println(card);
            }
        }
    }


    /**
     * Sends the list of players to the client.
     * Sends each player's name, ID, and color.
     */
    public void playerList(){
        out.println("PLAYER_LIST:");
        out.println(risk.getPlayers().size());
        for (Player player : risk.getPlayers()){
            out.println(player.getName() + " " + player.getId() + " "+ player.getColor());
        }
    }

    /**
     * Sends information about the player currently taking their turn to all clients.
     * Sends the ID of the player who is currently on turn.
     */
    public void sendPlayerOnTurn(){
        server.broadcastMessage("PLAYER_ON_TURN:" + " " + risk.getPlayerOnTurn().getId());
        System.out.println("PLAYER_ON_TURN:" + " " + risk.getPlayerOnTurn().getId());
    }
}
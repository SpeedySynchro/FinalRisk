package risk.client.network;

import risk.common.entities.*;
import risk.common.entities.missions.MissionCard;
import risk.common.exceptions.CountryNotFoundException;
import risk.common.exceptions.InvalidUnitException;
import risk.common.interfaces.GameEventListener;
import risk.common.interfaces.RiskInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * The `ClientMain2` class is responsible for managing the client-side network
 * operations of the Risk game. It implements the `RiskInterface` and handles
 * communication with the game server, including sending requests, receiving
 * responses, and notifying listeners about game events.
 * <p>
 * Key functionalities include:
 * <ul>
 *     <li>Connecting to the server and initializing input/output streams</li>
 *     <li>Listening for server messages and handling different server commands</li>
 *     <li>Managing the current player's state and game data</li>
 *     <li>Notifying game event listeners about various game events</li>
 *     <li>Providing methods for player actions such as attacking, moving units, and
 *         interacting with country and mission data</li>
 * </ul>
 * <p>
 * This class is integral to the client-side game logic and ensures real-time
 * synchronization with the server, facilitating a seamless multiplayer experience.
 *
 * @see risk.common.interfaces.RiskInterface
 * @see risk.common.entities.Player
 * @see risk.common.entities.CountryData
 */
public class ClientMain implements RiskInterface {
    public static final int DEFAULT_PORT = 12344;
    private Socket socket;
    private BufferedReader sin;
    private PrintWriter sout;
    private Thread listenThread;
    private BlockingQueue<String> serverResponses = new LinkedBlockingQueue<>();
    private List<GameEventListener> gameEventListeners = new ArrayList<>();
    int playerOnTurn = 0;

    Player player;
    List<CountryData> countryData;
    List<Country> countries = new ArrayList<>();

    /**
     * Constructs a new ClientMain2 object and connects to the specified server.
     *
     * @param host the server host
     * @param port the server port
     */
    public ClientMain(String host, int port) {
        this.countryData = new ArrayList<>();
        try{
            socket = new Socket(host, port);
            sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sout = new PrintWriter(socket.getOutputStream(), true);
            System.err.println("Verbunden: " + socket.getInetAddress() + ":" + socket.getPort());
            System.out.println(sin.readLine()); //Willkommens nachricht
            listenForServerMessages();
        }catch (IOException e) {
            e.printStackTrace();
            close();
        }

    }

    /**
     * Starts a new thread to listen for server messages.
     */
    public void listenForServerMessages() {
        listenThread = new Thread(() -> {
            try {
                String message;
                while ((message = sin.readLine()) != null) {
                    System.out.println("Message from Server: " + message);
                    handleServerMessage(message);
                }
            } catch (IOException e) {
                if (!Thread.currentThread().isInterrupted()) {
                    e.printStackTrace();
                }
            }
        });
        listenThread.start();
    }


    /**
     * Handles messages received from the server.
     *
     * @param message the server message
     */
    public void handleServerMessage(String message) {
        String[] parts = message.split(" ");
        String command = parts[0];

        switch(command) {
            case "PLAYER_ADDED:":
                parts = message.split(" ");
                String name = parts[1];
                int id = Integer.parseInt(parts[2]);
                String color = parts[3];
                player = new Player(name, id, color);
                System.out.println("Player added: " + player);
                break;

            case "COUNTRY_DATA:":
                System.out.println("I get called! CountryData");
                receiveCountryData();
                break;

            case "PLAYER_ON_TURN:":
                int playerId = Integer.parseInt(parts[1]);
                playerOnTurn = playerId;
                System.out.println("Player on turn: " + playerId);
                if(playerOnTurn == player.getId()){
                    System.out.println("It's your turn!");
                    addPlayerUnitsPerRound(player.getName());
                    notifyAllowPlayerActions(true);
                } else {
                    System.out.println("It's not your turn!");
                    notifyAllowPlayerActions(false);
                }
                break;

            case "UPDATE_PLAYERS:":
                notifyPlayerListChange();
                break;

            case "GAME_STARTED:":
                notifyGameStarted();
                break;

            case "ADD_PLAYER_UNITS:":
                try {
                    int units = Integer.parseInt(sin.readLine());
                    player.addUnits(units);
                    System.out.println("Player units: " + player.getNumberOfUnits());
                } catch (IOException e) {
                    System.out.println("Error in ADD_PLAYER_UNITS" + e.getMessage());
                }
                break;

            case "COUNTRY_OBJECTS:":
                try {
                    List <Country> countries = new ArrayList<>();
                    int amount = Integer.parseInt(sin.readLine());
                    for (int i = 0; i < amount; i++) {
                        String country = sin.readLine();
                        String shortName = sin.readLine();
                        int units = Integer.parseInt(sin.readLine());
                        countries.add(new Country(country, shortName, units, player, true));
                    }
                    player.setCountries(countries);
                    System.out.println(player.getCountries());
                } catch (IOException e){
                    System.err.println(e.getMessage());
                }
                break;

            case "ADD_PLAYER_UNITS_TO_COUNTRY:":
                if (parts[1].equals("Success")) {
                    int units = Integer.parseInt(parts[2]);
                    player.setNumberOfUnits(units);
                    //getCountryData();
                    System.out.println("Units added to country");
                } else {
                    System.out.println("Failed to add units to country");
                }
                break;

            case "GETTING_ATTACKED" :
                int defender = Integer.parseInt(parts[6]);
                System.out.println("parts[6]: " + parts[6]);
                if (defender == player.getId()){
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "You are getting attacked, do you want to defend?", "Defend", JOptionPane.YES_NO_OPTION);
                    });

                    //int response = JOptionPane.showConfirmDialog(null, "You are getting attacked, do you want to defend?", "Defend", JOptionPane.YES_NO_OPTION);
                }

                System.out.println("You are getting attacked");

                break;

            default:
                try{
                    serverResponses.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Sends a request to the server and waits for a response.
     *
     * @param request the request to be sent
     */
    public void sendRequestAndWaitForResponse(String request) {
        // Send the request to the server
        sout.println(request);
        // Wait for a response
        String response;
        try {
            response = serverResponses.take(); // This will block until a response is available
            handleServerMessage(response);
            System.out.println("Response from Server: " + response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the given message contains an integer.
     *
     * @param message the message to be checked
     * @return true if the message contains an integer, false otherwise
     */
    public boolean containsInteger(String message) {
        return message.matches(".*\\d+.*");
    }

    /**
     * Stops the thread that listens for server messages.
     */
    public void stopListening(){
        if (listenThread != null) {
            listenThread.interrupt();
        }
    }

    /**
     * Adds a game event listener.
     *
     * @param listener the game event listener to be added
     */
    public void addGameEventListener(GameEventListener listener) {
        gameEventListeners.add(listener);
    }

    /**
     * Removes a game event listener.
     *
     * @param listener the game event listener to be removed
     */
    public void removeGameEventListener(GameEventListener listener) {
        gameEventListeners.remove(listener);
    }

    /**
     * Notifies all game event listeners that the player list has changed.
     */
    public void notifyPlayerListChange() {
        for (GameEventListener listener : gameEventListeners) {
            listener.onPlayerListChanged();
        }
    }

    /**
     * Notifies all game event listeners that the game has started.
     */
    public void notifyGameStarted() {
        for (GameEventListener listener : gameEventListeners) {
            listener.onGameStarted();
        }
    }

    /**
     * Notifies listeners whether player actions are allowed.
     *
     * @param allow True if player actions are allowed, false otherwise.
     */

    @Override
    public void notifyAllowPlayerActions(boolean allow) {
        for (GameEventListener listener : gameEventListeners) {
            listener.onAllowPlayerActions(allow);
        }
    }


    /**
     * Sends a server action response.
     *
     * @param message the server action response message
     * @return the server action response message
     */
    private String serverActionResponse(String message) {
        return message;
    }

    /**
     * Updates the list of players.
     *
     * @return The updated list of players.
     */
    public List<Player> updatePlayers(){
        List<Player> playerList = new ArrayList<>();
        String recieved;
        try{
            recieved = sin.readLine();
            int amount = Integer.parseInt(recieved);
            for (int i=0; i<amount; i++) {
                String player = sin.readLine();
                String[] parts = player.split(" ");
                playerList.add(new Player(parts[0], Integer.parseInt(parts[1]), parts[2]));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return playerList;
    }

    /**
     * Closes the connection to the server.
     */
    public void close(){
        stopListening();
        System.out.println("close is called");
        try {
            if(socket != null && !socket.isClosed()){
                sout.println("disconnect");
                System.out.println("closing");
                socket.close();
            } else {
                System.out.println("socket already closed");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * Adds a player to the game.
     *
     * @param name The name of the player.
     * @param color The color of the player.
     */
    public void addPlayer(String name, String color) {
        if(sout != null) {
            sout.println("addPlayer" + " " + name + " " + color);
            System.out.println("addPlayer" + " " + name + " " + color);
            System.out.println("Trying to add Player");
        }
    }

    /**
     * Handles the server message indicating a new player has been added.
     * Reads the player's name, ID, and color from the server input stream
     * and creates a new Player object with this information.
     */
    private void receivePlayerAdded() {
        try {
            String name = sin.readLine();
            int id = Integer.parseInt(sin.readLine());
            String color = sin.readLine();
            player = new Player(name, id, color);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints "startGame" to the console followed by the ID of the player.
     * Assumes that the player object is initialized and has a valid ID.
     */

    public void startGame(){
        sout.println("startGame");
        sout.println(player.getId());
    }


    /**
     * Attempts to remove a player with the specified name from the game.
     * Sends a request to remove the player and processes the response to confirm removal.
     * If successful, sets the current player object to null.
     *
     * @param name The name of the player to be removed.
     */

    public void removePlayer(String name) {
        sout.println("removePlayer" + " " + name);

        String response= "?";
        try{
            response = sin.readLine();
            System.out.println("response: " + response);
            if (response.equals("playerRemoved")) {
                this.player = null;
                System.out.println("player Removed");
                //update playerList
            } else {
                System.out.println("player " +name+ " could not be removed");
            }
        }catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
    }


    /**
     * Sends a request to receive country objects associated with the specified name.
     *
     * @param name The name identifier for which country objects are requested.
     */

    public void recieveCountryObjects(String name){
        sout.println("sendCountries" + " " + name);
    }

    /**
     * This method is used to get the country data from the server
     * Used for the GUI (Primitive Data)
     */
    public void getCountryData(){
        sout.println("sendCountryData");
        System.out.println("sendCountryData");
    }

    /**
     * Receives country data from the input stream and populates the countryData list.
     * Clears the existing countryData list before populating with new data.
     *
     * @throws IOException If there is an error reading from the input stream.
     */

    private void receiveCountryData(){
        countryData.clear();
        try {
            int countryCount = Integer.parseInt(sin.readLine());
            for (int i = 0; i < countryCount; i++) {
                String countryName = sin.readLine();
                String playerName = sin.readLine();
                int units = Integer.parseInt(sin.readLine());
                String continentName = sin.readLine();
                int neighborCount = Integer.parseInt(sin.readLine());
                String[] neighbors = new String[neighborCount];
                for (int j = 0; j < neighborCount; j++) {
                    neighbors[j] = sin.readLine();
                }
                // Now you have all the data for a country, so you can create a CountryData object and add it to your list
                countryData.add(new CountryData(countryName, playerName, units, continentName, neighbors));
                //System.out.println(countryData.get(i).toString());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + "Error in recieveCountryData");
        }
        recieveCountryObjects(player.getName());

    }

    /**
     * Sends a message to the output stream if it is not null.
     *
     * @param message The message to be sent.
     */

    public void sendMessage(String message) {
        if (sout != null){
            sout.println(message);
        }
    }

//    public Player getPLayerByName(String name){
//
//        return player;
//    }

    public String listPlayers() {
        return "";
    }

    /**
     * Retrieves the list of players from the server.
     * Sends a request to fetch the player list and processes the response to populate the list of players.
     *
     * @return A list of Player objects representing the players currently active in the game.
     */

    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        sout.println("getPlayers");
        try {
            String received = serverResponses.take();
            if(received.equals("PLAYER_LIST:")){
                int amount = Integer.parseInt(serverResponses.take());
                for (int i = 0; i < amount; i++) {
                    String player = serverResponses.take();
                    String[] parts = player.split(" ");
                    players.add(new Player(parts[0], Integer.parseInt(parts[1]), parts[2]));
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return players;
    }

    /**
     * Requests a mission card for the current player from the server.
     * Sends a request with the player's name to receive the mission card and assigns it to the player.
     *
     */

    public void getMissionCard(){
        sout.println("sendMissionCard" + " " + player.getName());
        try {
            String missionCard = sin.readLine();
            MissionCard missionCard1 = new MissionCard(missionCard);
            missionCard1.setPlayer(this.player);
            this.player.setMissionCard(missionCard1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests unit cards for the current player from the server.
     * Sends a request with the player's name to receive unit cards and adds them to the player's unit card collection.
     *
     */

    public void getUnitCards() {
        sout.println("sendUnitCards" + " " + player.getName());
        try {
            int unitCardAmount = Integer.parseInt(sin.readLine());
            for (int i = 0; i<unitCardAmount; i++) {
                String unitCard = sin.readLine();
                UnitCard unitCard1 = new UnitCard(unitCard);
                this.player.addUnitCard(unitCard1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initiates an attack command to the server.
     * Sends a command to attack from one country to another with specified units and attacker.
     *
     * @param fromCou The name of the attacking country.
     * @param toCou The name of the defending country.
     * @param attacker The name of the player initiating the attack.
     * @param numberOfUnits The number of units used in the attack.
     */

    @Override
    public void makeAttack(String fromCou, String toCou, String attacker, int numberOfUnits) {
        String command_ = "makeAttack " + fromCou + " " + toCou + " " + attacker + " " + numberOfUnits;
        sendMessage(command_);
        System.out.println("Command sent :" + command_);
    }

    /**
     * Requests to move units from one country to another for a specified player.
     * Sends a request to move units with the player's name, source country, destination country, and number of units.
     *
     * @param player The name of the player requesting to move units.
     * @param fromCountry The name of the country from which units are being moved.
     * @param toCountry The name of the country to which units are being moved.
     * @param units The number of units to move.
     * @return An empty string (placeholder for future implementation).
     */

    public String moveUnits(String player, String fromCountry, String toCountry, int units) {
        sout.println("moveUnits" + " " + player + " " + fromCountry + " " + toCountry + " " + units);
        return "";
    }

    public String getAvailableColors() {
        return "";
    }

    public void distributeUnits() {

    }

    /**
     * Retrieves a player object by their name.
     * Currently returns null as placeholder for future implementation.
     *
     * @param name The name of the player to retrieve.
     * @return The Player object corresponding to the given name, or null if not found.
     */

    @Override
    public Player getPlayerByName(String name) {
        return null;
    }

    /**
     * Retrieves the player currently taking their turn.
     *
     * @return The Player object currently on turn.
     */
    public Player getPlayerOnTurn() {
        if(player.getId() == playerOnTurn){
            return player;
        }
        return player;
    }

    /**
     * Requests to turn in unit cards for a player to the server.
     * Sends a request with the player's name and three unit card identifiers.
     *
     * @param player The name of the player turning in the unit cards.
     * @param card1 The identifier of the first unit card to turn in.
     * @param card2 The identifier of the second unit card to turn in.
     * @param card3 The identifier of the third unit card to turn in.
     * @return "Success" if turning in the unit cards is successful, "Failed" otherwise.
     *         Returns an empty string if an IOException occurs during communication.
     */
    public String turnInUnitCards(String player, String card1, String card2, String card3) {
        sout.println("turnInUnitCards" + " " + player + " " + card1 + " " + card2 + " " + card3);
        String response = "?";
        try {
            response = sin.readLine();
            if (response.equals("Success")) {
                return "Success";
            } else {
                return "Failed";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void nextTurn() {

    }

    /**
     * Validates whether a player owns a specific country.
     *
     * @param country The name of the country to check ownership.
     * @param player The name of the player to validate ownership.
     * @return true if the player owns the specified country, false otherwise.
     */

    public boolean validateOwnership(String country, String player) {
        for(CountryData data : countryData){
            if(data.getName().equals(country)){
                return data.getPlayerName().equals(player);
            }
        }
        return false;
    }

    /**
     * Retrieves a Country object by its name.
     *
     * @param name The name of the country to retrieve.
     * @return The Country object corresponding to the given name.
     * @throws CountryNotFoundException If the country with the specified name is not found.
     */

    public Country getCountryByName(String name) throws CountryNotFoundException {
        return null;
    }

    /**
     * Retrieves the player owning a country by its name.
     *
     * @param name The name of the country to retrieve the player owning it.
     * @return The name of the player owning the country, or null if the country is not found.
     */

    public String getCountryPlayer(String name) {
        for (CountryData country : countryData) {
            if (country.getName().equals(name)) {
                return country.getPlayerName();
            }
        }
        return null;
    }

    /**
     * Checks if a player owns any continent.
     *
     * @param playername The name of the player to check continent ownership.
     * @return An empty string (placeholder for future implementation).
     */

    public String continentPlayerCheck(String playername) {
        return "";
    }

    /**
     * Requests to add units to a country for a specific player.
     * Sends a request with the player's name, country name, and number of units to add.
     *
     * @param player The name of the player adding units to the country.
     * @param country The name of the country to which units are being added.
     * @param units The number of units to add to the country.
     * @return An empty string (placeholder for future implementation).
     * @throws InvalidUnitException If the number of units to add is invalid.
     */

    public String addPlayerUnitsToCountry(String player, String country, int units) throws InvalidUnitException {
        sout.println("addPlayerUnitsToCountry" + " " + player + " " + country + " " + units);

        return "";
    }

    /**
     * Retrieves information about a country for display in a graphical user interface (GUI).
     *
     * @param country The name of the country to retrieve information for.
     * @return A string representation of the country's information, or an empty string if the country is not found.
     */

    public String getCountryInformationGUI(String country) {
        if (countryData!=null) {
            for (CountryData data : countryData) {
                if (data.getName().equals(country)) {
                    return data.toString();
                }
            }
        }
        return "";
    }

    public void distributeStartingUnits() {

    }

    /**
     * Requests to add units per round for a specific player.
     * Sends a request with the player's name to add units per round.
     *
     * @param player The name of the player to add units per round.
     * @return "Failed" indicating that the operation could not be completed.
     */

    @Override
    public String addPlayerUnitsPerRound(String player) {
        if (sout != null) {
            sout.println("addPlayerUnitsPerRound" + " " + player);
            System.out.println("addPlayerUnitsPerRound" + " " + player);
        }
        return "Faield";
    }

    /**
     * Creates and adds a new player to the game.
     *
     * @param name The name of the player to add.
     * @param id The ID of the player.
     * @param color The color associated with the player.
     * @return The newly created Player object, or null (placeholder for future implementation).
     */

    @Override
    public Player addPlayer(String name, int id, String color) {
        return null;
    }

    /**
     * Retrieves a list of countries.
     *
     * @return An empty list of countries.
     */

    public List<Country> getCountries() {
        return List.of();
    }

    public List<Continent> getContinents() {
        return List.of();
    }

    /**
     * Retrieves the number of units in a country.
     *
     * @param country The name of the country to retrieve units for.
     * @return The number of units in the specified country, or 1 if the country is not found.
     */
    @Override
    public int getUnitsByCountry(String country) {
        for (CountryData data : countryData) {
            if (data.getName().equals(country)) {
                return data.getUnits();
            }
        }
        return 1;
    }

    @Override
    public String addPlayerUnitsPerRound(Player player) {
        return "";
    }

    /**
     * Checks if two countries are neighbors.
     *
     * @param country1 The name of the first country.
     * @param country2 The name of the second country.
     * @return true if country1 and country2 are neighbors, false otherwise.
     */
    @Override
    public boolean checkCountryNeighbor(String country1, String country2) {
        for (CountryData data : countryData) {
            if (data.getName().equals(country1)) {
                for (String neighbor : data.getNeighbors()) {
                    if (neighbor.equals(country2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

package risk.server.network;

import risk.common.entities.Player;
import risk.common.interfaces.RiskInterface;
import risk.server.domain.Risk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages incoming client connections, game state, and communication among clients in a Risk game server.
 * Handles client connections through ClientRequestHandler2 instances, manages player states,
 * and facilitates game preparation and broadcasting messages to connected clients.
 */
public class GameServer {
    private ServerSocket serverSocket;
    private RiskInterface risk;
    private List<ClientRequestHandler> clientRequestHandlers;
    private List<Player> players = new ArrayList<>();
    private boolean[] isReady = new boolean[6];
    public int playerID = 0;

    /**
     * Constructs a GameServer2 object.
     * Initializes the server on the specified port and initializes the game with the provided data.
     *
     * @param port The port number on which the server will listen for client connections.
     * @param data The initial data for setting up the game.
     */
    public GameServer(int port, String data){
        try {
            serverSocket = new ServerSocket(port);
            risk = new Risk(data);
            clientRequestHandlers = new ArrayList<>();
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the server and listens for incoming client connections.
     * Creates a new ClientRequestHandler2 for each connected client, starts a new thread for each handler,
     * and sends a welcome message to the client.
     */
    public void start(){
        while (true){
            try{
                Socket clientSocket = serverSocket.accept();
                ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientSocket, risk, this);
                clientRequestHandlers.add(clientRequestHandler);
                new Thread(clientRequestHandler).start();
                //clientRequestHandler.
                clientRequestHandler.sendMessage("Wilkommen");
                System.out.println("New client connected" + clientRequestHandler.getClientID());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    /**
     * Adds a ClientRequestHandler2 instance to the list of active client handlers.
     *
     * @param client The ClientRequestHandler2 instance representing the connected client.
     */
    public void addClient(ClientRequestHandler client){
        clientRequestHandlers.add(client);
    }
    /**
     * Adds a player to the game server's list of active players.
     *
     * @param player The Player object representing the player to be added.
     */
    public void addPlayer(Player player){
        players.add(player);
    }
    /**
     * Removes a player from the game server's list of active players.
     *
     * @param player The Player object representing the player to be removed.
     */
    public void removePlayer(Player player){
        players.remove(player);
    }
    /**
     * Marks a player as ready to start the game.
     *
     * @param playerID The ID of the player to mark as ready.
     */
    public void addIsReady(int playerID){
        isReady[playerID] = true;
    }
    /**
     * Marks a player as not ready to start the game.
     *
     * @param playerID The ID of the player to mark as not ready.
     */
    public void removeIsReady(int playerID){
        isReady[playerID] = false;
    }
    /**
     * Checks if a specific player is ready to start the game.
     *
     * @param playerID The ID of the player to check.
     * @return True if the player is ready, false otherwise.
     */
    public boolean isReady(int playerID){
        return isReady[playerID];
    }
    /**
     * Retrieves the array indicating the readiness status of all players.
     *
     * @return An array of booleans representing the readiness status of each player.
     */
    public boolean[] getIsReady(){
        return isReady;
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message The message to broadcast to all clients.
     */
    public void broadcastMessage(String message){
        for(ClientRequestHandler client : clientRequestHandlers){
            client.sendMessage(message);
        }
        System.out.println("Broadcasting: " + message);
    }
    /**
     * Removes a client from the list of active client handlers.
     *
     * @param client The ClientRequestHandler2 instance representing the client to remove.
     */
    public void removeClient(ClientRequestHandler client) {
        clientRequestHandlers.remove(client);
    }
    /**
     * Main method to start the GameServer2 instance on a specified port.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args){
        GameServer server = new GameServer(12344, "Risk");
        server.start();
    }



}

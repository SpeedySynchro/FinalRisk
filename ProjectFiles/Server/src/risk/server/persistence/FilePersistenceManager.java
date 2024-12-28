package risk.server.persistence;

import risk.common.entities.Country;
import risk.common.entities.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages file persistence for players in the game.
 * <p>
 * This class implements the {@link PersistenceManager} interface to provide file-based persistence operations for {@link Player} objects. It includes methods to save a list of players to a file and to load players from a file. The class uses standard I/O operations to read from and write to files, handling any I/O exceptions that occur during these processes.
 * <p>
 * The {@code speicherPlayer} method writes player information to a specified file, creating or overwriting the file as necessary. Each player's data is written in a custom format designed for easy parsing.
 * <p>
 * The {@code loadPlayers} method reads player information from a specified file, parsing the custom format to reconstruct {@link Player} objects. This method is capable of handling files written by the {@code speicherPlayer} method, allowing for persistent storage of player data across game sessions.
 */
public class FilePersistenceManager implements PersistenceManager {

    private List<Player> players;

    /**
     * Saves a list of {@link Player} objects to a specified file.
     * <p>
     * This method iterates over a list of players, converting each player to a string representation and writing it to the specified file. Each player's data is separated by a custom delimiter ("\n---\n") for easy reading and parsing. The file is overwritten each time this method is called.
     * <p>
     * If an I/O exception occurs during the writing process, the exception is caught, logged to the standard error stream, and the method returns {@code false} to indicate failure. Otherwise, the method returns {@code true} to indicate success.
     *
     * @param players the list of {@link Player} objects to be saved.
     * @param filename the name of the file where the players' data will be written. The file will be created or overwritten.
     * @return {@code true} if the players were successfully saved to the file, {@code false} otherwise.
     */
    @Override
    public boolean speicherPlayer(List<Player> players, String filename) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (Player player : players) {
//                if (p.getId() == player.getId()) {
//                    return false;
//                }
                writer.write(player.toString());
                writer.write("\n---\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads players from a specified file.
     * <p>
     * This method reads player information from a file, where each player's data is expected to be in a specific format, starting with "Player{name=" and followed by player attributes and their countries. The method creates {@link Player} objects, sets their attributes, and adds them to a list. Each player's countries are also parsed and added to their respective player object.
     * <p>
     * The method uses a {@link BufferedReader} to read the file line by line. It checks for the start of a new player's data with a specific prefix and then splits the attributes for further processing. Countries associated with a player are also parsed in a similar manner. The method ensures that all players and their countries are correctly instantiated and added to the list of players.
     * <p>
     * If the file does not exist or an error occurs during reading, an {@link IOException} is thrown.
     *
     * @param fileName the name of the file from which to load the players. The file should be in the expected format.
     * @return A list of {@link Player} objects loaded from the file, each with their attributes and countries set.
     * @throws IOException if an error occurs during file reading.
     */
    @Override
    public  List<Player> loadPlayers(String fileName) throws IOException {
        List<Player> players = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            Player currentPlayer = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Player{name=")) {
                    if (currentPlayer != null) {
                        players.add(currentPlayer);
                    }
                    currentPlayer = new Player();
                    String[] playerParts = line.substring(7, line.indexOf("countries=[") - 2).split(", ");
                    for (String part : playerParts) {
                        if (part.startsWith("name=")) {
                            currentPlayer.setName(part.substring(6, part.length() - 1));
                        } else if (part.startsWith("id=")) {
                            currentPlayer.setId(Integer.parseInt(part.substring(3)));
                        } else if (part.startsWith("color=")) {
                            currentPlayer.setColor(part.substring(6));
                        } else if (part.startsWith("numberOfUnits=")) {
                            currentPlayer.setNumberOfUnits(Integer.parseInt(part.substring(14)));
                        } else if (part.startsWith("numberOfCountries=")) {
                            currentPlayer.setNumberOfCountries(Integer.parseInt(part.substring(18)));
                        } else if (part.startsWith("missionCard=")) {
                            //MissionCard missionCard = new MissionCard(part.substring(12));
                           // currentPlayer.setMissionCard(part.substring(12).equals("null") ? null : missionCard);
                        } else if (part.startsWith("isAlive=")) {
                            currentPlayer.setAlive(Boolean.parseBoolean(part.substring(8)));
                        }
                    }

                    String countriesData = line.substring(line.indexOf("countries=[") + 11, line.length() - 1);
                    String[] countryParts = countriesData.split("Country\\{");
                    for (String countryData : countryParts) {
                        if (countryData.trim().isEmpty()) continue;
                        Country country = new Country();
                        countryData = countryData.replace("}", "").trim();
                        String[] countryAttributes = countryData.split(", ");
                        for (String attr : countryAttributes) {
                            if (attr.startsWith("name=")) {
                                country.setName(attr.substring(6, attr.length() - 1));
                            } else if (attr.startsWith("units=")) {
                                country.setUnits(Integer.parseInt(attr.substring(6)));
                            } else if (attr.startsWith("player=")) {
                                country.setPlayer(currentPlayer);
                            } else if (attr.startsWith("hasPlayer=")) {
                                country.setHasPlayer(Boolean.parseBoolean(attr.substring(10)));
                            }
                        }
                        currentPlayer.getCountries().add(country);
                    }
                }
            }
            if (currentPlayer != null) {
                players.add(currentPlayer);
            }
        }
        return players;
    }



}

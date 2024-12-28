package risk.common.persistence;

import risk.common.entities.Country;
import risk.common.entities.Player;
import risk.common.entities.missions.MissionCard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages file persistence for players and their states in the game.
 * <p>
 * This class provides functionality to save and load player data to and from files. It supports operations such as saving a list of players to a file and loading players from a file. The class is designed to handle the serialization and deserialization of player objects, ensuring data integrity and ease of data manipulation.
 */
public class FilePersistenceManager implements PersistenceManager{

    private List<Player> players;

    /**
     * Saves a list of {@code Player} objects to a file.
     * <p>
     * This method iterates over a list of players, converting each player to a string representation and writing it to the specified file. Each player's data is separated by a newline followed by "---" for readability. If the operation is successful, it returns {@code true}; otherwise, it returns {@code false} if an I/O exception occurs.
     *
     * @param players The list of {@code Player} objects to be saved.
     * @param filename The name of the file where the players' data will be saved.
     * @return {@code true} if the players were successfully saved, {@code false} otherwise.
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
     * Loads players from a specified file and returns a list of {@code Player} objects.
     * <p>
     * This method reads player data from a file, where each player's information is expected to be in a specific format. It processes each line of the file, creating {@code Player} objects and setting their attributes based on the parsed data. The method handles the parsing of player attributes, including name, ID, color, number of units, number of countries, mission card, and alive status. It also processes the countries associated with each player, setting their attributes and adding them to the respective player's list of countries.
     * <p>
     * The expected format for a player's data starts with "Player{name=", followed by the player's attributes and the countries they own. Each player's data is separated by a newline, and each attribute within a player's data is separated by commas. The method ensures that each player and their associated countries are correctly instantiated and added to the list of players to be returned.
     *
     * @param fileName The name of the file from which to load the players.
     * @return A list of {@code Player} objects loaded from the file, with their attributes and associated countries properly set.
     * @throws IOException If an I/O error occurs while reading from the file.
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
                            MissionCard missionCard = new MissionCard(part.substring(12));
                            currentPlayer.setMissionCard(part.substring(12).equals("null") ? null : missionCard);
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

package risk.common.persistence;

import risk.common.entities.Player;

import java.io.IOException;
import java.util.List;

/**
 * Defines the persistence operations for player data management.
 * <p>
 * This interface outlines methods for saving and loading player data to and from a file. Implementations of this interface should provide the logic for serializing a list of {@code Player} objects to a file and deserializing them back into a list of {@code Player} objects from the file. This is crucial for maintaining game state across sessions.
 *
 * @see Player
 */
public interface PersistenceManager {

    /**
     * Saves a list of {@code Player} objects to a specified file.
     * <p>
     * This method serializes a list of {@code Player} objects into a string format and writes them to the specified file. Each {@code Player} object is converted to a string using its {@code toString} method and then written to the file, with each player entry separated by a specific delimiter for readability. The method ensures that all player data is accurately saved, allowing for the persistence of game state across sessions.
     * <p>
     * If the operation is successful, the method returns {@code true}. If an exception occurs during the write operation, the method catches the exception, logs it, and returns {@code false}, indicating the failure of the operation.
     *
     * @param players The list of {@code Player} objects to be saved.
     * @param filename The name of the file where the players' data will be saved.
     * @return {@code true} if the players were successfully saved, {@code false} otherwise.
     * @throws Exception If an error occurs during the writing process.
     */
    public boolean speicherPlayer(List<Player> players, String filename) throws Exception;

    /**
     * Loads a list of {@code Player} objects from a specified file.
     * <p>
     * This method deserializes player data from a given file into a list of {@code Player} objects. It reads the file line by line, parsing the data according to the expected format and instantiating {@code Player} objects with their attributes set accordingly. The method is designed to handle errors gracefully, throwing an {@code IOException} if any issues occur during file reading.
     * <p>
     * The expected format for player data includes detailed information about each player, such as their name, ID, and the state of their game entities (e.g., units, countries). This method ensures that all necessary data is accurately parsed and that each {@code Player} object is correctly constructed and added to the list to be returned.
     *
     * @param filename The path to the file from which player data will be loaded.
     * @return A list of {@code Player} objects, each representing a player's data loaded from the file.
     * @throws IOException If an error occurs while reading from the file.
     */
    public List<Player> loadPlayers(String filename) throws IOException;
}

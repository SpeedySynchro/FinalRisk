package risk.server.persistence;

import risk.common.entities.Player;

import java.io.IOException;
import java.util.List;

/**
 * Defines the persistence operations for player data in the game.
 * <p>
 * This interface outlines methods for saving and loading player data to and from a file. Implementations of this interface should provide the logic for serializing a list of {@link Player} objects to a file and deserializing them back into a list of {@link Player} objects from the file. This allows for persistent storage of player states across game sessions.
 * <p>
 * The {@code speicherPlayer} method is responsible for writing player data to a specified file, potentially throwing an exception if an error occurs during the write operation.
 * <p>
 * The {@code loadPlayers} method is responsible for reading player data from a specified file and reconstructing the list of {@link Player} objects based on this data, potentially throwing an IOException if an error occurs during reading.
 */
public interface PersistenceManager {

    public boolean speicherPlayer(List<Player> players, String filename) throws Exception;
    public List<Player> loadPlayers(String filename) throws IOException;
}

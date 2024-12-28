package risk.common.exceptions;

/**
 * Represents an exception for when a specified player is not found within the game.
 * <p>
 * This exception is thrown to indicate that an operation targeting a specific player could not be completed because the player with the specified name does not exist in the game's context. It extends the {@code Exception} class, adding specificity to handling errors related to player identification in the game.
 */
public class PlayerNotFoundException extends Exception {
    /**
     * Constructs a new {@code PlayerNotFoundException} with the specified player name.
     * <p>
     * This constructor initializes a new instance of {@code PlayerNotFoundException} by passing a detailed message to the superclass. The message indicates that the player with the provided name does not exist within the game's context, guiding the user or developer to check the player's name for accuracy.
     *
     * @param playerName The name of the player that was not found, leading to this exception.
     */
    public PlayerNotFoundException(String playerName) {
        super("Player " + playerName + " not found");
    }
}

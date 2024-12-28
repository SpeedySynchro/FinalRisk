package risk.common.exceptions;

/**
 * Represents an exception for when an input does not correspond to a valid unit in the game.
 * <p>
 * This exception is thrown to indicate that a given input, expected to represent a unit in the game, does not match any valid unit types. It extends the {@code Exception} class, providing a clear message to the developer or user about the nature of the error, specifically focusing on unit validation.
 */
public class InvalidUnitException extends Exception {
    /**
     * Constructs a new {@code InvalidUnitException} with the specified input.
     * <p>
     * This constructor initializes a new instance of {@code InvalidUnitException} by passing a detailed message to the superclass. The message indicates that the provided input does not correspond to any valid unit in the game, guiding the user or developer to provide a valid unit.
     *
     * @param input The input that was found to be invalid as a unit.
     */
    public InvalidUnitException(String input) {
        super("The input: " + input + " is not a valid unit. Please enter a valid unit.");
    }
}

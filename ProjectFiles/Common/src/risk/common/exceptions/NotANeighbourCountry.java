package risk.common.exceptions;

/**
 * Represents an exception for when a specified country is not recognized as a neighboring country in the game.
 * <p>
 * This exception is thrown to indicate that an operation involving two countries cannot be completed because one country is not a neighbor of the other, as required by the game's rules. It extends the {@code Exception} class, providing a clear indication of the nature of the error related to geographical constraints within the game.
 */
public class NotANeighbourCountry extends Exception {
    /**
     * Constructs a new {@code NotANeighbourCountry} exception with the specified country name.
     * <p>
     * This constructor initializes a new instance of {@code NotANeighbourCountry} by passing a detailed message to the superclass. The message specifies that the operation could not be completed because the country named does not qualify as a neighboring country according to the game's rules, guiding the user or developer to provide a valid neighboring country.
     *
     * @param countryName The name of the country that was not recognized as a neighbor.
     */
    public NotANeighbourCountry(String countryName) {
        super("Country " + countryName + " is not a neighbour country. Please enter a valid neighbour country.");
    }
}

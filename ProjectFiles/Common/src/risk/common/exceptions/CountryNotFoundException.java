package risk.common.exceptions;

/**
 * Represents an exception for when a specified country is not found within the game.
 * <p>
 * This exception is thrown to indicate that an operation targeting a specific country could not be completed because the country with the specified name does not exist in the game's context. It extends the {@code Exception} class, adding specificity to handling errors related to country identification in the game.
 */
public class CountryNotFoundException extends Exception {
    /**
     * Constructs a new {@code CountryNotFoundException} with the specified country name.
     * <p>
     * This constructor initializes a new instance of {@code CountryNotFoundException} by passing a detailed message to the superclass. The message includes the name of the country that could not be found, providing clarity on the nature of the exception.
     *
     * @param countryName The name of the country that was not found, leading to this exception.
     */
    public CountryNotFoundException(String countryName) {
        super("Country " + countryName + " not found");
    }
}

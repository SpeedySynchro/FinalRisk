package risk.common.entities;

import java.util.Arrays;

/**
 * Represents the data for a country in the game.
 * <p>
 * This class encapsulates all the necessary information about a country, including its name, the player who controls it, the number of units stationed there, its continent, and its neighboring countries. It provides getters and setters for each of these properties to allow for easy manipulation and retrieval of country data.
 * <p>
 * The {@code neighbors} array contains the names of adjacent countries, facilitating the implementation of game mechanics that require knowledge of geographical relationships between countries, such as attacking or moving units.
 */
public class CountryData {
    private String name;
    private String playerName;
    private int units;
    private String continent;
    private String[] neighbors;

    /**
     * Constructs a new {@code CountryData} object with specified details.
     * <p>
     * This constructor initializes a new instance of {@code CountryData} with the provided country name, player name, number of units, continent, and neighboring countries. It sets up the basic information required to represent a country in the game, including who controls it, how many units are stationed there, which continent it belongs to, and its adjacent countries.
     *
     * @param name the name of the country.
     * @param playerName the name of the player who controls the country.
     * @param units the number of units stationed in the country.
     * @param continent the continent to which the country belongs.
     * @param neighbors an array of names of neighboring countries.
     */
    public CountryData(String name, String playerName, int units, String continent, String[] neighbors) {
        this.name = name;
        this.playerName = playerName;
        this.units = units;
        this.continent = continent;
        this.neighbors = neighbors;
    }

    /**
     * Retrieves the name of the country.
     * <p>
     * This method returns the name of the country as a {@code String}. The country name is a key identifier used throughout the game to reference and manage country-specific data and actions.
     *
     * @return The name of the country.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the name of the player who controls the country.
     * <p>
     * This method returns the name of the player controlling the country as a {@code String}. The player's name is crucial for identifying which player has sovereignty over the country, impacting game decisions and actions related to the country.
     *
     * @return The name of the player controlling the country.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Retrieves the number of units stationed in the country.
     * <p>
     * This method returns the total number of military units currently stationed in the country. It is a crucial piece of information for making strategic decisions in the game, such as planning attacks or defenses.
     *
     * @return The total number of units stationed in the country.
     */
    public int getUnits() {
        return units;
    }

    /**
     * Retrieves the continent to which the country belongs.
     * <p>
     * This method returns the name of the continent as a {@code String}. The continent is an important attribute for understanding the geographical grouping of countries within the game, affecting various game mechanics such as reinforcements and continent control bonuses.
     *
     * @return The name of the continent.
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Retrieves the neighboring countries of this country.
     * <p>
     * This method returns an array of {@code String} objects representing the names of neighboring countries. Neighboring countries are those that are directly adjacent to this country, allowing for potential actions such as attacks or unit movements between them in the game context.
     *
     * @return An array of names of neighboring countries.
     */
    public String[] getNeighbors() {
        return neighbors;
    }

    /**
     * Sets the name of the country.
     * <p>
     * This method updates the name of the country to the specified {@code String}. The country name is a fundamental identifier used throughout the game to reference and manage country-specific data and actions.
     *
     * @param name The new name of the country.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the name of the player who controls the country.
     * <p>
     * This method updates the player name associated with the country to the specified {@code String}. The player's name is essential for tracking which player has control over the country, influencing game strategies and decisions.
     *
     * @param playerName The new name of the player controlling the country.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Sets the number of units stationed in the country.
     * <p>
     * This method updates the total number of military units currently stationed in the country to the specified value. It is essential for reflecting changes in military strength due to game events such as battles, reinforcements, or unit movements.
     *
     * @param units The new total number of units stationed in the country.
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * Sets the continent to which the country belongs.
     * <p>
     * This method updates the continent associated with the country to the specified {@code String}. The continent is a crucial attribute for categorizing countries within the game, influencing game mechanics such as strategic positioning and continent control bonuses.
     *
     * @param continent The new continent of the country.
     */
    public void setContinent(String continent) {
        this.continent = continent;
    }

    /**
     * Returns a string representation of this {@code CountryData} instance.
     * <p>
     * This method constructs a comprehensive string that includes the country's name, the player's name who controls the country, the number of units stationed in the country, the continent to which the country belongs, and a list of neighboring countries. It is primarily used for debugging and logging purposes to provide a quick overview of the country's current state.
     *
     * @return A string representation of the country's data, including name, controlling player, unit count, continent, and neighbors.
     */
    @Override
    public String toString() {
        return
                "Country: " + name + "\n" +
                ", playerName: " + playerName + "\n" +
                ", units: " + units +
                ", continent: " + continent + "\n" +
                ", neighbors: " + Arrays.toString(neighbors) + "\n";
    }
}

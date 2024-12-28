package risk.common.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a continent in a risk-like game, encapsulating its name, controlling player, bonus units, and constituent countries.
 * <p>
 * This class models a continent by maintaining a list of countries that form the continent, the player who currently controls the continent, and the bonus units awarded for controlling the continent. It provides methods to add or remove countries, assign or remove a player as the controller, and retrieve continent details such as its name, the list of countries, and the bonus units.
 * <p>
 * Control of a continent is a strategic aspect of the game, as it provides additional units to the controlling player, potentially affecting the game's outcome.
 */
public class Continent {
    String name;
    Player player;
    int bonusUnits;
    List<Country> countries;

    /**
     * Constructs a new Continent instance with the specified name and bonus units.
     * <p>
     * This constructor initializes a continent with a given name and the number of bonus units awarded for controlling it. It also initializes the list of countries within this continent to an empty list and sets the controlling player to {@code null}, indicating that the continent is not controlled by any player at the start.
     *
     * @param name The name of the continent.
     * @param bonusUnits The number of bonus units awarded for controlling the continent.
     */
    public Continent(String name, int bonusUnits){
        this.name = name;
        this.bonusUnits = bonusUnits;
        this.countries = new ArrayList<>();
        this.player = null;
    }

    /**
     * Retrieves the name of the continent.
     * <p>
     * This method returns the name of the continent as a string. The name is a unique identifier for the continent within the game and is used to reference the continent in game operations and UI displays.
     *
     * @return The name of the continent.
     */
    public String getName(){
        return name;
    }

    /**
     * Returns a list of countries that are part of this continent.
     * <p>
     * This method provides access to the internal list of countries that make up the continent. It allows for retrieval of all countries currently associated with the continent, which can be used for various game mechanics, such as calculating bonus units, determining continent control, or displaying information in the UI.
     *
     * @return A list of {@link Country} objects representing the countries within this continent.
     */
    public List<Country> getCountries(){
        return countries;
    }

    /**
     * Generates a comma-separated string of country names in this continent.
     * <p>
     * This method iterates over the list of countries in the continent, concatenating their names into a single string separated by commas. This can be useful for displaying the countries belonging to a continent in a user interface or for logging purposes.
     *
     * @return A string containing the names of all countries in the continent, separated by commas. If the continent has no countries, an empty string is returned.
     */
    public String getCountryNames(){
        String countryNames = "";
        for(Country country : countries){
            countryNames += country.getName() + ", ";
        }
        return countryNames;
    }

    /**
     * Returns the number of bonus units awarded for controlling this continent.
     * <p>
     * This method is used to retrieve the total bonus units that are awarded to the player who controls this continent. Bonus units are crucial for the game's strategy, as they can be used to strengthen the player's position by deploying additional units to their territories.
     *
     * @return The number of bonus units for this continent.
     */
    public int getBonusUnits(){
        return bonusUnits;
    }

    /**
     * Adds a country to the continent's list of countries.
     * <p>
     * This method allows for the dynamic addition of countries to a continent. It updates the continent's internal list of countries, effectively changing its composition. This is particularly useful for initializing the continent or modifying its structure during the game setup.
     *
     * @param country The {@link Country} object to be added to the continent.
     */
    public void addCountry(Country country){
        countries.add(country);
    }

    /**
     * Sets the player who controls this continent.
     * <p>
     * This method assigns a specific player as the controller of the continent. Control of a continent can influence the game's dynamics, as it often comes with benefits such as bonus units. If the continent was previously controlled by another player, this method updates the controller to the new player.
     *
     * @param player The {@link Player} object representing the new controller of the continent.
     */
    public void setPlayer(Player player){
        this.player = player;
    }

    /**
     * Retrieves the player currently controlling this continent.
     * <p>
     * This method returns the {@link Player} instance representing the player who currently has control over the continent. If no player controls the continent, this method returns {@code null}.
     *
     * @return The {@link Player} controlling the continent, or {@code null} if the continent is not under the control of any player.
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Removes the player currently controlling this continent.
     * <p>
     * This method sets the controlling player of the continent to {@code null}, indicating that the continent is no longer under the control of any player. This can be used to reset the continent's control status during the game, such as when a player loses all their countries in this continent.
     */
    public void removePlayer(){
        this.player = null;
    }

}

package risk.common.entities;

import risk.common.entities.missions.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a player in the game, holding various attributes such as name, ID, color, and the countries and continents controlled.
 * <p>
 * This class encapsulates all the necessary details and functionalities of a player, including their mission cards, unit cards, and the list of countries and continents they control. It provides methods to manage these attributes effectively, such as adding or removing countries, setting the number of units, and checking if the player owns a specific country.
 */
public class Player {
    private String name;
    private int id;
    private String color;
    private int numberOfUnits;
    private int numberOfCountries;
    private MissionCard missionCard ;
    private boolean isAlive;
    private List<UnitCard> unitCards;
    private List<Country> countries;
    private List<Continent> continents;
    private boolean hasConquered;

    /**
     * Default constructor for creating a new Player instance.
     * <p>
     * Initializes the player's list of countries as an empty ArrayList. This setup is essential for ensuring that each player starts with no countries under their control, allowing for dynamic assignment of countries as the game progresses.
     */
    public Player(){
        this.countries = new ArrayList<>();
    }

    /**
     * Constructs a new Player instance with specified name, ID, and color.
     * <p>
     * This constructor initializes a player with the provided name, ID, and color. It sets the number of units and countries to 0, indicating that the player starts without any units or countries. The mission card is set to null, and lists for unit cards, countries, and continents are initialized as empty. The player is marked as alive, and a flag for whether the player has conquered in their last turn is set to false, indicating no conquests at the start.
     *
     * @param name the name of the player
     * @param id the unique identifier for the player
     * @param color the color assigned to the player, used for UI representation
     */
    public Player(String name, int id, String color) {
        this();
        this.name = name;
        this.id = id;
        this.color = color;
        this.numberOfUnits = 0;
        this.numberOfCountries = 0;
        this.missionCard = null;
        this.unitCards = new ArrayList<>();
        this.isAlive = true;
        this.countries = new ArrayList<>();
        this.continents = new ArrayList<>();
        hasConquered = false;
    }

    /**
     * Retrieves the name of the player.
     * <p>
     * This method returns the name of the player. The name is a string that may contain any characters, including spaces. It is used to identify the player in the game, and it is set during the creation of a Player instance or through the {@code setName} method.
     *
     * @return The name of the player as a {@code String}.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the unique identifier of the player.
     * <p>
     * This method returns the unique identifier (ID) of the player. The ID is an integer value that uniquely identifies each player in the game. It is set during the creation of a Player instance and does not change during the lifetime of the object.
     *
     * @return The unique identifier of the player as an {@code int}.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the color of the player.
     * <p>
     * This method returns the color assigned to the player. The color is a string that represents the player's color in the game's user interface. It is set during the creation of a Player instance or through the {@code setColor} method.
     *
     * @return The color of the player as a {@code String}.
     */
    public String getColor() {
        return color;
    }

    /**
     * Retrieves the number of units the player currently has.
     * <p>
     * This method returns the total number of units that the player has at their disposal. Units are essential for attacking and defending countries in the game. The number of units can increase or decrease based on game events such as conquering countries or losing battles.
     *
     * @return The total number of units owned by the player.
     */
    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    /**
     * Retrieves the number of countries currently controlled by the player.
     * <p>
     * This method returns the total number of countries under the player's control. It is a crucial aspect of the game's strategy, as controlling more countries can provide various advantages, including additional units during the reinforcement phase.
     *
     * @return The total number of countries controlled by the player.
     */
    public int getNumberOfCountries() {
        return numberOfCountries;
    }

    /**
     * Retrieves the mission card assigned to the player.
     * <p>
     * This method returns the mission card currently held by the player. Mission cards are part of the game's objectives, guiding players towards specific goals to achieve victory. Each player is assigned a unique mission card at the beginning of the game or through specific game mechanics.
     *
     * @return The {@link MissionCard} assigned to the player, which contains the details of the player's mission.
     */
    public MissionCard getMissionCard() {
        return missionCard;
    }

    /**
     * Checks if the player is currently alive in the game.
     * <p>
     * This method returns a boolean value indicating whether the player is still active and participating in the game. A player is considered alive if they have not been eliminated according to the game's rules, typically by losing all their territories or units.
     *
     * @return {@code true} if the player is alive; {@code false} otherwise.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Sets the conquest status of the player.
     * <p>
     * This method updates the player's conquest status to reflect whether they have conquered at least one territory during their current turn. It is crucial for game mechanics that reward players for successful conquests within a turn.
     *
     * @param bool {@code true} if the player has conquered a territory during their turn; {@code false} otherwise.
     */
    public void setHasConquered(boolean bool){
        this.hasConquered = bool;
    }

    /**
     * Checks if the player has conquered at least one territory during their current turn.
     * <p>
     * This method returns a boolean value indicating whether the player has successfully conquered at least one territory in the current turn. It is a critical aspect of the game's mechanics, influencing the player's ability to earn certain bonuses or complete specific objectives.
     *
     * @return {@code true} if the player has conquered a territory during their turn; {@code false} otherwise.
     */
    public boolean getHasConquered(){
        return hasConquered;
    }

    /**
     * Sets the mission card for the player.
     * <p>
     * This method assigns a specific mission card to the player, which defines their objectives within the game. Mission cards are crucial for guiding players towards their goals and can significantly influence their strategies and actions.
     *
     * @param missionCard The {@link MissionCard} to be assigned to the player.
     */
    public void setMissionKarten(MissionCard missionCard) {
        this.missionCard = missionCard;
    }

    /**
     * Sets the number of countries controlled by the player.
     * <p>
     * This method updates the player's count of controlled countries to the specified value. It is essential for tracking the player's progress and territorial control within the game. The number of countries a player controls can affect various game mechanics, such as reinforcements and mission objectives.
     *
     * @param numberOfCountries The new total number of countries controlled by the player.
     */
    public void setNumberOfCountries(int numberOfCountries) {
        this.numberOfCountries = numberOfCountries;
    }

    /**
     * Retrieves the list of countries controlled by the player.
     * <p>
     * This method returns a list containing all the countries currently under the player's control. It is essential for game mechanics that involve territory control, such as calculating reinforcements or determining eligibility for certain bonuses.
     *
     * @return A list of {@link Country} objects representing the countries controlled by the player.
     */
    public List<Country> getCountries() {
        return countries;
    }

    /**
     * Retrieves the names of countries controlled by the player in a formatted string.
     * <p>
     * This method compiles a list of the names of all countries currently under the player's control, formatting each name with a preceding ">" and appending a newline character. It is useful for displaying the player's controlled territories in a readable format.
     *
     * @return A formatted {@code String} containing the names of the countries controlled by the player, each on a new line.
     */
    public String getCountryNames(){
        String countryNames = "";
        int index = 0;
        for(Country country : countries){
            countryNames += "> " + country.getName() + "\n";
        }
        return countryNames;
    }

    /**
     * Retrieves the list of continents controlled by the player.
     * <p>
     * This method returns a list containing all the continents currently under the player's control. Controlling entire continents can provide players with strategic advantages and bonuses, according to the game's rules. It is essential for strategies that involve territorial control and expansion.
     *
     * @return A list of {@link Continent} objects representing the continents controlled by the player.
     */
    public List<Continent> getContinents() {
        return continents;
    }

    /**
     * Adds a continent to the list of continents controlled by the player.
     * <p>
     * This method appends the specified continent to the player's current list of controlled continents. It is crucial for tracking the player's territorial expansion and control over entire continents, which can provide strategic advantages and bonuses according to the game's rules.
     *
     * @param continent The {@link Continent} object to be added to the player's control.
     */
    public void addContinent(Continent continent) {
        continents.add(continent);
    }

    /**
     * Adds a country to the list of countries controlled by the player.
     * <p>
     * This method appends the specified {@link Country} object to the player's current list of controlled countries. It is crucial for tracking the player's territorial expansion and maintaining an up-to-date record of the territories under their control. Adding a country is typically performed as a result of conquering it in the game.
     *
     * @param country The {@link Country} object to be added to the player's control.
     */
    public void addCountry(Country country){
        this.countries.add(country);
    }

    /**
     * Removes a specified country from the list of countries controlled by the player.
     * <p>
     * This method removes the given {@link Country} object from the player's current list of controlled countries. It is used when a player loses control of a country, either through game mechanics or player actions. The removal is crucial for accurately tracking the territories under the player's control.
     *
     * @param country The {@link Country} object to be removed from the player's control.
     */
    public void removeCountry(Country country){
        countries.remove(country);
    }

    /**
     * Sets the name of the player.
     * <p>
     * This method assigns a new name to the player. The name is a fundamental attribute that identifies the player in the game. It can contain any characters, including spaces. This method allows for the dynamic update of a player's name during the game.
     *
     * @param name The new name to be assigned to the player.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the unique identifier for the player.
     * <p>
     * This method updates the player's unique identifier (ID) to the specified value. The ID is a crucial attribute that uniquely identifies each player in the game. It is used in various game mechanics, such as tracking player actions and determining player ownership of territories.
     *
     * @param id The new unique identifier to be assigned to the player.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the color of the player.
     * <p>
     * This method updates the player's color attribute to the specified value. The color is a string that represents the player's color in the game's user interface. It is crucial for distinguishing players in the game visually.
     *
     * @param color The new color to be assigned to the player, used for UI representation.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Sets the number of units for the player.
     * <p>
     * This method updates the total number of units that the player has at their disposal. Units are essential for attacking and defending territories in the game. The number of units a player has can increase or decrease based on game events such as conquering territories or losing battles.
     *
     * @param numberOfUnits The new total number of units to be assigned to the player.
     */
    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    /**
     * Adds a specified number of units to the player's total units.
     * <p>
     * This method increases the player's total number of units by the specified amount. Units are essential for attacking and defending territories in the game. Adding units is a common action, usually as a result of game events such as conquering territories or receiving reinforcements.
     *
     * @param units The number of units to be added to the player's total. Must be a non-negative integer.
     */
    public void addUnits(int units) {
        this.numberOfUnits += units;
    }

    /**
     * Increases the number of countries controlled by the player.
     * <p>
     * This method increments the player's total count of controlled countries by the specified amount. It is used when the player conquers new countries. Ensuring accurate tracking of the number of countries a player controls is crucial for game mechanics that depend on territorial control, such as calculating reinforcements or achieving mission objectives.
     *
     * @param numberOfCountries The number of countries to be added to the player's total. Must be a non-negative integer.
     */
    public void addNumberOfCountries(int numberOfCountries) {
        this.numberOfCountries += numberOfCountries;
    }

    /**
     * Decreases the number of countries controlled by the player by a specified amount.
     * <p>
     * This method reduces the player's total count of controlled countries by the given number. It is used when the player loses control of countries, reflecting changes in territorial possession. Accurate tracking of the number of countries a player controls is crucial for game mechanics that depend on territorial control, such as calculating reinforcements or determining victory conditions.
     *
     * @param numberOfCountries The number of countries to be subtracted from the player's total. Must be a non-negative integer.
     */
    public void removeNumberOfCountries(int numberOfCountries) {
        this.numberOfCountries -= numberOfCountries;
    }

    /**
     * Sets the mission card for the player.
     * <p>
     * This method assigns a specific mission card to the player, updating their current mission objective within the game. Mission cards are crucial elements that define the goals a player must achieve to win the game. Assigning a mission card is a key action in the game setup or during gameplay when missions are updated or changed.
     *
     * @param missionCard The {@link MissionCard} to be assigned to the player, representing their new mission objective.
     */
    public void setMissionCard(MissionCard missionCard) {
        this.missionCard = missionCard;
    }

    /**
     * Sets the player's alive status.
     * <p>
     * This method updates the player's alive status to reflect whether they are still active and participating in the game. A player is considered alive if they have not been eliminated according to the game's rules, typically by losing all their territories or units. This status is crucial for game mechanics that depend on player participation.
     *
     * @param alive {@code true} if the player is alive and participating in the game; {@code false} if the player has been eliminated.
     */
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    /**
     * Sets the list of countries controlled by the player.
     * <p>
     * This method updates the player's list of controlled countries to the specified list. It is used to directly set the countries under the player's control, typically after significant game events that alter territorial possession on a large scale. This method replaces the current list of countries with the new list provided.
     *
     * @param countries The list of {@link Country} objects to be set as the countries controlled by the player.
     */
    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    /**
     * Adds a unit card to the player's collection of unit cards.
     * <p>
     * This method appends the specified {@link UnitCard} to the player's current list of unit cards. Unit cards are essential elements in the game, providing players with additional capabilities or bonuses. Adding a unit card is typically performed as a result of game events such as winning battles or completing specific objectives.
     *
     * @param unitCard The {@link UnitCard} to be added to the player's collection.
     */
    public void addUnitCard(UnitCard unitCard){
        unitCards.add(unitCard);
    }

    /**
     * Retrieves the list of unit cards held by the player.
     * <p>
     * This method returns a list of {@link UnitCard} objects representing the unit cards currently in the player's possession. Unit cards are essential game elements that provide players with additional capabilities or bonuses. This list is dynamic and can change throughout the game as the player acquires or loses unit cards.
     *
     * @return A list of {@link UnitCard} objects representing the player's collection of unit cards.
     */
    public List<UnitCard> getUnitCards(){
        return unitCards;
    }

    /**
     * Removes a specified unit card from the player's collection of unit cards.
     * <p>
     * This method removes the given {@link UnitCard} from the player's current list of unit cards, if it exists. Unit cards represent additional capabilities or bonuses for the player. Removing a unit card is typically performed as a result of game mechanics such as trading or using the card's ability.
     *
     * @param unitCard The {@link UnitCard} to be removed from the player's collection.
     */
    public void removeUnitCard(UnitCard unitCard){
        unitCards.remove(unitCard);
    }

    /**
     * Removes specified unit cards from the player's collection of unit cards.
     * <p>
     * This method removes the given {@link UnitCard} objects from the player's current list of unit cards, if they exist. Removing unit cards is typically performed as a result of game mechanics such as trading or using the cards' abilities. It supports the removal of up to three unit cards in a single operation, enhancing the efficiency of card management within the game.
     *
     * @param card1 The first {@link UnitCard} to be removed from the player's collection.
     * @param card2 The second {@link UnitCard} to be removed from the player's collection.
     * @param card3 The third {@link UnitCard} to be removed from the player's collection.
     */
    public void removeUnitCards(UnitCard card1, UnitCard card2, UnitCard card3) {
        unitCards.remove(card1);
        unitCards.remove(card2);
        unitCards.remove(card3);
    }

    /**
     * Checks if the player owns a specific country.
     * <p>
     * This method determines whether the player currently controls a specified country. It iterates through the list of countries controlled by the player, comparing the name of each country (ignoring case) with the specified country name. If a match is found, it indicates that the player owns the country.
     *
     * @param country The name of the country to check for ownership.
     * @return {@code true} if the player owns the specified country; {@code false} otherwise.
     */
    public boolean ownsCountry(String country) {
        if (countries == null || country==null){
            return false;
        }
        for (Country c : countries) {
            if (c.getName().equalsIgnoreCase(country)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the specified object is equal to the current player.
     * <p>
     * This method checks if the provided object is the same as the current player instance based on their unique identifier (ID), color, and name. It first checks for reference equality, then checks if the object is an instance of the {@link Player} class, and finally compares the ID, color, and name fields of both objects.
     *
     * @param o The object to be compared with the current player for equality.
     * @return {@code true} if the specified object is equal to the current player; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return getId() == player.getId() && getColor() == player.getColor() && Objects.equals(getName(), player.getName());
    }

    /**
     * Calculates the hash code for a {@code Player} object.
     * <p>
     * This method overrides the {@code hashCode} method to provide a hash code calculation specific to the {@code Player} class. It uses a combination of the player's unique identifier (ID), color, and name to calculate the hash code. The hash code is computed using the {@link Objects#hash(Object...)} method, multiplied by a prime number (31) to reduce the risk of collisions.
     *
     * @return An integer representing the hash code of the {@code Player} object.
     */
    @Override
    public int hashCode() {
        return 31 * Objects.hash(getId(), getColor(), getName());
    }

    /**
     * Returns a string representation of the {@code Player} object.
     * <p>
     * This method overrides the {@code toString} method to provide a detailed string representation of the player's state. It includes the player's name, unique identifier (ID), color, number of units, number of countries controlled, mission card status, alive status, and the list of countries controlled. This representation is useful for debugging and logging purposes, offering a quick overview of the player's current status in the game.
     *
     * @return A string representation of the {@code Player} object, detailing the player's name, ID, color, number of units, number of countries controlled, mission card status, alive status, and countries controlled.
     */
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", color=" + color +
                ", numberOfUnits=" + numberOfUnits +
                ", numberOfCountries=" + numberOfCountries +
                ", missionCard=" + (missionCard != null ? missionCard.toString() : "null") +
                ", isAlive=" + isAlive +
                ", countries=" + countries +
                '}';
    }
}

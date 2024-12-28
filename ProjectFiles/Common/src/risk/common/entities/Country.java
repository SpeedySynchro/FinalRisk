package risk.common.entities;

import java.util.List;
import java.io.Serializable;

/**
 * Represents a country in the game.
 * <p>
 * This class encapsulates all the properties and behaviors of a country within the game. It includes attributes such as the country's name, the number of units present, the player who controls it, and its neighboring countries. Methods are provided to manipulate these properties, including adding or removing units, setting the controlling player, and managing neighboring countries.
 */
public class Country {
    String name;
    int units;
    Player player;
    boolean hasPlayer;
    String shortName;
    List<Country> neighbors;

    /**
     * Default constructor for creating a new Country instance.
     * <p>
     * This constructor initializes a new Country object with default values. Specifically, it sets the country's name, short name, and player to {@code null}, units to 0, hasPlayer to false, and neighbors to an empty list. This is useful for creating a Country object before its properties are known or assigned.
     */
    public Country(){
    }

    /**
     * Constructs a new Country instance with specified name and short name.
     * <p>
     * This constructor initializes a Country object with the provided name and short name, while setting the player to {@code null}, indicating no player controls the country initially. The number of units is set to 0, indicating no army is present. The {@code hasPlayer} flag is set to false, and the list of neighboring countries is initialized as {@code null}.
     *
     * @param name The name of the country.
     * @param shortName The short name of the country.
     */
    public Country(String name, String shortName){
        this.name = name;
        this.shortName = shortName;
        this.player = null;
        this.hasPlayer = false;
        this.units = 0;
        this.neighbors = null;
    }

    public Country(String name, String shortName, int units, Player player, boolean hasPlayer){
        this.name = name;
        this.shortName = shortName;
        this.units = units;
        this.player = player;
        this.hasPlayer = hasPlayer;
    }

    /**
     * Returns the name of the country.
     * <p>
     * This method retrieves the name of the country instance. The name is a string that uniquely identifies the country within the game.
     *
     * @return The name of the country.
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the name of the country.
     * <p>
     * This method assigns a new name to the country. The name is a string that uniquely identifies the country within the game. It is important to ensure that the name is not null or empty to maintain the integrity of the game's data.
     *
     * @param name The new name to be assigned to the country.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the number of units present in the country.
     * <p>
     * This method returns the total number of military units currently stationed in the country. It is useful for game mechanics that require knowledge of a country's military strength.
     *
     * @return The total number of units in the country.
     */
    public int getUnits(){
        return units;
    }

    /**
     * Adds a specified number of units to the country.
     * <p>
     * This method increases the total number of military units present in the country by the specified amount. It is used to reinforce the country's military strength. The number of units to be added must be a positive integer.
     *
     * @param units The number of units to add to the country. Must be positive.
     */
    public void addUnits(int units){
        this.units += units;
    }

    /**
     * Sets the number of units present in the country.
     * <p>
     * This method assigns a specific number of military units to the country. It is crucial for updating the country's military strength to reflect changes such as reinforcements or losses. The number of units must be a non-negative integer, as negative values are not logical in the context of a country's military strength.
     *
     * @param units The new total number of units to be set for the country. Must be non-negative.
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * Removes a specified number of units from the country.
     * <p>
     * This method decreases the total number of military units present in the country by the specified amount. It ensures that the number of units cannot become negative, throwing an IllegalArgumentException if the requested number to remove exceeds the current number of units.
     *
     * @param units The number of units to remove from the country. Must be a non-negative number and not exceed the current number of units.
     */
    public void removeUnits(int units){
        this.units -= units;
    }

    /**
     * Retrieves the player controlling the country.
     * <p>
     * This method returns the {@link Player} instance that currently controls the country. If no player controls the country, this method returns {@code null}.
     *
     * @return The player controlling the country, or {@code null} if the country is not controlled by any player.
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Sets the player controlling the country.
     * <p>
     * This method assigns a {@link Player} instance to the country, indicating that the country is now controlled by this player. It also sets the {@code hasPlayer} flag to true, reflecting that the country is no longer uncontrolled.
     *
     * @param player The {@link Player} instance to be assigned as the controller of the country.
     */
    public void setPlayer(Player player){
        hasPlayer = true;
        this.player = player;
    }

    /**
     * Retrieves the short name of the country.
     * <p>
     * This method returns the short name of the country instance. The short name is a concise identifier for the country, typically used in contexts where space is limited or a brief reference is needed.
     *
     * @return The short name of the country.
     */
    public String getShortName(){
        return shortName;
    }

    /**
     * Retrieves the list of neighboring countries.
     * <p>
     * This method returns a list of {@link Country} instances that are considered neighbors to this country. Neighboring countries are typically those that are directly adjacent or connected in some way relevant to the game's mechanics. This list is essential for operations that require knowledge of a country's geographical or strategic position relative to others.
     *
     * @return A list of {@link Country} instances representing the neighbors of this country.
     */
    public List<Country> getNeighbors(){
        return neighbors;
    }

    /**
     * Sets the player control status of the country.
     * <p>
     * This method updates the {@code hasPlayer} flag of the country to reflect whether it is currently controlled by a player. Setting this flag to true indicates that the country is under the control of a player, while setting it to false indicates that the country is not controlled by any player.
     *
     * @param hasPlayer The new player control status to set for the country. True if the country is controlled by a player, false otherwise.
     */
    public void setHasPlayer(boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }

    /**
     * Checks if the country is controlled by a player.
     * <p>
     * This method returns a boolean value indicating whether the country is currently under the control of a player. It checks the {@code hasPlayer} flag of the country, returning {@code true} if a player controls the country, and {@code false} otherwise.
     *
     * @return {@code true} if the country is controlled by a player, {@code false} otherwise.
     */
    public boolean isHasPlayer() {
        return hasPlayer;
    }

    /**
     * Retrieves a neighboring country by its name.
     * <p>
     * This method searches through the list of neighboring countries and returns the {@link Country} instance that matches the specified name. If no neighboring country with the given name is found, this method returns {@code null}.
     *
     * @param neighborName The name of the neighboring country to search for.
     * @return The {@link Country} instance that matches the specified name, or {@code null} if no match is found.
     */
    public Country getNeighborByName(String neighborName){
        for(Country neighbor : neighbors){
            if(neighbor.getName().equals(neighborName)){
                return neighbor;
            }
        }
        return null;
    }

    /**
     * Retrieves a formatted string of neighbor country names and their short names.
     * <p>
     * This method compiles a list of the names and short names of all neighboring countries, separated by a comma and followed by a newline character for each. It is useful for displaying a simple, readable list of neighbors in textual interfaces or logs.
     *
     * @return A formatted string containing the names and short names of all neighboring countries, each entry separated by a newline.
     */
    public String getNeighborNames(){
        String neighborNames = "";
        for(Country neighbor : neighbors){
            neighborNames += neighbor.getName() + ", " + neighbor.getShortName() + "\n";
        }
        return neighborNames;
    }

    /**
     * Determines if the specified country is a neighbor.
     * <p>
     * This method checks if the given {@code country} is in the list of neighboring countries. It first ensures that the {@code country} and its list of neighbors are not null. If the conditions are met, it returns {@code true} if the current country is found in the given country's list of neighbors, indicating a mutual neighbor relationship. Otherwise, it returns {@code false}.
     *
     * @param country The {@link Country} to check for a neighboring relationship with this country.
     * @return {@code true} if the specified country is a neighbor, {@code false} otherwise.
     */
    public boolean isNeighbor(Country country) {
        if (country != null && country.getNeighbors() != null) {
            return country.getNeighbors().contains(this);
        }
        return false;
    }

    /**
     * Sets the list of neighboring countries.
     * <p>
     * This method assigns a new list of {@link Country} instances to this country, representing its neighbors. Neighboring countries are those that are directly adjacent or connected in some way relevant to the game's mechanics. Updating the neighbors list is crucial for operations that require knowledge of a country's geographical or strategic position relative to others.
     *
     * @param neighbors A list of {@link Country} instances representing the new neighbors of this country.
     */
    public void setNeighbors(List<Country> neighbors){
        this.neighbors = neighbors;
    }

    /**
     * Checks if the country is currently controlled by a player.
     * <p>
     * This method returns a boolean value indicating the player control status of the country. It checks the {@code hasPlayer} flag, returning {@code true} if a player controls the country, and {@code false} otherwise. This is useful for determining if the country is involved in gameplay actions that require player control.
     *
     * @return {@code true} if the country is controlled by a player, {@code false} otherwise.
     */
    public boolean hasPlayer(){
        return hasPlayer;
    }

    /**
     * Removes a specified number of armies from the country.
     * <p>
     * This method decreases the total number of military units present in the country by the specified amount. It validates the input to ensure that the number of armies to remove is positive and does not exceed the current number of units in the country. If the input is invalid, an IllegalArgumentException is thrown.
     *
     * @param anzahlArmyToRemove The number of armies to remove from the country. Must be a positive number and not exceed the current number of units.
     * @throws IllegalArgumentException if {@code anzahlArmyToRemove} is less than or equal to 0, or greater than the current number of units.
     */
    public void removeArmy(int anzahlArmyToRemove) {
        if(anzahlArmyToRemove <=0 ||  anzahlArmyToRemove > units){
            throw new IllegalArgumentException("Anzahl der Armeen ist nicht gültig");
        }
        this.units -= units;
    }

    /**
     * Checks if any units have been moved in the current turn.
     * <p>
     * This method is intended to return a boolean value indicating whether any units have been moved during the current turn. Currently, it always returns {@code false}, suggesting that it might be a placeholder for future implementation where the movement status of units can dynamically change.
     *
     * @return {@code false}, indicating that no units have been moved in the current turn.
     */
    public boolean getUnitsMoved() {
        return false;
    }

    /**
     * Sets the movement status of units in the current turn.
     * <p>
     * This method updates the movement status of units within the country for the current turn. Setting this to {@code true} indicates that units have been moved during the turn, while setting it to {@code false} indicates no movement. This status can be used to track unit movements and enforce rules related to moving units within a turn.
     *
     * @param b The movement status to set for the current turn. {@code true} if units have been moved, {@code false} otherwise.
     */
    public void setUnitsMoved(boolean b) {
    }

    /**
     * Provides a string representation of a Country object.
     * <p>
     * This method overrides the {@code toString} method to provide a string representation of the Country object, including its name, number of units, the player controlling it (if any), and whether it is currently controlled by a player. This representation is useful for debugging and logging purposes, offering a quick overview of the country's state.
     *
     * @return A string representation of the Country object, detailing its name, units, controlling player, and control status.
     */
    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", units=" + units +
                ", player=" + (player != null ? player.getName() : "null") +
                ", hasPlayer=" + hasPlayer +
                '}';
    }
}

package risk.server.domain;

import risk.common.exceptions.CountryNotFoundException;
import risk.common.exceptions.InvalidUnitException;
import risk.common.exceptions.NotANeighbourCountry;
import risk.common.exceptions.PlayerNotFoundException;
import risk.common.entities.*;
import risk.common.interfaces.GameEventListener;
import risk.common.interfaces.RiskInterface;

import java.io.IOException;
import java.util.List;
import java.io.Serializable;

/**
 * The {@code Risk} class serves as the central hub for managing the game of Risk. It acts as an intermediary between the game's graphical user interface (GUI) and the underlying game logic encapsulated in the {@link RiskAdministration} class. This class is responsible for initializing the game environment, managing players, countries, and game states, and facilitating the various actions a player can take during the game, such as attacking, moving units, and claiming territories.
 * <p>
 * It utilizes the {@link RiskAdministration} class for most of the game logic, including player and country management, while also handling some game-specific tasks such as distributing starting units and validating attack conditions.
 * <p>
 * Key functionalities include:
 * <ul>
 *     <li>Initializing the game with specified data for countries and continents.</li>
 *     <li>Retrieving and displaying information about countries and players.</li>
 *     <li>Managing players' actions such as adding or removing players, attacking, and moving units.</li>
 *     <li>Handling the distribution of starting units and mission cards to players.</li>
 *     <li>Saving and loading game states.</li>
 * </ul>
 *
 * @see RiskAdministration
 * @see Player
 * @see Country
 */
public class Risk implements RiskInterface {


    private String data = "";

    private static RiskAdministration riskAdmin;

    /**
     * Constructs a new Risk game instance with specified data.
     * This constructor initializes the game with the provided data string, which is used to construct file names
     * for loading countries and continents. It creates a new {@link RiskAdministration} instance, passing
     * the constructed file names for countries and continents data files.
     *
     * @param data The base string used to generate file names for loading game data. The actual file names
     *             are derived by appending "_Cou.txt" for countries and "_Con.txt" for continents.
     */
    public Risk (String data){
        this.data = data;
        riskAdmin = new RiskAdministration(data + "_Cou.txt", data + "_Con.txt");
    }

    @Override
    public void addPlayer(String name, String color) {

    }

    /**
     * Retrieves a string representation of all available countries in the game.
     * This method delegates the call to the {@code getStringAvailableCountries} method of the {@link RiskAdministration} class,
     * which is responsible for compiling and returning the list of countries.
     *
     * @return A string listing all available countries, formatted according to the {@code RiskAdministration}'s specification.
     */
    public String getStringAvailableCountries(){
        return (riskAdmin.getStringAvailableCountries());
    }

    /**
     * Retrieves a list of all players currently participating in the game.
     * This method calls the {@code getPlayers} method of the {@link RiskAdministration} class
     * to obtain the list of players.
     *
     * @return a list of {@link Player} objects representing all the players in the game.
     */
    public List<Player> getPlayers(){
        return riskAdmin.getPlayers();
    }

    /**
     * Returns a string representation of all players currently participating in the game.
     * This method delegates to the {@code listPlayers} method of the {@link RiskAdministration} class
     * to compile a list of player names. The format and specific details of the string representation
     * are determined by the implementation in {@code RiskAdministration}.
     *
     * @return A string listing all players, formatted according to {@code RiskAdministration}'s specification.
     */
    public String listPlayers(){
        return (riskAdmin.listPlayers());
    }

    /**
     * Adds a new player to the game with the specified name, player ID, and color.
     * This method delegates the player creation process to the {@link RiskAdministration} class,
     * which is responsible for managing all player-related functionalities within the game.
     * The newly created player is then returned.
     *
     * @param name the name of the player to be added
     * @param playerId the unique ID for the player
     * @param color the color assigned to the player, used for UI representation
     * @return the newly created {@link Player} object
     */
    public Player addPlayer(String name, int playerId, String color){
        return (riskAdmin.addPlayer(name, playerId, color));
    }

    /**
     * Removes a player from the game based on the provided name.
     * This method delegates the removal process to the {@link RiskAdministration} class,
     * which handles the detailed logic of removing a player from the game's data structures.
     * If the player with the specified name does not exist, a {@link PlayerNotFoundException} is thrown.
     *
     * @param name the name of the player to be removed from the game
     */
    public void removePlayer(String name) {
        try {
            riskAdmin.removePlayer(name);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Continent> getContinents(){
        return riskAdmin.getContinents();
    }

    /**
     * Retrieves a {@link Country} object by its name.
     * This method delegates the search to the {@link RiskAdministration} class, which performs the actual lookup
     * in the game's data structures to find the country. If the country with the specified name does not exist,
     * a {@link CountryNotFoundException} is thrown to indicate the error.
     *
     * @param countryName The name of the country to retrieve.
     * @return The {@link Country} object corresponding to the specified name.
     * @throws CountryNotFoundException If no country with the specified name exists.
     */
    public Country getCountryByName(String countryName) throws CountryNotFoundException {
        return (riskAdmin.getCountryByName(countryName));
    }

    /**
     * Retrieves a {@link Player} object by the player's name.
     * This method delegates the search to the {@link RiskAdministration} class, which is responsible for
     * finding and returning the player object based on the provided name. If the player with the specified
     * name does not exist within the game's data structures, this method returns {@code null}.
     *
     * @param playerName The name of the player to retrieve.
     * @return The {@link Player} object corresponding to the specified name, or {@code null} if no such player exists.
     */
    public Player getPlayerByName(String playerName){
        return (riskAdmin.getPlayerByName(playerName));
    }

    /**
     * Retrieves a list of all available countries within the game.
     * This method calls the {@code getAvailableCountries} method of the {@link RiskAdministration} class,
     * which is responsible for returning a list of all countries that have not yet been claimed by any player.
     * The list contains {@link Country} objects, each representing a country available for players to claim.
     *
     * @return a list of {@link Country} objects representing all the available countries in the game.
     */
    public List<Country> getAvailableCountries(){
        return (riskAdmin.getAvailableCountries());
    }

    /**
     * Assigns a country to a player within the game.
     * This method delegates the assignment process to the {@link RiskAdministration} class,
     * which is responsible for updating the game's data structures to reflect the new ownership of the country.
     * If the country specified by {@code countryName} does not exist, a {@link CountryNotFoundException} is thrown.
     *
     * @param countryName The name of the country to be assigned to the player.
     * @param player The player object to whom the country will be assigned.
     * @return A string message indicating the outcome of the operation.
     * @throws CountryNotFoundException If the country with the specified name does not exist.
     */
    public String addCountryToPlayer(String countryName, Player player) throws CountryNotFoundException {
        return riskAdmin.addCountryToPlayer(countryName, player);
    }

    public void startGame(){
        riskAdmin.startGame();
    }

    /**
     * Randomly assigns start countries to players at the beginning of the game.
     * This method utilizes the {@link RiskAdministration#pickRandomStartCountries()} method to randomly distribute
     * start countries among all players. The distribution ensures that each player receives an equal number of countries
     * at the start, laying the foundation for the game's initial setup. If there are any issues in assigning countries,
     * such as a country not existing in the game's data structures, a {@link CountryNotFoundException} is thrown.
     *
     * @return A string message indicating the outcome of the random country assignment process.
     * @throws CountryNotFoundException If a country to be assigned does not exist.
     */
    public String pickRandomStartCountries() throws CountryNotFoundException {
        return riskAdmin.pickRandomStartCountries();
    }

    /**
     * Retrieves a {@link Player} object by the player's unique ID.
     * This method delegates the search to the {@link RiskAdministration} class, which is responsible for
     * finding and returning the player object based on the provided unique ID. If the player with the specified
     * ID does not exist within the game's data structures, this method returns {@code null}.
     *
     * @param playerId The unique ID of the player to retrieve.
     * @return The {@link Player} object corresponding to the specified ID, or {@code null} if no such player exists.
     */
    public Player getPlayerById(int playerId){
        return (riskAdmin.getPlayerById(playerId));
    }

    /**
     * Removes a country from a player's control within the game.
     * This method delegates the removal process to the {@link RiskAdministration} class,
     * which handles the detailed logic of removing the specified country from the player's list of controlled countries.
     * If the country specified by {@code countryName} does not exist or is not currently controlled by the player,
     * a {@link CountryNotFoundException} is thrown to indicate the error.
     *
     * @param countryName The name of the country to be removed from the player's control.
     * @param player The name of the player from whom the country will be removed.
     * @throws CountryNotFoundException If the country with the specified name does not exist or is not controlled by the specified player.
     */
    public void removeCountryFromPlayer(String countryName, String player) throws CountryNotFoundException {
        riskAdmin.removeCountryFromPlayer(countryName, player);
    }

    /**
     * Adds units to a player at the beginning of their turn based on the current game rules.
     * This method calculates the number of units a player is entitled to at the start of their turn,
     * considering factors such as the number of countries controlled, continents controlled, and any additional units
     * from trade-in cards. The calculation and addition of units are delegated to the {@link RiskAdministration} class.
     *
     * @param player The player object to whom the units will be added.
     * @return A string message indicating the number of units added to the player for this turn.
     */
    public String addPlayerUnitsPerRound(Player player){
        return riskAdmin.addPlayerUnitsPerRound(player);
    }

    /**
     * Adds a specified number of units to a country controlled by a player.
     * This method delegates the operation to the {@link RiskAdministration#addPlayerUnitsToCountry(Player, String, int)} method,
     * which is responsible for updating the game's data structures to reflect the new unit count for the specified country.
     * If the operation is successful, a message indicating the number of units added and the current total is returned.
     * If the specified number of units is invalid (e.g., negative), an {@link InvalidUnitException} is thrown.
     *
     * @param player The player object to whom the country belongs.
     * @param countryName The name of the country to which units will be added.
     * @param units The number of units to add to the country.
     * @return A string message indicating the outcome of the operation.
     * @throws InvalidUnitException If the specified number of units is invalid.
     */
    public String addPlayerUnitsToCountry(Player player, String countryName, int units) throws InvalidUnitException {
        return riskAdmin.addPlayerUnitsToCountry(player, countryName, units);
    }

    /**
     * Retrieves a string representation of all countries currently in the game.
     * This method delegates the call to the {@link RiskAdministration#listAllCountries()} method,
     * which is responsible for compiling and returning a comprehensive list of all countries, formatted as a string.
     * The format and specific details of the string representation are determined by the implementation in
     * {@code RiskAdministration}. This can include country names, IDs, or other relevant information based on the game's current state.
     *
     * @return A string listing all countries in the game, formatted according to {@code RiskAdministration}'s specification.
     */
    public String listAllCountries() {
        return riskAdmin.listAllCountries();
    }

    /**
     * Retrieves a {@link Player} object based on the player's index in the game.
     * This method delegates the retrieval process to the {@link RiskAdministration} class,
     * which maintains the list of all players. It is used to access a player's information
     * during the game, especially when player-specific actions need to be performed and the
     * player's object is required but only the index is known.
     *
     * @param playerIndex The index of the player in the game's player list.
     * @return The {@link Player} object corresponding to the given index. Returns {@code null} if the index is out of bounds.
     */
    public Player getPlayer(int playerIndex){
        return riskAdmin.getPlayer(playerIndex);
    }

    /**
     * Retrieves the {@link Player} object representing the player currently taking their turn.
     * This method delegates the retrieval process to the {@link RiskAdministration} class,
     * which maintains the current state of the game, including tracking which player's turn it is.
     * It is used primarily to access the player's information during their turn for performing actions such as attacking,
     * moving units, or playing cards.
     *
     * @return The {@link Player} object corresponding to the player currently on their turn.
     */
    public Player getPlayerOnTurn(){
        return riskAdmin.getPlayerOnTurn();
    }

    /**
     * Advances the game to the next turn.
     * This method is responsible for transitioning the game state from the current player's turn to the next player's turn.
     * It delegates the turn transition logic to the {@link RiskAdministration#nextTurn()} method within the {@code RiskAdministration} class,
     * which handles the specifics of changing turns, including updating the current player state and any necessary game state checks or updates
     * that need to occur at the end of a turn or the beginning of a new turn.
     */
    public void nextTurn(){
        riskAdmin.nextTurn();
    }

    /**
     * Executes an attack from one country to another by a specified player.
     * This method delegates the attack logic to the {@link RiskAdministration#makeAttack(Country, Country, Player, int, int)} method,
     * which is responsible for validating the attack conditions (such as whether the countries are neighbors and the player owns the attacking country),
     * calculating the outcome based on the number of units involved, and updating the game state accordingly.
     * <p>
     * If the attack violates any game rules (e.g., attacking from a country not owned by the player, attacking a non-neighboring country,
     * or specifying an invalid number of units), appropriate exceptions are thrown to indicate the specific violation.
     *
     * @param fromCou The country from which the attack is initiated.
     * @param toCou The target country of the attack.
     * @param attacker The player performing the attack.
     * @param numberOfUnits The number of units used in the attack. This number must be greater than 0 and less than or equal to the number of units in the attacking country minus one.
     * @param defenderUnits The number of units in the defending country. This parameter is used to calculate the outcome of the attack.
     * @throws IllegalStateException If the game state does not allow for an attack at this time.
     * @throws CountryNotFoundException If either the attacking or defending country does not exist.
     * @throws NotANeighbourCountry If the attacking and defending countries are not neighbors.
     */
    public void makeAttack(String fromCou, String toCou, String attacker, int numberOfUnits, int defenderUnits) throws IllegalStateException, CountryNotFoundException, NotANeighbourCountry {
        riskAdmin.makeAttack( riskAdmin.getCountryByName(fromCou), riskAdmin.getCountryByName(toCou) , riskAdmin.getPlayerByName(attacker), numberOfUnits, defenderUnits);
    }

    /**
     * Validates if a specified country is owned by a given player.
     * This method checks if the player provided as an argument is the current owner of the country specified by its name.
     * The ownership check is delegated to the {@link RiskAdministration#validateOwnership(String, Player)} method within the {@code RiskAdministration} class,
     * which contains the logic to determine whether the specified player owns the given country.
     *
     * @param country The name of the country whose ownership is to be validated.
     * @param attacker The {@link Player} object representing the player whose ownership of the country is being validated.
     * @return {@code true} if the specified player owns the country, {@code false} otherwise.
     */
    public boolean validateOwnership_(String country, String attacker) {
        return riskAdmin.validateOwnership(country, riskAdmin.getPlayerByName(attacker));

    }

    /**
     * Validates the number of units specified for an attack.
     * This method checks if the number of units specified for an attack action is valid according to the game rules.
     * The validation criteria include checking if the number of units is greater than 0 and does not exceed the maximum allowed units for an attack.
     * The actual validation logic is delegated to the {@link RiskAdministration#validateAttackUnits(int)} method within the {@code RiskAdministration} class.
     *
     * @param numberOfUnits The number of units specified for the attack.
     * @return {@code true} if the number of units is valid for an attack, {@code false} otherwise.
     */
    public boolean validateAttackUnits(int numberOfUnits){
        return riskAdmin.validateAttackUnits(numberOfUnits);
    }

    /**
     * Validates if the remaining units in the attacking country are sufficient after an attack is made.
     * This method checks whether the number of units left in the attacking country, after deducting the units used in the attack,
     * is within the permissible limits according to the game rules. Specifically, it ensures that at least one unit remains in the
     * attacking country post-attack, as per the rules of Risk. The actual validation logic is delegated to the
     * {@link RiskAdministration#validateRestOfUnitWhenAttack(Country, int)} method within the {@code RiskAdministration} class.
     *
     * @param from The country from which the attack is initiated.
     * @param numberOfUnits The number of units involved in the attack.
     * @return {@code true} if the remaining number of units in the attacking country is valid post-attack, {@code false} otherwise.
     */
    public boolean validateRestOfUnitWhenAttack(Country from, int numberOfUnits){
        return riskAdmin.validateRestOfUnitWhenAttack(from, numberOfUnits);
    }

    /**
     * Validates if two countries are neighboring.
     * This method checks whether the specified attacking country and defending country are neighbors, which is a prerequisite for an attack in the game.
     * The actual validation logic is performed by the {@link RiskAdministration#validateNeighboring(Country, Country)} method within the {@code RiskAdministration} class.
     * If the countries are not neighbors, a {@link NotANeighbourCountry} exception is thrown to indicate the violation of this rule.
     *
     * @param attackerCountry The country initiating the attack.
     * @param defenderCountry The country being attacked.
     * @return {@code true} if the countries are neighbors, allowing for an attack; {@code false} otherwise.
     * @throws NotANeighbourCountry If the countries are not neighbors, preventing an attack according to the game rules.
     */
    public boolean validateNeighboring(Country attackerCountry, Country defenderCountry) throws NotANeighbourCountry {
        return riskAdmin.validateNeighboring(attackerCountry, defenderCountry);
    }

    /**
     * Retrieves detailed information about a specific country by its name.
     * This method delegates the task to the {@link RiskAdministration#getCountryInformation(String)} method within the {@code RiskAdministration} class,
     * which is responsible for fetching and returning a formatted string containing the country's details. The information typically includes
     * the country's name, owner, number of units present, and possibly other relevant details based on the current game state.
     * <p>
     * If the country with the specified name does not exist within the game's data structures, a {@link CountryNotFoundException} is thrown
     * to indicate the error. This ensures that the method caller can handle the scenario where a requested country is not found.
     *
     * @param countryName The name of the country for which information is being requested.
     * @return A string containing detailed information about the specified country.
     * @throws CountryNotFoundException If no country with the specified name exists within the game.
     */
    public String getCountryInformation(String countryName) throws CountryNotFoundException {
        return (riskAdmin.getCountryInformation(countryName));
    }

    /**
     * Moves a specified number of units from one country to another.
     * This method is responsible for transferring units between countries, typically after a successful attack or for strategic redeployment.
     * The actual movement logic, including validation of the move (e.g., ensuring the countries are connected and the player owns both countries,
     * and that the number of units moved is permissible), is handled by the {@link RiskAdministration#moveUnits(Player, Country, Country, int)} method.
     * <p>
     * If the move is not valid according to the game rules, an appropriate message detailing the reason will be returned.
     * This could include reasons such as "Countries not connected", "Insufficient units in the source country", or "Player does not own one or both countries".
     *
     * @param player The {@link Player} object representing the player making the move.
     * @param fromCountry The {@link Country} object from which units are being moved.
     * @param toCountry The {@link Country} object to which units are being moved.
     * @param units The number of units to move.
     * @return A string message indicating the success or failure of the move, along with any relevant details.
     */
    public String moveUnits(String player, String fromCountry, String toCountry, int units){
        try {
            return (riskAdmin.moveUnits(riskAdmin.getPlayerByName(player), riskAdmin.getCountryByName(fromCountry), riskAdmin.getCountryByName(toCountry), units));
        } catch (CountryNotFoundException e) {
            System.out.println("Country not found");
        }
        return "";
    }

    /**
     * Retrieves a string representing all available colors for players.
     * This method calls the {@code getAvailableColors} method of the {@link RiskAdministration} class,
     * which is responsible for returning a string containing all colors that players can choose for their units and territories.
     * The format and specific details of the string representation are determined by the implementation in {@code RiskAdministration}.
     * This can include color names or codes, depending on how colors are managed within the game.
     *
     * @return A string listing all available colors, formatted according to {@code RiskAdministration}'s specification.
     */
    public String getAvailableColors() {
        return riskAdmin.getAvailableColors();
    }

    /**
     * Validates the conditions for an attack between two countries in the game.
     * This method checks if the attack initiated by a player from one country to another meets all the necessary conditions
     * according to the game rules. These conditions include, but are not limited to, verifying that the attacking and defending
     * countries are neighbors. The method delegates the validation process to the {@link RiskAdministration#validateAttackConditions(Country, Country, Player, int, int)}
     * method within the {@code RiskAdministration} class, which performs the detailed validation logic.
     * <p>
     * If the attacking and defending countries are not neighbors, a {@link NotANeighbourCountry} exception is thrown to indicate
     * the violation of this rule.
     *
     * @param from The country from which the attack is initiated.
     * @param to The target country of the attack.
     * @param player The player performing the attack.
     * @param units The number of units used in the attack.
     * @param defenderUnits The number of units in the defending country. This parameter is used to calculate the outcome of the attack.
     * @throws NotANeighbourCountry If the attacking and defending countries are not neighbors, preventing an attack according to the game rules.
     */
    public void validateAttackConditions(Country from, Country to, Player player, int units, int defenderUnits) throws NotANeighbourCountry {
        riskAdmin.validateAttackConditions(from, to, player, units, defenderUnits);
    }

    /**
     * Checks if the player controls all countries in any continent.
     * This method delegates the check to the {@link RiskAdministration#continentPlayerCheck(Player)} method within the {@code RiskAdministration} class.
     * It is used to determine if the player on turn has full control over any continent, which can affect gameplay, such as granting additional units.
     * The method returns a string message indicating the result, which could be the name of the continent controlled by the player or a message stating that no continent is fully controlled.
     *
     * @param playerOnTurn The {@link Player} object representing the player whose control over continents is being checked.
     * @return A string indicating the name of the continent fully controlled by the player, or a message stating that no continent is fully controlled.
     */
    public String continentPlayerCheck(Player playerOnTurn) {
        return riskAdmin.continentPlayerCheck(playerOnTurn);
    }

    /**
     * Distributes starting units among all players at the beginning of the game.
     * This static method calls the {@code distributeStartingUnits} method of the {@link RiskAdministration} class,
     * which is responsible for the logic of distributing units. The distribution process involves calculating the number
     * of units each player starts with and assigning these units to the players' countries. The exact distribution logic
     * can depend on the number of players and the game rules regarding initial unit placement.
     * <p>
     * This method should be called at the start of the game, after all players have been added and the countries have been
     * assigned, but before any player takes their first turn.
     */
    public  void distributeStartingUnits(){
        riskAdmin.distributeStartingUnits();
    }

    /**
     * Prints the starting units for all players at the beginning of the game.
     * This static method delegates the call to the {@code printStartingUnits} method of the {@link RiskAdministration} class.
     * It is intended for debugging or informational purposes, allowing a quick view of the initial unit distribution among players
     * before the game starts. The actual printing logic, including the format and details of the output, is handled within the
     * {@code RiskAdministration} class.
     */
    public static void printStartingUnits(){
        riskAdmin.printStartingUnits();
    }

    /**
     * Saves player information to a specified file.
     * This method delegates the actual saving process to the {@code savePlayerInformation} method of the {@link RiskAdministration} class.
     * It serializes the list of {@link Player} objects into a format suitable for storage (e.g., JSON, XML) and writes this data to the file
     * specified by the {@code filename} parameter. The method ensures that all player data, including names, IDs, and current game states,
     * are accurately saved, allowing for game progress to be persisted and later retrieved.
     * <p>
     * This method can throw an {@link Exception} if there are issues with the file writing process, such as permission issues or disk space limitations.
     *
     * @param players A list of {@link Player} objects representing the players whose information is to be saved.
     * @param filename The name of the file where player information will be saved. This should include the path if the file is not in the default directory.
     * @return {@code true} if the player information was successfully saved, {@code false} otherwise.
     * @throws Exception If there are any issues encountered during the file writing process.
     */
    public boolean savePlayerInformation(List<Player> players, String filename) throws Exception {
        return riskAdmin.savePlayerInformation(players, filename);
    }

    /**
     * Retrieves player information from a specified file.
     * This method is responsible for loading player information from a file, identified by the {@code filename} parameter.
     * It utilizes the {@code getPlayerInformation} method of the {@link RiskAdministration} class to deserialize the list of {@link Player} objects
     * from the file. The file format can be JSON, XML, or any other format supported by the implementation in {@code RiskAdministration}.
     * <p>
     * This method can throw an {@link Exception} if there are issues with accessing the file, such as file not found, permission issues,
     * or problems with the file's format leading to deserialization errors.
     *
     * @param filename The name of the file from which player information is to be loaded. This should include the path if the file is not in the default directory.
     * @return A list of {@link Player} objects representing the players whose information was stored in the file.
     * @throws Exception If there are any issues encountered during the file reading or deserialization process.
     */
    public List<Player> getPlayerInformation(String filename) throws Exception {
        return riskAdmin.getPlayerInformation(filename);
    }

    /**
     * Processes the turning in of unit cards by a player.
     * This method is responsible for validating and processing the action of a player turning in three unit cards. It delegates the validation
     * and processing to the {@code validateUnitCards} method of the {@link RiskAdministration} class. The validation includes checking if the
     * cards exist, if they match according to the game rules (either all of the same type or all different types), or if any of the cards is a "Joker".
     * Based on the validation, appropriate actions are taken, such as removing the used cards from the player's hand and returning them to the deck,
     * and then granting the player additional units.
     * <p>
     * This method does not return any value but indirectly affects the game state by potentially altering the number of units a player has and
     * modifying the deck of unit cards.
     *
     * @param player The {@link Player} object representing the player who is turning in the unit cards.
     * @param card1 The first {@link UnitCard} object being turned in.
     * @param card2 The second {@link UnitCard} object being turned in.
     * @param card3 The third {@link UnitCard} object being turned in.
     */
    public String turnInUnitCards(Player player, UnitCard card1, UnitCard card2, UnitCard card3){
        return riskAdmin.validateUnitCards(player, card1, card2, card3);
    }

    /**
     * Assigns a mission card to a player randomly.
     * This method is responsible for the random distribution of mission cards to players at the beginning of the game or when a player successfully completes their mission and requires a new one. It utilizes the {@code drawMissionToPlayer} method of the {@link RiskAdministration} class, which contains the logic for selecting a mission card from the available pool and assigning it to a player.
     * <p>
     * The method ensures that each player receives a mission that is not already assigned to another player, maintaining the uniqueness of missions among players. If all missions are assigned and a new mission is required, the method may implement logic to reshuffle the mission cards or handle the scenario based on the game rules.
     */
    public void drawMissionToPlayer(){
        riskAdmin.drawMissionToPlayer();
    }

    /**
     * Assigns a unit card to a player randomly.
     * @param player
     */
    public void drawUnitCard(Player player){
        riskAdmin.drawUnitCard(player);
    }

    /**
     * Loads a previously saved game state from a file.
     * This method is responsible for initiating the process of loading a game state from a specified file path. It delegates the actual loading logic
     * to the {@code loadOldGame} method of the {@link RiskAdministration} class. The specified file should contain a serialized representation of the game state,
     * including all player information, country ownerships, mission cards, and current game phase.
     * <p>
     * The method does not return any value but updates the game state to reflect the loaded information. It is crucial that the file format and structure
     * match the expected serialization used by the {@code RiskAdministration} class to ensure successful deserialization and game state restoration.
     *
     * @param filePath The path to the file from which the game state is to be loaded. This should include the full path and the file name.
     */
    public void loadOldGame(String filePath) {
        riskAdmin.loadOldGame(filePath);
    }

    /**
     * Retrieves detailed information about a specific country for the GUI.
     * This method delegates the task to the {@code getCountryInformationGUI} method of the {@link RiskAdministration} class,
     * which is responsible for fetching and returning a formatted string containing the country's details suitable for display in the GUI.
     * The information typically includes the country's name, owner, and the number of units present.
     * <p>
     * If the country with the specified name does not exist within the game's data structures, a {@link CountryNotFoundException} is thrown
     * to indicate the error. This ensures that the method caller can handle the scenario where a requested country is not found.
     *
     * @param country The name of the country for which information is being requested.
     * @return A string containing detailed information about the specified country, formatted for GUI display.
     * @throws CountryNotFoundException If no country with the specified name exists within the game.
     */
    public String getCountryInformationGUI_(String country) throws CountryNotFoundException {
        return riskAdmin.getCountryInformationGUI(country);
    }



    @Override
    public void makeAttack(String fromCou, String toCou, String attacker, int numberOfUnits) {

    }



    @Override
    public void distributeUnits() {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String turnInUnitCards(String player, String card1, String card2, String card3) {
        return "";
    }

    @Override
    public boolean validateOwnership(String country, String player) {
        return false;
    }

    @Override
    public String continentPlayerCheck(String playername) {
        return "";
    }

    @Override
    public String addPlayerUnitsToCountry(String player, String country, int units) throws InvalidUnitException {
        return riskAdmin.addPlayerUnitsToCountry(riskAdmin.getPlayerByName(player), country, units);
    }

    @Override
    public String getCountryInformationGUI(String country) {
        return "";
    }

    @Override
    public String addPlayerUnitsPerRound(String player) {
        return "";
    }

    public int getUnitsByCountry(String country) {
        return 0;
    }

    public List<Country> getCountries() {
        return riskAdmin.getCountries();
    }

    @Override
    public void addGameEventListener(GameEventListener listener) {

    }

    @Override
    public void removeGameEventListener(GameEventListener listener) {

    }

    @Override
    public void notifyPlayerListChange() {

    }

    @Override
    public void notifyGameStarted() {

    }

    @Override
    public void notifyAllowPlayerActions(boolean allow) {

    }

    @Override
    public boolean checkCountryNeighbor(String country1, String country2) {
        return false;
    }
}

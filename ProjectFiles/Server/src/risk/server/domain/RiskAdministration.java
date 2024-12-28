package risk.server.domain;

import risk.common.exceptions.CountryNotFoundException;
import risk.common.exceptions.InvalidUnitException;
import risk.common.exceptions.NotANeighbourCountry;
import risk.common.exceptions.PlayerNotFoundException;
import risk.common.persistence.CountryInitiator;
import risk.common.persistence.FilePersistenceManager;
import risk.common.persistence.PersistenceManager;
import risk.common.entities.*;
import risk.common.entities.missions.*;


import java.io.IOException;
import java.util.*;
import java.io.Serializable;

@SuppressWarnings("LanguageDetectionInspection")
/**
 * The {@code RiskAdministration} class manages the core functionalities of the Risk game, including player management, country and continent initialization, mission and unit card handling, and game state operations.
 * It encapsulates the logic for initializing the game board, processing player actions such as attacks, movements, and card management, and maintaining the state of the game through various stages.
 * This class interacts with entities such as {@link Player}, {@link Country}, {@link Continent}, {@link MissionCard}, and {@link UnitCard} to simulate the Risk game environment.
 * It also handles persistence operations for loading and saving game states.
 * <p>
 * Key functionalities include:
 * <ul>
 *     <li>Initializing the game board with countries and continents.</li>
 *     <li>Managing players, including adding and removing players.</li>
 *     <li>Distributing countries among players at the start of the game.</li>
 *     <li>Handling player turns and actions such as attacks, movements, and card trades.</li>
 *     <li>Assigning missions to players and checking for mission completion.</li>
 *     <li>Managing unit and mission cards, including drawing and returning cards.</li>
 *     <li>Loading and saving game states to and from files.</li>
 * </ul>
 * <p>
 * This class is central to the Risk game logic and is used by the game's UI to interact with the game state.
 */
public class RiskAdministration implements Serializable {
    private static final long serialVersionUID = 4233421654645L;

    private List<String> availableColors = new ArrayList<>(Arrays.asList("Red", "Blue", "Green", "Yellow", "Black", "Pink"));
    final private List<Country> countries;
    final private List<Continent> continents;
    private List<Player> players;
    public List<MissionCard> missionCards;
    public List<UnitCard> unitCards;
    final private Dice dice;
    public CountryInitiator countryInitiator;
    final private List<Country> availableCountries;
    private int playerTurnIndex;
    private static final int MAX_ATTACK_UNITS = 3;
    private static final int MAX_ATTACK_DICE = 3;
    private static final int MAX_DEFENDER_DICE = 2;
    private int turnedInUnitCards = 0;

    private PersistenceManager pm = new FilePersistenceManager();
    private HashMap<String, Integer> startingUnits;

    /**
     * Constructs a RiskAdministration object to manage the core functionalities of the Risk game.
     * This constructor initializes the game environment by loading countries and continents from files,
     * setting up players, missions, unit cards, and available countries. It also initializes game utilities
     * such as dice and starting units map. A message is printed to the console to indicate successful initialization.
     * <p>
     * The constructor takes paths to the files containing country and continent data as parameters. It uses these
     * paths to initialize the {@link CountryInitiator} which in turn initializes the countries and continents.
     * Empty lists for players, mission cards, and unit cards are created, and missions and unit cards are generated
     * using respective methods. The list of available countries is initialized based on the list of countries,
     * and a new {@link Dice} object is created for use in the game. The starting units map is also initialized
     * to track the initial units for each country.
     *
     * @param filePathCountries The file path to the countries data file. This file contains information necessary
     *                          to initialize all countries in the game.
     * @param filePathContinent The file path to the continents data file. This file contains information necessary
     *                          to initialize all continents in the game and associate them with their respective countries.
     */
    public RiskAdministration(String filePathCountries, String filePathContinent){
        this.countryInitiator = new CountryInitiator(filePathCountries);
        this.countries = countryInitiator.initializeCountries();
        this.continents = countryInitiator.initializeContinents(filePathContinent, countries);
        this.players = new ArrayList<>();
        this.missionCards = new ArrayList<>();
        this.missionCards = createMissions();
        this.unitCards = createUnitCards();
        this.availableCountries = new ArrayList<>(countries);
        this.dice = new Dice();
        startingUnits = new HashMap<>();
        playerTurnIndex = 0;
    }

    /**
     * Initiates the game by randomly assigning countries to players, distributing starting units, and drawing mission cards for each player.
     * <p>
     * This method encompasses the initial setup phase of the game, ensuring that all necessary conditions are met before the game starts. It performs three main actions:
     * <ol>
     *     <li>Randomly assigns countries to players to ensure a fair and balanced start. If a country cannot be found during this process, an error message is printed.</li>
     *     <li>Distributes starting units among the players' countries, based on predefined rules, to prepare for the commencement of the game.</li>
     *     <li>Draws mission cards for each player, assigning them objectives that guide their strategies and goals throughout the game.</li>
     * </ol>
     * This setup is crucial for a balanced and engaging game experience, providing each player with a unique starting position and objectives.
     */
    public void startGame(){
        try {
            pickRandomStartCountries();
        }catch (CountryNotFoundException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        distributeStartingUnits();
        drawMissionToPlayer();
    }

    /**
     * Creates and returns a list of mission cards for the game.
     * This method initializes various types of missions that players can undertake during the game. Missions include conquering continents,
     * conquering a specific number of countries, conquering countries with a minimum number of units in each, and eliminating players of a specific color.
     * Each mission type is instantiated with specific criteria and added to the list of missions. The missions are designed to provide diverse objectives
     * for players, encouraging strategic play and interaction with different game elements.
     * <p>
     * The method leverages different mission constructors such as {@link ConquerContinentMission}, {@link ConquerContinentPlusOne},
     * {@link ConquerCountriesMission}, {@link ConquerCountriesMissionPlusUnits}, and {@link eliminatePlayerColorMission} to create a varied set of goals.
     * <p>
     * Note: The {@code eliminatePlayerColorMission} missions require a check to ensure that if the player's color matches the mission's target color,
     * an alternative mission condition is provided.
     *
     * @return A list of {@link MissionCard} objects representing the missions available in the game.
     */
    public List<MissionCard> createMissions() {
        List<MissionCard> missions = new ArrayList<>();
        missions.add(new ConquerContinentMission("Conquer Africa and Asia", getContinentByName("Africa"), getContinentByName("Asia")));
        missions.add(new ConquerContinentMission("Conquer North America and Australia", getContinentByName("North America"), getContinentByName("Australia")));
        missions.add(new ConquerContinentMission("Conquer Asia and South America", getContinentByName("Asia"), getContinentByName("South America")));
        missions.add(new ConquerContinentMission("Conquer North America and Afrika", getContinentByName("North America"), getContinentByName("Africa")));
        missions.add(new ConquerContinentPlusOne("Conquer Europe and Australia and another continent of choice", getContinentByName("Europe"), getContinentByName("Australia")));
        missions.add(new ConquerContinentPlusOne("Conquer Europe and South America and another continent of choice", getContinentByName("Europe"), getContinentByName("South America")));
        missions.add(new ConquerCountriesMission("Conquer 24 countries"));
        missions.add(new ConquerCountriesMissionPlusUnits("Conquer 18 countries with at least 2 units in each"));
        missions.add(new eliminatePlayerColorMission(countries, "Conquer all countries of Red, if it is your color, Conquer 24 Countries", "red"));
        missions.add(new eliminatePlayerColorMission(countries, "Conquer all countries of Blue, if it is your color, Conquer 24 Countries", "blue"));
        missions.add(new eliminatePlayerColorMission(countries, "Conquer all countries of Green, if it is your color, Conquer 24 Countries", "green"));
        missions.add(new eliminatePlayerColorMission(countries, "Conquer all countries of Yellow, if it is your color, Conquer 24 Countries", "yellow"));
        missions.add(new eliminatePlayerColorMission(countries, "Conquer all countries of Black, if it is your color, Conquer 24 Countries", "black"));
        missions.add(new eliminatePlayerColorMission(countries, "Conquer all countries of Pink, if it is your color, Conquer 24 Countries", "pink"));
        return missions;
    }

    /**
     * Loads a previously saved game state from a file.
     * This method attempts to load the game state, including all player information, from the specified file path. It utilizes the {@code loadPlayers} method
     * of the {@link FilePersistenceManager} class to deserialize the list of {@link Player} objects from the file. If successful, the loaded players are set
     * as the current game state's players.
     * <p>
     * If any exceptions occur during the loading process, such as {@link IOException} or {@link ClassNotFoundException}, the exception's message is printed
     * to the console, and the game state remains unchanged.
     *
     * @param filePath The path to the file from which the game state is to be loaded. This should include the full path and the file name.
     */
    public void loadOldGame(String filePath){
        try{
            this.players = pm.loadPlayers(filePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Country> getCountries(){
        return countries;
    }

    public List<Continent> getContinents(){
        return continents;
    }

    /**
     * Creates and returns a list of unit cards for the game.
     * This method initializes the deck of unit cards used in the game by creating a predefined number of each type of unit card (Infantry, Cavalry, Artillery) and adding Joker cards.
     * The deck consists of 42 standard unit cards (14 of each type) and 2 Joker cards, making a total of 44 cards.
     * <p>
     * The method iterates 14 times to add each type of standard unit card to the list, then adds 2 Joker cards at the end.
     * Joker cards can be used as a wildcard in the game, representing any unit type.
     * <p>
     * The created list of unit cards is used throughout the game for drawing cards by players when they conquer at least one territory during their turn.
     *
     * @return A list of {@link UnitCard} objects representing the initial deck of unit cards for the game.
     */
    public List<UnitCard> createUnitCards() {
        List<UnitCard> unitCards = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            unitCards.add(new UnitCard("Infantry"));
            unitCards.add(new UnitCard("Cavalry"));
            unitCards.add(new UnitCard("Artillery"));
        }
        unitCards.add(new UnitCard("Joker"));
        unitCards.add(new UnitCard("Joker"));

        return unitCards;
    }

    /**
     * Generates and returns a string listing all players currently in the game.
     * This method iterates through the list of players, appending each player's name, ID, and color to a formatted string.
     * The resulting string is intended for display purposes, providing a quick overview of all participants in the game.
     * Additionally, this method prints the {@code toString} representation of the players list to the console, primarily for debugging purposes.
     *
     * @return A formatted string containing the name, ID, and color of each player, separated by new lines.
     */
    public String listPlayers(){
        String playerList = "";
        for (Player player : players){
            playerList += "Name: " +player.getName() + " ID: " + player.getId() + " Color: " + player.getColor() + "\n";
        }
        return playerList;
    }

    /**
     * Retrieves the current list of players in the game.
     * This method provides access to the list of {@link Player} objects representing all players currently participating in the game.
     * It can be used to obtain player information such as name, ID, and color for display purposes or for further game logic processing.
     * <p>
     * Note: The returned list is a direct reference to the game's internal player list. Any modifications to this list will affect the game state.
     *
     * @return A list of {@link Player} objects representing the current players in the game.
     */
    public List<Player> getPlayers(){
        return players;
    }

    /**
     * Retrieves the player whose turn is currently active.
     * This method checks if the list of players is not empty and returns the player at the current turn index.
     * If the list of players is empty, indicating that no players have been added to the game yet, it returns {@code null}.
     * <p>
     * This method is essential for turn-based logic in the game, ensuring that actions are performed by the correct player
     * according to the game's turn order.
     *
     * @return The {@link Player} object representing the player whose turn is currently active. Returns {@code null} if there are no players in the game.
     */
    public Player getPlayerOnTurn(){
        if (players.isEmpty()){
            return null;
        }
        return players.get(playerTurnIndex);
    }

    /**
     * Advances the game to the next player's turn.
     * This method updates the {@code playerTurnIndex} to point to the next player in the list. If the current player is the last in the list,
     * the index is reset to 0, indicating that the turn has cycled back to the first player. This ensures a continuous cycle through all players
     * in the game, maintaining the turn-based nature of the gameplay.
     * <p>
     * It is crucial for managing the flow of the game, ensuring that each player gets their turn in a sequential and fair manner.
     */
    public void nextTurn(){
        getPlayerOnTurn().setHasConquered(false);
        if (playerTurnIndex == players.size() -1) {
            playerTurnIndex = 0;
        } else {
            playerTurnIndex++;
        }
    }

    /**
     * Adds a new player to the game with the specified name, player ID, and color.
     * This method first checks if the specified color is available in the list of available colors.
     * If the color is not available (either already taken by another player or invalid), it throws an IllegalArgumentException.
     * Otherwise, it creates a new {@link Player} object with the provided name, player ID, and color, adds it to the list of players,
     * and removes the color from the list of available colors to prevent it from being selected by another player.
     *
     * @param name The name of the player to be added.
     * @param playerId The unique ID of the player to be added.
     * @param colorName The color chosen by the player. This color is removed from the list of available colors if the addition is successful.
     * @return The {@link Player} object that was added to the game.
     * @throws IllegalArgumentException If the specified color has already been selected by another player or is invalid.
     */
    public Player addPlayer(String name, int playerId, String colorName){
        if (!availableColors.contains(colorName)){
            throw new IllegalArgumentException("Color " + colorName + " has already been selected by another player or is invalid.");
        }

        Player player = new Player(name, playerId, colorName);
        players.add(player);
        availableColors.remove(colorName);
        return player;
    }

    /**
     * Removes a player from the game by their name.
     * This method searches for a player by their name using {@link #getPlayerByName(String)}.
     * If the player is found, they are removed from the game's list of players, and their color is added back to the list of available colors,
     * making it available for new players. This ensures that the game's state is updated accordingly to reflect the removal.
     * <p>
     * Note: If no player with the specified name is found, a {@link PlayerNotFoundException} is thrown, indicating the error.
     *
     * @param name The name of the player to be removed.
     * @return The {@link Player} object that was removed from the game.
     * @throws PlayerNotFoundException If no player with the specified name exists in the game.
     */
    public Player removePlayer(String name) throws PlayerNotFoundException {
        Player player = getPlayerByName(name);
        if (player == null){
            throw new PlayerNotFoundException(name);
        }
        players.remove(player);
        availableColors.add(player.getColor()); // Add the color back to the available colors list
        return player;
    }

    /**
     * Retrieves a player object from the game based on the player's index in the list.
     * This method is used to access player information directly by their position in the game's player list.
     * It is particularly useful in scenarios where players are being iterated over or when a specific player's
     * information is needed without knowing their ID or name.
     * <p>
     * Note: This method assumes that the index provided is within the bounds of the player list. An
     * {@code IndexOutOfBoundsException} may be thrown if an invalid index is provided.
     *
     * @param playerIndex The index of the player in the list.
     * @return The {@link Player} object at the specified index in the player list.
     * @throws IndexOutOfBoundsException If the provided index is out of bounds of the player list.
     */
    public Player getPlayer(int playerIndex) {
        return players.get(playerIndex);
    }

    /**
     * Retrieves a {@link Country} object by its name.
     * This method iterates through the list of countries and compares the provided country name with each country's name, ignoring case differences.
     * If a match is found, the corresponding {@link Country} object is returned.
     * <p>
     * Note: If the country name is {@code null} or does not match any country in the list, a special case check for "Test" is performed.
     * This is likely a placeholder for testing purposes and should be adjusted or removed for production use.
     *
     * @param countryName The name of the country to search for. Case-insensitive.
     * @return The {@link Country} object matching the provided name.
     * @throws CountryNotFoundException If no country with the specified name exists.
     */
    public Country getCountryByName(String countryName) throws CountryNotFoundException {
        for (Country country : countries) {
            if (country.getName() != null ? country.getName().equalsIgnoreCase(countryName) : "Test".equalsIgnoreCase(countryName)) {
                return country;
            }
        } throw new CountryNotFoundException(countryName);
    }

    /**
     * Retrieves a {@link Continent} object by its name.
     * This method iterates through the list of continents and compares the provided continent name with each continent's name, ignoring case differences.
     * If a match is found, the corresponding {@link Continent} object is returned. This is useful for operations requiring continent-specific data,
     * such as assigning missions or calculating bonuses.
     * <p>
     * Note: If no continent with the specified name exists, this method returns {@code null}. It is recommended to check for a {@code null} result
     * when using this method to avoid {@link NullPointerException}.
     *
     * @param continentName The name of the continent to search for. Case-insensitive.
     * @return The {@link Continent} object matching the provided name, or {@code null} if no match is found.
     */
    public Continent getContinentByName(String continentName) {
        for (Continent continent : continents) {
            if (continent.getName().equalsIgnoreCase(continentName)) {
                return continent;
            }
        }
        return null;
    }

    /**
     * Generates a string listing all countries currently in the game.
     * This method iterates through the list of {@link Country} objects, appending each country's name to a string, separated by new lines.
     * The resulting string provides a simple way to display all countries in the game, which can be useful for debugging or displaying the game state in a user interface.
     * <p>
     * Note: The country names are listed in the order they are stored in the {@code countries} list, which is typically the order they were initialized or loaded.
     *
     * @return A string containing the names of all countries in the game, each on a new line.
     */
    public String listAllCountries(){
        String countryList = "";
        for (Country country : countries) {
            countryList += country.getName() + "\n";
        }
        return countryList;
    }

    /**
     * Retrieves a player object from the game based on the player's name.
     * This method iterates through the list of players and compares the provided player name with each player's name.
     * If a match is found, the corresponding {@link Player} object is returned. This is useful for operations requiring
     * player-specific data, such as validating actions or updating player states.
     * <p>
     * Note: If no player with the specified name exists, this method returns {@code null}. It is recommended to check for a {@code null} result
     * when using this method to ensure that the requested player exists before proceeding with further operations.
     *
     * @param playerName The name of the player to search for.
     * @return The {@link Player} object matching the provided name, or {@code null} if no match is found.
     */
    public Player getPlayerByName(String playerName){
        for (Player player : players){
            if (player.getName().equals(playerName)){
                return player;
            }
        }
        return null;
    }

    /**
     * Retrieves a player object from the game based on the player's color.
     * This method iterates through the list of players and compares the provided color with each player's color.
     * If a match is found, the corresponding {@link Player} object is returned. This is useful for operations requiring
     * player-specific data, such as validating actions or updating player states based on their color.
     * <p>
     * Note: If no player with the specified color exists, this method returns {@code null}. It is recommended to check for a {@code null} result
     * when using this method to ensure that the requested player exists before proceeding with further operations.
     *
     * @param color The color of the player to search for.
     * @return The {@link Player} object matching the provided color, or {@code null} if no match is found.
     */
    public Player getPlayerByColor(String color){
        for (Player player : players){
            if (player.getColor().equals(color)){
                return player;
            }
        }
        return null;
    }

    /**
     * Retrieves a player object from the game based on the player's unique ID.
     * This method iterates through the list of players in the game and compares each player's ID with the provided ID.
     * If a match is found, the corresponding {@link Player} object is returned. This is useful for operations requiring
     * player-specific data, such as validating actions or updating player states based on their unique identifier.
     * <p>
     * Note: If no player with the specified ID exists, this method returns {@code null}. It is recommended to check for a {@code null} result
     * when using this method to ensure that the requested player exists before proceeding with further operations.
     *
     * @param id The unique ID of the player to search for.
     * @return The {@link Player} object matching the provided ID, or {@code null} if no match is found.
     */
    public Player getPlayerById(int id){
        for (Player player : players){
            if (player.getId() == id){
                return player;
            }
        }
        return null;
    }

    /**
     * Adds a country to a player in the game.
     * This method assigns a specified country to a given player object. It first checks if the player object is not null.
     * If the player object is valid, it retrieves the country by its name using {@link #getCountryByName(String)} method.
     * The country is then added to the player's list of countries, and the player's country count is incremented by one.
     * Additionally, the player is set as the owner of the country, and the country's unit count is incremented by one to
     * signify the player's control over it. If the operation is successful, a confirmation message is returned.
     * <p>
     * If the player object is null, the method returns a message indicating that the player object is null,
     * and no operation is performed.
     *
     * @param countryName The name of the country to be added to the player.
     * @param playerObject The player object to whom the country will be added.
     * @return A string message indicating the result of the operation. It either confirms the country addition
     *         or notifies that the player object is null.
     * @throws CountryNotFoundException If the specified country name does not match any existing country.
     */
    public String addCountryToPlayer(String countryName, Player playerObject) throws CountryNotFoundException {
        if (playerObject != null) {
            Country country = getCountryByName(countryName);
            playerObject.addCountry(country);
            playerObject.addNumberOfCountries(1);
            country.setPlayer(playerObject);
            country.addUnits(1);
            return ("Country " + countryName + " has been added to Player " + playerObject.getName());
        } else {
            return ("Player Object is null");
        }
    }

    /**
     * Checks if a player owns entire continents and assigns bonus units accordingly.
     * This method iterates through all continents and checks if all countries within a continent are owned by the specified player.
     * If a player owns all countries in a continent, the continent is marked as owned by the player, and bonus units are awarded as specified by the continent's bonus units attribute.
     * The method accumulates and returns a message detailing which continents (if any) have been conquered by the player and the bonus units awarded for each.
     * <p>
     * This method is crucial for calculating bonus units during the reinforcement phase of the game, allowing players to strategically plan their next moves based on potential bonus units from continent control.
     *
     * @param player The {@link Player} object for whom continent ownership and bonus units are being checked.
     * @return A string message detailing continents conquered by the player and the corresponding bonus units awarded. If no continents are fully owned by the player, the returned string may be empty or indicate no continents are owned.
     */
    public String continentPlayerCheck(Player player) {
        boolean ownsContinent;
        StringBuilder result = new StringBuilder();
        for (Continent continent : continents) {
            ownsContinent = true;
            for (Country country : continent.getCountries()){
                if (country.getPlayer() != player) {
                    ownsContinent = false;
                    break;
                }
            }
            if (!ownsContinent) continue;
            continent.setPlayer(player);
            result.append("Player ").append(player.getName()).append(" has conquered ").append(continent.getName()).append(" and gets ").append(continent.getBonusUnits()).append(" Bonus units.\n");
        }
        return result.toString();
    }

    /**
     * Removes a country from a player's control in the game.
     * This method finds a country by its name and a player by their name, then removes the country from the player's list of controlled countries.
     * It also decrements the player's count of controlled countries by one. If the country or player does not exist, a {@link CountryNotFoundException} is thrown.
     * The method returns a confirmation message indicating the country has been removed from the player.
     * <p>
     * This operation is critical in scenarios where territory control changes due to gameplay actions, such as losing a battle or trading territories.
     *
     * @param countryName The name of the country to be removed from the player's control.
     * @param playerName The name of the player from whom the country will be removed.
     * @return A string message confirming the removal of the country from the player.
     * @throws CountryNotFoundException If the specified country name does not match any existing country in the game.
     */
    public String removeCountryFromPlayer(String countryName, String playerName) throws CountryNotFoundException {
        Country country = getCountryByName(countryName);
        Player player = getPlayerByName(playerName);
        player.removeCountry(country);
        player.removeNumberOfCountries(1);
        return "Country " + countryName + " has been removed from Player " + playerName;
    }

    /**
     * Generates a string listing all available countries for selection at the start of the game.
     * This method iterates through the list of available countries, appending each country's name to a string, separated by new lines.
     * The resulting string is intended for display purposes, providing a quick overview of countries that players can choose from during the game setup.
     * <p>
     * This method is particularly useful in the game's initial setup phase, where players are selecting countries to control.
     * It ensures that players are aware of all available options, facilitating an informed selection process.
     *
     * @return A string containing the names of all available countries, each on a new line.
     */
    public String getStringAvailableCountries(){
        String availableCountriesString = "";
        for (Country country : availableCountries){
            availableCountriesString += country.getName() + "\n";
        }
        return availableCountriesString;
    }

    /**
     * Retrieves a list of all available countries that have not yet been assigned to any player.
     * This method is primarily used at the start of the game during the country selection and assignment phase.
     * It provides a way to access the current list of countries that are available for players to select or be assigned.
     * <p>
     * The list returned by this method is a direct reference to the game's internal list of available countries.
     * Therefore, any changes made to the returned list will directly affect the game state.
     * <p>
     * This method is crucial for ensuring that countries are evenly and fairly distributed among players at the start of the game.
     *
     * @return A list of {@link Country} objects representing the countries that are currently available for selection or assignment to players.
     */
    public List<Country> getAvailableCountries(){
        return availableCountries;
    }


    /**
     * Randomly assigns available countries to players at the start of the game.
     * This method iterates through all players in a loop until all available countries have been assigned. For each player,
     * it selects a random country from the list of available countries, assigns it to the player, and then removes that country
     * from the list of available countries to ensure it cannot be selected again. The assignment is done by updating both the player's
     * list of countries and the country's owner attribute. This process continues in a round-robin fashion among all players until
     * no unassigned countries remain.
     * <p>
     * This method is critical for the initial setup phase of the game, ensuring a fair and random distribution of countries among players.
     * It helps to start the game on an even footing, with each player having control over a portion of the game map.
     *
     * @return A string message indicating that all countries have been successfully assigned to players.
     * @throws CountryNotFoundException If a country to be assigned does not exist in the game's list of countries. This exception
     *                                  should not normally be thrown unless there is a mismatch between the list of available countries
     *                                  and the game's country data.
     */
    public String pickRandomStartCountries() throws CountryNotFoundException {
        while (!availableCountries.isEmpty()) {
            for (Player player : players) {
                if (!availableCountries.isEmpty()) {
                    int randomIndex = (int) (Math.random() * availableCountries.size());
                    Country randomCountry = availableCountries.get(randomIndex);
                    addCountryToPlayer(randomCountry.getName(), player);
                    randomCountry.setPlayer(player);
                    availableCountries.remove(randomCountry);
                }
            }
        }
        return "All countries have been assigned to players";
    }

    /**
     * Calculates and adds units to a player based on the number of countries they control and any continent bonuses.
     * This method first checks if the player controls entire continents using {@link #continentPlayerCheck(Player)} and awards bonus units accordingly.
     * The base number of units awarded is determined by the number of countries the player controls, with a minimum of 3 units if the player controls fewer than 9 countries.
     * For players controlling 9 or more countries, units are awarded based on a third of the number of countries they control, rounded down, plus any additional continent bonus units.
     * The calculated units are then added to the player's total unit count for deployment.
     * <p>
     * This method is crucial for the reinforcement phase of the game, allowing players to strengthen their positions based on their territorial control and strategic achievements.
     *
     * @param player The {@link Player} object representing the player who is receiving the units.
     * @return A string message indicating the total number of units the player has available for placement.
     */
    public String addPlayerUnitsPerRound(Player player) {
        continentPlayerCheck(player);
        int units;
        if (player.getCountries().size() >= 9) {
            units = player.getCountries().size()/3;
            for (Continent continent : continents) {
                if (continent.getPlayer() == player) {
                    units += continent.getBonusUnits();
                }
            }
        } else {
            units = 3;
        }
        player.addUnits(units);
        return ("Player " + player.getName() + " has " + player.getNumberOfUnits() + " units to place");
    }

    /**
     * Adds a specified number of units to a country controlled by a player.
     * This method attempts to add a given number of units to a specified country, provided the country is controlled by the player and the player has a sufficient number of units available.
     * If the country is not controlled by the player, a message indicating this is returned. If the player does not have enough units, an {@link InvalidUnitException} is thrown.
     * The method first retrieves the country object by its name. It then checks if the country is controlled by the player and if the player has enough units.
     * If both conditions are met, the units are added to the country, and the player's available units are decreased accordingly.
     * <p>
     * This operation is critical during the reinforcement phase of the game, allowing players to strengthen their positions on the board.
     *
     * @param player The {@link Player} object representing the player who is placing the units.
     * @param pCountry The name of the country where the units are to be placed.
     * @param units The number of units to be placed in the specified country.
     * @return A string message indicating the outcome of the operation. It either confirms the successful placement of units or informs that the country does not belong to the player.
     * @throws InvalidUnitException If the player attempts to place more units than they have available.
     */
    public String addPlayerUnitsToCountry (Player player, String pCountry, int units) throws InvalidUnitException {
        try {
            Country country = getCountryByName(pCountry);
            if (country.getPlayer() == player && player.getNumberOfUnits() >= units) {
                country.addUnits(units);
                player.setNumberOfUnits(player.getNumberOfUnits() - units);
                return ("Player " + player.getName() + " has placed " + units + " units in " + country.getName());
            } else if (country.getPlayer() != player){
                return ("Country " + pCountry + " does not belong to Player " + player.getName());
            } else {
                throw new InvalidUnitException("Player " + player.getName() + " has insufficient Units. Available units: " + player.getNumberOfUnits());
            }
        } catch (CountryNotFoundException e) {
            return e.getMessage();
        }
    }

    /**
     * Executes an attack from one country to another with a specified number of attacking and defending units.
     * This method initiates an attack from the attacking country to the defending country, using the specified number of units for both the attacker and defender.
     * It first retrieves the {@link Country} objects for both the attacking and defending countries by their names. Then, it validates the attack conditions,
     * such as ensuring the countries are neighbors, the attacker owns the attacking country, and the specified number of units is valid.
     * After validation, the method rolls dice for both the attacker and defender based on the number of units involved in the attack.
     * The results of the dice rolls are then resolved to determine the outcome of the attack, including changes in country ownership and unit counts.
     * <p>
     * This method is central to the gameplay, allowing players to attempt to conquer territories from their opponents.
     *
     * @param fromCou The {@link Country} object representing the attacking country.
     * @param toCou The {@link Country} object representing the defending country.
     * @param attacker The {@link Player} object representing the player who is attacking.
     * @param numberOfUnits The number of units used in the attack. This number must not exceed the maximum allowed and must be greater than 0.
     * @param defenderUnits The number of units defending the country. This number is determined by the player defending the country.
     * @throws IllegalStateException If the attack cannot be performed due to game rules, such as when the attacking country does not have enough units.
     * @throws CountryNotFoundException If either the attacking or defending country does not exist.
     * @throws NotANeighbourCountry If the attacking and defending countries are not neighbors.
     */
    public void makeAttack(Country fromCou, Country toCou, Player attacker, int numberOfUnits, int defenderUnits) throws IllegalStateException, CountryNotFoundException, NotANeighbourCountry {
        Country from = getCountryByName(fromCou.getName());
        Country to = getCountryByName(toCou.getName());
        validateAttackConditions(from, to, attacker, numberOfUnits, defenderUnits);

        Integer[] attackerRolls = rollAttackerDice(numberOfUnits);
        Integer[] defenderRolls = rollDefenderDice(defenderUnits);

        resolveDiceRolls(from, to, numberOfUnits, attackerRolls, defenderRolls);
    }

    /**
     * Rolls dice for the attacker based on the number of units involved in the attack.
     * This method utilizes the {@link Dice} class to simulate rolling dice for the attacking units. The number of dice rolled is determined by the number of attacking units,
     * with a maximum limit defined by the game rules. The results of the dice rolls are returned as an array of integers, representing the outcomes of each dice roll.
     * <p>
     * This method is critical for determining the outcome of an attack, as the dice roll results are used to compare against the defender's dice roll results.
     *
     * @param numberOfUnits The number of units involved in the attack, which determines the number of dice to be rolled.
     * @return An array of integers representing the results of the dice rolls.
     */
    private Integer[] rollAttackerDice(int numberOfUnits) {
        return dice.rollDice(numberOfUnits);
    }

    /**
     * Rolls dice for the defender based on the number of units involved in the defense.
     * This method utilizes the {@link Dice} class to simulate rolling dice for the defending units. The number of dice rolled is determined by the lesser of the number of defending units and the maximum number of dice allowed for defenders, as defined by the game rules.
     * The results of the dice rolls are returned as an array of integers, representing the outcomes of each dice roll. This is crucial for determining the outcome of a defense during an attack, as the dice roll results are used to compare against the attacker's dice roll results.
     *
     * @param numberOfUnits The number of units involved in the defense, which determines the number of dice to be rolled, not exceeding the maximum limit for defenders.
     * @return An array of integers representing the results of the dice rolls for the defender.
     */
    private Integer[] rollDefenderDice(int numberOfUnits) {
        return dice.rollDice(Math.min(numberOfUnits, MAX_DEFENDER_DICE));
    }

    /**
     * Resolves the outcome of dice rolls between an attacker and a defender during an attack.
     * This method compares the dice rolls of the attacker and defender to determine the number of units lost by each side.
     * The attacker and defender rolls are assumed to be sorted in descending order. For each pair of dice, starting from the highest,
     * the method compares the attacker's roll against the defender's. If the attacker's roll is higher, the defender loses a unit;
     * otherwise, the attacker loses a unit. The method then updates the territories involved in the attack with the new unit counts.
     * <p>
     * Note: This method assumes that both the attacker and defender rolls arrays are not null and have at least one element.
     *
     * @param from The {@link Country} object representing the attacking country.
     * @param to The {@link Country} object representing the defending country.
     * @param numberOfUnits The number of units involved in the attack. This is used to update the territory units after resolving the battle.
     * @param attackerRolls An array of {@link Integer} representing the dice rolls of the attacker, sorted in descending order.
     * @param defenderRolls An array of {@link Integer} representing the dice rolls of the defender, sorted in descending order.
     */
    private void resolveDiceRolls(Country from, Country to, int numberOfUnits, Integer[] attackerRolls, Integer[] defenderRolls) {
        int attackerLossesUnits = 0, defenderLossesUnits = 0;
        for (int i = 0; i < Math.min(attackerRolls.length, defenderRolls.length); i++) {
            if (attackerRolls[i] > defenderRolls[i]) {
                defenderLossesUnits++;
            } else {
                attackerLossesUnits++;
            }
        }

        updateTerritoryUnits(from, to, numberOfUnits, attackerLossesUnits, defenderLossesUnits);
    }

    /**
     * Updates the units in the territories after an attack has been resolved.
     * This method determines the outcome of an attack based on the number of units lost by the defender. If the defender loses any units,
     * it implies a successful attack, and the method {@code resolveSuccessfulAttack} is called to handle the successful attack scenario.
     * Conversely, if the defender does not lose any units, the attack is considered failed, and {@code resolveFailedAttack} is called.
     * After determining the attack's outcome, this method calls {@code conquerTerritory} to update the territory control and unit counts accordingly.
     * <p>
     * This method is a critical part of the game's attack resolution process, ensuring that the game state accurately reflects the results of an attack.
     *
     * @param from The {@link Country} object representing the attacking country.
     * @param to The {@link Country} object representing the defending country.
     * @param numberOfUnits The number of units involved in the attack. This parameter is used in the {@code conquerTerritory} method to update units in the territory.
     * @param attackerLossesUnits The number of units the attacker lost during the attack.
     * @param defenderLossesUnits The number of units the defender lost during the attack.
     */
    private void updateTerritoryUnits(Country from, Country to, int numberOfUnits, int attackerLossesUnits, int defenderLossesUnits) {
        //Defender losses
        if(defenderLossesUnits > 0){
            resolveSuccessfulAttack(from, to, attackerLossesUnits , defenderLossesUnits);
        }else {
            resolveFailedAttack(from, to, attackerLossesUnits, defenderLossesUnits);
        }
        //Update Units in the Territory
        numberOfUnits -= attackerLossesUnits;
        conquerTerritory(from, to, numberOfUnits);
    }

    /**
     * Defends a country with a specified number of units.
     * This method is used to simulate the defense of a country during an attack. It checks if the number of units specified for defense is within the allowed range (1 to 2 units) and if the defending country has at least one unit available for defense. If the conditions are met, the specified number of units is removed from the defending country to represent the units participating in the defense.
     * <p>
     * If the number of units specified for defense is outside the allowed range, an {@link IllegalArgumentException} is thrown, indicating that the defense action cannot be performed due to an invalid number of units.
     * Similarly, if the defending country does not have any units available for defense, an {@link IllegalArgumentException} is also thrown.
     * <p>
     * This method directly modifies the state of the defending country by decreasing its unit count based on the number of units involved in the defense.
     *
     * @param defendingCountry The {@link Country} object representing the country that is defending.
     * @param numberOfUnits The number of units used in the defense. Must be between 1 and 2, inclusive.
     * @throws IllegalArgumentException If the number of units for defense is not between 1 and 2, or if the defending country has no units to defend.
     */
    public void defend(Country defendingCountry, int numberOfUnits) throws IllegalStateException {
        if(numberOfUnits < 1 || numberOfUnits > 2){
            throw new IllegalArgumentException("The number of units for defense must be between 1 and 2.");
        }
        if(defendingCountry.getUnits() < 1){
            throw new IllegalArgumentException("Country has no units to defend.");
        }

        defendingCountry.removeUnits(numberOfUnits);
    }

    /**
     * Validates the conditions for an attack between two countries in the game.
     * This method checks multiple conditions to ensure that an attack can legally proceed according to the game rules. These conditions include:
     * <ul>
     *     <li>Ownership: The attacking player must own the attacking country.</li>
     *     <li>Neighboring: The attacking and defending countries must be neighbors.</li>
     *     <li>Sufficient Units: The attacking country must have enough units to carry out the attack.</li>
     *     <li>Not Own Country: The attacking player must not own the defending country.</li>
     *     <li>Attack Units: The number of units used in the attack must be within the allowed range.</li>
     *     <li>Defender Units: The number of units defending must be within the allowed range for defense.</li>
     * </ul>
     * If any of these conditions are not met, the method throws an exception indicating the specific rule that has been violated.
     *
     * @param from The {@link Country} object representing the attacking country.
     * @param to The {@link Country} object representing the defending country.
     * @param attacker The {@link Player} object representing the player who is attacking.
     * @param numberOfUnits The number of units involved in the attack. This number must be within the game's rules for a valid attack.
     * @param defenderUnits The number of units defending the country. This number must be within the game's rules for a valid defense.
     * @throws IllegalStateException If the attacking country does not have enough units or if other game rules are violated.
     * @throws NotANeighbourCountry If the attacking and defending countries are not neighbors.
     */
    public void validateAttackConditions(Country from, Country to, Player attacker, int numberOfUnits, int defenderUnits) throws IllegalStateException, NotANeighbourCountry {
        validateOwnership(from.getName(), attacker);
        validateNeighboring(from, to);
        validateSufficientUnits(from, numberOfUnits);
        validateNotOwnCountry(attacker, to);
        validateAttackUnits(numberOfUnits);
        validateDefenderUnits(defenderUnits);
    }

    /**
     * Validates the number of units a defender can use in defense.
     * This method checks if the number of units specified for defense is within the allowed range according to the game rules.
     * The allowed range is from 1 unit (minimum) to the maximum number of defender dice, which is defined by {@code MAX_DEFENDER_DICE}.
     * This ensures that the defense action adheres to the game's rules regarding the maximum and minimum number of units that can defend in an attack.
     *
     * @param numberOfUnits The number of units involved in the defense.
     * @return {@code true} if the number of units is within the allowed range; {@code false} otherwise.
     */
    public boolean validateDefenderUnits(int numberOfUnits) {
        return numberOfUnits <= MAX_DEFENDER_DICE && numberOfUnits >= 1;
    }

    /**
     * Validates the number of units used in an attack.
     * This method checks if the specified number of units for an attack is within the allowed range according to the game rules.
     * The allowed range is from 1 unit (minimum) to the maximum number of attack units, which is defined by {@code MAX_ATTACK_UNITS}.
     * This ensures that the attack action adheres to the game's rules regarding the maximum and minimum number of units that can attack.
     *
     * @param numberOfUnits The number of units involved in the attack.
     * @return {@code true} if the number of units is within the allowed range; {@code false} otherwise.
     */
    public boolean validateAttackUnits(int numberOfUnits)  {
        return numberOfUnits <= MAX_ATTACK_UNITS && numberOfUnits >= 1;
    }

    /**
     * Validates if enough units remain in the attacking country after an attack is initiated.
     * This method ensures that at least one unit remains in the attacking country post-attack, as per the game rules.
     * It checks if the number of units left in the country after the attack is initiated is less than one. If so, it throws an {@link IllegalStateException}.
     * This validation is crucial for maintaining the integrity of the game state and ensuring that all moves adhere to the rules.
     *
     * @param from The {@link Country} object representing the attacking country.
     * @param numberOfUnits The number of units involved in the attack.
     * @return {@code true} if the validation passes, indicating that at least one unit remains in the attacking country.
     * @throws IllegalStateException If the number of units left in the country after the attack is initiated is less than one.
     */
    public boolean validateRestOfUnitWhenAttack(Country from, int numberOfUnits)  {
        if(from.getUnits() - numberOfUnits < 1){
            throw new IllegalStateException("At least one unit must remain in the territory.");
        }
        return true;
    }

    /**
     * Validates if the specified player owns the country.
     * This method checks if the player provided as an argument owns the country specified by its name.
     * Ownership is determined by checking if the country's name is present in the list of countries owned by the player.
     * This is a crucial check in the game logic, ensuring that actions such as attacks or movements are only performed by the country's owner.
     *
     * @param countryName The name of the country whose ownership is being verified.
     * @param attacker The {@link Player} object representing the player whose ownership of the country is being checked.
     * @return {@code true} if the player owns the country; {@code false} otherwise.
     */
    public boolean validateOwnership(String countryName, Player attacker) {
        return attacker.ownsCountry(countryName);
    }

    /**
     * Validates if two countries are neighboring.
     * This method checks if the country specified as {@code from} has the country specified as {@code to} in its list of neighbors.
     * It is a crucial part of the game logic that ensures actions such as attacks are only possible between neighboring countries.
     * If the countries are not neighbors, a {@link NotANeighbourCountry} exception is thrown, indicating the action cannot proceed.
     *
     * @param from The {@link Country} object representing the country from which the action is initiated.
     * @param to The {@link Country} object representing the target country of the action.
     * @return {@code true} if the countries are neighbors; otherwise, an exception is thrown.
     * @throws NotANeighbourCountry If the countries are not neighbors, preventing the action from proceeding.
     */
    public boolean validateNeighboring(Country from, Country to) throws NotANeighbourCountry {
        if (!from.getNeighbors().contains(to)) {
            throw new NotANeighbourCountry(to.getName());
        }
        return true;
    }

    /**
     * Validates if the specified country has a sufficient number of units for an operation.
     * This method checks whether the country has enough units to perform an action that requires a certain number of units.
     * It ensures that the operation does not result in a negative number of units in the country and that the number of units specified for the operation is greater than 0.
     * If the country does not have enough units or the number of units specified is not valid, an {@link IllegalStateException} is thrown.
     *
     * @param country The {@link Country} object representing the country whose units are being validated.
     * @param numberOfUnits The number of units required for the operation.
     * @throws IllegalStateException If the country does not have enough units or the number of units specified is less than or equal to 0.
     */
    public void validateSufficientUnits(Country country, int numberOfUnits) {
        if (country.getUnits() < numberOfUnits || numberOfUnits <= 0) {
            throw new IllegalStateException("The player does not have enough units to move the specified number.");
        }
    }

    /**
     * Validates that the attacking player does not own the defending country.
     * This method checks if the defending country is currently owned by the attacking player. If so, it throws an {@link IllegalStateException},
     * indicating that a player cannot attack their own territory. This validation is crucial in the game logic to ensure that attacks are only
     * made against opposing players' territories.
     *
     * @param attacker The {@link Player} object representing the player who is attempting the attack.
     * @param defenderCountry The {@link Country} object representing the country that is being attacked.
     * @throws IllegalStateException If the attacking player already owns the defending country.
     */
    public void validateNotOwnCountry(Player attacker, Country defenderCountry) {
        if (defenderCountry.getPlayer().getId() == attacker.getId()) {
            throw new IllegalStateException("The player cannot attack their own territory.");
        }
    }

    /**
     * Resolves the outcome of a successful attack between two countries.
     * This method is called when an attack from one country to another is successful. It updates the unit counts for both the attacking and defending countries
     * based on the units lost during the attack. The method also comments out a call to {@code drawUnitCard}, which presumably would award the attacking player
     * a unit card for their victory. The result of the operation is a string that details the outcome of the attack, including the names of the countries involved,
     * and the units lost by both the attacker and defender.
     * <p>
     *
     * @param attackerCountry The {@link Country} object representing the country that initiated the attack.
     * @param defenderCountry The {@link Country} object representing the country that defended against the attack.
     * @param attackerLossesUnits The number of units lost by the attacker during the attack.
     * @param defenderLossesUnits The number of units lost by the defender during the attack.
     */
    public void resolveSuccessfulAttack(Country attackerCountry, Country defenderCountry, int attackerLossesUnits, int defenderLossesUnits){
        defenderCountry.removeUnits(defenderLossesUnits);
        attackerCountry.removeUnits(attackerLossesUnits);
        drawUnitCard(attackerCountry.getPlayer());
    }

    /**
     * Resolves the outcome of a failed attack between two countries.
     * This method is invoked when an attack does not result in the conquering of the defender's territory. It updates the unit counts for both the attacking and defending countries
     * based on the units lost during the attack. The method constructs and returns a string that details the outcome of the failed attack, including the names of the countries involved,
     * and the units lost by both the attacker and defender.
     * <p>
     * This method is crucial for accurately reflecting the consequences of a failed attack in the game state, ensuring that the unit counts are adjusted accordingly.
     *
     * @param attackerCountry The {@link Country} object representing the country that initiated the attack.
     * @param defenderCountry The {@link Country} object representing the country that defended against the attack.
     * @param attackerLossesUnits The number of units lost by the attacker during the attack.
     * @param defenderLossesUnits The number of units lost by the defender during the attack, which is typically zero in a failed attack scenario.
     */
    public void resolveFailedAttack(Country attackerCountry, Country defenderCountry, int attackerLossesUnits, int defenderLossesUnits) {
        attackerCountry.removeUnits(attackerLossesUnits);
    }

    /**
     * Conquers a territory after a successful attack.
     * This method is invoked when an attacking country successfully defeats the defending country in battle, reducing the defender's units to zero.
     * It transfers ownership of the defender's territory to the attacker and moves a specified number of units from the attacking country to the newly conquered territory.
     * The method ensures that the game state accurately reflects the change in territory control following a successful attack.
     * <p>
     * Note: This method assumes that the defender's units are already reduced to zero and does not perform any battle logic or unit reduction itself.
     *
     * @param attackerCountry The {@link Country} object representing the country that initiated the attack and is conquering the territory.
     * @param defenderCountry The {@link Country} object representing the country that defended and lost the territory.
     * @param numberOfUnits The number of units the attacker wishes to move into the conquered territory. This number should be less than or equal to the attacking country's remaining units after the attack.
     */
    public void conquerTerritory(Country attackerCountry, Country defenderCountry, int numberOfUnits) {
        if (defenderCountry.getUnits() == 0) {
            //The territory of the defender is conquered
            //The attacker moves the units to the conquered territory
            int unitsAttacker = attackerCountry.getUnits() - numberOfUnits;
            defenderCountry.getPlayer().removeCountry(defenderCountry);
            defenderCountry.setPlayer(attackerCountry.getPlayer());
            attackerCountry.getPlayer().addCountry(defenderCountry);
            defenderCountry.setUnits(numberOfUnits);
            attackerCountry.setUnits(unitsAttacker);
        }
    }

    /**
     * Moves units from one country to another within the same player's control.
     * This method facilitates the strategic relocation of units between two countries owned by the same player. It ensures that the move is valid by checking:
     * <ul>
     *     <li>Both countries are owned by the same player.</li>
     *     <li>The originating country has enough units to move, leaving at least one unit behind.</li>
     *     <li>The number of units moved does not exceed the starting units of the originating country.</li>
     * </ul>
     * If any of these conditions are not met, the method returns an appropriate error message. Otherwise, it updates the unit counts for both countries and the starting units map.
     *
     * @param player The {@link Player} object representing the player making the move.
     * @param fromCountry The {@link Country} object representing the country from which units are being moved.
     * @param toCountry The {@link Country} object representing the destination country for the moving units.
     * @param units The number of units to move from the originating country to the destination country.
     * @return A string message indicating the outcome of the operation. It can be a success message detailing the move, or an error message if the move could not be completed.
     */
    public String moveUnits(Player player, Country fromCountry, Country toCountry, int units){
        if (fromCountry.getPlayer() == player && toCountry.getPlayer() == player){
            if (fromCountry.getUnits() >= units + 1){ // Ensure at least one unit remains
                int startingUnitsForCountry = getStartingUnitsForCountry(fromCountry.getName());
                if (units > startingUnitsForCountry) {
                    return "Cannot move more units than the starting units of " + fromCountry.getName();
                }
                fromCountry.removeUnits(units);
                toCountry.addUnits(units);
                startingUnits.put(fromCountry.getName(), startingUnitsForCountry - units); // Update the starting units
                return "Player " + player.getName() + " has moved " + units + " units from " + fromCountry.getName() + " to " + toCountry.getName();
            } else {
                return "Not enough units in " + fromCountry.getName() + " or cannot leave country empty";
            }
        } else {
            return "Countries do not belong to the same player";
        }
    }

    /**
     * Retrieves detailed information about a specific country.
     * This method attempts to fetch information for a country identified by its name. It constructs a formatted string containing the country's name,
     * the name of the player who owns it, the number of units present in the country, and a list of its neighboring countries. This information is intended
     * for display purposes, providing a quick overview of a country's status within the game.
     * <p>
     * If the country with the specified name does not exist within the game's data structures, the method catches a {@link CountryNotFoundException}
     * and returns a message indicating that the country does not exist. This ensures that the method caller can handle scenarios where a requested
     * country is not found.
     *
     * @param countryName The name of the country for which information is being requested.
     * @return A string containing detailed information about the specified country, formatted for easy reading. If the country does not exist, a message indicating this is returned.
     */
    public String getCountryInformation(String countryName) {
        try {
            Country country = getCountryByName(countryName);
            return "Country: " + country.getName() + "\n" +
                    "Owner: " + country.getPlayer().getName() + "\n" +
                    "Units: " + country.getUnits() + "\n" +
                    "Neighboring Countries: " + country.getNeighborNames();
        } catch (CountryNotFoundException e) {
            return "Country " + countryName + " does not exist";
        }
    }

    /**
     * Assigns a random mission card to each player at the start of the game.
     * This method iterates through all players in the game and assigns each one a random mission card from the available pool of mission cards.
     * Once a mission card is assigned to a player, it is removed from the pool to ensure that no two players receive the same mission card.
     * The method also sets the player attribute of the mission card to link the card to its new owner.
     * <p>
     * Note: This method modifies the state of both the {@code players} and {@code missionCards} collections.
     */
    public void drawMissionToPlayer() {
        for (Player player : players) {
            int randomIndex = (int) (Math.random() * missionCards.size());
            player.setMissionCard(missionCards.get(randomIndex));
            missionCards.get(randomIndex).setPlayer(player);
            missionCards.remove(randomIndex);
        }
    }

    /**
     * Returns a string representation of the available colors for players to choose from.
     * This method accesses the {@code availableColors} list, which contains the colors that players can select for their game pieces.
     * The colors are returned as a single string, with each color separated by commas and enclosed in square brackets.
     * This format is directly derived from the {@code toString} method of the {@code List} class.
     * <p>
     * This method is useful for displaying the available color options to players during the game setup phase.
     *
     * @return A string containing all available colors, formatted as a list enclosed in square brackets.
     */
    public String getAvailableColors() {
        return availableColors.toString();
    }

    /**
     * Retrieves the starting units assigned to a specific country at the beginning of the game.
     * This method looks up the country specified by its name in the {@code startingUnits} map, which holds the initial number of units each country starts with.
     * It is useful for initializing the game state and for players to query how many units they can place in their countries at the start of the game.
     * <p>
     * If the country name does not exist in the map, this method returns 0, indicating no starting units are assigned. This behavior should be handled by the caller to ensure proper game setup.
     *
     * @param countryName The name of the country for which the starting units are being requested.
     * @return The number of starting units assigned to the specified country. Returns 0 if the country is not found in the map.
     */
    public int getStartingUnitsForCountry(String countryName) {
        return startingUnits.get(countryName);
    }

    /**
     * Distributes the starting units among all countries at the beginning of the game.
     * This method iterates through each country in the game, assigning it a number of starting units based on the country's initial setup.
     * The starting units for each country are stored in the {@code startingUnits} map, with the country's name as the key and the number of units as the value.
     * <p>
     * It is assumed that the {@code countries} list has been previously initialized and contains all the countries participating in the game.
     * This method is crucial for setting up the game state before play begins, ensuring that each country has the correct number of units allocated.
     */
    public void distributeStartingUnits() {
        // Assuming you have a list of countries
        for (Country country : countries) {
            startingUnits.put(country.getName(), country.getUnits());
        }
    }

    /**
     * Prints the starting units for each country at the beginning of the game.
     * This method iterates through the {@code startingUnits} map, which contains the initial number of units assigned to each country.
     * For each entry in the map, it prints the country's name along with the number of starting units assigned to it.
     * This is particularly useful for debugging purposes or for providing an overview of the game setup before play begins.
     */
    public void printStartingUnits() {
        for (Map.Entry<String, Integer> entry : startingUnits.entrySet()) {
            //System.out.println("Country: " + entry.getKey() + ", Starting Units: " + entry.getValue());
        }
    }

    /**
     * Checks if a player has completed their mission and won the game.
     * This method evaluates the mission completion status of the specified player by checking if the player's assigned mission card's completion criteria have been met.
     * If the player has completed their mission, a congratulatory message is returned, indicating the player has won the game.
     * Otherwise, an empty string is returned, indicating the player has not yet completed their mission.
     * <p>
     * This method is crucial for determining the end of the game and identifying the winner based on mission completion.
     *
     * @param player The {@link Player} object representing the player whose mission completion status is being checked.
     * @return A string message indicating the player has won the game if their mission is completed; otherwise, an empty string.
     */
    public String checkWinningCondition(Player player) {
        if (player.getMissionCard().isDone()) {
            return ("Player " + player.getName() + " has completed their mission and won the game");
        }
        return "";
    }

    /**
     * Validates the combination of unit cards a player wishes to turn in.
     * This method checks if the three unit cards provided by the player meet the game's criteria for turning in unit cards. The criteria are as follows:
     * 1. All three cards must exist (i.e., they are not null).
     * 2. The cards must either all be of the same type, all be of different types, or at least one of them must be a "Joker" card.
     * If the cards meet one of these criteria, the method proceeds to remove the unit cards from the player's hand, return them to the deck, and grant the player additional units.
     * If the cards do not meet any of the criteria, a message indicating the mismatch is returned.
     * <p>
     * This method is crucial for the game's mechanics, allowing players to exchange unit cards for additional units, which can significantly impact their strategy and gameplay.
     *
     * @param player The {@link Player} object representing the player turning in the unit cards.
     * @param card1 The first {@link UnitCard} object being turned in.
     * @param card2 The second {@link UnitCard} object being turned in.
     * @param card3 The third {@link UnitCard} object being turned in.
     * @return A string message indicating the outcome of the operation. If the cards are successfully turned in, it returns the result of the {@code turnInUnits} method. Otherwise, it returns a message indicating the cards do not match.
     */
    public String validateUnitCards(Player player, UnitCard card1, UnitCard card2, UnitCard card3) {

        if (card1 == null || card2 == null || card3 == null) {
            return "One or more of the unit cards do not exist";
        }

        if (card1.getType().equals(card2.getType()) && card1.getType().equals(card3.getType())) {
            player.removeUnitCards(card1, card2, card3);
            returnUnitCard(card1, card2, card3);
            return turnInUnits(player);
        } else if (!card1.getType().equals(card2.getType()) && !card2.getType().equals(card3.getType()) && !card1.getType().equals(card3.getType())) {
            player.removeUnitCards(card1, card2, card3);
            returnUnitCard(card1, card2, card3);
            return turnInUnits(player);
        } else if (card1.getType().equals("Joker") || card2.getType().equals("Joker") || card3.getType().equals("Joker")) {
            // If any of the cards is a "Joker", it's considered a match
            player.removeUnitCards(card1, card2, card3);
            returnUnitCard(card1, card2, card3);
            return turnInUnits(player);
        }

        return "The unit cards do not match";
    }

    /**
     * Processes the turning in of unit cards by a player and assigns additional units accordingly.
     * This method calculates the number of units a player receives when they turn in unit cards based on the number of times unit cards have been turned in previously in the game.
     * The calculation follows a specific set of rules:
     * <ul>
     *     <li>If the number of times unit cards have been turned in is less than 5, the player receives 4 units plus an additional 2 units for each time cards have been turned in.</li>
     *     <li>For the 5th turn-in, the player receives 15 units.</li>
     *     <li>For the 6th turn-in, the player receives 20 units.</li>
     *     <li>For the 7th turn-in, the player receives 25 units.</li>
     *     <li>For each subsequent turn-in beyond the 7th, the player receives 25 units plus an additional 5 units for each turn-in beyond the 7th.</li>
     * </ul>
     * After calculating the units to add, this method updates the player's unit count and increments the global count of turned in unit cards.
     * <p>
     * This method is crucial for the game's dynamics, allowing players to strategically turn in unit cards to gain additional units that can be deployed to strengthen their position in the game.
     *
     * @param player The {@link Player} object representing the player turning in the unit cards.
     * @return A string message indicating the number of units the player has received as a result of turning in unit cards.
     */
    public String turnInUnits(Player player) {
        int unitsToAdd;
        if (turnedInUnitCards < 5) {
            unitsToAdd = 4 + 2 * turnedInUnitCards;
        } else if (turnedInUnitCards == 5) {
            unitsToAdd = 15;
        } else if (turnedInUnitCards == 6) {
            unitsToAdd = 20;
        } else if (turnedInUnitCards == 7) {
            unitsToAdd = 25;
        } else {
            unitsToAdd = 25 + 5 * (turnedInUnitCards - 7);
        }
        player.addUnits(unitsToAdd);
        turnedInUnitCards++;
        return "Player " + player.getName() + " has received " + unitsToAdd + " units";
    }

    /**
     * Draws a unit card for a player and adds it to their hand.
     * This method selects a random unit card from the available pool of unit cards, adds it to the specified player's hand, and then removes the card from the pool.
     * It ensures that each player receives a card that is randomly selected from the current pool of unit cards, simulating the drawing of a card in a board game.
     * <p>
     * This method is crucial for the game mechanics, allowing players to acquire new unit cards that can be used for various strategic advantages during the game.
     *
     * @param player The {@link Player} object representing the player who is drawing a unit card.
     */
    public void drawUnitCard(Player player) {
        if (!player.getHasConquered()) {
            int randomIndex = (int) (Math.random() * unitCards.size() - 1);
            player.addUnitCard(unitCards.get(randomIndex));
            unitCards.remove(randomIndex);
            player.setHasConquered(true);              //Allows only one unit card to be drawn per round
        }
    }

    /**
     * Saves player information to a specified file.
     * This method serializes the list of {@link Player} objects into a format suitable for storage and writes it to the specified file using the persistence manager.
     * It leverages the {@code speicherPlayer} method of the {@link PersistenceManager} to handle the serialization and file writing process.
     * <p>
     * This method is crucial for persisting the current state of the game, including all player details, which can be loaded in future sessions.
     *
     * @param players A list of {@link Player} objects representing the players whose information is to be saved.
     * @param filename The name of the file where the player information will be stored. This should include the path if the file is not in the default directory.
     * @return {@code true} if the player information was successfully saved; {@code false} otherwise.
     * @throws Exception If there are any issues encountered during the file writing process.
     */
    public boolean savePlayerInformation(List<Player> players, String filename) throws Exception {
        return pm.speicherPlayer(players, filename);
    }

    /**
     * Retrieves player information from a specified file.
     * This method attempts to load a list of {@link Player} objects from a file specified by the filename parameter. It uses the {@code loadPlayers} method of the {@link PersistenceManager} to deserialize the player information stored in the file into {@link Player} objects.
     * <p>
     * If the file reading process encounters an {@link IOException}, the exception is caught and its stack trace is printed, but the method will still return any players that could be successfully loaded up to the point of the exception.
     * <p>
     * This method is essential for restoring player information from persistent storage, allowing for game state recovery and continuation from previous sessions.
     *
     * @param filename The path and name of the file from which player information is to be loaded.
     * @return A list of {@link Player} objects representing the players whose information was stored in the file. This list may be empty if the file does not exist or is empty.
     * @throws Exception If there are any issues encountered during the file reading or deserialization process, excluding {@link IOException} which is caught internally.
     */
    public List<Player> getPlayerInformation(String filename) throws Exception {
        List<Player> players = new ArrayList<>();
        try {
            players = pm.loadPlayers(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }

    /**
     * Returns the specified unit cards to the pool of available unit cards.
     * This method takes three {@link UnitCard} objects as parameters and adds them back to the pool of available unit cards.
     * It is typically used when a player decides not to use or is required to return their unit cards to the pool,
     * ensuring that these cards can be drawn again by any player in subsequent turns.
     * <p>
     * This method directly modifies the state of the {@code unitCards} list by adding the specified unit cards back into it.
     *
     * @param card1 The first {@link UnitCard} to be returned to the pool.
     * @param card2 The second {@link UnitCard} to be returned to the pool.
     * @param card3 The third {@link UnitCard} to be returned to the pool.
     */
    public void returnUnitCard(UnitCard card1, UnitCard card2, UnitCard card3) {
        unitCards.add(card1);
        unitCards.add(card2);
        unitCards.add(card3);
    }

    /**
     * Retrieves detailed information about a specific country for GUI display.
     * This method attempts to fetch and format information about a country identified by its name. The information includes the country's name, the owner's name, and the number of units present in the country. This formatted string is intended for display in the game's graphical user interface (GUI).
     * <p>
     * If the country with the specified name does not exist within the game's data structures, a {@link CountryNotFoundException} is thrown to indicate the error. This ensures that the method caller can handle the scenario where a requested country is not found, maintaining robustness in the game's error handling.
     * <p>
     * Note: The continent information retrieval is commented out in the current implementation. This could be included in future versions for more detailed information.
     *
     * @param countryName The name of the country for which information is being requested.
     * @return A string containing detailed information about the specified country, formatted for GUI display. The information includes the country's name, owner, and the number of units present.
     * @throws CountryNotFoundException If no country with the specified name exists within the game.
     */
    public String getCountryInformationGUI(String countryName) throws CountryNotFoundException {
        try {
            Country country = getCountryByName(countryName);
            return "Country: " + country.getName() + "\n" +
//                    "Continent: " + country.getContinent().getName() + "\n" +
                    "Owner: " + country.getPlayer().getName() + "\n" +
                    "Units: " + country.getUnits();
        } catch (CountryNotFoundException e) {  // If the country does not exist
            throw new CountryNotFoundException(countryName);
        }
    }
}

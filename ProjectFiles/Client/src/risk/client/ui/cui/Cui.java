//package risk.client.ui.cui;
//
//import risk.common.entities.*;
//import risk.common.exceptions.CountryNotFoundException;
//import risk.common.exceptions.InvalidUnitException;
//import risk.common.exceptions.NotANeighbourCountry;
//import risk.common.exceptions.PlayerNotFoundException;
//import risk.common.interfaces.RiskInterface;
//
//import java.io.IOException;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.List;
//import java.util.Scanner;
//import java.lang.String;
//
// * Represents the command-line user interface (CUI) for the Risk game.
// * <p>
// * This class provides a text-based interface for interacting with the Risk game, allowing players to perform actions such as adding or removing players, starting the game, and executing game turns. It handles user input and displays game state information, including country ownership, troop deployments, and battle outcomes.
// * <p>
// * The Cui class interacts with the {@link RiskInterface} to manage the game logic and state. It uses a {@link BufferedReader} for reading user input and processes commands in a loop until the game is exited. The class supports operations like game setup, executing player turns, and saving game state.
// */
//public class Cui {
//    private List<Player> players;
//    private RiskInterface risk;
//    //private Risk risk!;
//
//
//    private BufferedReader input;
//    private int playerId = 1;
//
/**
//     * Constructs a new Cui instance for the Risk game.
//     * <p>
//     * This constructor initializes a new BufferedReader to read user input from the console. It also creates a new Risk game instance with the specified data name, which is used to load game data (such as countries and continents). After initialization, it calls the mainMenu method to display the main menu of the game and start the interaction with the user.
//     *
//     * @param dataName the name of the data set to be used for initializing the game. This could refer to a specific configuration or set of game data.
//     * @throws CountryNotFoundException if the specified data set includes references to countries that cannot be found in the game data.
//     * @throws InvalidUnitException if the game data contains invalid unit configurations.
//     */
//    public Cui(String dataName) throws CountryNotFoundException, InvalidUnitException {
//        this.input = new BufferedReader(new InputStreamReader(System.in));
//        this.risk = new Risk(dataName);
//        mainMenu();
//    }
// /**
//     * Reads a line of text from the console input.
//     * <p>
//     * This method attempts to read a single line of text from the console input. It uses a {@link BufferedReader} to read the input. If an {@link IOException} occurs during the reading process, the exception's stack trace is printed, and the method returns a string "null" to indicate the failure.
//     *
//     * @return The line of text read from the console, or "null" if an IOException occurs.
//     */
//    private String readInput(){
//        try {
//            return input.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "null";
//        }
//    }
//
/**
//     * Initiates the game by randomly assigning countries to players, drawing mission cards, and displaying initial country ownership.
//     * <p>
//     * This method performs several key actions to start the game:
//     * <ul>
//     *     <li>Randomly assigns countries to each player by calling {@code risk.pickRandomStartCountries()}.</li>
//     *     <li>Distributes mission cards to players using {@code risk.drawMissionToPlayer()}.</li>
//     *     <li>Iterates through all players to display the countries each player owns at the start of the game.</li>
//     * </ul>
//     * After these initial setup steps, the game menu is displayed by calling {@code gameMenu()}, allowing players to proceed with their turns.
//     *
//     * @throws CountryNotFoundException if a country specified in the game setup cannot be found.
//     * @throws InvalidUnitException if there is an issue with the units assigned to a country.
//     */
//    public void startGame() throws CountryNotFoundException, InvalidUnitException {
//
//        System.out.println(risk.pickRandomStartCountries());
//        risk.drawMissionToPlayer();
//        for (Player player : risk.getPlayers()){
//            System.out.println("Player " + player.getName() + " has the following countries: ");
//            System.out.println(player.getCountryNames());
//        }
//        gameMenu();
//    }
//
//    /**
//     * Manages the game menu interactions for the Risk game.
//     * <p>
//     * This method orchestrates the game menu flow, allowing the player on turn to perform actions such as placing units, attacking countries, moving units, and accessing country information. It ensures the game progresses by handling player inputs and executing the corresponding game logic. The method loops through the game phases, including unit placement at the beginning of a player's turn, followed by the option to attack, move units, or end the turn. It also provides an option to exit the game.
//     * <p>
//     * The method utilizes {@code readInput()} to capture player choices and interacts with the {@link RiskInterface} to execute game actions based on those choices. It handles exceptions and invalid inputs gracefully, ensuring the game's flow is not interrupted.
//     * <p>
//     * Actions such as attacking or moving units involve additional validation, such as checking for valid target countries and ensuring the player has enough units to perform the action. The method also updates the game state accordingly, including updating the number of units in each country and changing country ownership after successful attacks.
//     *
//     * @throws CountryNotFoundException if an operation involves a country not found in the game data.
//     * @throws InvalidUnitException if an operation involves an invalid number of units.
//     */
//    private void gameMenu() throws CountryNotFoundException, InvalidUnitException {
//        String option;
//        String input;
//        int units;
//
//
//        risk.nextTurn();
//        System.out.println("Player " +risk.getPlayerOnTurn().getName() +" is on turn");
//        System.out.println(risk.continentPlayerCheck(risk.getPlayerOnTurn().getName()));
//        System.out.println(risk.addPlayerUnitsPerRound(risk.getPlayerOnTurn().getName()));
//        while (risk.getPlayerOnTurn().getNumberOfUnits() > 0) {
//            System.out.println("Please pick a Country:");
//            System.out.println(risk.getPlayerOnTurn().getCountryNames());
//            input = readInput();
//            System.out.println("How many Units do you want to place?");
//            while (true) {
//                try {
//                    units = Integer.parseInt(readInput());
//                    if (units > risk.getPlayerOnTurn().getNumberOfUnits()) {
//                        System.out.println("You don't have enough units. You have " + risk.getPlayerOnTurn().getNumberOfUnits() + " units available.");
//                        continue;
//                    }
//                    break; // will only reach this line if the input was a valid integer and the player has enough units, so we break the loop
//                } catch (NumberFormatException e) {
//                    System.out.println("Invalid input. Please enter a valid number.");
//                }
//            }
//            System.out.println(risk.addPlayerUnitsToCountry(risk.getPlayerOnTurn().getName(), input, units));
//        }
//
//        risk.distributeStartingUnits();
////        Risk.printStartingUnits();
//
//        do {
//            System.out.println("Player " +risk.getPlayerOnTurn().getName() +" is on turn");
//            System.out.println("Your Mission is: " + risk.getPlayerOnTurn().getMissionCard().getDescription());
//            System.out.println("Please choose an option:");
//            System.out.println("1. Attack Country");
//            System.out.println("2. Move Units");
//            System.out.println("3. Get Country Information");
//            System.out.println("4. End Turn");
//            System.out.println("5. Exit");
//
//            option = readInput();
//            switch (option) {
//                case "4":
//                    risk.nextTurn();
//                    System.out.println("Player " +risk.getPlayerOnTurn().getName() +" is on turn");
//                    System.out.println(risk.continentPlayerCheck(risk.getPlayerOnTurn().getName()));
//                    System.out.println(risk.addPlayerUnitsPerRound(risk.getPlayerOnTurn().getName()));
//                    while (risk.getPlayerOnTurn().getNumberOfUnits() > 0) {
//                        System.out.println("Please pick a Country:");
//                        System.out.println(risk.getPlayerOnTurn().getCountryNames());
//                        input = readInput();
//                        System.out.println("How many Units do you want to place?");
//                        while (true) {
//                            try {
//                                units = Integer.parseInt(readInput());
//                                if (units > risk.getPlayerOnTurn().getNumberOfUnits()) {
//                                    System.out.println("You don't have enough units. You have " + risk.getPlayerOnTurn().getNumberOfUnits() + " units available.");
//                                    continue;
//                                }
//                                break; // will only reach this line if the input was a valid integer and the player has enough units, so we break the loop
//                            } catch (NumberFormatException e) {
//                                System.out.println("Invalid input. Please enter a valid number.");
//                            }
//                        }
//                        System.out.println(risk.addPlayerUnitsToCountry(risk.getPlayerOnTurn().getName(), input, units));
//                    }
//                    break;
//                case "1":
//                    try {
//                        makeAttack();
//                    } catch (IllegalStateException | NumberFormatException e) {
//                        System.out.println("Attention: " + e.getMessage());
//                    } catch (NotANeighbourCountry e) {
//                        throw new RuntimeException(e);
//                    }
//                    break;
//                case "5":
//                    try {
//                        risk.savePlayerInformation(risk.getPlayers(), "player.txt");
//                    } catch (Exception e) {
//                        System.out.println("error");
//                    }
//                    System.exit(0);
//                    break;
//                case "2":
//                    boolean movingUnits = true;
//                    System.out.println("Do you want to move units? [y/n]");
//                    String answer = readInput();
//                    while (movingUnits = true) {
//                        while (!answer.equals("y") && !answer.equals("n")) {
//                            System.out.println("Invalid input. Please enter y or n.");
//                            answer = readInput();
//                        }
//                        if (answer.equals("n")) {
//                            movingUnits = false;
//                            break;
//                        }
//                        try {
//                            System.out.println("Enter the name of the country from where you want to move units: ");
//                            String fromCountryName = readInput();
//                            while (!risk.validateOwnership(fromCountryName, risk.getPlayerOnTurn().getName())) {
//                                System.out.println("Give a country that you own :");
//                                fromCountryName = readInput();
//                            }
//                            Country fromCountry = risk.getCountryByName(fromCountryName);
//                            System.out.println("The neighbors of " + fromCountryName + " are: ");
//                            for (Country neighbor : fromCountry.getNeighbors()) {
//                                System.out.println(neighbor.getName());
//                            }
//
//                            System.out.println("Enter the name of the neighbor country to where you want to move units: ");
//                            // check for if neigbour country is valid
//                            String toCountryName = readInput();
//                            Country toCountry = risk.getCountryByName(toCountryName);
//
//                            // check if number of units is valid
//                            System.out.println("The number of units in " + fromCountryName + " is: " + fromCountry.getUnits());
//                            System.out.println("Enter the number of units you want to move: ");
//                            while (true) {
//                                try {
//                                    units = Integer.parseInt(readInput());
//                                    if (units > fromCountry.getUnits() - 1) {
//                                        System.out.println("You don't have enough units. You have " + (fromCountry.getUnits() - 1) + " units available.");
//                                        continue;
//                                    }
//                                    break; // will only reach this line if the input was a valid integer and the player has enough units, so we break the loop
//                                } catch (NumberFormatException e) {
//                                    System.out.println("Invalid input. Please enter a valid number.");
//                                }
//                            }
//
//                            System.out.println(risk.moveUnits(risk.getPlayerOnTurn().getName(), fromCountry.getName(), toCountry.getName(), units));
//                        } catch (CountryNotFoundException e) {
//                            System.out.println("Error: " + e.getMessage());
//                        }
////                        } catch (InvalidUnitException e) {
////                            System.out.println("Error: " + e.getMessage());
////                        }
//                        System.out.println("Do you want to move units again? [y/n]");
//                        answer = readInput();
//                        while (!answer.equals("y") && !answer.equals("n")) {
//                            System.out.println("Invalid input. Please enter y or n.");
//                            answer = readInput();
//                        }
//                        if (answer.equals("n")) {
//                            movingUnits = false;
//                            break;
//                        }
//                    }
//                    break;
//                case "3":
//                    System.out.println(risk.listAllCountries());
//                    System.out.println("Enter the name of the country you want to get information about: ");
//                    String countryName = readInput();
//                    System.out.println(risk.getCountryInformation(countryName));
//                    break;
//                default:
//                    System.out.println("Invalid option");
//                    gameMenu();
//            }
//        } while (option !="4");
//    }
//    /**
//     * Displays the main menu and handles user interactions for the Risk game.
//     * <p>
//     * This method presents the main menu options to the user, including loading an old game, adding or removing players, listing all players, starting the game, and exiting. It processes the user's input to execute the corresponding action. The method supports dynamic player management and game initialization, ensuring a flexible and user-friendly interface for game setup.
//     * <p>
//     * The method utilizes a {@link BufferedReader} for reading user input and interacts with the {@link RiskInterface} to manage game state and player information. It handles exceptions such as {@link CountryNotFoundException} and {@link InvalidUnitException} to ensure robust game setup and initialization.
//     * <p>
//     * The loop continues until the user chooses to exit, ensuring the game remains in the setup phase until ready to start. This method is critical for setting up the game environment before gameplay begins.
//     *
//     * @throws CountryNotFoundException if a country specified during game setup cannot be found.
//     * @throws InvalidUnitException if the game data contains invalid unit configurations during setup.
//     */
//    public void mainMenu() throws CountryNotFoundException, InvalidUnitException {
//        String playerName;
//        int color;
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Welcome to Risiko!");
//        String option;
//        do {
//            System.out.println("Please choose an option:");
//            System.out.println("y = Load old game");
//            System.out.println("a = Add Player");
//            System.out.println("r = Remove Player");
//            System.out.println("l = List all Players");
//            System.out.println("s = Start Game");
//            System.out.println("q = Exit");
//
//            option = readInput();
//            switch (option) {
//                case "a":
//                    System.out.print("Enter the player name: \n>");
//                    playerName = readInput();
//                    String colorName;
//                    do {
//                        System.out.println("Available colors: " + risk.getAvailableColors());
//                        System.out.print("Enter player color: ");
//                        colorName = readInput();
//                        if (!risk.getAvailableColors().contains(colorName)){
//                            System.out.println("Color " + colorName + " has already been selected by another player or is invalid. Please choose a different color.");
//                        }
//                    } while (!risk.getAvailableColors().contains(colorName));
//
//                    //System.out.println((risk.addPlayer(playerName, playerId, colorName)));
//                    playerId++;
//                    break;
//
//                case "y" :
//                    System.out.println("Loading old Game");
//                    risk.loadOldGame("player.txt");
//                    System.out.println("GameLoaded");
//                    break;
//
//                case "r":
//                    System.out.println("Current Players are: " + risk.listPlayers());
//                    System.out.print("Enter player name: \n>");
//                    playerName = readInput();
//                    try {
//                        risk.removePlayer(playerName);
//                    } catch (PlayerNotFoundException e) {
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//
//                case "l":
//                    System.out.println(risk.listPlayers());
//                    break;
//                case "s":
//                    if (risk.getPlayers().size() < 2) {
//                    System.out.println("You need at least 2 players to start the game");
//                    break;
//                }
//                    startGame();
//                    break;
//
//                case "q":
//                    System.exit(0);
//                    break;
//                default:
//                    System.out.println("Invalid option");
//                    mainMenu();
//            }
//        } while (option !="q");
//    }
//
//    /**
//     * Executes an attack from one country to another in the Risk game.
//     * <p>
//     * This method guides the attacking player through the process of selecting a country to attack from, choosing a target country, and deciding the number of units to use in the attack. It ensures that the attacking country belongs to the player, has more than one unit (necessary for an attack), and that the target country is a neighbor. The method also validates the number of units used for the attack and handles the defense response from the target country's player.
//     * <p>
//     * The attack process involves several checks and validations:
//     * <ul>
//     *     <li>Verifying that the player has countries with more than one unit to attack from.</li>
//     *     <li>Ensuring the selected country to attack from is owned by the player and has the required number of units.</li>
//     *     <li>Validating that the target country is a neighbor and not owned by the attacking player.</li>
//     *     <li>Checking that the number of units used in the attack is within the allowed range.</li>
//     *     <li>Allowing the defender to choose the number of units for defense, within the constraints of the defending country's unit count.</li>
//     * </ul>
//     * After the attack is made, the method prompts the player to decide whether to continue attacking or end their attack phase.
//     * <p>
//     * Note: This method modifies the game state by potentially changing the ownership of countries and the distribution of units across countries.
//     *
//     * @throws CountryNotFoundException if the selected country to attack from or the target country does not exist.
//     * @throws NotANeighbourCountry if the target country is not a neighbor of the country attacking from.
//     */
//    public void makeAttack() throws CountryNotFoundException, NotANeighbourCountry {
//        boolean attackLoop = true;
//        System.out.println(risk.getPlayerOnTurn().getName() + " do you want to attack a country? [y/n]");
//        String answer = readInput();
//        while (!answer.equals("y") && !answer.equals("n")) {
//            System.out.println("Invalid input. Please enter y or n.");
//            answer = readInput();
//        } if (answer.equals("n")) {
//            attackLoop = false;
//        }
//        while (attackLoop) {
//            String fromCountry = "";
//            String currentNeighbor;
//            System.out.println("From which country do you want to attack?");
//            Boolean hasAttackCountries = null;
//            for (int i = 0; i < risk.getPlayerOnTurn().getCountries().size(); i++) {
//                Country currentCountry = risk.getPlayerOnTurn().getCountries().get(i);
//                if (currentCountry.getUnits() > 1) {
//                    System.out.println(currentCountry);
//                    hasAttackCountries = true;
//                }
//            }
//            if (!Boolean.TRUE.equals(hasAttackCountries)) {
//                System.out.println("You do not have any countries to attack from.");
//                attackLoop = false;
//                return;
//            }
//            boolean validCountry = false;
//            while (!validCountry) {
//                try {
//                    fromCountry = readInput();
//                    while (risk.getCountryByName(fromCountry).getUnits() < 2) {
//                        System.out.println("You need at least 2 units to attack. Give another country :");
//                        fromCountry = readInput();
//                    }
//                    while (!risk.validateOwnership(fromCountry, risk.getPlayerOnTurn().getName())) {
//                        System.out.println("Give a country that you own :");
//                        fromCountry = readInput();
//                    }
//                    validCountry = true; // If we reach this point, the country is valid and we can exit the loop
//                } catch (CountryNotFoundException e) {
//                    System.out.println("Country not found. Please enter a valid country name.");
//                }
//            }
//            int units;
//            if (risk.getCountryByName(fromCountry).getUnits() - 1 < 3) {
//                units = risk.getCountryByName(fromCountry).getUnits() - 1;
//            } else {
//                units = 3;
//            }
//            System.out.println("You can place 1-" + units + " units to attack. How many units do you want to place?");
//            int units2 = Integer.parseInt(readInput());
//            while (!risk.validateAttackUnits(units2)) {
//                System.out.println("Your input was invalid. Give another number [1-" + units + "]:");
//                units2 = Integer.parseInt(readInput());
//            }
//            String toCountry = "";
//            System.out.println("Which country do you want to attack?");
//            for (int i = 0; i < risk.getCountryByName(fromCountry).getNeighbors().size(); i++) {
//                currentNeighbor = risk.getCountryByName(fromCountry).getNeighbors().get(i).getName();
//                if (!risk.validateOwnership(currentNeighbor, risk.getPlayerOnTurn().getName())) {
//                    System.out.println(currentNeighbor);
//                }
//            }
//            boolean validCountry2 = false;
//            Country from = null;
//            Country to = null;
//            while (!validCountry2) {
//                try {
//                    toCountry = readInput();
//                    risk.getCountryByName(toCountry); // This will throw CountryNotFoundException if the country does not exist
//                    while (risk.validateOwnership(toCountry, risk.getPlayerOnTurn().getName())) {
//                        System.out.println("Give a country that you don't own :");
//                        toCountry = readInput();
//                    }
//                    validCountry2 = true; // If we reach this point, the country is valid and we can exit the loop
//                } catch (CountryNotFoundException e) {
//                    System.out.println("Country not found. Please enter a valid country name.");
//                }
//                to = risk.getCountryByName(toCountry);
//                from = risk.getCountryByName(fromCountry);
//                boolean areNeighbours = false;
//                while (!areNeighbours) {
//                    try {
//                        if (!risk.validateNeighboring(from, to)) {
//                            throw new NotANeighbourCountry(toCountry);
//                        }
//                        areNeighbours = true; // If we reach this point, the countries are neighbours and we can exit the loop
//                    } catch (NotANeighbourCountry e) {
//                        System.out.println("Error: " + e.getMessage());
//                        toCountry = readInput();
//                        to = risk.getCountryByName(toCountry);
//                    }
//                }
//            }
//            System.out.println("From: " + from.getName() + " To: " + to.getName() + " Units: " + units);
//            Player player = risk.getPlayerOnTurn();
//            if (to.getUnits() > 1) {
//                System.out.println(to.getPlayer().getName() + " how many units to you want to place to defend " + to.getName() + " [1 - 2]?");
//            } else {
//                System.out.println(to.getPlayer().getName() + " how many units to you want to place to defend " + to.getName() + " [1]?");
//            }
//            int defenderUnits = 0;
//            while (defenderUnits > 2 || defenderUnits < 1) {
//                defenderUnits = Integer.parseInt(readInput());
//                System.out.println("You can only place 1 or 2 units to defend.");
//                System.out.println("Please enter a valid number of units to defend [1 - 2]:");
//            }
//            risk.makeAttack(from.getName(), to.getName(), player.getName(), units);
//            System.out.println("Do you want to attack again? [y/n]");
//            answer = readInput();
//            while (!answer.equals("y") && !answer.equals("n")) {
//                System.out.println("Invalid input. Please enter y or n.");
//                answer = readInput();
//            }
//            if (answer.equals("n")) {
//                attackLoop = false;
//                break;
//            }
//        }
//
//    }
//    /**
//     * The entry point for the Risk game command-line user interface (CUI).
//     * <p>
//     * This method initializes the game by creating a new instance of {@link Cui} with the specified game data name. It then calls the {@code mainMenu} method to display the main menu and start the game interaction. The game data name "Risk" is passed to the {@link Cui} constructor, which sets up the game environment based on this data.
//     * <p>
//     * Exceptions such as {@link CountryNotFoundException} and {@link InvalidUnitException} are thrown if there are issues with the game data, such as missing countries or invalid unit configurations.
//     *
//     * @param args the command line arguments, not used in this application.
//     * @throws CountryNotFoundException if the game data includes references to countries that cannot be found.
//     * @throws InvalidUnitException if the game data contains invalid unit configurations.
//     */
//    public static void main (String[] args) throws CountryNotFoundException, InvalidUnitException {
//        Cui cui = new Cui("Risk");
//        cui.mainMenu();
//    }
//}

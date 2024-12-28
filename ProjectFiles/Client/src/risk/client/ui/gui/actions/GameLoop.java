package risk.client.ui.gui.actions;


import risk.common.entities.Country;
import risk.common.entities.UnitCard;
import risk.common.exceptions.CountryNotFoundException;
import risk.common.exceptions.InvalidUnitException;
import risk.client.ui.gui.subPanels.GeneratingMap;
import risk.client.ui.gui.MainMenuGui;
import risk.client.ui.gui.subPanels.GameInfoPanel;
import risk.common.interfaces.GameEventListener;
import risk.common.interfaces.RiskInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manages the main game loop, including unit distribution, attacks, and unit movements.
 * <p>
 * This class orchestrates the flow of the game by handling user interactions on the game map, such as distributing units at the beginning of a turn, executing attacks between countries, and moving units. It utilizes mouse listeners to detect user actions and updates the game state accordingly.
 * <p>
 * The game loop interacts with the {@link RiskInterface} to perform game logic operations and updates the UI components based on the current game state. It also handles the display of country information when the mouse hovers over a country and processes the actions taken by the player, such as attacking or moving units.
 */
public class GameLoop implements GameEventListener {
    private RiskInterface risk;
    //private Risk risk;
    private GeneratingMap map;
    private MainMenuGui mainFrame;
    private GameInfoPanel info;
    private boolean allowPlayerActions;


    public String country1;
    public String country2;
    private String action;

    /**
     * Constructs a GameLoop instance to manage the main game loop, including unit distribution, attacks, and unit movements.
     * <p>
     * This constructor initializes the game loop with references to the main game frame, the game state interface, the map panel, and the game information panel. It sets the initial game action to "turnInCards" and updates the action text to prompt the player to place units. Additionally, it sets up mouse listeners for handling country selection and information display.
     * <p>
     * The game loop is responsible for orchestrating the flow of the game, responding to user interactions, and updating the game state and UI accordingly.
     *
     * @param mainFrame The main frame of the application, used for displaying UI components.
     * @param risk The game state interface, providing access to game logic and state management.
     * @param map The map panel, displaying the game map and allowing for country selection.
     * @param info The game information panel, displaying current game information and updates.
     * @throws InvalidUnitException If an invalid unit operation is attempted during initialization.
     */
    public GameLoop(MainMenuGui mainFrame,RiskInterface risk, GeneratingMap map, GameInfoPanel info) throws InvalidUnitException {
        this.mainFrame = mainFrame;
        this.risk = risk;
        this.map = map;
        this.info = info;

        allowPlayerActions = true;
        action = "turnInCards";
//        risk.continentPlayerCheck(risk.getPlayerOnTurn().getName());
//        risk.addPlayerUnitsPerRound(risk.getPlayerOnTurn().getName());
        updateActionText("Click on a country to place units. Available units: " + risk.getPlayerOnTurn().getNumberOfUnits());
        mouseListener();
        mouseHover();
    }

    @Override
    public void onPlayerListChanged() {

    }

    @Override
    public void onGameStarted() {

    }

    @Override
    public void onAllowPlayerActions(boolean allow) {
        SwingUtilities.invokeLater(() -> {
            allowPlayerActions = allow;
            //System.out.println("Allow player actions: " + allowPlayerActions);
        });

    }

    /**
     * Displays country information in a floating window when the mouse hovers over a country on the map.
     * <p>
     * This method initializes a non-focusable floating window that shows detailed information about a country when the mouse pointer hovers over it. The information displayed includes the country's name, its owner, and the number of units present, formatted as HTML for better readability. The window's visibility is dynamically updated based on the mouse's position relative to the map's countries.
     * <p>
     * The method uses a {@link MouseMotionAdapter} to track mouse movement over the map. When the mouse moves over a country, it fetches the country's information using the {@code getCountryInformationGUI} method from the {@link RiskInterface}. The information is then displayed in a {@link JWindow} near the mouse cursor. If the mouse moves away from a country, the window is hidden.
     * <p>
     * Padding and offsets for the information window can be adjusted to change its appearance and position relative to the mouse cursor.
     */
    public void mouseHover() {
        // Create a JWindow to display the country information
        JWindow infoWindow = new JWindow();
        JLabel infoLabel = new JLabel();
        // Add padding to the infoLabel
        int topPadding = 5; // Change this to the desired top padding
        int leftPadding = 5; // Change this to the desired left padding
        int bottomPadding = 5; // Change this to the desired bottom padding
        int rightPadding = 5; // Change this to the desired right padding
        infoLabel.setBorder(BorderFactory.createEmptyBorder(topPadding, leftPadding, bottomPadding, rightPadding));

        infoWindow.add(infoLabel);

        infoWindow.setFocusableWindowState(false);
        infoWindow.setSize(new Dimension(200, 100)); // Change the width and height as needed
        infoLabel.setFont(new Font(infoLabel.getFont().getName(), Font.PLAIN, 16)); // Change the font size as needed


        map.label.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (allowPlayerActions) {
                    super.mouseMoved(e);
                    int x = e.getX();
                    int y = e.getY();
                    Color color = getPixelColor(x, y);
                    String hexColor = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                    String countryString = map.colorCountryMap.get(hexColor);

                    int xOffset = 15; // Change this to the desired x offset
                    int yOffset = 10; // Change this to the desired y offset

                    Point location = e.getLocationOnScreen();
                    location.translate(xOffset, yOffset);

                    if (countryString != null) {
                        try {
                            String countryInfo = risk.getCountryInformationGUI(countryString);
                            // Update the label with the country information
                            // Use HTML tags to allow line breaks
                            infoLabel.setText("<html>" + countryInfo.replace("\n", "<br>") + "</html>");
                            // Position the window at the mouse cursor and make it visible
                            infoWindow.setLocation(location);
                            infoWindow.pack();
                            infoWindow.setVisible(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        // Hide the window when the mouse is not over a country
                        infoWindow.setVisible(false);
                    }
                }
            }
        });
    }

    /**
     * Adds a mouse listener to the map label to handle mouse clicks.
     * <p>
     * This method attaches a {@link MouseAdapter} to the map's label component. When the mouse is clicked, it calculates the clicked position's color on the map and uses this color to determine the corresponding country. If a country is identified, it proceeds to handle the action associated with the country based on the current game state.
     * <p>
     * The method utilizes the {@code getPixelColor} method to determine the color of the clicked pixel, translates this color to a hexadecimal string, and then matches this string to a country in the {@code colorCountryMap}. If a match is found, it calls {@code actionTextCases} and {@code clickOptions} with the country's identifier to process the game logic associated with the click.
     */
    public void mouseListener() {
        map.label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();
                Color color = getPixelColor(x, y);
                String hexColor = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                String countryString = map.colorCountryMap.get(hexColor);
                if(countryString !=null) {
                    System.out.println("Call atc");
                    actionTextCases(countryString);
                    clickOptions(countryString);
                }
            }
        });
    }

    /**
     * Distributes units to the selected country during the distribution phase of the game.
     * <p>
     * This method is invoked during the distribution phase, where players allocate their available units to their owned countries. It prompts the player to choose the number of units to place in the selected country. If the player attempts to place more units than available, an error message is displayed.
     * <p>
     * The method checks if the player owns the selected country and if the player has any units left to distribute. Based on the player's response and the number of units specified, units are added to the country. The game state is updated accordingly, transitioning to the attack phase if no units remain or continuing in the distribution phase.
     * <p>
     * Note: The method also handles the case where the distribution is done automatically by the server in a networked game environment.
     * @throws InvalidUnitException If an invalid operation is performed with the units.
     * @throws CountryNotFoundException If the specified country does not exist in the game state.
     */
    public void distributeUnits() throws InvalidUnitException, CountryNotFoundException {
        if (risk.getPlayerOnTurn().getNumberOfUnits() == 0) {  //CHANGE BACK to == 0
            action = "attack";
        } else if (risk.getPlayerOnTurn().ownsCountry(country1)){
            int response = JOptionPane.showConfirmDialog(mainFrame, "Do you want to place units in " + country1 + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int input = Integer.parseInt(JOptionPane.showInputDialog(mainFrame, "How many units do you want to place?", "Input", JOptionPane.QUESTION_MESSAGE));
                System.out.println("Input: " + input);
                if (input > risk.getPlayerOnTurn().getNumberOfUnits()) {
                    System.out.println("You don't have enough units. You have " + risk.getPlayerOnTurn().getNumberOfUnits() + " units available.");
                    JOptionPane.showMessageDialog(mainFrame, "You don't have enough units. You have " + risk.getPlayerOnTurn().getNumberOfUnits() + " units available.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    SwingUtilities.invokeLater(() -> {
                        try {

                            risk.addPlayerUnitsToCountry(risk.getPlayerOnTurn().getName(), country1, input);
                            country1 = null;
                            country2 = null;
                        } catch (InvalidUnitException e) {
                            throw new RuntimeException(e);
                        }
                       // System.out.println(risk.getUnitsByCountry(country1) + " units in " + country1);
                        //System.out.println(risk.getPlayerOnTurn().getNumberOfUnits());
                    });
                        action = risk.getPlayerOnTurn().getNumberOfUnits() == 0 ? "attack" : "distribution"; //CHANGE BACK to == 0
                }
            } else if (response == JOptionPane.NO_OPTION) {
                country1 = null;
                country2 = null;
            }
            //risk.distributeStartingUnits();    //Muss an anderestelle durchgeführt werden / Server
            updateActionText("Click on a country to place units. Available units: " + risk.getPlayerOnTurn().getNumberOfUnits());
        }
        if (risk.getPlayerOnTurn().getNumberOfUnits() == 0) {
            updateActionText("Choose a country to attack from");
        }
    }

    /**
     * Executes an attack from one country to another during the game's attack phase.
     * <p>
     * This method manages the attack process, including confirming the attack initiation, selecting the attacking and defending countries, and determining the number of units used in the attack. It prompts the user for confirmation and input through dialog boxes. Depending on the user's choices and the outcome of the attack, it updates the game state accordingly.
     * <p>
     * If the player decides to start an attack, they must first select the attacking country (country1) and then the defending country (country2). The player is then asked to specify the number of units to use in the attack, adhering to the game rules (1-3 units, but not more than the number of units in the attacking country minus one). After the attack, the player can choose to attack again or move to the next phase of the game.
     * <p>
     * The method handles various scenarios, such as invalid input for the number of units or cancelling the attack. It ensures that the game's state is updated correctly after each action and provides feedback to the player through the UI.
     *
     * @throws RuntimeException if an unexpected error occurs during the attack process.
     */
    public void attack() {
        if (country1 != null && country2 == null) {
            int response1 = JOptionPane.showConfirmDialog(mainFrame, "Do you want to start an attack from " + country1 + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (response1 == JOptionPane.NO_OPTION) {
                country1 = null;
            } else if (response1 == JOptionPane.YES_OPTION) {
                updateActionText("Select a country you want to attack");
                action = "attack";
            }
        } else if (country1 != null && country2 != null) {
            int response2 = JOptionPane.showConfirmDialog(mainFrame, "Do you want to attack " + country2 + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (response2 == JOptionPane.NO_OPTION) {
                country2 = null;
            } else if (response2 == JOptionPane.YES_OPTION) {
                try {
                    int input = 0;
                    try {
                        if (risk.getUnitsByCountry(country1) - 1 > 3) {
                            input = Integer.parseInt(JOptionPane.showInputDialog(mainFrame, "You can place 1-3 units to attack. How many units do you want to place?", "Input", JOptionPane.QUESTION_MESSAGE));
                        } else {
                            input = Integer.parseInt(JOptionPane.showInputDialog(mainFrame, "You can place 1-" + (risk.getUnitsByCountry(country1) - 1) + " units to attack. How many units do you want to place?", "Input", JOptionPane.QUESTION_MESSAGE));
                        }
                    } catch (NumberFormatException e) {
                        updateActionText("! Attacker: Only numbers please :(");
                        timedText("Choose a country to attack from");
                    }
                    if (input > risk.getUnitsByCountry(country1) - 1) {
                        updateActionText("! You can't place more units than you have in " + country1);
                        timedText("Choose a country to attack from");
                    } else if (input < 1) {
                        updateActionText("! You need to place at least 1 unit to attack.");
                        timedText("Choose a country to attack from");
                    } else if (input > 3) {
                        updateActionText("! You can't place more than 3 units to attack.");
                        timedText("Choose a country to attack from");
                    } else {
                        try {
                            risk.makeAttack(country1, country2, risk.getPlayerOnTurn().getName(), input);
                            //info.redrawPanel();
//                                        JOptionPane.showMessageDialog(mainFrame "You have conquered " + country2 + "!", "Result", JOptionPane.INFORMATION_MESSAGE);
                            int answer = JOptionPane.showConfirmDialog(mainFrame, "Do you want to attack again?", "Confirm", JOptionPane.YES_NO_OPTION);
                            if (answer == JOptionPane.YES_OPTION) {
                                action = "attack";
                                updateActionText("Select a country to attack from");
                            } else if (answer == JOptionPane.NO_OPTION) {
                                action = "moveUnits";
                                updateActionText("Choose a country to move your units from");
                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (Exception e) {
                    country2 = null;
                    country1 = null;
                    throw new RuntimeException(e);
                }
            }
            country2 = null;
            country1 = null;
        }
    }

    /**
     * Moves units from one country to another if the player decides to do so.
     * <p>
     * This method first checks if there are more than one unit available in the source country. If so, it prompts the player to specify the number of units they wish to move. The method ensures that the number of units specified does not exceed the available units minus one, as at least one unit must remain in the source country.
     * <p>
     * After successfully moving the units, the method nullifies the source and destination country variables to reset the state for future actions. It then prompts the player to decide if they want to move more units. Depending on the player's response, it either proceeds to the next turn or allows the player to select another country from which to move units.
     * <p>
     * The method also interacts with the game state to check the current player's turn, update the UI components accordingly, and manage the transition between different phases of the game.
     *
     * @throws NumberFormatException If the input for the number of units to move is not a valid integer.
     */
    public void moveUnits(){
        int availableUnits;
        availableUnits = risk.getUnitsByCountry(country1);
        if (availableUnits > 1) {
            updateActionText("Available Units in " + country1 + ": " + availableUnits);
            String input = JOptionPane.showInputDialog(mainFrame, "How many Units do you want to move?", "Units amount", JOptionPane.QUESTION_MESSAGE);
            int units = Integer.parseInt(input);
            if (units > availableUnits - 1) {
                while (units > availableUnits - 1) {
                    units = Integer.parseInt(JOptionPane.showInputDialog(mainFrame, "Insufficient units, units available:" + availableUnits, "Units amount", JOptionPane.QUESTION_MESSAGE));
                }
            }
            final int finalunits = units;

            SwingUtilities.invokeLater(() -> {
                try {
                    risk.moveUnits(risk.getPlayerOnTurn().getName(), country1, country2, finalunits);
                    country1 = null;
                    country2 = null;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        int response = JOptionPane.showConfirmDialog(mainFrame, "Do you want to move more Units?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION){
            risk.nextTurn();
            risk.continentPlayerCheck(risk.getPlayerOnTurn().getName());
            //risk.addPlayerUnitsPerRound(risk.getPlayerOnTurn()); //muss bei den Servern pro runde durchgeführt werden
            //info.redrawPanel();
            action = "turnInCards";
            updateActionText("Click on a country to place units. Available units: " + risk.getPlayerOnTurn().getNumberOfUnits());
        } else {
            updateActionText("Select a Country from where you want to move units");
        }

    }

    /**
     * Handles the logic for different game actions based on the current state and the selected country.
     * <p>
     * This method is responsible for managing the game's logic in response to user actions such as turning in cards, distributing units, attacking, and moving units. It checks the current game state and the selected country to determine the appropriate action to take. The method updates the action text based on the game state and the outcome of the action.
     * <p>
     * The method uses a switch statement to handle different game actions:
     * <ul>
     *     <li>turnInCards: No specific action, designed to be extended.</li>
     *     <li>distribution: Checks if the player owns the selected country and updates the action text accordingly.</li>
     *     <li>attack: Validates if the selected country can be attacked based on ownership and adjacency rules.</li>
     *     <li>moveUnits: Validates if units can be moved from one country to another based on ownership and adjacency.</li>
     * </ul>
     * <p>
     * If the selected country does not meet the criteria for the intended action, the method updates the action text with an appropriate message. It also handles exceptions such as {@link CountryNotFoundException} to manage errors gracefully.
     *
     * @param country The country selected by the player for the current action.
     */
    private void actionTextCases(String country){
        try {
            switch (action) {
                case "turnInCards":
                    break;
                case "distribution":
                    if (!risk.getPlayerOnTurn().ownsCountry(country)) {
                        updateActionText("! You dont own " + country);
                        timedText("Click on a country to place units. Available units: ICH KANN DIE FKING UNITS NICHT ÜBERTRAGEN!!!!");
                    }
                    break;

                case "attack":
                    if (country1 == null) {
                        updateActionText("Choose a country to attack from");
                        if (!risk.getPlayerOnTurn().ownsCountry(country)) {
                            updateActionText("! You dont own " + country);
                            timedText("Choose a country to attack from");
                        }
                        if (risk.getUnitsByCountry(country) < 2) {
                            updateActionText("! Not enough Units, you need at least 2 Units");
                            timedText("Choose a country to attack from");
                        }
                    } else {
                        if (risk.validateOwnership(country, risk.getPlayerOnTurn().getName())) {
                            updateActionText("! You can't attack your own country");
                            timedText("Choose a country to attack to");
                        }
                    }
                    if (!risk.checkCountryNeighbor(country1, country)) {
                        updateActionText("! " + country + " is not a neighbor of " + country1);
                        timedText("Choose a country to attack to");
                    }

                    break;

                case "moveUnits":
                    if (!risk.getPlayerOnTurn().ownsCountry(country) && (country1 == null || country2 == null)) {
                        updateActionText("! You dont own " + country);
                        if (country1 == null) {
                            timedText("Choose a country to move units from");
                        } else {
                            timedText("Choose a country to move units to");
                        }
                    }
                    if (!risk.checkCountryNeighbor(country1, country)) {
                        updateActionText("! " +country + " is not a neighbor of " + country1);
                        timedText("Choose a country to move units to");
                    }
                    break;

                default:
                    updateActionText("Im WRONG");
            }
        } catch (Exception e) {
            System.out.println("AHHHHH");
        }
    }

    /**
     * Handles click actions on the game map based on the current game phase and selected country.
     * <p>
     * This method manages the game's logic in response to user clicks on the game map. Depending on the current game phase (e.g., turning in cards, distributing units, attacking, or moving units), it performs different actions. The method uses the current game state, the action selected by the player, and the country clicked on to determine the appropriate response.
     * <p>
     * In the "turnInCards" phase, the player can turn in unit cards if they have more than two but less than five, or must turn them in if they have five. In the "distribution" phase, the player selects a country to distribute their units. During the "attack" phase, the player selects an attacking country and a target country. In the "moveUnits" phase, the player can move units between countries they own.
     * <p>
     * The method updates the game state and UI based on the actions taken, and handles exceptions such as {@link InvalidUnitException} and {@link CountryNotFoundException} to manage errors gracefully.
     *
     * @param country The country selected by the player for the current action.
     * @throws RuntimeException If an unexpected error occurs during the execution of the game action.
     */
    private void clickOptions(String country){
        try {
            switch (action) {
                case "turnInCards":    //Change drawUnitCard in riskAdmin!!!!
                    if (risk.getPlayerOnTurn().getUnitCards().size() == 5) {
                        turnInCards();
                        action = "distribution";
                    } else if (risk.getPlayerOnTurn().getUnitCards().size() > 2 && risk.getPlayerOnTurn().getUnitCards().size() <5){
                        int response = JOptionPane.showConfirmDialog(mainFrame, "You have available Unit Cards. \n Do you want to turn them in?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.NO_OPTION) {
                            action = "distribution";
                        } else {
                            turnInCards();
                            action = "distribution";
                        }
                    } else action = "distribution";
                    break;
                case "distribution":
                    country1 = country;
                    try {
                        distributeUnits();
                    } catch (InvalidUnitException | CountryNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "attack":
                    if (country1 == null) {
                        if (risk.getUnitsByCountry(country) < 2) break;
                        if (!risk.getPlayerOnTurn().ownsCountry(country)) break;
                        int response = JOptionPane.showConfirmDialog(mainFrame, "Do you want to make an attack?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            country1 = country;
                            attack();
                        } else {
                            updateActionText("Choose a country to move units from");
                            action = "moveUnits";
                        }
                    } else if (country1.equals(country)) {
                        int response = JOptionPane.showConfirmDialog(mainFrame, "Do you want to cancel the attack?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION){
                            country1 = null; country2 = null;
                            updateActionText("Choose a country to attack from");
                        }
                    } else {
                        if (risk.validateOwnership(country, risk.getPlayerOnTurn().getName())) break;
                        country2 = country;
                        attack();
                    }
                    break;
                case "moveUnits":
                    if (!risk.validateOwnership(country, risk.getPlayerOnTurn().getName())) break;
                    if (country1 == null) {
                        int response = JOptionPane.showConfirmDialog(mainFrame, "Do you want to move units from " + country + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            country1 = country;
                        }
                    } else if (risk.checkCountryNeighbor(country1, country)) {
                        int response = JOptionPane.showConfirmDialog(mainFrame, "Do you want to move units to " + country + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION) {
                            country2 = country;
                            moveUnits();
                        }
                    } else if (country1.equals(country)) {
                        int response = JOptionPane.showConfirmDialog(mainFrame, "Do you want to cancel move Units from " + country1 + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (response == JOptionPane.YES_OPTION){
                            country1 = null; country2 = null;
                            updateActionText("Choose a country to move Units from");
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("not found");
        }
    }

    /**
     * Schedules a delayed update of the action text.
     * <p>
     * This method creates a {@link javax.swing.Timer} with a specified delay, during which it waits before executing the action defined in its {@link java.awt.event.ActionListener}. Once the delay has passed, it updates the action text with the provided string. This method is useful for providing feedback to the user after a certain period, allowing for temporary messages or prompts before reverting or updating the text displayed to the user.
     * <p>
     * The timer is set to not repeat, ensuring that the action text is updated exactly once after the specified delay.
     *
     * @param text the text to display after the delay
     */
    private void timedText(String text) {
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call updateActionText again after 1 second
                updateActionText(text);
            }
        });
        // The timer should only run once, so we set repeats to false
        timer.setRepeats(false);
        // Start the timer
        timer.start();
    }

    /**
     * Creates a panel containing checkboxes for each unit card owned by the current player.
     * <p>
     * This method iterates through the unit cards owned by the player currently taking their turn. For each unit card, it creates a checkbox with the card's type as the label. Each checkbox is associated with its corresponding unit card through the {@code putClientProperty} method, allowing for easy retrieval of the card object when the checkbox is interacted with.
     * <p>
     * The checkboxes are added to a panel, which is then returned. This panel can be used in a GUI to allow players to select which unit cards they wish to turn in.
     *
     * @return A {@link JPanel} containing a checkbox for each unit card owned by the current player.
     */
    private JPanel turnCardInPanel(){
        JPanel panel = new JPanel();
        for (UnitCard card : risk.getPlayerOnTurn().getUnitCards()) {
            JCheckBox checkBox = new JCheckBox(card.getType());
            checkBox.putClientProperty("UnitCard", card); // Store the UnitCard in the JCheckBox
            panel.add(checkBox);
        }
        return panel;
    }

    /**
     * Processes the turning in of unit cards by the player.
     * <p>
     * This method displays a panel with checkboxes for each unit card owned by the current player, allowing them to select which cards to turn in. The player must select exactly three cards to turn in. If the player has exactly five unit cards, they are required to turn in cards. If the player selects an appropriate combination of cards and confirms their choice, the selected cards are turned in, and the game state is updated accordingly.
     * <p>
     * The method validates the number of selected cards, ensuring that exactly three cards are chosen. If the selection is valid, it proceeds to turn in the cards and updates the game state. If the selection is invalid (either more than three cards or fewer than three), it prompts the player again until a valid selection is made or the operation is cancelled.
     * <p>
     * After successfully turning in cards, the method updates the action text to reflect the new state of the game, such as prompting the player to place units. If the player cancels the operation or an invalid state is detected (such as having five cards but not turning them in), appropriate error messages are displayed, and the player is prompted as necessary.
     *
     * @throws RuntimeException if an unexpected error occurs during the card turn-in process.
     */
    private void turnInCards(){
        JPanel panel = turnCardInPanel();
        int result = JOptionPane.showConfirmDialog(null, panel, "Select Options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Create a list to store the selected UnitCards
            List<UnitCard> selectedCards = new ArrayList<>();
            for (Component component : panel.getComponents()) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    if (checkBox.isSelected()) {
                        UnitCard card = (UnitCard) checkBox.getClientProperty("UnitCard"); // Retrieve the UnitCard from the JCheckBox
                        selectedCards.add(card);
                    }
                }
            }
            if (selectedCards.size() == 3) {
                String validationMessage = risk.turnInUnitCards(risk.getPlayerOnTurn().getName(), selectedCards.getFirst().getType(), selectedCards.get(1).getType(), selectedCards.get(2).getType());
                info.redrawPanel();
                updateActionText(validationMessage);
                timedText("Click on a country to place units. Available units: " + risk.getPlayerOnTurn().getNumberOfUnits());
            } else if (selectedCards.size() > 3){
                updateActionText("! You can only turn in 3 cards");
                turnInCards();
            } else {
                updateActionText("! You need to pick 3 unit cards to turn in");
                turnInCards();
            }
        } else if (risk.getPlayerOnTurn().getUnitCards().size() == 5){
            updateActionText("! You have 5 unit cards, you need to turn them in");
            turnInCards();
        } else {
            updateActionText("Click on a country to place units. Available units: " + risk.getPlayerOnTurn().getNumberOfUnits());
        }
    }






    public void startGame() throws CountryNotFoundException {
        //risk.pickRandomStartCountries();
    }

    /**
     * Retrieves the color of a specific pixel from the game map image.
     * <p>
     * This method accesses the game map's BufferedImage to extract the RGB value of the pixel at the specified coordinates. It then creates and returns a Color object based on this RGB value, supporting alpha values for transparency.
     *
     * @param x the x-coordinate of the pixel whose color is to be retrieved
     * @param y the y-coordinate of the pixel whose color is to be retrieved
     * @return the Color of the pixel at the specified x and y coordinates
     */
    private Color getPixelColor(int x, int y) {
        int rgb = map.mapImage.getRGB(x, y);
        return new Color(rgb, true);
    }

    /**
     * Updates the action text displayed on the game map.
     * <p>
     * This method sets the text of the action label in the {@link GeneratingMap} class to the specified text. It is used to provide feedback or instructions to the player based on the current state of the game. After updating the text, it triggers a repaint of the map to ensure that the updated text is displayed to the user.
     *
     * @param text The new text to be displayed on the action label.
     */
    public void updateActionText(String text) {
        map.actionText.setText(text);
        map.repaint();
    }

    /**
     * Retrieves the map image currently being used in the game.
     * <p>
     * This method accesses the {@link GeneratingMap} instance to obtain the {@link BufferedImage} representing the game map. The returned image can be used for various purposes, such as displaying the map in the UI, performing image analysis, or debugging.
     *
     * @return the {@link BufferedImage} of the game map.
     */
    public BufferedImage getMapImage() {
        return map.mapImage;
    }

    /**
     * Updates the image of a specified country on the game map with a grayscale image.
     * <p>
     * This method searches for the color associated with the specified country in the color-country mapping. If found, it converts the hex color code to RGB and iterates over all pixels in the map image. For each pixel matching the country's color, it replaces the pixel with the corresponding pixel from the provided grayscale image. This process effectively overlays the grayscale image onto the specified country's area on the map.
     * <p>
     * After updating the pixels, the method triggers a repaint of the map to reflect the changes. This is useful for visually indicating changes in the game state, such as a country being conquered or affected by a specific event.
     *
     * @param country The country whose image is to be updated.
     * @param grayImage The grayscale image to overlay on the country's area.
     */
    public void updateCountryImage(Country country, BufferedImage grayImage) {
        // Find the color of the country in the color map
        String countryColorHex = null;
        for (Map.Entry<String, String> entry : map.colorCountryMap.entrySet()) {
            if (entry.getValue().equals(country.getName())) {
                countryColorHex = entry.getKey();
                break;
            }
        }

        if (countryColorHex == null) {
            System.out.println("Country color not found");
            return;
        }

        // Convert the hex color to RGB
        int countryColorRGB = Integer.parseInt(countryColorHex, 16);

        // Iterate over the pixels in the map image
        for (int y = 0; y < map.mapImage.getHeight(); y++) {
            for (int x = 0; x < map.mapImage.getWidth(); x++) {
                // If the pixel color matches the country color, replace it with the gray image pixel
                if (map.mapImage.getRGB(x, y) == countryColorRGB) {
                    int grayX = x % grayImage.getWidth();
                    int grayY = y % grayImage.getHeight();
                    map.mapImage.setRGB(x, y, grayImage.getRGB(grayX, grayY));
                }
            }
        }

        // Repaint the GUI to reflect the changes
        map.repaint();
    }

    public void gameLoop(){

    }
}

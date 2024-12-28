package risk.client.ui.gui.panels;


import risk.common.entities.*;
import risk.client.ui.gui.MainMenuGui;
import risk.client.ui.gui.actions.GameLoop;
import risk.client.ui.gui.subPanels.*;
import risk.common.interfaces.GameEventListener;
import risk.common.interfaces.RiskInterface;
import risk.common.exceptions.InvalidUnitException;
import risk.client.ui.gui.subPanels.GameInfoPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * Represents the panel for setting up the game, including player addition, color selection, and game start.
 * <p>
 * This panel allows users to add players with unique colors to the game, delete players, and start the game. It includes text fields for entering player names, radio buttons for color selection, and buttons for adding or deleting players and starting the game. The layout is organized into top, center, and bottom panels, with the center panel containing the main interactive components.
 * <p>
 * The {@code GameSetupPanel} is a crucial part of the game's initial setup phase, ensuring that players are properly added and configured before the game begins.
 */
public class GameSetupPanel extends JPanel implements GameEventListener {
    private RiskInterface risk;
    //private risk risk;
    //private Risk risk;
    private GamePanel gamePanel;
    private JTextField playerNameField;
    private JPanel topButtonsPanel = new JPanel(new GridLayout(1, 2));
    private JPanel bottomButtonsPanel = new JPanel(new GridLayout(1, 2));
    private DefaultListModel<String> listModel;
    private ButtonGroup colorGroup = new ButtonGroup();
    private JPanel centerPanel;
    private JList<String> playerList;
    private JPanel topRightPanel; // Panel to hold the delete button
    JPanel rightPanel;
    private MainMenuGui mainFrame;

    /**
     * Constructs a new GameSetupPanel instance, initializing the UI components and setting up the game logic.
     * <p>
     * This constructor initializes the panel by setting up its layout and UI components, such as buttons, text fields, and panels for player addition, color selection, and game start. It also associates the panel with the main game frame and the game logic interface to facilitate interactions between the UI and the game state.
     * <p>
     * @param mainFrame the main frame of the game's UI, used for navigating between different panels
     * @param risk the game logic and data handler, containing information about the game state and players
     */
    public GameSetupPanel(MainMenuGui mainFrame, RiskInterface risk) {
        this.risk = risk;
        initialize(mainFrame);
        this.mainFrame = mainFrame;
        risk.addGameEventListener(this);
    }

    @Override
    public void onPlayerListChanged(){
        SwingUtilities.invokeLater(() -> updatePlayerList());
    }

    @Override
    public void onGameStarted(){
        SwingUtilities.invokeLater(() -> {
            this.gamePanel = mainFrame.getGamePanel();
            System.out.println("gamePanel esssss");
            GeneratingMap map = new GeneratingMap(risk);
            System.out.println("Map got generated");
            GameInfoPanel gameInfoPanel = new GameInfoPanel(risk);
            //                      map.addGameInfoPanel(gameInfoPanel);
            gamePanel.setUpPlayerInfoPanel(rightPanel, risk);
            gamePanel.add(map, BorderLayout.CENTER);
            gamePanel.add(new GameInfoPanel(risk), BorderLayout.EAST);
            try {
                GameLoop gameLoop = new GameLoop(mainFrame, risk, map, gameInfoPanel);
                risk.addGameEventListener(gameLoop);
            } catch (InvalidUnitException e) {
                System.out.println(e.getMessage()+ "WWWWWWWWWW!!!!!");
            }
            mainFrame.setGamePanel(gamePanel); // You'll need to add a setGamePanel method in MainMenuGui
        });
    }

    @Override
    public void onAllowPlayerActions(boolean allow) {

    }

    /**
     * Initializes the UI components and layout for the GameSetupPanel.
     * <p>
     * This method sets up the main layout and UI components of the GameSetupPanel, including the top, left, center, right, and bottom panels. It configures the dimensions, borders, and opaqueness of these panels and adds them to the GameSetupPanel. The method also calls helper methods to further set up individual panels with specific components like buttons, text fields, and radio buttons for player interaction.
     * <p>
     * The layout is primarily based on a BorderLayout, with additional panels added to create a structured and user-friendly interface for game setup. The method ensures that all components are properly aligned and visually consistent, providing a seamless experience for users setting up a new game.
     *
     * @param mainFrame the main frame of the game's UI, providing context for navigation and interaction within the game setup
     */
    private void initialize(MainMenuGui mainFrame){
        setPreferredSize(new Dimension(300, 300));

        // Panel principal
        JPanel topPanel = new JPanel();
        JPanel leftPanel = new JPanel();
        centerPanel = new JPanel();
        rightPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JPanel centerWrepperPanel = new JPanel();

        // Border layout
        topPanel.setBorder(null);
        leftPanel.setBorder(new LineBorder(Color.GRAY, 1));
        centerPanel.setBorder(new LineBorder(Color.GRAY, 1));
        rightPanel.setBorder(new LineBorder(Color.GRAY, 1));
        bottomPanel.setBorder(null);

//        leftPanel.setOpaque(false);
//        topPanel.setOpaque(false);
        centerPanel.setOpaque(false);
//        bottomPanel.setOpaque(false);

        // Add panels to the frame
        this.setLayout(new BorderLayout(5, 5));
        this.setOpaque(false);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(leftPanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);
//        this.add(rightPanel, BorderLayout.EAST);
        this.add(bottomPanel, BorderLayout.SOUTH);



        this.setBorder(BorderFactory.createEmptyBorder(80, 10, 0, 10));

        // Top panel and bottom panel
        topPanel.setPreferredSize(new Dimension(300, 50));
        topPanel.setMinimumSize(new Dimension(300, 50));
        topPanel.setMaximumSize(new Dimension(300, 50));

        bottomPanel.setPreferredSize(new Dimension(300, 41));
        bottomPanel.setMinimumSize(new Dimension(300, 41));
        bottomPanel.setMaximumSize(new Dimension(300, 41));

        centerPanel.setPreferredSize(new Dimension(300, 420)); // Setzt die bevorzugte Größe
        centerPanel.setMinimumSize(new Dimension(300, 400)); // Setzt die minimale Größe
//        centerPanel.setMaximumSize(new Dimension(300, 420)); // Setzt die maximale Größe

        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));

        // Minimal and maximal size for the panels
        leftPanel.setPreferredSize(new Dimension(280, 220));
        leftPanel.setMinimumSize(new Dimension(150, 200));
        leftPanel.setMaximumSize(new Dimension(280, 220));

//        leftPanel.setBorder(BorderFactory.createEmptyBorder(60, 0, 80, 0));

        centerWrepperPanel.setPreferredSize(new Dimension(300, 250));
        centerWrepperPanel.setMinimumSize(new Dimension(300, 200));
        centerWrepperPanel.setMaximumSize(new Dimension(300, 250));



//        rightPanel.setPreferredSize(new Dimension(200, 200));
//        rightPanel.setMinimumSize(new Dimension(150, 190));
//        rightPanel.setMaximumSize(new Dimension(200, 190));

        // Adding components to panels
        setupTopPanel(topPanel);
        setupLeftPanel(leftPanel);
//        setupRightPanel(rightPanel);
        setupCenterPanel(centerPanel);
        setupBottomPanel(bottomPanel, mainFrame);


        centerWrepperPanel.setBackground(new Color(255, 255, 255, 180));
        centerWrepperPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        centerWrepperPanel.add(leftPanel);
        centerWrepperPanel.add(centerPanel);
//        centerWrepperPanel.add(topPanel);
//        centerWrepperPanel.add(bottomPanel);
        this.add(centerWrepperPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(200, 170));

    }

    /**
     * Sets up the top panel with a centered title and a specific background color.
     * <p>
     * This method configures the layout of the provided panel to use a {@link FlowLayout} centered alignment. It adds a {@link JLabel} titled "Add Players" with a specific font and sets the panel's background color to a semi-transparent white. This setup is intended for the top section of a user interface, providing a clear label and a visually appealing design.
     *
     * @param panel the {@link JPanel} to be configured as the top panel of the UI
     */
    private void setupTopPanel(JPanel panel) {
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel titleLabel = new JLabel("Add Players");
        titleLabel.setFont(new Font("Algerian", Font.PLAIN, 40));  // Setting the font to Algerian
        panel.add(titleLabel);
        panel.setBackground(new Color(255, 255, 255, 180));
    }

//    private void setupRightPanel(JPanel panel) {
//        panel.setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.anchor = GridBagConstraints.NORTHWEST;
//        gbc.insets = new Insets(5, 5, 5, 5);
//
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weightx = 1.0;
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 1;
//
//        JButton deleteButton = new JButton("Delete");
//        deleteButton.setBackground(Color.RED);
//        deleteButton.setForeground(Color.WHITE);
//        deleteButton.addActionListener(e -> deletePlayer());
//
//        panel.add(deleteButton, gbc);
//
//        gbc.gridy = 1;
//        gbc.weighty = 1.0;
//        panel.add(Box.createVerticalGlue(), gbc);
//    }

    /**
     * Sets up the left panel of the game setup interface, including components for player name input, color selection, and player management buttons.
     * <p>
     * This method configures the left panel using a {@link GridBagLayout} to arrange its components, which include a label and text field for player name input, a panel with radio buttons for color selection, and buttons for adding or deleting players. The layout is designed to ensure that components are well-organized and visually appealing.
     * <p>
     * The color selection is managed through a {@link ButtonGroup} to ensure that only one color can be selected at a time. The "Add Player" and "Delete" buttons are equipped with action listeners to handle the respective operations.
     * <p>
     * @param panel the {@link JPanel} that will be configured as the left panel of the game setup interface
     */
    private void setupLeftPanel(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        // Label for player name (aligned left)
        JLabel playerNameLabel = new JLabel("Player Name:");
        playerNameLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(playerNameLabel, gbc);

        // Player name field (default alignment)
        playerNameField = new JTextField(20); // Set initial columns
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(playerNameField, gbc);

        // Panel for radio buttons (just for visualization)
        JPanel radioPanel = new JPanel(new GridLayout(2, 1));  // 2 rows, 1 column for top and bottom buttons
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(radioPanel, gbc);

        // Radio buttons for color selection - Left side (top and bottom)
        JRadioButton redLeftButton = new JRadioButton("Red");
        JRadioButton blueLeftButton = new JRadioButton("Blue");
        JRadioButton blackLeftButton = new JRadioButton("Black");

        colorGroup.add(redLeftButton);
        colorGroup.add(blueLeftButton);
        colorGroup.add(blackLeftButton);

        topButtonsPanel.add(redLeftButton);
        topButtonsPanel.add(blueLeftButton);
        topButtonsPanel.add(blackLeftButton);

        // Radio buttons for color selection - Right side (top and bottom)
        JRadioButton greenRightButton = new JRadioButton("Green");
        JRadioButton yellowRightButton = new JRadioButton("Yellow");
        JRadioButton pinkRightButton = new JRadioButton("Pink");

        redLeftButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        blueLeftButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        blackLeftButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        greenRightButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        yellowRightButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        pinkRightButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));

        colorGroup.add(greenRightButton);
        colorGroup.add(yellowRightButton);
        colorGroup.add(pinkRightButton);

        bottomButtonsPanel.add(greenRightButton);
        bottomButtonsPanel.add(yellowRightButton);
        bottomButtonsPanel.add(pinkRightButton);

        radioPanel.add(topButtonsPanel);
        radioPanel.add(bottomButtonsPanel);

        // Add player button
        JButton addButton = new JButton("Add Player");
        addButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        addButton.setBackground(new Color(7, 159,31, 255));
        addButton.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(addButton, gbc);

        // Add action listener to the button
        addButton.addActionListener(e -> addPlayer());

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deletePlayer());
        gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(deleteButton, gbc);

        // Adding glue to push components to the top
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);


    }

    /**
     * Configures the center panel of the game setup interface, including the player list and its scroll pane.
     * <p>
     * This method sets the layout of the specified panel to {@link BorderLayout} and initializes the player list with a custom cell renderer to center text horizontally. It also adds a mouse listener to the list to allow item removal on double click. The list is wrapped in a {@link JScrollPane} to ensure it remains scrollable when the number of players exceeds the visible area.
     * <p>
     * @param panel the {@link JPanel} that will be configured as the center panel of the game setup interface
     */
    private void setupCenterPanel(JPanel panel) {
        panel.setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        playerList = new JList<>(listModel);
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playerList.setLayoutOrientation(JList.VERTICAL);
        playerList.setVisibleRowCount(-1);

        playerList.setFont(new Font("JetBrains Mono", Font.PLAIN, 18));

        // Set custom renderer to center the text horizontally
        playerList.setCellRenderer(new CenteredListCellRenderer());

        JScrollPane listScroller = new JScrollPane(playerList);
        listScroller.setPreferredSize(new Dimension(150, 80));
        panel.add(listScroller, BorderLayout.CENTER);


        // Add mouse listener to handle item removal
        playerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = playerList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        listModel.remove(index);
                    }
                }
            }
        });
    }

    /**
     * Sets up the bottom panel of the game setup interface, including the start and quit buttons.
     * <p>
     * This method configures the bottom panel by setting its layout and background color. It creates and adds a "Start Game" button, which initiates the game start process, and a "Quit" button, which exits the application. The "Start Game" button is configured with an action listener that calls the {@code startGame} method, handling potential {@link InvalidUnitException} by throwing a {@link RuntimeException}. The "Quit" button's action listener terminates the application.
     * <p>
     * @param panel the {@link JPanel} that will be configured as the bottom panel of the game setup interface
     * @param mainFrame the main frame of the game's UI, providing context for navigation and interaction within the game setup
     */
    private void setupBottomPanel(JPanel panel, MainMenuGui mainFrame) {
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(255, 255, 255, 180));
        JButton startButton = new JButton("Set Ready");
        startButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        startButton.setBackground(Color.DARK_GRAY);
        startButton.setForeground(Color.WHITE);
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        quitButton.setBackground(Color.DARK_GRAY);
        quitButton.setForeground(Color.WHITE);

        startButton.addActionListener(e -> {
            try {
                startGame(mainFrame);
            } catch (InvalidUnitException ex) {
                System.out.println("Invalid unit exception");
            }
        });
        quitButton.addActionListener(e -> System.exit(0));

        panel.add(startButton);
        panel.add(quitButton);
    }

    /**
     * Adds a new player to the game setup with a specified color.
     * <p>
     * This method retrieves the player name from the input field and validates it to ensure it is not empty. It then checks if a color has been selected. If no color is selected, it displays an error message prompting the user to select a color. If a color is selected, it checks if another player has already been added with the same color to prevent color duplication among players. If the color is unique, the player is added to the game setup, and the player's name and color are displayed in the list of players. The input field is then cleared for the next entry.
     * <p>
     * If the player name is empty or the selected color is already in use, an error message is displayed to the user.
     */
    private void addPlayer() {
        String playerName = playerNameField.getText().trim();
        if (playerName.isEmpty()){
            JOptionPane.showMessageDialog(this, "Player name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String color = getSelectedColor();
        if (color == null){
            JOptionPane.showMessageDialog(this, "Please select a color.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < listModel.size(); i++) {
            String[] parts = listModel.getElementAt(i).split(" - ");
            if (parts[1].equals(color)) {
                JOptionPane.showMessageDialog(this, "There is already a player with the color " + color + ".", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        risk.addPlayer(playerName, color);

        SwingUtilities.invokeLater(() -> {

            updatePlayerList();
            playerNameField.setText("");
        });
    }

    /**
     * Updates the player list displayed in the UI.
     * <p>
     * This method clears the current list model and repopulates it with the updated list of players. Each player's name and color are concatenated into a single string and added to the list model. This ensures that the UI reflects the current state of the game, including any players that have been added or removed.
     */
    private void updatePlayerList() {
        listModel.clear();
        for (Player player : risk.getPlayers()) {
            listModel.addElement(player.getName() + " - " + player.getColor());
        }
    }

    /**
     * Removes a selected player from the game setup.
     * <p>
     * This method first checks if a player is selected in the player list. If a player is selected, it prompts the user with a confirmation dialog to ensure the user's intention to delete the player. Upon confirmation, the method removes the selected player from both the UI list model and the game's player list. If no player is selected, it displays an error message prompting the user to select a player to delete.
     * <p>
     * The deletion process involves updating the UI to reflect the removal and ensuring the game's state is consistent with the UI.
     */
    private void deletePlayer() {
        int selectedIndex = playerList.getSelectedIndex();
        if (selectedIndex != -1) {
            String playerName = listModel.getElementAt(selectedIndex).split(" - ")[0];
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + playerName + "?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                risk.getPlayers().remove(selectedIndex);
                listModel.remove(selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a player to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retrieves the selected color from the color selection buttons.
     * <p>
     * This method iterates through all buttons in the {@code colorGroup} to check which one is selected. It returns the text of the selected button, which corresponds to the color chosen by the user. If no button is selected, it returns {@code null}, indicating that no color has been selected.
     *
     * @return the text of the selected button representing the chosen color, or {@code null} if no color is selected
     */
    private String getSelectedColor() {
        Enumeration<AbstractButton> buttons = colorGroup.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null; // No color selected
    }

    /**
     * Initiates the game start process in a separate thread, handling server communication and UI updates.
     * <p>
     * This method creates a new thread to handle the game starting process, ensuring that the UI remains responsive. It first sends a "startGame" message to the server via the {@code risk} interface. Upon successful communication, it updates the UI on the Event Dispatch Thread (EDT) to display the game map and information panels. If any exceptions occur during the process, error messages are displayed to the user.
     * <p>
     * The method utilizes {@link SwingUtilities#invokeLater(Runnable)} to ensure that UI updates are performed on the EDT, following Swing's single-thread rule. It also handles {@link InvalidUnitException} by converting it to a {@link RuntimeException} to simplify error handling.
     * <p>
     * @param mainFrame the main frame of the game's UI, providing context for navigation and interaction within the game setup
     * @throws InvalidUnitException if an invalid unit operation is attempted during the game start process
     */
    private void startGame(MainMenuGui mainFrame) throws InvalidUnitException {
        new Thread(() -> {
           try{
               //Server response
               risk.startGame();
               SwingUtilities.invokeLater(() ->{
//                   ReadyPopUp readyPopUp = new ReadyPopUp();
//
//                   JDialog dialog = new JDialog();
//                   dialog.setContentPane(readyPopUp);
//                   dialog.setSize(500, 100);
//                   dialog.setLocationRelativeTo(null);
//                   dialog.setUndecorated(true);
//                   dialog.setVisible(true);
//
//                   Point panelLocation = GameSetupPanel.this.getLocationOnScreen();
//                   int panelWidht = GameSetupPanel.this.getWidth();
//                   int panelHeight = GameSetupPanel.this.getHeight();
//
//                   dialog.setLocation(panelLocation.x + panelWidht / 2 - dialog.getWidth() / 2, panelLocation.y - dialog.getHeight());

//                   try {
//                       this.gamePanel = mainFrame.getGamePanel();
//                       System.out.println("gamePanel esssss");
//                       GeneratingMap map = new GeneratingMap(risk);
//                       System.out.println("Map got generated");
//                       GameInfoPanel gameInfoPanel = new GameInfoPanel(risk);
//    //                      map.addGameInfoPanel(gameInfoPanel);
//                       gamePanel.setUpPlayerInfoPanel(rightPanel, risk);
//                       gamePanel.add(map, BorderLayout.CENTER);
//                       gamePanel.add(new GameInfoPanel(risk), BorderLayout.EAST);
//                       try {
//                           GameLoop gameLoop = new GameLoop(mainFrame, risk, map, gameInfoPanel);
//                       } catch (InvalidUnitException e) {
//                           System.out.println(e.getMessage()+ "WWWWWWWWWW!!!!!");
//                       }
//                       mainFrame.setGamePanel(gamePanel); // You'll need to add a setGamePanel method in MainMenuGui
//                   } catch (Exception e) {
//                       e.printStackTrace();
//                       JOptionPane.showMessageDialog(this,"Failed to start game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                   }
               });
           }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to communicate with the server :" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           }
        }).start();
    }
}

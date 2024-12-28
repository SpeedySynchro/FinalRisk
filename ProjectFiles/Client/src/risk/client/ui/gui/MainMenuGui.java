package risk.client.ui.gui;


import risk.client.ui.gui.panels.GamePanel;
import risk.client.ui.gui.panels.GameSetupPanel;
import risk.client.ui.gui.panels.MainMenuPanel;
import risk.client.ui.gui.subPanels.BackgroundPanel;
import risk.common.FileUtils;
import risk.common.interfaces.GameEventListener;
import risk.common.interfaces.RiskInterface;
import risk.client.network.ClientMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents the main user interface for the Risk game, extending {@link JFrame} and implementing {@link GameEventListener} to handle game events.
 * This class is responsible for initializing the game's main menu, setting up panels for game setup and gameplay, and managing transitions between different views.
 * <p>
 * The {@code MainMenuGui} class initializes the game client, sets up the main window, and loads the background image. It uses a {@link CardLayout} to switch between different panels:
 * the main menu, game setup, and game panel. It also listens for game events to update the UI accordingly.
 * <p>
 * This class acts as the central hub for the game's graphical user interface, coordinating the display and interaction with the game's various components.
 */
public class MainMenuGui extends JFrame implements GameEventListener {
    private GameEventListener gameEventListener;
    private RiskInterface risk;
    //Risk risk;
    CardLayout cardLayout;
    JPanel mainPanel;
    public static final String MAIN_MENU = "Main Menu";
    public static final String SETUP_PANEL = "Setup Panel";
    public static final String GAME_PANEL = "Game Panel";
    private GamePanel gamePanel;

    /**
     * Initializes the main user interface for the Risk game, setting up the game client, main window, and background image.
     * This constructor also attempts to connect to the game server using the provided host and port, registers the current
     * instance as a game event listener, and initializes the UI components.
     * <p>
     * In case of a failure during initialization or connection to the game server, an error message is printed to the standard
     * error stream and the stack trace of the exception is printed.
     * <p>
     * This method calls {@code initialize()} to set up the UI components after successfully establishing a connection to the game server.
     *
     * @param title the title of the game window
     * @param host the hostname for the game server connection
     * @param port the port number for the game server connection
     */
    public MainMenuGui(String title, String host, int port) {
        super(title);

        //risk = new Risk("Risk");
         try{
             this.risk = new ClientMain(host, port);
             risk.addGameEventListener(this);
         }catch (Exception e){
             System.err.println("Error initialising");
             e.printStackTrace();
         }
        initialize();
    }

    @Override
    public void onPlayerListChanged() {

    }

    @Override
    public void onGameStarted() {
        //showCard(GAME_PANEL);
    }

    @Override
    public void onAllowPlayerActions(boolean allow) {

    }

    /**
     * Initializes the main user interface components for the Risk game. This method sets the default close operation, size of the window,
     * and initializes the card layout for switching between different panels such as the main menu, game setup, and game panel.
     * <p>
     * It attempts to load a background image and wraps the main panel in a {@link BackgroundPanel} to enhance the visual appeal. If the
     * background image cannot be loaded, an error is printed to the standard error stream.
     * <p>
     * This method also creates instances of {@link MainMenuPanel}, {@link GameSetupPanel}, and {@link GamePanel}, adding them to the main
     * panel with respective identifiers. Finally, it makes the main window visible.
     */
    private void initialize() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);

        try {
            BufferedImage background = ImageIO.read(FileUtils.getResource("/assets/background.png"));
            BackgroundPanel backgroundPanel = new BackgroundPanel(background);
            backgroundPanel.setLayout(new BorderLayout()); // Verwenden Sie BorderLayout, um das mainPanel zu umfassen
            backgroundPanel.add(mainPanel, BorderLayout.CENTER); // Fügen Sie das mainPanel zum BackgroundPanel hinzu
            setContentPane(backgroundPanel); // Setzen Sie das BackgroundPanel als Content Pane
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainMenuPanel mainMenuPanel = new MainMenuPanel(this, risk);
        GameSetupPanel gameSetupPanel = new GameSetupPanel(this, risk);
        gamePanel = new GamePanel(this, risk);


        mainPanel.add(mainMenuPanel, MAIN_MENU);
        mainPanel.add(gameSetupPanel, SETUP_PANEL);
        mainPanel.add(gamePanel, GAME_PANEL);


        add(mainPanel);
        setVisible(true);

    }

    /**
     * Switches the visible card in the main panel to the specified card.
     * This method is responsible for changing the currently displayed panel within the main application window.
     * It utilizes the {@link CardLayout} to switch between different panels based on the name of the card passed as an argument.
     * After switching the card, it also triggers a revalidation and repaint of the main panel to ensure the UI is updated correctly.
     *
     * @param cardName the name of the card to be displayed. This name must correspond to one of the cards added to the main panel's card layout.
     */
    public void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
        revalidate();
        repaint();
    }

    /**
     * Sets the current game panel and updates the display to show the specified game panel.
     * This method assigns the provided {@link GamePanel} to the current instance and makes it the active panel displayed in the UI.
     * It also triggers the card layout to switch to the game panel view, ensuring the user interface reflects the current state of the game.
     * Additionally, it prints a debug message indicating whether the main window is currently being shown.
     *
     * @param gamePanel the {@link GamePanel} to be set and displayed in the main application window
     */
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        //this.gamePanel.setUpPlayerInfoPanel(this.gamePanel.rightPanel, risk);
        this.showCard(GAME_PANEL);
    }

    public GamePanel getGamePanel() {
        return this.gamePanel;
    }

    /**
     * The main entry point for the Risk game application.
     * <p>
     * This method initializes the game's main user interface by creating an instance of {@link MainMenuGui} with the specified host and port for the game server connection.
     * It sets the size of the main window and displays it. The initialization is performed on the Event Dispatch Thread to ensure thread safety for Swing components.
     * <p>
     * In case of an initialization error, an error message is printed to the standard error stream.
     *
     * @param args the command line arguments, not used in this application
     */
    public static void main(String[] args) {

        final String host = "localhost";
        final int port = 12344;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try{
                    MainMenuGui mainMenuGui = new MainMenuGui("Risiko", host, port);
                    mainMenuGui.setSize(1280, 720);
                } catch (Exception e){
                    System.err.println("Error creating MainMenuGui" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

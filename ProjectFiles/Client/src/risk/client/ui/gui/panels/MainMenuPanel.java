package risk.client.ui.gui.panels;

import risk.client.ui.gui.MainMenuGui;
import risk.client.ui.gui.subPanels.TitleSubPanel;
import risk.client.ui.gui.subPanels.BackgroundPanel;
import risk.common.FileUtils;
import risk.common.interfaces.RiskInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Represents the main menu panel of the game.
 * <p>
 * This class is responsible for initializing and displaying the main menu of the game. It includes buttons for starting a new game and exiting the game. The background of the menu is set to a specified image, and the layout is designed to center the buttons on the screen. This panel serves as the entry point for users to interact with the game's features.
 * <p>
 * The main menu panel is constructed with a reference to the main frame of the application and the game's risk interface. It initializes the panel's components, including setting up the background image, configuring button fonts and sizes, and adding action listeners to the buttons.
 */
public class MainMenuPanel extends JPanel{
    private RiskInterface risk;
    private JButton startGame = new JButton();
    private JButton exitGame = new JButton();
    private BufferedImage background;

    private Font buttonFont = new Font("Algerian", Font.PLAIN, 36);

    JPanel boxLayout = new JPanel();
    JPanel borderLayout = new JPanel();

    Dimension buttonSize = new Dimension(1080, 80);

    /**
     * Constructs a MainMenuPanel with a reference to the main game frame and the game's risk interface.
     * <p>
     * This constructor initializes the MainMenuPanel by storing the provided {@link MainMenuGui} and {@link RiskInterface} references. It then calls the {@code initialize} method to set up the panel's UI components, including background, buttons, and layout configurations.
     *
     * @param mainFrame The main frame of the application, used to set the content pane and manage UI transitions.
     * @param risk The game's risk interface, providing access to game logic and state management.
     */
    public MainMenuPanel(MainMenuGui mainFrame, RiskInterface risk){
        this.risk = risk;
        initialize(mainFrame);
    }

    /**
     * Initializes the main menu panel with UI components and configurations.
     * <p>
     * This method is responsible for setting up the main menu panel's UI, including loading and scaling the background image, configuring the layout, and initializing buttons with their properties and action listeners. It sets the content pane of the main frame to a new {@link BackgroundPanel} with the scaled background image, and arranges the title and buttons within a {@link BoxLayout} and a {@link BorderLayout}.
     * <p>
     * The method also sets up margins for the title and buttons to ensure proper alignment and spacing within the panel. Action listeners are added to the buttons to handle user interactions, such as starting a new game or exiting the application.
     *
     * @param mainFrame The {@link MainMenuGui} instance that represents the main frame of the application, used for UI transitions and displaying panels.
     */
    private void initialize(MainMenuGui mainFrame){
        try {
            background = ImageIO.read(FileUtils.getResource("/assets/background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Scale the background image to fit the screen
        int width = (int) (background.getWidth() / 1.5);
        int height = (int) (background.getHeight() / 1.5);

        Image scaledBackground = background.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        BufferedImage scaledBackgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledBackgroundImage.createGraphics();
        g.drawImage(scaledBackground, 0, 0, null);
        g.dispose();

        background = scaledBackgroundImage;

        BackgroundPanel backgroundPanel = new BackgroundPanel(background);
        mainFrame.setContentPane(backgroundPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);


        boxLayout.setLayout(new BoxLayout(boxLayout, BoxLayout.Y_AXIS));
        boxLayout.setOpaque(false);
        borderLayout.setLayout(new BorderLayout());
        borderLayout.setOpaque(false);

        Border marginTitle = BorderFactory.createEmptyBorder(70, 250, 0, 0);
        Border marginStartButton = BorderFactory.createEmptyBorder(-40, 250, 0, 0);
        Border marginExitButton = BorderFactory.createEmptyBorder(0, 250, 40, 0);
//        Border marginButton = BorderFactory.createEmptyBorder(0, 250, 0, 0);

        TitleSubPanel titlePanel = new TitleSubPanel();
        titlePanel.setBorder(marginTitle);
        titlePanel.setOpaque(false);

        startGame.setText("Start Game");
        startGame.setFont(buttonFont);
        startGame.setForeground(Color.WHITE);
        startGame.setBorderPainted(true);
        startGame.setFocusPainted(true);
        startGame.setContentAreaFilled(false);
        startGame.setMaximumSize(buttonSize);
        startGame.setPreferredSize(buttonSize);
        startGame.setBorder(marginStartButton);
//        startGame.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameSetupPanel gameSetupPanel = new GameSetupPanel(mainFrame, risk);
                mainFrame.showCard(MainMenuGui.SETUP_PANEL);
            }
        });

        exitGame.setText("Exit Game");
        exitGame.setFont(buttonFont);
        exitGame.setForeground(Color.WHITE);
        exitGame.setBorderPainted(true);
        exitGame.setFocusPainted(true);
        exitGame.setContentAreaFilled(false);
        exitGame.setMaximumSize(buttonSize);
        exitGame.setPreferredSize(buttonSize);
        exitGame.setBorder(marginExitButton);
        exitGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        borderLayout.add(titlePanel, BorderLayout.NORTH);
        boxLayout.add(Box.createVerticalGlue());
        boxLayout.add(Box.createRigidArea(new Dimension(0, 50)));
        boxLayout.add(startGame);
        boxLayout.add(Box.createRigidArea(new Dimension(0, 20)));
        boxLayout.add(exitGame);
        boxLayout.add(Box.createVerticalGlue());
        borderLayout.add(boxLayout, BorderLayout.CENTER);
        this.add(borderLayout);
        this.setOpaque(false);
        this.setVisible(true);

    }

}

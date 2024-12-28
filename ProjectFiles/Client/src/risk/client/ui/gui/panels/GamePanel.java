package risk.client.ui.gui.panels;

import risk.common.entities.*;
import risk.client.ui.gui.MainMenuGui;
import risk.client.ui.gui.subPanels.GameInfoPanel;
import risk.common.interfaces.RiskInterface;

import javax.swing.*;


import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Represents the main game panel in the GUI, integrating various components and functionalities of the game.
 * <p>
 * This class extends {@link JPanel} and is responsible for initializing and displaying the main game interface, including player information, game state, and interactive elements. It serves as a container for other panels and components such as the game information panel, player list, and mission information. The class also handles the layout and visibility of these components, ensuring a cohesive and user-friendly game experience.
 * <p>
 * The constructor takes a {@link MainMenuGui} and a {@link RiskInterface} object to set up the game environment, including the layout and initial state of the game panel. Methods such as {@code setUpPlayerInfoPanel} and {@code createMissionInfo} are used to dynamically update the panel based on the current game state.
 *
 * @see JPanel
 * @see RiskInterface
 * @see MainMenuGui
 */
public class GamePanel extends JPanel {
    private RiskInterface risk;
    private GameInfoPanel gameInfoPanel;
    private DefaultListModel<String> listModel;
    public JPanel rightPanel;
    JList<String> TestList;

    /**
     * Constructs a new GamePanel instance, initializing the game's main interface.
     * <p>
     * This constructor sets up the main game panel by associating it with the main game frame and the current game state. It initializes the layout and components of the game panel, including player information and game state panels. The initialization process is handled by the {@code initialize} method, which is called with the {@link MainMenuGui} and {@link RiskInterface} objects passed as arguments.
     *
     * @param mainFrame the main frame of the game's GUI, used for displaying the game panel
     * @param risk the current state of the game, containing all relevant data and logic
     */
    public GamePanel(MainMenuGui mainFrame, RiskInterface risk) {
        this.risk = risk;
        initialize(mainFrame, risk);

    }

    /**
     * Initializes the main game panel, setting up its layout, size, and background color. It also creates and configures the left and right panels within the game panel.
     * <p>
     * The method sets the layout of the main game panel to {@link BorderLayout}, specifies its size, and sets its background color to a semi-transparent white. It initializes a {@link DefaultListModel} for future use in listing player information.
     * <p>
     * A left panel is created for potential future use, set to opaque false for styling purposes. The right panel is similarly created and configured with a {@link BoxLayout} to hold components vertically. This method also adds these panels to the main game panel in their respective positions (east and west).
     * <p>
     * Finally, the method makes the game panel visible and instructs the {@link MainMenuGui} to display the game panel card.
     *
     * @param mainFrame the main frame of the game's GUI, used for displaying the game panel
     * @param risk the current state of the game, containing all relevant data and logic
     */
    private void initialize(MainMenuGui mainFrame, RiskInterface risk){
        this.setLayout(new BorderLayout());
        this.setSize(1280, 720);
        this.setBackground(new Color(255, 255, 255, 210));
        listModel = new DefaultListModel<>();


        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        //rightPanel.add(listScroller);
        //setUpPlayerInfoPanel(rightPanel,risk);
        //createMissionInfo(rightPanel, risk);


        System.out.println(rightPanel.getComponentCount());
        //this.add(testButton, BorderLayout.SOUTH);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(leftPanel, BorderLayout.WEST);
        this.setVisible(true);
        mainFrame.showCard(MainMenuGui.GAME_PANEL);
    }

    /**
     * Sets up the player information panel within the game UI.
     * <p>
     * This method configures the panel provided as an argument to display player information, including their names and colors. It first clears the panel and sets its properties, such as opacity, layout, and border. The method then checks if the list of players from the {@code Risk} instance is not null and updates a {@link DefaultListModel} with the player information. This model is then used to populate a {@link JList} which is added to the panel inside a {@link JScrollPane} to ensure it can handle a variable number of players.
     * <p>
     * If the list of players is null, the method populates the list model with placeholder data. This is primarily for testing purposes and should be replaced or removed in a production environment.
     *
     * @param panel the {@link JPanel} to be set up with player information
     * @param risk the current game state, containing the list of players and their information
     */
    public void setUpPlayerInfoPanel(JPanel panel, RiskInterface risk){
        this.risk = risk;
        panel.removeAll();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new LineBorder(Color.BLACK));
        panel.setMaximumSize(new Dimension(300, 500));
        panel.add(new JLabel("Player Info:"));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Update the listModel with the player information
        listModel.clear();
        if (risk.getPlayers() != null){
//            System.out.println("Players is not null");
            for (Player player : risk.getPlayers()) {
                listModel.addElement(player.getName() + " - " + player.getColor());
//                System.out.println(player.getName() + " - " + player.getColor());
            }
        } else {
            for (int i=0; i<5; i++) {
                listModel.addElement("Test");
            }
        }

        // Update the JList with the new listModel
        TestList = new JList<>(listModel);
        TestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TestList.setLayoutOrientation(JList.VERTICAL);
        TestList.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(TestList);
        listScroller.setOpaque(false);
        listScroller.setPreferredSize(new Dimension(400, 800));
        panel.add(listScroller);
        System.out.println(risk.getPlayerOnTurn().toString());
    }

    /**
     * Creates and displays the mission information panel for the current player.
     * <p>
     * This method configures a panel to display the mission assigned to the player currently taking their turn. It sets the maximum size of the panel, adds a label to indicate the section's purpose, and ensures the panel's transparency for aesthetic consistency. A {@link DefaultListModel} is used to manage the mission descriptions dynamically.
     * <p>
     * If the current player has a mission card, the mission description is retrieved and displayed. Otherwise, a placeholder text is shown. This functionality supports both actual gameplay scenarios and testing environments.
     * <p>
     * The method encapsulates the creation of a {@link JList} to display the mission information and wraps it in a {@link JScrollPane} to handle overflow, ensuring that the mission text is always accessible to the player, regardless of length.
     *
     * @param panel the {@link JPanel} on which the mission information will be displayed
     * @param risk the current game state, used to retrieve the mission information for the player currently taking their turn
     */
    public void createMissionInfo(JPanel panel, RiskInterface risk){
        panel.setMaximumSize(new Dimension(300, 720));
        panel.add(new JLabel("Player Mission:"));
        panel.setOpaque(false);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        DefaultListModel<String> listModel = new DefaultListModel<>();

        listModel.clear();
        if (risk.getPlayerOnTurn() != null && risk.getPlayerOnTurn().getMissionCard() != null){
//            System.out.println("Mission is not null");
            listModel.addElement(risk.getPlayerOnTurn().getMissionCard().getDescription());
        } else {
            listModel.addElement("Test");
        }
        JList<String> PlayerMission = new JList<>(listModel);
        JScrollPane listScroller = new JScrollPane(PlayerMission);
        listScroller.setOpaque(false);
        listScroller.setPreferredSize(new Dimension(300, 200));
        panel.add(listScroller);
    }

}

package risk.client.ui.gui.subPanels;

import risk.common.entities.UnitCard;
import risk.common.interfaces.RiskInterface;
import risk.common.entities.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

/**
 * Represents the information panel in the game's user interface. This panel displays various game-related
 * information to the player, including active players, current actions, the player's mission, and unit cards.
 * It is designed to dynamically update its content based on the current state of the game.
 *
 * <p>The panel is divided into several sections, each dedicated to a specific type of information. These sections
 * include a list of active players, details about the current action or phase of the game, the mission assigned
 * to the current player, and a list of unit cards held by the player. The panel uses a {@link GridBagLayout} to
 * organize these sections, ensuring a clear and coherent presentation of information.</p>
 *
 * <p>Interaction with the game's state is managed through a {@link RiskInterface} instance, allowing the panel
 * to display up-to-date information as the game progresses. This design facilitates a responsive and informative
 * user experience, enhancing player engagement and understanding of the game's dynamics.</p>
 */
public class GameInfoPanel extends JPanel {
    RiskInterface risk;
    Font font = new Font("JetBrains Mono", Font.PLAIN, 20);

    /**
     * Initializes the {@code GameInfoPanel} with a specific {@link RiskInterface} instance. This constructor sets up
     * the panel's preferred size, layout, and transparency. It also initializes and adds several sub-panels to display
     * information about active players, the current action, the player's mission, and unit cards.
     *
     * <p>The layout is managed using {@link GridBagLayout} to arrange the sub-panels in a grid. Each sub-panel is
     * created by calling its respective method and is added to the main panel with specific {@link GridBagConstraints}
     * to control its size and position.</p>
     *
     * <p>This panel serves as a central component in the game's UI, providing players with all necessary information
     * about the game state dynamically.</p>
     *
     * @param risk the {@link RiskInterface} instance that provides access to the game's state and data. It is used
     *             to populate the sub-panels with current game information.
     */
    public GameInfoPanel(RiskInterface risk) {
        this.risk = risk;

        Dimension preferredSize = new Dimension(200, 650);
        this.setPreferredSize(preferredSize);
        // Set the layout for the main panel to GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        setOpaque(false);

        // Panel for active players
        JPanel activePlayersPanel = createActivePlayersPanel();
        activePlayersPanel.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        add(activePlayersPanel, gbc);

        JPanel actionPanel = createActionPanel();
        actionPanel.setOpaque(false);
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        add(actionPanel, gbc);


        // Panel for current player's mission
        JPanel missionPanel = createMissionPanel();
        missionPanel.setOpaque(false);
        gbc.gridy = 2;
        gbc.weighty = 0.3;
        add(missionPanel, gbc);

        // Panel for current player's unit cards
        JPanel unitCardsPanel = createUnitCardsPanel();
        unitCardsPanel.setOpaque(false);
        gbc.gridy = 3;
        gbc.weighty = 0.3;
        add(unitCardsPanel, gbc);
    }

    /**
     * Creates and returns a JPanel that displays the list of active players in the game. Each player's name and color
     * are shown. This panel is designed with a vertical BoxLayout and a titled border indicating it's for "Active Players".
     * The list of players is dynamically generated from the current game state, making it responsive to changes in the
     * active player roster.
     *
     * <p>The method iterates over the collection of players obtained from the game's {@link RiskInterface} instance,
     * concatenating each player's name and color into a formatted string. This string is then displayed in a custom
     * {@link JTextArea} within the panel, allowing for text wrapping and a consistent visual appearance.</p>
     *
     * @return A {@link JPanel} that visually represents the list of active players, including their names and colors.
     */
    private JPanel createActivePlayersPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Active Players", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLACK));
        String players ="";
        for (Player player : risk.getPlayers()) {
            players += (player.getName() + " - " + player.getColor() + "\n");
        }
        panel.add(createWrappedLabel(players));
        return panel;
    }

    /**
     * Creates and returns a JPanel designed to display the current action in the game. This panel is visually configured
     * with a BoxLayout aligned along the Y-axis, making it suitable for vertically ordered content. It features a titled
     * border labeled "Current Action", which encapsulates the area where action-related information will be displayed.
     *
     * <p>The panel is initially empty and is intended to be dynamically populated with components that represent the
     * current action or phase of the game. This dynamic nature allows for the panel to be updated in real-time as the
     * game progresses, ensuring that players are always informed of the current state of play.</p>
     *
     * @return A {@link JPanel} that serves as a container for displaying the current game action. The panel is configured
     *         with a transparent background and a gray line border, emphasizing its content over visual design.
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Current Action", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLACK));

        return panel;
    }

    /**
     * Creates and returns a JPanel designed to display the current action in the game. This panel is visually configured
     * with a BoxLayout aligned along the Y-axis, making it suitable for vertically ordered content. It features a titled
     * border labeled "Current Action", which encapsulates the area where action-related information will be displayed.
     *
     * <p>The panel is initially empty and is intended to be dynamically populated with components that represent the
     * current action or phase of the game. This dynamic nature allows for the panel to be updated in real-time as the
     * game progresses, ensuring that players are always informed of the current state of play.</p>
     *
     * @return A {@link JPanel} that serves as a container for displaying the current game action. The panel is configured
     *         with a transparent background and a gray line border, emphasizing its content over visual design.
     */
    private JPanel createMissionPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Mission", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLACK));

        // Add mission details (example data)
        if (risk.getPlayerOnTurn().getMissionCard() != null){
            panel.add(createWrappedLabel(risk.getPlayerOnTurn().getMissionCard().getDescription()));
        } else {
            panel.add(createWrappedLabel("No mission card"));
        }

        return panel;
    }

    /**
     * Creates and returns a JPanel designed to display the unit cards of the current player in the game. This panel is visually
     * configured with a BoxLayout aligned along the Y-axis, making it suitable for vertically ordered content. It features a
     * titled border labeled "Unit cards", which encapsulates the area where unit card information will be displayed.
     *
     * <p>The panel dynamically updates to show the unit cards held by the player currently taking their turn. Each card's type
     * is displayed within the panel. If the player has no unit cards, a placeholder text indicating "No unit cards" is shown
     * instead. This ensures that the panel accurately reflects the state of the player's unit cards at any given time.</p>
     *
     * <p>A "Turn in cards" button is also added to the panel, allowing players to interact with their unit cards according to
     * the game's rules. This button is styled to match the game's UI and provides a clear call to action for the player.</p>
     *
     * @return A {@link JPanel} that serves as a container for displaying the current player's unit cards. The panel is configured
     *         with a transparent background and a gray line border, emphasizing its content over visual design.
     */
    private JPanel createUnitCardsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Unit cards", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLACK));

        if (risk.getPlayerOnTurn().getUnitCards() != null){
            for (UnitCard unitCard : risk.getPlayerOnTurn().getUnitCards()) {
                panel.add(createWrappedLabel(unitCard.getType()));
            }
        } else {
            panel.add(createWrappedLabel("No unit cards"));
        }

        JButton turnInButton = new JButton("Turn in cards");
        turnInButton.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        turnInButton.setBackground(Color.DARK_GRAY);
        turnInButton.setForeground(Color.WHITE);
        panel.add(turnInButton);


        return panel;
    }

    /**
     * Creates and returns a {@link JScrollPane} containing a {@link JTextArea} for displaying text with word wrap.
     * This method is designed to create labels that can handle multiline text effectively. The {@link JTextArea} is
     * configured to be non-editable, transparent, and with no border, making it suitable for displaying read-only text.
     * The {@link JScrollPane} is also set to be transparent and without borders, and it disables both vertical and
     * horizontal scroll bars to mimic the behavior of a label.
     *
     * <p>This utility method is particularly useful for displaying text content that requires word wrapping in a GUI,
     * ensuring that all text remains visible and neatly formatted within the confines of a specified area.</p>
     *
     * @param text the String of text to be displayed within the label. This text will be wrapped according to the
     *             dimensions of the {@link JTextArea}.
     * @return A {@link JScrollPane} containing a {@link JTextArea} that displays the provided text. The scroll pane
     *         and text area are configured to be visually consistent with a label that supports multiline text.
     */
    private JScrollPane createWrappedLabel(String text) {
        // Create a JTextArea and set its properties to wrap text
        JTextArea textArea = new JTextArea(text);
        textArea.setOpaque(false);
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBorder(null); // Remove the border
        textArea.setPreferredSize(new Dimension(200, 200));

        // Use a JScrollPane to make sure the JTextArea behaves like a JLabel
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBackground(new Color(0, 0, 0, 0));
        scrollPane.setBorder(null); // Remove the border
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    /**
     * Redraws the {@code GameInfoPanel} by removing all components and then adding them back. This method is used to
     * update the panel with the latest game information. It specifically re-adds the panels for active players, the
     * current player's mission, and the current player's unit cards. After adding these components, it revalidates and
     * repaints the panel to ensure that the updated content is displayed correctly.
     *
     * <p>This method is crucial for maintaining an up-to-date view of the game state for the player. It ensures that
     * changes in the game state, such as a change in the active players list, mission updates, or unit card changes,
     * are reflected in the user interface without the need to recreate the entire panel from scratch.</p>
     */
    public void redrawPanel(){
        this.removeAll();
        this.add(createActivePlayersPanel());
        this.add(createMissionPanel());
        this.add(createUnitCardsPanel());
        this.revalidate();
        this.repaint();
    }

    public static void main(String[] args) {
        // Create the frame

    }
}
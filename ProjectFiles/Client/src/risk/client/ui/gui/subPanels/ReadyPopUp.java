package risk.client.ui.gui.subPanels;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

/**
 * Represents a pop-up panel used in the game's UI to indicate that the game is waiting for other players to be ready.
 * This panel displays a message to the user and provides a button to set the current player's status to "unready".
 *
 * <p>The panel is divided into two main components: a non-editable text field displaying a waiting message, and a button
 * that allows the user to change their readiness state. The text field is styled to be visually consistent with the game's
 * theme, and the button is configured to trigger an action that updates the player's readiness state and closes the pop-up.</p>
 *
 * <p>This class is designed to be used within a dialog or another container that can be displayed over the main game
 * interface. It serves as a temporary interface element that informs players of the game's current state and collects
 * their readiness input.</p>
 */
public class ReadyPopUp extends JPanel {
    private JTextField info;
    private JButton unready;

    /**
     * Constructs a {@code ReadyPopUp} panel which is used to display a waiting message and provide an option for the user
     * to set their status to "unready". This panel is part of the game's user interface and is shown when the game is
     * waiting for other players to ready up.
     *
     * <p>The panel is organized in a {@code GridLayout} with two rows. The first row contains a non-editable {@code JTextField}
     * displaying a waiting message, styled to match the game's theme. The second row contains a {@code JButton} that allows
     * the user to change their readiness state back to "unready".</p>
     *
     * <p>Both the text field and the button are styled with custom fonts, colors, and borders to ensure visual consistency
     * with the game's interface. An action listener is attached to the button to handle the unready action, which closes
     * the pop-up dialog.</p>
     */
    public ReadyPopUp() {
        setLayout(new GridLayout(2, 1));

        info = new JTextField("Waiting for other players to ready up...");
        info.setFont(new Font("Algerian", Font.PLAIN, 18));
        info.setForeground(Color.DARK_GRAY);
        info.setBackground(new Color(255, 255, 255, 180));
        info.setEditable(false);

        unready = new JButton("Set Unready");
        unready.setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 14));
        unready.setForeground(Color.WHITE);
        unready.setBackground(Color.DARK_GRAY);

        Border whiteBorder = BorderFactory.createLineBorder(Color.WHITE, 5);
        info.setBorder(whiteBorder);
        unready.setBorder(whiteBorder);

        unready.addActionListener(e -> {
            Component component = ReadyPopUp.this;
            while (component != null && !(component instanceof JDialog)) {
                component = component.getParent();
            }
            if (component != null) {
                ((JDialog) component).dispose();
            }
        });

        add(info);
        add(unready);
    }
}

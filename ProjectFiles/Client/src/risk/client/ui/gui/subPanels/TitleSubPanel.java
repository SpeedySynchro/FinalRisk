package risk.client.ui.gui.subPanels;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Represents a sub-panel specifically designed for displaying the game title in the Risk game user interface. This panel
 * contains a single {@link JLabel} which is used to display the game's title. The title is styled with a specific font,
 * size, and color to match the game's theme and is centered within the panel.
 *
 * <p>The {@code TitleSubPanel} class extends {@link JPanel} and is intended to be used as part of a larger frame or
 * another container. Its primary role is to enhance the visual appeal of the game's main menu or any other interface
 * that requires the display of the game's title prominently.</p>
 *
 * <p>This class utilizes a custom font and color scheme to ensure the title is visually distinct and aligns with the
 * overall design of the game's user interface. The title's font size and style are chosen to make the title stand out
 * while maintaining readability.</p>
 */
public class TitleSubPanel extends JPanel {
    private JLabel titleText = new JLabel();
    private Font lableFont = new Font("Algerian", Font.PLAIN, 180);

    /**
     * Constructs a {@code TitleSubPanel} which is a specialized panel for displaying the game's title within the Risk game user interface.
     * This constructor initializes the panel with a predefined title, sets the font style, size, and color to match the game's theme,
     * and aligns the title text in the center of the panel.
     *
     * <p>The title text is set to "Risk" and is displayed using a custom font ("Algerian", Font.PLAIN, 180) to ensure it stands out
     * while maintaining readability. The text color is set to white to contrast with the panel's background, enhancing visibility.</p>
     *
     * <p>This panel is designed to be a visually appealing component that can be added to a larger frame or container, serving as a
     * prominent display of the game's title in the main menu or other interfaces within the game.</p>
     */
    public TitleSubPanel(){
        titleText.setText("Risk");
        titleText.setFont(lableFont);
        titleText.setForeground(Color.WHITE);
        titleText.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleText);
    }
}

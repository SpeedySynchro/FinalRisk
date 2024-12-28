package risk.client.ui.gui.subPanels;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Provides a custom JPanel with a background image. This panel is designed to display a single image as its background,
 * scaling the image to fill the entire panel area. The panel is made transparent to ensure that the background image
 * is visible. This class is suitable for creating visually rich UIs where a static background image is required.
 *
 * <p>Usage example:</p>
 * <pre>
 * BufferedImage myImage = ImageIO.read(FileUtils.getResource("path/to/image.jpg"));
 * BackgroundPanel backgroundPanel = new BackgroundPanel(myImage);
 * frame.add(backgroundPanel);
 * </pre>
 *
 * <p>Note: The image scaling is performed in the {@code paintComponent} method, which is overridden to draw the image
 * covering the panel's entire area. It is important to ensure that the image is not null to avoid {@link NullPointerException}.</p>
 */
public class BackgroundPanel extends JPanel {
    private BufferedImage background;

    /**
     * Constructs a {@code BackgroundPanel} with a specified background image. This constructor initializes the panel
     * with the given image and sets the panel's opacity to false to make it transparent. This allows the background
     * image to be visible through the panel.
     *
     * @param background the {@link BufferedImage} to set as the background of the panel. It should not be null to avoid
     *                   {@link NullPointerException} during the painting process.
     */
    public BackgroundPanel(BufferedImage background) {
        this.background = background;
        this.setOpaque(false); // Make the panel transparent so the background can show
    }

    /**
     * Overrides the {@code paintComponent} method to draw a background image scaled to fill the panel. This method is
     * called by the Swing framework to render the component's appearance on the screen. It ensures that the background
     * image covers the entire area of the panel, maintaining the aspect ratio of the image.
     *
     * <p>This implementation first calls {@code super.paintComponent(g)} to allow the panel to draw its components
     * properly. Then, if a background image has been set (i.e., it is not null), the image is drawn scaled to the
     * size of the panel. This is achieved by drawing the image from the top-left corner (0, 0) to the bottom-right
     * corner (width, height) of the panel.</p>
     *
     * @param g the {@link Graphics} object to protect. It provides the context in which the painting is done.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image, scaled to fill the panel
        if (background != null) {
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}

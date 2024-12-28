package risk.client.ui.gui.subPanels;

import risk.common.FileUtils;
import risk.common.interfaces.RiskInterface;
import risk.common.entities.Player;
import risk.common.exceptions.CountryNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Represents the panel for generating and displaying the game map in the Risk game. This panel includes functionality
 * for displaying the game map, continents, and countries, as well as interactive elements such as a checkbox to toggle
 * the visibility of continents and a close button to exit the game. It utilizes a {@link JLayeredPane} to overlay
 * different components such as the map, action text, and interactive buttons.
 *
 * <p>The map is loaded from an image file and scaled to fit the screen. A checkbox allows the user to toggle between
 * viewing the basic map and a version with continents highlighted. The action text is displayed at the top of the map
 * to guide the player's actions. A close button provides a simple way to exit the game.</p>
 *
 * <p>This panel is designed to be added to a {@link JFrame} to create a standalone window for the map display. It
 * serves as a central component of the game's user interface, allowing players to interact with the game world visually.</p>
 */
public class GeneratingMap extends JPanel {
    public static RiskInterface risk;
    private static List<Player> players;
    public final Map<String, String> colorCountryMap;
    public BufferedImage mapImage;
    private BufferedImage iconImage;
    private BufferedImage continentImage;
    public static JLabel actionText;
    public JLabel label;
    public ImageIcon close;

    /**
     * Initializes a new instance of the {@code GeneratingMap} class, which is responsible for generating and displaying the game map in the Risk game.
     * This constructor sets up the game map, continents, and countries, along with interactive elements such as a checkbox for toggling continent visibility
     * and a close button to exit the game. It loads and scales images for the map and continents, sets up a layered pane for displaying these elements,
     * and configures action listeners for interactive components.
     *
     * @param risk the {@link RiskInterface} instance that provides access to game logic and state. This interface allows the panel to interact with the game's core functionalities.
     */
    public GeneratingMap(RiskInterface risk) {
        this.risk = risk;
        this.players = new ArrayList<>();

        colorCountryMap = new HashMap<>();
        colorCountryMap.put("ff0000", "Alaska");
        colorCountryMap.put("ff0030", "Northwest Territory");
        colorCountryMap.put("ff00ea", "Greenland");
        colorCountryMap.put("ff0084", "Alberta");
        colorCountryMap.put("ff00cc", "Ontario");
        colorCountryMap.put("e400ff", "Quebec");
        colorCountryMap.put("c600ff", "Western United States");
        colorCountryMap.put("b400ff", "Eastern United States");
        colorCountryMap.put("9600ff", "Central America");
        colorCountryMap.put("8400ff", "Venezuela");
        colorCountryMap.put("3600ff", "Peru");
        colorCountryMap.put("6c00ff", "Brazil");
        colorCountryMap.put("0000ff", "Argentina");
        colorCountryMap.put("0018ff", "Iceland");
        colorCountryMap.put("002aff", "Great Britain");
        colorCountryMap.put("004eff", "Scandinavia");
        colorCountryMap.put("005aff", "Northern Europe");
        colorCountryMap.put("008aff", "Western Europe");
        colorCountryMap.put("009cff", "Southern Europe");
        colorCountryMap.put("00aeff", "Ukraine");
        colorCountryMap.put("00d2ff", "North Africa");
        colorCountryMap.put("00deff", "Egypt");
        colorCountryMap.put("00fffc", "Middle East");
        colorCountryMap.put("00ffd8", "East Africa");
        colorCountryMap.put("00ffba", "Congo");
        colorCountryMap.put("00ffa2", "South Africa");
        colorCountryMap.put("00ff84", "Madagascar");
        colorCountryMap.put("00ff60", "Ural");
        colorCountryMap.put("00ff3c", "Afghanistan");
        colorCountryMap.put("00ff18", "China");
        colorCountryMap.put("66ff00", "Mongolia");
        colorCountryMap.put("84ff00", "Irkutsk");
        colorCountryMap.put("ccff00", "Yakutsk");
        colorCountryMap.put("deff00", "Kamchatka");
        colorCountryMap.put("fcff00", "Japan");
        colorCountryMap.put("42ff00", "Siam");
        colorCountryMap.put("ffd800", "Indonesia");
        colorCountryMap.put("ffb401", "New Guinea");
        colorCountryMap.put("ff6d01", "Western Australia");
        colorCountryMap.put("ff9601", "Eastern Australia");
        colorCountryMap.put("12ff00", "India");
        colorCountryMap.put("a2ff00", "Siberia");

        try {
            mapImage = ImageIO.read(FileUtils.getResource("/assets/Game2.png"));
            iconImage = ImageIO.read(FileUtils.getResource("/assets/Logo.png"));
            continentImage = ImageIO.read(FileUtils.getResource("/assets/GameContinents2.png"));
            close = new ImageIcon(FileUtils.getResource("/assets/close5.png").readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Scale the map image to fit the screen
        int newWidth = (int) (mapImage.getWidth() / 1.5);
        int newHeight = (int) (mapImage.getHeight() / 1.5);

        Image scaledMapImage = mapImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        BufferedImage scaledMapImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledMapImageNew.createGraphics();
        g.drawImage(scaledMapImage, 0, 0, null);
        g.dispose();

        mapImage = scaledMapImageNew;

        // Scale the continent image to fit the screen
        int newWidthContinent = (int) (continentImage.getWidth() / 1.5);
        int newHeightContinent = (int) (continentImage.getHeight() / 1.5);

        Image scaledContinentImage = continentImage.getScaledInstance(newWidthContinent, newHeightContinent, Image.SCALE_SMOOTH);

        BufferedImage scaledContinentImageNew = new BufferedImage(newWidthContinent, newHeightContinent, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledContinentImageNew.createGraphics();
        g2.drawImage(scaledContinentImage, 0, 0, null);
        g2.dispose();

        continentImage = scaledContinentImageNew;


        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setPreferredSize(new Dimension(mapImage.getWidth(), mapImage.getHeight()));

        actionText = new JLabel("Select a country", SwingConstants.CENTER);
        int textHeight = 140;
        actionText.setBounds(0, 0, mapImage.getWidth() + 65, textHeight);
        actionText.setFont(new Font("JetBrains Mono", Font.PLAIN, 20));
        actionText.setForeground(Color.WHITE);
        actionText.setOpaque(false);

        JCheckBox showContinentsCheckbox = new JCheckBox("Show continents");
        showContinentsCheckbox.setBounds(mapImage.getWidth() - 853, 545, 200, 50); // Adjust the position and size as needed
        showContinentsCheckbox.setFont(new Font("Algerian", Font.PLAIN, 18));
        showContinentsCheckbox.setOpaque(false);
        showContinentsCheckbox.setFocusable(false);
        showContinentsCheckbox.setForeground(Color.WHITE);

        ImageIcon imageIcon = new ImageIcon(mapImage);
        label = new JLabel(imageIcon);
        label.setOpaque(false);
        label.setBounds(0, 0, mapImage.getWidth(), mapImage.getHeight());

        JButton closeButton = new JButton(close);
        closeButton.setContentAreaFilled(false);
        closeButton.setOpaque(false);
        closeButton.setBorder(null);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setBounds(840, 10, 50, 50);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        layeredPane.add(label, Integer.valueOf(1));
        layeredPane.add(actionText, Integer.valueOf(2));
        layeredPane.add(showContinentsCheckbox, Integer.valueOf(2));
        layeredPane.setLayer(closeButton, 3);
        layeredPane.add(closeButton, Integer.valueOf(3));
        layeredPane.setPreferredSize(new Dimension(mapImage.getWidth(), mapImage.getHeight()));
        //this.setIconImage(iconImage);

        add(layeredPane);

        final BufferedImage finalAlternateImage = continentImage;

        showContinentsCheckbox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // Checkbox is selected, show the alternate image
                label.setIcon(new ImageIcon(finalAlternateImage));
            } else {
                // Checkbox is deselected, show the original image
                label.setIcon(new ImageIcon(mapImage));
            }
        });

        this.setLayout(new BorderLayout());
        this.add(layeredPane, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEmptyBorder());
    }

//    public void addGameInfoPanel(JPanel gameInfoPanel) {
//        this.add(gameInfoPanel, BorderLayout.EAST);
//    }

    public static void main(String[] args) throws CountryNotFoundException {
        //risk = new Risk("Risk");
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            GeneratingMap map = new GeneratingMap(risk);
            frame.add(map);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

package risk.client.ui.gui;

import risk.common.entities.*;

import javax.swing.*;

/**
 * Represents the setup GUI for the Risk game. This class extends {@link JFrame} to create a window where players can configure game settings before starting.
 * <p>
 * The GUI includes components for setting up the game, such as player name input, color selection, and starting the game. It initializes these components
 * and arranges them within the frame. Additional functionality may include connecting to a game server, selecting maps, and configuring game rules.
 */
public class SetupGameGUI extends JFrame{

    /**
     * Constructs a new setup GUI for the Risk game with the specified title.
     * <p>
     * This constructor initializes the GUI window with the given title. It sets up the frame's properties and layout by calling the {@code initialize} method,
     * which is responsible for creating and arranging the GUI components necessary for game setup, such as player name input, color selection, and game start options.
     *
     * @param title the title of the game setup window
     */
    public SetupGameGUI(String title) {
        super(title);
        initialize();
    }

    private void initialize(){

    }
}

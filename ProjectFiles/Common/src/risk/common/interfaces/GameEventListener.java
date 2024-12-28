package risk.common.interfaces;

import risk.common.entities.Player;

import java.util.List;

/**
 * Represents a listener for game-related events within the Risk game framework.
 * This interface defines methods that are called in response to various game events such as changes in the player list or the start of the game.
 * Implementing classes should provide specific behavior for handling these events, which may include updating the game state, notifying players, or refreshing the UI.
 *
 * @see risk.common.entities.Player
 */
public interface GameEventListener{

     void onPlayerListChanged();
     void onGameStarted();
     void onAllowPlayerActions(boolean allow);
}

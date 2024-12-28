package risk.common.entities.missions;

import risk.common.entities.Player;

/**
 * Represents the base class for all mission cards in the game.
 * <p>
 * This class provides the foundational attributes and methods that are common across different types of missions in the game. Each mission card has a description that outlines its objectives, and a reference to the player to whom the mission is assigned. The class also includes methods for checking if the mission is completed, retrieving the mission's description, assigning a player to the mission, and representing the mission as a string.
 * <p>
 * Subclasses of {@code MissionCard} should implement the {@code isDone} method to provide specific logic for determining if the mission's objectives have been met.
 */
public class MissionCard {
    String description;
    Player player;

    /**
     * Constructs a new {@code MissionCard} with the specified mission description.
     * <p>
     * This constructor initializes a new instance of {@code MissionCard} by setting the mission's description. The player is initially set to {@code null}, indicating that the mission has not yet been assigned to any player.
     *
     * @param description The description of the mission, outlining its objectives.
     */
    public MissionCard(String description){

        this.description = description;
        this.player = null;
    }

    /**
     * Determines if the mission's objectives have been met.
     * <p>
     * This method should be overridden in subclasses to provide specific logic for determining if the mission assigned to a player is completed based on the mission's unique criteria. The default implementation always returns {@code false}, indicating that, without specific criteria being checked, the mission is not completed.
     * <p>
     * Subclasses should implement this method to check conditions specific to the mission type, such as territory control, elimination of certain players, or distribution of units across the game board.
     *
     * @return {@code true} if the mission objectives are met; {@code false} otherwise.
     */
    public boolean isDone(){return false;}

    /**
     * Retrieves the mission's description.
     * <p>
     * This method returns the description of the mission. The description provides an overview of the mission's objectives and is used to inform the player about the specific goals to be achieved.
     *
     * @return The description of the mission.
     */
    public String getDescription(){
        return description;
    }

    /**
     * Assigns a player to this mission card.
     * <p>
     * This method sets the specified player as the owner of this mission card. It is crucial for tracking the progress of the mission against the actions of the assigned player. Once a player is set, the mission card can be used to check if the mission's objectives have been met by this player.
     *
     * @param player The player to be assigned to this mission card.
     */
    public void setPlayer(Player player){
        this.player = player;
    }

    /**
     * Returns a string representation of the mission card.
     * <p>
     * This method overrides the {@code toString} method to provide a string representation of the mission card, primarily focusing on its description. It is useful for debugging purposes or when a simple textual representation of the mission card's current state is needed.
     *
     * @return A string containing the description of the mission card.
     */
    @Override
    public String toString(){
        return (description);
    }
}

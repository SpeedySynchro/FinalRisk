package risk.common.entities.missions;

import risk.common.entities.Country;

import java.util.List;

/**
 * Represents a mission where the player must eliminate all countries controlled by a player of a specific color.
 * <p>
 * This class extends {@code MissionCard} to define a mission with the objective of eliminating all countries controlled by a player whose armies are identified by a specific color. It challenges the player to strategically target and conquer territories held by the target player color, effectively removing their presence from the game board.
 * <p>
 * The mission is considered completed if no country is controlled by the player of the target color, reflecting a significant achievement in eliminating a rival player from the game.
 */
public class eliminatePlayerColorMission extends MissionCard {

    final String targetPlayerColor;
    List<Country> countries;

    /**
     * Constructs a new {@code eliminatePlayerColorMission} with the specified countries, description, and target player color.
     * <p>
     * This constructor initializes a new instance of {@code eliminatePlayerColorMission} by setting the list of countries on the game board, the mission's description, and the color of the player to be eliminated. The mission challenges the player to strategically target and conquer territories held by the target player color, effectively removing their presence from the game board.
     * <p>
     * The mission is considered completed if no country is controlled by the player of the target color, reflecting a significant achievement in eliminating a rival player from the game.
     *
     * @param countries The list of countries on the game board.
     * @param description The description of the mission, outlining the elimination objective.
     * @param targetPlayerColor The color of the player to be eliminated from the game.
     */
    public eliminatePlayerColorMission(List<Country> countries, String description, String targetPlayerColor){
        super(description);
        this.countries = countries;
        this.targetPlayerColor = targetPlayerColor;
    }

    /**
     * Determines if the mission to eliminate all countries controlled by a player of a specific color has been completed.
     * <p>
     * This method overrides the {@code isDone} method from the {@code MissionCard} class. It first checks if the target player color matches the player's color and if the player controls at least 24 countries, indicating a self-elimination scenario. If not, it iterates through all countries on the game board to check if any country is still controlled by the target player color. The mission is considered completed if no country is controlled by the target player color.
     * <p>
     * The method returns {@code true} if the mission is completed, either by self-elimination in the case of matching colors or if no country is controlled by the target player color. Otherwise, it returns {@code false}.
     *
     * @return {@code true} if the mission objectives are met; {@code false} otherwise.
     */
    @Override
    public boolean isDone(){
        if (targetPlayerColor.equals(player.getColor())){
            return player.getCountries().size() >= 24;
        }
        for (Country country : countries){
            if (country.getPlayer().getColor().equals(targetPlayerColor)){
                return false;
            }
        }
        return true;
    }
}
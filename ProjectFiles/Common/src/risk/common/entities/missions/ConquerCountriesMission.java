package risk.common.entities.missions;

/**
 * Represents a mission where the player must conquer a specific number of countries.
 * <p>
 * This class extends {@code MissionCard} to define a mission with the objective of conquering a predetermined number of countries. It challenges the player to expand their territory significantly across the game board to achieve victory.
 * <p>
 * The mission is considered completed if the player controls at least the required number of countries, reflecting a significant achievement in territorial expansion within the game.
 */
public class ConquerCountriesMission extends MissionCard {

    /**
     * Constructs a new {@code ConquerCountriesMission} with the specified mission description.
     * <p>
     * This constructor initializes a new instance of {@code ConquerCountriesMission} by setting the description of the mission. The description outlines the objectives of the mission, specifically the number of countries that must be conquered by the player to achieve victory.
     *
     * @param description The description of the mission, detailing the conquest objectives.
     */
    public ConquerCountriesMission(String description){
        super(description);
    }

    /**
     * Determines if the mission objectives have been met by the player.
     * <p>
     * This method overrides the {@code isDone} method from the {@code MissionCard} class. It checks if the player has conquered at least the required number of countries specified for this mission. Achieving this number signifies the player's successful territorial expansion across the game board.
     *
     * @return {@code true} if the player controls at least the required number of countries; {@code false} otherwise.
     */
    @Override
    public boolean isDone(){
        if (player.getCountries().size() >= 24){
            return true;
        }
        return false;
    }
}
package risk.common.entities.missions;

import risk.common.entities.Continent;

/**
 * Represents a mission where the player must conquer two specific continents.
 * <p>
 * This class extends {@code MissionCard} to define a mission where the objective is to conquer two predetermined continents. It holds references to these continents and overrides the {@code isDone} method to check if the player has successfully conquered both target continents.
 */
public class ConquerContinentMission extends MissionCard {
    private Continent targetContinent1;
    private Continent targetContinent2;

    /**
     * Constructs a new {@code ConquerContinentMission} with the specified description and target continents.
     * <p>
     * This constructor initializes a new instance of {@code ConquerContinentMission} by setting the description of the mission and references to the two target continents. These continents are the objectives that the player must conquer to complete the mission.
     *
     * @param description The description of the mission.
     * @param target1 The first target continent that must be conquered to complete the mission.
     * @param target2 The second target continent that must be conquered to complete the mission.
     */
    public ConquerContinentMission(String description, Continent target1, Continent target2){
        super(description);
        this.targetContinent1 = target1;
        this.targetContinent2 = target2;
    }

    /**
     * Checks if the mission objectives have been met by the player.
     * <p>
     * This method overrides the {@code isDone} method from the {@code MissionCard} class. It determines whether the player has successfully conquered the two target continents specified in this mission. The mission is considered completed if the player controls both target continents.
     *
     * @return {@code true} if the player controls both target continents; {@code false} otherwise.
     */
    @Override
    public boolean isDone(){
        if (player.getContinents().contains(targetContinent1) && player.getContinents().contains(targetContinent2)){
            return true;
        }
        return false;
    }

}
package risk.common.entities.missions;

import risk.common.entities.Continent;

/**
 * Represents a mission where the player must conquer two specific continents and control at least one additional continent.
 * <p>
 * This class extends {@code MissionCard} to define a mission with objectives that include conquering two predetermined continents and controlling at least one more continent beyond those. It is a more challenging variation of the {@code ConquerContinentMission}, requiring broader territorial control to achieve victory.
 * <p>
 * The mission is considered completed if the player controls both target continents and at least one additional continent, reflecting a significant achievement in territorial expansion within the game.
 */
public class ConquerContinentPlusOne extends MissionCard {
    private Continent targetContinent1;
    private Continent targetContinent2;

    /**
     * Constructs a new {@code ConquerContinentPlusOne} mission with specified description and target continents.
     * <p>
     * This constructor initializes a new instance of {@code ConquerContinentPlusOne} by setting the mission's description and the references to the two target continents. This mission variant requires the player to not only conquer the specified continents but also to control at least one additional continent, making it a more challenging objective compared to the basic {@code ConquerContinentMission}.
     * <p>
     * The additional complexity introduced by this mission encourages strategic planning and broader territorial control to achieve victory.
     *
     * @param description The description of the mission, outlining the objectives and the strategic importance of the target continents.
     * @param target1 The first target continent that must be conquered to fulfill part of the mission's objectives.
     * @param target2 The second target continent that must be conquered to fulfill part of the mission's objectives.
     */
    public ConquerContinentPlusOne(String description, Continent target1, Continent target2){
        super(description);
        this.targetContinent1 = target1;
        this.targetContinent2 = target2;
    }

    /**
     * Determines if the mission objectives have been met by the player.
     * <p>
     * This method checks if the player controls at least three continents, including the two specific target continents required for this mission. It overrides the {@code isDone} method from the {@code MissionCard} class to accommodate the additional requirement of controlling at least one continent beyond the two target continents. This ensures the mission's completion criteria are more challenging, reflecting a significant achievement in territorial expansion within the game.
     *
     * @return {@code true} if the player controls both target continents and at least one additional continent; {@code false} otherwise.
     */
    @Override
    public boolean isDone(){
        if (player.getContinents().size() >= 3) {
            if (player.getContinents().contains(targetContinent1) && player.getContinents().contains(targetContinent2)){
                return true;
            }
            return false;
        }
        return false;
    }

}
package risk.common.entities.missions;

import risk.common.entities.Country;

/**
 * Represents a mission where the player must conquer a specific number of countries and ensure each country has a minimum number of units.
 * <p>
 * This class extends {@code MissionCard} to define a mission with dual objectives: conquering a predetermined number of countries and maintaining a minimum unit threshold in each. It challenges the player not only to expand their territory across the game board but also to strategically distribute their forces to meet the mission's requirements.
 * <p>
 * The mission is considered completed if the player controls at least the required number of countries and each country has at least the specified minimum number of units, reflecting a significant achievement in both territorial expansion and military distribution within the game.
 */
public class ConquerCountriesMissionPlusUnits extends MissionCard {

    /**
     * Constructs a new {@code ConquerCountriesMissionPlusUnits} mission with the specified description.
     * <p>
     * This constructor initializes a new instance of {@code ConquerCountriesMissionPlusUnits} by setting the mission's description. The description outlines the dual objectives of the mission: conquering a specific number of countries and ensuring each country has a minimum number of units. This adds an additional layer of strategy to the mission, as players must not only focus on territorial expansion but also on the distribution of their forces.
     *
     * @param description The description of the mission, detailing the objectives including the number of countries to conquer and the minimum number of units each country must have.
     */
    public ConquerCountriesMissionPlusUnits(String description){
        super(description);
    }

    /**
     * Checks if the mission objectives regarding country conquest and unit distribution have been met.
     * <p>
     * This method overrides the {@code isDone} method from the {@code MissionCard} class. It first checks if the player controls at least the required number of countries. If this condition is met, it then verifies that each controlled country has at least the minimum required number of units. This ensures that the player has not only expanded their territory but also strategically distributed their forces across the game board.
     * <p>
     * The method returns {@code true} if both conditions are satisfied, indicating successful completion of the mission objectives. Otherwise, it returns {@code false}.
     *
     * @return {@code true} if the player controls the required number of countries with the minimum number of units in each; {@code false} otherwise.
     */
    @Override
    public boolean isDone(){
        if (player.getCountries().size() >= 18) {
            for (Country country : player.getCountries()){
                if (country.getUnits() < 2){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
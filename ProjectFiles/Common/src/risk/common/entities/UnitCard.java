package risk.common.entities;

import java.io.Serializable;

/**
 * Represents a unit card in the game.
 * <p>
 * This class models a unit card, which is a key component in the game's mechanics. Each unit card has a unique name that represents a type of unit in the game. These cards can be used by players to perform various actions or strategies within the game context.
 */
public class UnitCard {

    String unitName;

    /**
     * Constructs a new {@code UnitCard} with the specified unit name.
     * <p>
     * This constructor initializes a new instance of {@code UnitCard} by setting the {@code unitName} field to the provided name. The {@code unitName} represents the type of unit this card corresponds to in the game, which is crucial for game mechanics and player strategies.
     *
     * @param unitName The name of the unit represented by this card.
     */
    public UnitCard(String unitName){
        this.unitName = unitName;
    }

    /**
     * Retrieves the type of the unit represented by this card.
     * <p>
     * This method returns the name of the unit associated with this {@code UnitCard}, indicating the type of unit it represents. The unit name is crucial for identifying the card's role and functionality within the game's mechanics.
     *
     * @return The name of the unit represented by this card.
     */
    public String getType() {
        return unitName;
    }
}

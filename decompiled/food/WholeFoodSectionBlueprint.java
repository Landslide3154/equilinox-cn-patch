/*
 * Decompiled with CFR 0.152.
 */
package food;

import death.DeathAiBlueprint;
import food.FoodComponent;
import food.FoodSection;
import food.FoodSectionBlueprint;
import food.FoodSectionType;
import food.WholeFoodSection;

public class WholeFoodSectionBlueprint
extends FoodSectionBlueprint {
    protected final DeathAiBlueprint deathAnimation;

    protected WholeFoodSectionBlueprint(String name, int foodPoints, DeathAiBlueprint deathAnimation) {
        super(FoodSectionType.WHOLE, name, foodPoints);
        this.deathAnimation = deathAnimation;
    }

    @Override
    public FoodSection createInstance(FoodComponent foodComp) {
        return new WholeFoodSection(this);
    }
}


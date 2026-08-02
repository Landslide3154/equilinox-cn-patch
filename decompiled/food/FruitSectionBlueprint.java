/*
 * Decompiled with CFR 0.152.
 */
package food;

import food.FoodComponent;
import food.FoodSection;
import food.FoodSectionBlueprint;
import food.FoodSectionType;
import food.FruitSection;

public class FruitSectionBlueprint
extends FoodSectionBlueprint {
    protected FruitSectionBlueprint(String name, int foodPoints) {
        super(FoodSectionType.FRUIT, name, foodPoints);
    }

    @Override
    public FoodSection createInstance(FoodComponent foodComp) {
        return new FruitSection(this);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package food;

import food.FoodCompBlueprint;
import food.FoodComponent;
import food.FoodSection;
import food.FoodSectionType;

public abstract class FoodSectionBlueprint {
    protected final int foodPoints;
    protected final FoodSectionType type;
    protected final String name;

    protected FoodSectionBlueprint(FoodSectionType type, String name, int foodPoints) {
        this.foodPoints = foodPoints;
        this.name = name;
        this.type = type;
    }

    protected abstract FoodSection createInstance(FoodComponent var1);

    protected void addTraits(FoodCompBlueprint foodCompBlueprint) {
    }
}


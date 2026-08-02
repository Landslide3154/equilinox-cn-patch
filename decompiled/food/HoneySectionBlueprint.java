/*
 * Decompiled with CFR 0.152.
 */
package food;

import food.FoodComponent;
import food.FoodSection;
import food.FoodSectionBlueprint;
import food.FoodSectionType;
import food.HoneySection;

public class HoneySectionBlueprint
extends FoodSectionBlueprint {
    protected HoneySectionBlueprint(String name, int foodPoints) {
        super(FoodSectionType.HONEY, name, foodPoints);
    }

    @Override
    protected FoodSection createInstance(FoodComponent foodComp) {
        return new HoneySection(this);
    }
}


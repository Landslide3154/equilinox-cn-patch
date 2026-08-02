/*
 * Decompiled with CFR 0.152.
 */
package food;

import food.FoodComponent;
import food.FoodSection;
import food.FoodSectionBlueprint;
import food.FoodSectionType;
import food.SampleSection;

public class SampleSectionBlueprint
extends FoodSectionBlueprint {
    protected SampleSectionBlueprint(String name, int foodPoints) {
        super(FoodSectionType.SAMPLE, name, foodPoints);
    }

    @Override
    protected FoodSection createInstance(FoodComponent foodComp) {
        return new SampleSection(this);
    }
}


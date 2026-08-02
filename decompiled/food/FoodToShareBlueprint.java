/*
 * Decompiled with CFR 0.152.
 */
package food;

import food.FoodComponent;
import food.FoodSection;
import food.FoodSectionBlueprint;
import food.FoodSectionType;
import food.FoodToShare;

public class FoodToShareBlueprint
extends FoodSectionBlueprint {
    protected final int portionCount;

    protected FoodToShareBlueprint(String name, int portionFoodPoints, int portions) {
        super(FoodSectionType.TO_SHARE, name, portionFoodPoints);
        this.portionCount = portions;
    }

    @Override
    protected FoodSection createInstance(FoodComponent foodComp) {
        return new FoodToShare(this);
    }
}


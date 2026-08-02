/*
 * Decompiled with CFR 0.152.
 */
package eating;

import classification.Classification;
import eating.EatingAnimBlueprint;
import food.FoodSectionType;

public class DietOption {
    private final Classification classification;
    private final FoodSectionType foodType;
    private final EatingAnimBlueprint eatingAnimation;
    private String overwriteName = null;

    public DietOption(Classification classification, FoodSectionType foodType, EatingAnimBlueprint eatingAnimation) {
        this.classification = classification;
        this.foodType = foodType;
        this.eatingAnimation = eatingAnimation;
    }

    public void setOverwriteName(String overwriteName) {
        this.overwriteName = overwriteName;
    }

    public String getName() {
        return this.overwriteName == null ? this.classification.getName() : this.overwriteName;
    }

    public Classification getClassification() {
        return this.classification;
    }

    public FoodSectionType getFoodType() {
        return this.foodType;
    }

    public EatingAnimBlueprint getEatingAnimation() {
        return this.eatingAnimation;
    }
}


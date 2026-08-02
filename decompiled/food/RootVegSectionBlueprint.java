/*
 * Decompiled with CFR 0.152.
 */
package food;

import breedingTraits.FloatTraitBlueprint;
import death.DeathAiBlueprint;
import death.SpawnDeathBlueprint;
import food.FoodCompBlueprint;
import food.FoodComponent;
import food.FoodSection;
import food.FoodSectionBlueprint;
import food.FoodSectionType;
import food.RootVegSection;
import languages.GameText;

public class RootVegSectionBlueprint
extends FoodSectionBlueprint {
    private static final String YIELD = GameText.getText(993);
    private static final String UNIT = GameText.getText(994);
    private static final float AVERAGE_POTATOES = 7.0f;
    protected final DeathAiBlueprint deathAnimation;
    private int traitIndex;

    protected RootVegSectionBlueprint(String name, int foodPoints, DeathAiBlueprint deathAnimation) {
        super(FoodSectionType.ROOT_VEG, name, foodPoints);
        this.deathAnimation = deathAnimation;
    }

    @Override
    protected FoodSection createInstance(FoodComponent foodComp) {
        return new RootVegSection(this, foodComp);
    }

    public int getTraitIndex() {
        return this.traitIndex;
    }

    @Override
    protected void addTraits(FoodCompBlueprint foodCompBlueprint) {
        if (this.deathAnimation instanceof SpawnDeathBlueprint) {
            FloatTraitBlueprint edibility = new FloatTraitBlueprint(YIELD, 7.0f, 2.0f, 7.0f){

                @Override
                public String formatTrait(float value) {
                    int number = Math.round(value);
                    return String.valueOf(number) + " " + UNIT;
                }
            };
            this.traitIndex = foodCompBlueprint.addTrait(edibility);
        }
    }
}


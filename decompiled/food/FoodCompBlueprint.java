/*
 * Decompiled with CFR 0.152.
 */
package food;

import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import effects.Effect;
import food.FoodComponent;
import food.FoodSection;
import food.FoodSectionBlueprint;
import food.FoodSectionType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FoodCompBlueprint
extends ComponentBlueprint {
    private static final String EDIBLE = GameText.getText(899);
    private static final String FOOD_POINTS = GameText.getText(540);
    protected FoodSectionBlueprint[] sections;
    protected final Effect effect;
    private final float eatingRadius = 0.2f;

    protected FoodCompBlueprint(FoodSectionBlueprint[] sections, Effect effect) {
        super(ComponentType.FOOD);
        this.effect = effect;
        this.sections = sections;
        FloatTraitBlueprint edibility = new FloatTraitBlueprint(EDIBLE, 1.0f, 2.0f, 7.0f){

            @Override
            public String formatTrait(float value) {
                int number = Math.round(value * 100.0f);
                return String.valueOf(number) + "%";
            }
        };
        super.addTrait(edibility);
        FoodSectionBlueprint[] foodSectionBlueprintArray = sections;
        int n = sections.length;
        int n2 = 0;
        while (n2 < n) {
            FoodSectionBlueprint section = foodSectionBlueprintArray[n2];
            section.addTraits(this);
            ++n2;
        }
    }

    @Override
    public void delete() {
    }

    public float getEatingRadius() {
        return 0.2f;
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        if (this.effect != null) {
            this.effect.getInfo(info.get((Object)SpeciesInfoType.ABILITIES));
        }
        if (this.sections.length == 1) {
            info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine(FOOD_POINTS, Integer.toString(this.sections[0].foodPoints)));
        } else {
            FoodSectionBlueprint[] foodSectionBlueprintArray = this.sections;
            int n = this.sections.length;
            int n2 = 0;
            while (n2 < n) {
                FoodSectionBlueprint section = foodSectionBlueprintArray[n2];
                info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine(String.valueOf(FOOD_POINTS) + " - " + section.name, Integer.toString(section.foodPoints)));
                ++n2;
            }
        }
    }

    @Override
    public Component createInstance() {
        FoodComponent foodComp = new FoodComponent(this);
        HashMap<FoodSectionType, FoodSection> sectionInstances = new HashMap<FoodSectionType, FoodSection>();
        int i = 0;
        while (i < this.sections.length) {
            sectionInstances.put(this.sections[i].type, this.sections[i].createInstance(foodComp));
            ++i;
        }
        foodComp.setSections(sectionInstances);
        return foodComp;
    }
}


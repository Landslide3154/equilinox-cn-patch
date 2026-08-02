/*
 * Decompiled with CFR 0.152.
 */
package food;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import death.DeathAICreator;
import death.DeathAiBlueprint;
import effects.Effect;
import effects.EffectLoader;
import food.FoodCompBlueprint;
import food.FoodSectionBlueprint;
import food.FoodSectionType;
import food.FoodToShareBlueprint;
import food.FruitSectionBlueprint;
import food.HoneySectionBlueprint;
import food.RootVegSectionBlueprint;
import food.SampleSectionBlueprint;
import food.WholeFoodSectionBlueprint;
import languages.GameText;
import utils.CSVReader;

public class FoodCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int count = reader.getNextInt();
        FoodSectionBlueprint[] sections = this.loadSections(count, reader);
        Effect effect = null;
        if (!reader.isEndOfLine()) {
            effect = EffectLoader.loadEffect(reader);
        }
        return new FoodCompBlueprint(sections, effect);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }

    private FoodSectionBlueprint[] loadSections(int count, CSVReader reader) {
        FoodSectionBlueprint[] sections = new FoodSectionBlueprint[count];
        int i = 0;
        while (i < count) {
            DeathAiBlueprint deathAi;
            String name = GameText.getText(reader.getNextInt());
            int foodPoints = reader.getNextInt();
            FoodSectionType type = FoodSectionType.valueOf(reader.getNextString());
            if (type == FoodSectionType.WHOLE) {
                deathAi = DeathAICreator.loadDeathAi(reader);
                sections[i] = new WholeFoodSectionBlueprint(name, foodPoints, deathAi);
            } else if (type == FoodSectionType.FRUIT) {
                sections[i] = new FruitSectionBlueprint(name, foodPoints);
            } else if (type == FoodSectionType.SAMPLE) {
                sections[i] = new SampleSectionBlueprint(name, foodPoints);
            } else if (type == FoodSectionType.TO_SHARE) {
                int portions = reader.getNextLabelInt();
                sections[i] = new FoodToShareBlueprint(name, foodPoints, portions);
            } else if (type == FoodSectionType.HONEY) {
                sections[i] = new HoneySectionBlueprint(name, foodPoints);
            } else if (type == FoodSectionType.ROOT_VEG) {
                deathAi = DeathAICreator.loadDeathAi(reader);
                sections[i] = new RootVegSectionBlueprint(name, foodPoints, deathAi);
            } else {
                sections[i] = new WholeFoodSectionBlueprint(name, foodPoints, null);
            }
            ++i;
        }
        return sections;
    }
}


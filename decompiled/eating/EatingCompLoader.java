/*
 * Decompiled with CFR 0.152.
 */
package eating;

import blueprints.Blueprint;
import breedingTrees.ReqInfo;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import eating.DietOption;
import eating.EatingAnimBlueprint;
import eating.EatingCompBlueprint;
import eating.EatingComponent;
import eating.TempEatAnimBlueprint;
import food.FoodSectionType;
import instances.Entity;
import java.util.List;
import languages.GameText;
import utils.CSVReader;

public class EatingCompLoader
implements ComponentLoader {
    private String DIET_REQ = GameText.getText(559);

    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int maxHungerPoints = reader.getNextLabelInt();
        float hungerPerHour = reader.getNextLabelFloat();
        float eatingRadius = reader.getNextLabelFloat();
        EatingAnimBlueprint[] anims = this.loadPossibleAnimations(reader);
        DietOption[] diet = this.loadDietOptions(reader, anims);
        boolean runs = false;
        boolean eggStage = false;
        int aiId = 0;
        if (!reader.isEndOfLine()) {
            runs = reader.getNextLabelBool();
            aiId = reader.getNextLabelInt();
            if (!reader.isEndOfLine()) {
                eggStage = reader.getNextLabelBool();
            }
        }
        return new EatingCompBlueprint(maxHungerPoints, hungerPerHour, eatingRadius, diet, blueprint, runs, aiId, eggStage);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        final Classification classification = Classifier.getClassification(reader.getNextString());
        return new Requirement(){

            @Override
            public boolean check(Entity entity) {
                EatingComponent eatComp = (EatingComponent)entity.getComponent(ComponentType.EATING);
                return eatComp.getCurrentDiet().isDietPredominantly(classification);
            }

            @Override
            public void getGuiInfo(List<ReqInfo> components) {
                components.add(new ReqInfo(EatingCompLoader.this.DIET_REQ, classification.getName()));
            }

            @Override
            public boolean isSecret() {
                return false;
            }
        };
    }

    private EatingAnimBlueprint[] loadPossibleAnimations(CSVReader reader) {
        int count = reader.getNextLabelInt();
        EatingAnimBlueprint[] anims = new EatingAnimBlueprint[count];
        int i = 0;
        while (i < count) {
            int id = reader.getNextInt();
            anims[i] = new TempEatAnimBlueprint(id);
            ++i;
        }
        return anims;
    }

    private DietOption[] loadDietOptions(CSVReader reader, EatingAnimBlueprint[] anims) {
        int count = reader.getNextLabelInt();
        DietOption[] diet = new DietOption[count];
        int i = 0;
        while (i < count) {
            Classification classification = Classifier.getClassification(reader.getNextString());
            FoodSectionType type = FoodSectionType.valueOf(reader.getNextString());
            EatingAnimBlueprint animation = anims[reader.getNextInt()];
            diet[i] = new DietOption(classification, type, animation);
            if (type.overwritesName()) {
                diet[i].setOverwriteName(type.getOverwriteName());
            }
            ++i;
        }
        return diet;
    }
}


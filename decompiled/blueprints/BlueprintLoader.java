/*
 * Decompiled with CFR 0.152.
 */
package blueprints;

import blueprints.AdditionSubBlueprint;
import blueprints.Blueprint;
import blueprints.ModelLoader;
import blueprints.SubBlueprint;
import classification.Classifier;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import picking.AABB;
import utils.CSVReader;
import utils.MyFile;

public class BlueprintLoader {
    public static void loadBlueprint(Blueprint blueprint, MyFile blueprintFile) throws Exception {
        CSVReader reader = new CSVReader(blueprintFile);
        reader.nextLine();
        float size = reader.getNextFloat();
        String extraName = BlueprintLoader.loadOverrideName(reader);
        Integer overrideIndex = BlueprintLoader.loadOverrideModelIndex(reader);
        boolean randomize = BlueprintLoader.loadPotentialBoolean(reader);
        boolean alwaysVis = BlueprintLoader.loadPotentialBoolean(reader);
        BlueprintLoader.loadIconInfo(reader, blueprint);
        blueprint.alwaysVisible = alwaysVis;
        BlueprintLoader.loadBlueprintInfo(blueprint, reader);
        reader.nextLine();
        int subBlueprintCount = reader.getNextInt();
        ArrayList<SubBlueprint> subBlueprints = new ArrayList<SubBlueprint>();
        int i = 0;
        while (i < subBlueprintCount) {
            BlueprintLoader.loadSubBlueprint(reader, subBlueprints, size);
            ++i;
        }
        BlueprintLoader.calculateGrowthFactors(subBlueprints);
        blueprint.setSubBlueprints(subBlueprints);
        blueprint.setOverrideName(extraName);
        blueprint.setRandomizeModelStages(randomize);
        blueprint.setOverrideMainSubBlueprintIndex(overrideIndex);
        BlueprintLoader.loadComponents(blueprint, reader);
        blueprint.indicateLoaded();
        reader.close();
    }

    private static void loadSubBlueprint(CSVReader reader, List<SubBlueprint> subBlueprints, float size) {
        reader.nextLine();
        Vector3f mins = reader.getNextVector();
        mins.scale(size);
        Vector3f maxs = reader.getNextVector();
        maxs.scale(size);
        float increaseFactor = reader.getNextFloat();
        boolean additive = false;
        AABB[] extraAabbs = null;
        if (!reader.isEndOfLine()) {
            additive = reader.getNextBool();
            extraAabbs = BlueprintLoader.loadExtraAabbs(reader, size);
        }
        float[] modelData = ModelLoader.loadModel(reader, size);
        if (additive) {
            SubBlueprint base = subBlueprints.get(subBlueprints.size() - 1);
            subBlueprints.add(new AdditionSubBlueprint(base, modelData));
        } else {
            subBlueprints.add(new SubBlueprint(modelData, new AABB(mins, maxs), extraAabbs, increaseFactor));
        }
    }

    private static AABB[] loadExtraAabbs(CSVReader reader, float size) {
        if (reader.isEndOfLine()) {
            return null;
        }
        int count = reader.getNextLabelInt();
        AABB[] aabbs = new AABB[count];
        int i = 0;
        while (i < count) {
            Vector3f mins = reader.getNextVector();
            mins.scale(size);
            Vector3f maxs = reader.getNextVector();
            maxs.scale(size);
            aabbs[i] = new AABB(mins, maxs);
            ++i;
        }
        return aabbs;
    }

    private static String loadOverrideName(CSVReader reader) {
        if (!reader.isEndOfLine()) {
            int index = reader.getNextLabelInt();
            if (index == -1) {
                return null;
            }
            return GameText.getText(index);
        }
        return null;
    }

    private static void loadIconInfo(CSVReader reader, Blueprint blueprint) {
        if (reader.isEndOfLine()) {
            return;
        }
        float size = reader.getNextLabelFloat();
        float y = reader.getNextLabelFloat();
        blueprint.setOverrideIconValues(size, y);
    }

    private static boolean loadPotentialBoolean(CSVReader reader) {
        if (!reader.isEndOfLine()) {
            return reader.getNextLabelBool();
        }
        return false;
    }

    private static Integer loadOverrideModelIndex(CSVReader reader) {
        if (!reader.isEndOfLine()) {
            int value = reader.getNextLabelInt();
            if (value >= 0) {
                return value;
            }
            return null;
        }
        return null;
    }

    private static void calculateGrowthFactors(List<SubBlueprint> subBlueprints) {
        int i = 0;
        while (i < subBlueprints.size()) {
            SubBlueprint nextStage = i < subBlueprints.size() - 1 ? subBlueprints.get(i + 1) : null;
            subBlueprints.get(i).calculateGrowths(i == 0, nextStage);
            ++i;
        }
    }

    private static void loadComponents(Blueprint blueprint, CSVReader reader) {
        reader.nextLine();
        int componentCount = reader.getNextInt();
        int i = 0;
        while (i < componentCount) {
            reader.nextLine();
            ComponentType type = ComponentType.valueOf(reader.getNextString());
            ComponentBlueprint component = type.loadComponent(reader, blueprint);
            blueprint.addComponent(component);
            ++i;
        }
    }

    private static void loadBlueprintInfo(Blueprint blueprint, CSVReader reader) {
        reader.nextLine();
        String classification = reader.getNextString();
        blueprint.setClassification(Classifier.getClassification(classification));
        reader.nextLine();
        boolean underwater = reader.getNextBool();
        boolean overwater = reader.getNextBool();
        float offset = 0.0f;
        if (!reader.isEndOfLine()) {
            offset = reader.getNextFloat();
        }
        blueprint.setWaterRequirements(underwater, overwater, offset);
    }
}


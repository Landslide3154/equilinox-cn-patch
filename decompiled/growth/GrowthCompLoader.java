/*
 * Decompiled with CFR 0.152.
 */
package growth;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import growth.GrowthCompBlueprint;
import utils.CSVReader;

public class GrowthCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        boolean dynamic = reader.getNextLabelBool();
        float averageGrowthTime = reader.getNextLabelFloat();
        int modelStages = reader.getNextLabelInt();
        if (dynamic) {
            return new GrowthCompBlueprint.DynamicGrowthCompBlueprint(averageGrowthTime, modelStages);
        }
        int subStages = reader.getNextLabelInt();
        return new GrowthCompBlueprint.StaticGrowthCompBlueprint(averageGrowthTime, modelStages, subStages);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


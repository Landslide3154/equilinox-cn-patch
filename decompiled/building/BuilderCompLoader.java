/*
 * Decompiled with CFR 0.152.
 */
package building;

import blueprints.Blueprint;
import building.BuilderCompBlueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import utils.CSVReader;

public class BuilderCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int buildModelId = reader.getNextLabelInt();
        int buildSpeed = reader.getNextLabelInt();
        boolean needsPerch = reader.getNextLabelBool();
        float averageBuildTime = reader.getNextLabelFloat();
        float buildAgeFactor = reader.getNextLabelFloat();
        float earlyBuildTime = 0.0f;
        if (!reader.isEndOfLine()) {
            earlyBuildTime = reader.getNextLabelFloat();
        }
        return new BuilderCompBlueprint(buildModelId, buildSpeed, needsPerch, averageBuildTime, buildAgeFactor, earlyBuildTime);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


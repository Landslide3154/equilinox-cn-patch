/*
 * Decompiled with CFR 0.152.
 */
package building;

import blueprints.Blueprint;
import building.BuildCompBlueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import utils.CSVReader;

public class BuildCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int stageCount = reader.getNextInt();
        int maxBuildPoints = reader.getNextInt();
        if (!reader.isEndOfLine()) {
            int fullyBuiltPoints = reader.getNextLabelInt();
            return new BuildCompBlueprint(stageCount, maxBuildPoints, fullyBuiltPoints);
        }
        return new BuildCompBlueprint(stageCount, maxBuildPoints);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


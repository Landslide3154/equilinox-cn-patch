/*
 * Decompiled with CFR 0.152.
 */
package hiveComponents;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import hiveComponents.HiveCompBlueprint;
import utils.CSVReader;

public class HiveCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int maxHoney = reader.getNextInt();
        int startStage = reader.getNextInt();
        int stageCount = reader.getNextInt();
        return new HiveCompBlueprint(maxHoney, startStage, stageCount);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


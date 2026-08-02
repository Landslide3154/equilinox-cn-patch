/*
 * Decompiled with CFR 0.152.
 */
package building;

import blueprints.Blueprint;
import building.DecomposeCompBlueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import utils.CSVReader;

public class DecomposeCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float timePerLoss = reader.getNextLabelFloat();
        return new DecomposeCompBlueprint(timePerLoss);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


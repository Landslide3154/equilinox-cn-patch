/*
 * Decompiled with CFR 0.152.
 */
package flinging;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import flinging.FlingingCompBlueprint;
import utils.CSVReader;

public class FlingingCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float min = reader.getNextLabelFloat();
        float max = reader.getNextLabelFloat();
        return new FlingingCompBlueprint(min, max);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


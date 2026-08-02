/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import meerkats.TimeOutCompBlueprint;
import utils.CSVReader;

public class TimeOutCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float time = reader.getNextLabelFloat();
        return new TimeOutCompBlueprint(time);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


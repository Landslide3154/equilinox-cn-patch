/*
 * Decompiled with CFR 0.152.
 */
package equipping;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import equipping.ItemCompBlueprint;
import utils.CSVReader;

public class ItemCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float decayTime = reader.getNextLabelFloat();
        return new ItemCompBlueprint(decayTime);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


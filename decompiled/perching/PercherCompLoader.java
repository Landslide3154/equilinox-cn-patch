/*
 * Decompiled with CFR 0.152.
 */
package perching;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import perching.PercherCompBlueprint;
import utils.CSVReader;

public class PercherCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        boolean removeOnRemoval = reader.getNextLabelBool();
        return new PercherCompBlueprint(removeOnRemoval);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


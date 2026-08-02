/*
 * Decompiled with CFR 0.152.
 */
package nightBloom;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import nightBloom.BloomCompBlueprint;
import utils.CSVReader;

public class BloomCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        return new BloomCompBlueprint();
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


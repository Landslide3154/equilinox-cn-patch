/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import meerkats.BurrowCompBlueprint;
import utils.CSVReader;

public class BurrowCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        return new BurrowCompBlueprint();
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package hiveComponents;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import hiveComponents.BeeCompBlueprint;
import utils.CSVReader;

public class BeeCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        return new BeeCompBlueprint();
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


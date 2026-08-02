/*
 * Decompiled with CFR 0.152.
 */
package loot;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import loot.DropCompBlueprint;
import utils.CSVReader;

public class DropCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int id = reader.getNextLabelInt();
        return new DropCompBlueprint(id);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


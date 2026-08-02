/*
 * Decompiled with CFR 0.152.
 */
package carnivorePlant;

import blueprints.Blueprint;
import carnivorePlant.TongueShootCompBlueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import utils.CSVReader;

public class TongueShootCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        return new TongueShootCompBlueprint();
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


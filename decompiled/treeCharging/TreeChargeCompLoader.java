/*
 * Decompiled with CFR 0.152.
 */
package treeCharging;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import treeCharging.TreeChargeCompBlueprint;
import utils.CSVReader;

public class TreeChargeCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        return new TreeChargeCompBlueprint();
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


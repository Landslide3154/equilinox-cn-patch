/*
 * Decompiled with CFR 0.152.
 */
package spitting;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import spitting.ProjectileCompBlueprint;
import utils.CSVReader;

public class ProjectileCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        return new ProjectileCompBlueprint();
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


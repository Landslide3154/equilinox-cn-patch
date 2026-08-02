/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.Requirement;
import utils.CSVReader;

public interface ComponentLoader {
    public ComponentBlueprint load(CSVReader var1, Blueprint var2);

    public Requirement loadRequirement(CSVReader var1);
}


/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import fruit.DecayCompBlueprint;
import utils.CSVReader;

public class DecayCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float lifeTime = reader.getNextFloat();
        return new DecayCompBlueprint(lifeTime);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


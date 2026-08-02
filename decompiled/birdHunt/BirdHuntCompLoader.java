/*
 * Decompiled with CFR 0.152.
 */
package birdHunt;

import birdHunt.BirdHuntCompBlueprint;
import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import utils.CSVReader;

public class BirdHuntCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        return new BirdHuntCompBlueprint();
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import beavers.WoodCompBlueprint;
import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import toolbox.Colour;
import utils.CSVReader;

public class WoodCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float cutTime = reader.getNextLabelFloat();
        float barkFactor = reader.getNextLabelFloat();
        Colour colour = new Colour(reader.getNextLabelVector());
        return new WoodCompBlueprint(cutTime, barkFactor, colour);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


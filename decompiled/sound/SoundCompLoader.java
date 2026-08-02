/*
 * Decompiled with CFR 0.152.
 */
package sound;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import sound.SoundCompBlueprint;
import utils.CSVReader;

public class SoundCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        return new SoundCompBlueprint();
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


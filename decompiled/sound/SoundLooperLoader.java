/*
 * Decompiled with CFR 0.152.
 */
package sound;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import resourceManagement.SoundCache;
import sound.SoundLooperBlueprint;
import utils.CSVReader;

public class SoundLooperLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        String name = reader.getNextLabelString();
        float range = reader.getNextLabelFloat();
        float volume = reader.getNextLabelFloat();
        return new SoundLooperBlueprint(SoundCache.CACHE.requestSound(name, true).withVolume(volume), range);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


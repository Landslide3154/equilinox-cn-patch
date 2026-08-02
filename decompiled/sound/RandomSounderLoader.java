/*
 * Decompiled with CFR 0.152.
 */
package sound;

import audio.SoundEffect;
import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import java.util.ArrayList;
import java.util.List;
import resourceManagement.SoundCache;
import sound.RandomSounderBlueprint;
import utils.CSVReader;

public class RandomSounderLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float waitTime = reader.getNextFloat();
        float randomExtra = reader.getNextFloat();
        List<SoundEffect> sounds = this.getSounds(reader);
        int stageReq = 0;
        if (!reader.isEndOfLine()) {
            stageReq = reader.getNextLabelInt();
            if (!reader.isEndOfLine()) {
                float volume = reader.getNextLabelFloat();
                for (SoundEffect soundEffect : sounds) {
                    soundEffect.getSound().withVolume(volume);
                }
            }
        }
        return new RandomSounderBlueprint(waitTime, randomExtra, sounds, stageReq);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }

    private List<SoundEffect> getSounds(CSVReader reader) {
        int count = reader.getNextInt();
        ArrayList<SoundEffect> sounds = new ArrayList<SoundEffect>();
        int i = 0;
        while (i < count) {
            String soundName = reader.getNextString();
            float range = reader.getNextFloat();
            sounds.add(new SoundEffect(SoundCache.CACHE.requestSound(soundName, true), range, false));
            ++i;
        }
        return sounds;
    }
}


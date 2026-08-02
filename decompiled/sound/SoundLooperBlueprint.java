/*
 * Decompiled with CFR 0.152.
 */
package sound;

import audio.Sound;
import audio.SoundEffect;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import resourceManagement.SoundCache;
import sound.SoundLooper;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class SoundLooperBlueprint
extends ComponentBlueprint {
    protected final SoundEffect soundEffect;

    protected SoundLooperBlueprint(Sound soundEffect, float range) {
        super(ComponentType.SOUND_LOOPER);
        this.soundEffect = new SoundEffect(soundEffect, range, true);
    }

    @Override
    public Component createInstance() {
        return new SoundLooper(this);
    }

    @Override
    public void delete() {
        SoundCache.CACHE.releaseSound(this.soundEffect.getSound());
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


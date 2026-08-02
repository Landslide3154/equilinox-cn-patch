/*
 * Decompiled with CFR 0.152.
 */
package sound;

import audio.SoundEffect;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import sound.RandomSounder;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import toolbox.Maths;

public class RandomSounderBlueprint
extends ComponentBlueprint {
    private final float minWaitTime;
    private final float randomExtraTime;
    private final List<SoundEffect> sounds;
    private final int stageRequired;

    protected RandomSounderBlueprint(float waitTime, float randomExtra, List<SoundEffect> sounds, int stageReq) {
        super(ComponentType.RANDOM_SOUNDER);
        this.minWaitTime = waitTime;
        this.randomExtraTime = randomExtra;
        this.sounds = sounds;
        this.stageRequired = stageReq;
    }

    public float getMinWaitTime() {
        return this.minWaitTime;
    }

    public float getRandomExtraTime() {
        return this.randomExtraTime;
    }

    public int getStageRequirement() {
        return this.stageRequired;
    }

    @Override
    public Component createInstance() {
        return new RandomSounder(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }

    protected SoundEffect getRandomSound() {
        return this.sounds.get(Maths.RANDOM.nextInt(this.sounds.size()));
    }
}


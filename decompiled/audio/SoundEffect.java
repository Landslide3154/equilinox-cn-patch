/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.Sound;

public class SoundEffect {
    private Sound sound;
    private float range;
    private boolean loop;

    public SoundEffect(Sound sound, float range, boolean loop) {
        this.sound = sound;
        this.range = range;
        this.loop = loop;
    }

    public boolean isLooper() {
        return this.loop;
    }

    public Sound getSound() {
        return this.sound;
    }

    protected float getRange() {
        return this.range;
    }

    protected float getRangeSquared() {
        return this.range * this.range;
    }
}


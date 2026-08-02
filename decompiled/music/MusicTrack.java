/*
 * Decompiled with CFR 0.152.
 */
package music;

import audio.Sound;

public class MusicTrack {
    private final int id;
    private final Sound sound;
    private final String name;
    private final boolean isBonusTrack;
    private boolean locked;
    private boolean needsUiUpdate = true;

    public MusicTrack(int id, Sound sound, String name, boolean bonusTrack) {
        this.sound = sound;
        this.name = name;
        this.isBonusTrack = bonusTrack;
        this.locked = bonusTrack;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public boolean needsUiUpdate() {
        return this.needsUiUpdate;
    }

    public Sound getSound() {
        return this.sound;
    }

    public String getName() {
        return this.name;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void resetLock() {
        this.locked = this.isBonusTrack;
    }

    public void notifyUiUpdated() {
        this.needsUiUpdate = false;
    }

    public void unlock() {
        this.locked = false;
        this.needsUiUpdate = true;
    }
}


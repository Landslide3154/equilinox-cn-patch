/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.SoundSource;
import org.lwjgl.util.vector.Vector3f;

public class AudioController {
    private static final float FADE_TIME = 2.0f;
    private SoundSource source;
    private boolean active = true;
    private boolean fading = false;
    private float localVolume;
    private float fadeFactor = 1.0f;

    protected AudioController(SoundSource source, float localVolume) {
        this.source = source;
        this.localVolume = localVolume;
    }

    public void stop() {
        if (this.active) {
            this.source.stop();
        }
    }

    protected void fadeOut() {
        this.fading = true;
    }

    public boolean update(float delta, float masterVolume) {
        if (this.active) {
            this.updateActiveController(delta, masterVolume);
        }
        return this.active;
    }

    protected void setPosition(Vector3f position) {
        if (this.active) {
            this.source.setPosition(position);
        }
    }

    public void setLocalVolume(float localVolume) {
        this.localVolume = localVolume;
    }

    public void moveTowardIdealVolume(float idealVolume, float maxChangePerSec, float delta) {
        float maxChange = maxChangePerSec * delta;
        float toIdeal = idealVolume - this.localVolume;
        float changeMagnitude = Math.abs(toIdeal);
        this.localVolume = changeMagnitude <= maxChange ? idealVolume : (this.localVolume += maxChange * Math.signum(toIdeal));
    }

    public float getLocalVolume() {
        return this.localVolume;
    }

    protected boolean isActive() {
        return this.active;
    }

    protected void setInactive() {
        this.active = false;
    }

    private void updateActiveController(float delta, float masterVolume) {
        if (this.fading) {
            this.updateFadingOut(delta);
        }
        this.updateVolume(masterVolume);
    }

    private void updateFadingOut(float delta) {
        this.fadeFactor -= delta / 2.0f;
        if (this.fadeFactor <= 0.0f) {
            this.source.stop();
        }
    }

    private void updateVolume(float masterVolume) {
        this.source.setVolume(masterVolume * this.localVolume * this.fadeFactor);
    }
}


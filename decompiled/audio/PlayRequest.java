/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.Sound;
import org.lwjgl.util.vector.Vector3f;

public class PlayRequest {
    private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
    private float innerRange = 1.0f;
    private float outerRange = 1.0f;
    private boolean systemSound = true;
    private boolean loop = false;
    private float volume = 1.0f;
    private float pitch = 1.0f;
    private Sound sound;

    private PlayRequest(Sound sound, float volume, boolean loop) {
        this.sound = sound;
        this.volume = volume;
        this.loop = loop;
    }

    protected Vector3f getPosition() {
        return this.position;
    }

    protected float getPitch() {
        return this.pitch;
    }

    protected float getInnerRange() {
        return this.innerRange;
    }

    protected float getOuterRange() {
        return this.outerRange;
    }

    protected boolean isSystemSound() {
        return this.systemSound;
    }

    protected float getVolume() {
        return this.volume;
    }

    protected Sound getSound() {
        return this.sound;
    }

    protected void setLooping(boolean loop) {
        this.loop = loop;
    }

    protected void setPitch(float pitch) {
        this.pitch = pitch;
    }

    protected boolean isLooping() {
        return this.loop;
    }

    protected static PlayRequest newSystemPlayRequest(Sound systemSound) {
        return PlayRequest.newSystemPlayRequest(systemSound, systemSound.getPitch());
    }

    protected static PlayRequest newSystemPlayRequest(Sound systemSound, float pitch) {
        PlayRequest request = new PlayRequest(systemSound, 1.0f, false);
        request.setPitch(pitch);
        return request;
    }

    protected static PlayRequest newAmbientPlayRequest(Sound systemSound, float volume) {
        return new PlayRequest(systemSound, volume, true);
    }

    protected static PlayRequest new3dSoundPlayRequest(Sound sound, float volume, Vector3f position, float innerRange, float outerRange) {
        PlayRequest request = new PlayRequest(sound, volume, false);
        request.systemSound = false;
        request.innerRange = innerRange < 1.0f ? 1.0f : innerRange;
        request.outerRange = outerRange;
        request.position = position;
        return request;
    }
}


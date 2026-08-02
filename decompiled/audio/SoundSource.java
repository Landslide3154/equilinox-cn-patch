/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.AudioController;
import audio.Sound;
import audio.StreamManager;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

public class SoundSource {
    private int sourceID = SoundSource.createSource();
    private float volume = 1.0f;
    private Sound currentSound;
    private AudioController currentController;

    public SoundSource() {
        AL10.alSource3f(this.sourceID, 4100, 0.0f, 0.0f, 0.0f);
        AL10.alSource3f(this.sourceID, 4102, 1.0f, 0.0f, 0.0f);
        AL10.alSourcef(this.sourceID, 4129, 0.0f);
        AL10.alSourcef(this.sourceID, 4106, this.volume);
    }

    protected void setRange(float radius) {
        AL10.alSourcef(this.sourceID, 4128, 1.0f);
        AL10.alSourcef(this.sourceID, 4129, 1.0f);
        AL10.alSourcef(this.sourceID, 4131, radius);
    }

    public void setUndiminishing() {
        AL10.alSourcef(this.sourceID, 4129, 0.0f);
    }

    protected void setRanges(float primaryRadius, float secondaryRadius) {
        if (primaryRadius < 1.0f) {
            primaryRadius = 1.0f;
        }
        AL10.alSourcef(this.sourceID, 4128, primaryRadius);
        AL10.alSourcef(this.sourceID, 4129, 1.0f);
        AL10.alSourcef(this.sourceID, 4131, secondaryRadius);
    }

    public void setVolume(float newVolume) {
        if (newVolume != this.volume) {
            this.volume = newVolume;
            if (this.currentSound != null) {
                AL10.alSourcef(this.sourceID, 4106, this.volume * this.currentSound.getVolume());
            }
        }
    }

    protected void setPosition(Vector3f position) {
        AL10.alSource3f(this.sourceID, 4100, position.x, position.y, position.z);
    }

    public void loop(boolean loop) {
        AL10.alSourcei(this.sourceID, 4103, loop ? 1 : 0);
    }

    public AudioController playSound(Sound sound, float localVolume) {
        if (!sound.isLoaded()) {
            return null;
        }
        this.stop();
        this.currentSound = sound;
        AL10.alSourcef(this.sourceID, 4106, this.volume * this.currentSound.getVolume());
        this.currentController = new AudioController(this, localVolume);
        if (sound.needsStreaming()) {
            this.queue(sound.getBufferID());
            AL10.alSourcei(this.sourceID, 4103, 0);
            StreamManager.STREAMER.stream(sound, this, this.currentController);
        } else {
            AL10.alSourcei(this.sourceID, 4103, 0);
            AL10.alSourcei(this.sourceID, 4105, sound.getBufferID());
        }
        AL10.alSourcePlay(this.sourceID);
        return this.currentController;
    }

    protected void setPitch(float pitch) {
        AL10.alSourcef(this.sourceID, 4099, pitch);
    }

    public void stop() {
        if (this.isPlaying()) {
            AL10.alSourceStop(this.sourceID);
        }
        this.setInactive();
    }

    public void setInactive() {
        if (this.currentSound != null) {
            AL10.alSourcei(this.sourceID, 4105, 0);
            this.currentController.setInactive();
            int i = 0;
            while (i < this.getFinishedBuffersCount()) {
                this.unqueue();
                ++i;
            }
            this.currentSound = null;
        }
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(this.sourceID, 4112) == 4114;
    }

    public void delete() {
        this.stop();
        AL10.alDeleteSources(this.sourceID);
    }

    protected void queue(int buffer) {
        AL10.alSourceQueueBuffers(this.sourceID, buffer);
    }

    protected void unqueue() {
        AL10.alSourceUnqueueBuffers(this.sourceID);
    }

    protected int getFinishedBuffersCount() {
        return AL10.alGetSourcei(this.sourceID, 4118);
    }

    private static int createSource() {
        int sourceID = AL10.alGenSources();
        if (AL10.alGetError() != 0) {
            System.err.println("Problem creating source!");
        }
        return sourceID;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.AudioController;
import audio.PlayRequest;
import audio.SoundEffect;
import audio.SoundMaestro;
import basics.DisplayManager;
import main.Camera;
import org.lwjgl.util.vector.Vector3f;

public class SoundLoopingNode {
    private final SoundEffect sound;
    private Vector3f position;
    private boolean active = false;
    private AudioController currentlyPlaying = null;

    public SoundLoopingNode(SoundEffect sound, Vector3f position) {
        this.sound = sound;
        this.position = new Vector3f(position);
    }

    public void update() {
        if (!this.active && this.inRange()) {
            this.startPlaying();
        } else if (this.active && !this.inRange()) {
            this.stopPlaying();
        } else if (this.active) {
            this.updateSoundEffect();
        }
    }

    public void updatePosition(Vector3f position) {
        this.position.set(position);
        if (this.active) {
            this.currentlyPlaying.setPosition(position);
        }
    }

    private boolean inRange() {
        Vector3f camPos = Camera.getCamera().getListenerPosition();
        float disSquared = Vector3f.sub(this.position, camPos, null).lengthSquared();
        return disSquared < this.sound.getRangeSquared();
    }

    public void stopPlaying() {
        if (this.currentlyPlaying != null) {
            this.currentlyPlaying.stop();
        }
        this.active = false;
    }

    private void updateSoundEffect() {
        boolean stillPlaying = this.currentlyPlaying.update(DisplayManager.getDeltaSeconds(), SoundMaestro.SOUND_VOLUME);
        if (!stillPlaying) {
            this.startPlaying();
        }
    }

    private void startPlaying() {
        PlayRequest request = PlayRequest.new3dSoundPlayRequest(this.sound.getSound(), 1.0f, this.position, 0.0f, this.sound.getRange());
        this.currentlyPlaying = SoundMaestro.play3DSound(request);
        if (this.currentlyPlaying != null) {
            this.active = true;
        }
    }
}


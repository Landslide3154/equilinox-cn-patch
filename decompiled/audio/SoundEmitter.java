/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.AudioController;
import audio.PlayRequest;
import audio.SoundEffect;
import audio.SoundMaestro;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;

public class SoundEmitter {
    private static final float RANGE_THRESHOLD = 1.2f;
    private Vector3f position;
    private float volume = 1.0f;
    private Map<SoundEffect, AudioController> playingSounds = new HashMap<SoundEffect, AudioController>();

    public SoundEmitter(Vector3f position) {
        this.position = position;
    }

    public void update(float delta) {
        ArrayList<SoundEffect> playAgain = new ArrayList<SoundEffect>();
        Iterator<Map.Entry<SoundEffect, AudioController>> iterator = this.playingSounds.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<SoundEffect, AudioController> currentlyPlaying = iterator.next();
            boolean stillPlaying = this.updateAudioController(currentlyPlaying, delta, playAgain);
            if (stillPlaying) continue;
            iterator.remove();
        }
        for (SoundEffect effect : playAgain) {
            this.playSound(effect, 1.0f);
        }
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void updatePosition() {
        for (AudioController controller : this.playingSounds.values()) {
            controller.setPosition(this.position);
        }
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void playSound(SoundEffect soundEffect, float pitch) {
        if (!soundEffect.getSound().isLoaded() || !this.isInRange(soundEffect) || this.isPlayingSound(soundEffect)) {
            return;
        }
        PlayRequest request = PlayRequest.new3dSoundPlayRequest(soundEffect.getSound(), this.volume, this.position, 0.0f, soundEffect.getRange());
        request.setPitch(pitch);
        AudioController controller = SoundMaestro.play3DSound(request);
        if (controller != null) {
            this.playingSounds.put(soundEffect, controller);
        }
    }

    public void silence() {
        for (AudioController controller : this.playingSounds.values()) {
            controller.stop();
        }
        this.playingSounds.clear();
    }

    public boolean isInUse() {
        return !this.playingSounds.isEmpty();
    }

    public boolean isPlayingSound(SoundEffect sound) {
        return this.playingSounds.containsKey(sound);
    }

    private boolean updateAudioController(Map.Entry<SoundEffect, AudioController> entry, float delta, List<SoundEffect> playAgain) {
        AudioController controller = entry.getValue();
        if (!this.isInRange(entry.getKey())) {
            controller.stop();
            controller.update(delta, SoundMaestro.SOUND_VOLUME);
            return false;
        }
        boolean finished = controller.update(delta, SoundMaestro.SOUND_VOLUME);
        if (finished && entry.getKey().isLooper()) {
            playAgain.add(entry.getKey());
        }
        return finished;
    }

    private boolean isInRange(SoundEffect soundEffect) {
        float range;
        float rangeSquared;
        float disSquared = Vector3f.sub(SoundMaestro.getListener().getListenerPosition(), this.position, null).lengthSquared();
        return disSquared < (rangeSquared = (range = soundEffect.getRange() * 1.2f) * range);
    }
}


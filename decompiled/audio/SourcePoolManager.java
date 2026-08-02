/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.AudioController;
import audio.PlayRequest;
import audio.Sound;
import audio.SoundMaestro;
import audio.SoundSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SourcePoolManager {
    private List<SoundSource> sourcePool = new ArrayList<SoundSource>();
    private List<SoundSource> usedSources = new ArrayList<SoundSource>();

    protected SourcePoolManager(int numberOfSources) {
        int i = 0;
        while (i < numberOfSources) {
            this.sourcePool.add(new SoundSource());
            ++i;
        }
    }

    protected AudioController play(PlayRequest playRequest) {
        if (!this.sourcePool.isEmpty()) {
            SoundSource source = this.sourcePool.remove(0);
            this.usedSources.add(source);
            source.setPosition(playRequest.getPosition());
            source.loop(playRequest.isLooping());
            source.setPitch(playRequest.getPitch());
            if (!playRequest.isSystemSound()) {
                source.setRanges(playRequest.getInnerRange(), playRequest.getOuterRange());
            } else {
                source.setUndiminishing();
            }
            Sound sound = playRequest.getSound();
            source.setVolume(SoundMaestro.SOUND_VOLUME * playRequest.getVolume());
            AudioController controller = source.playSound(sound, playRequest.getVolume());
            return controller;
        }
        return null;
    }

    protected void update() {
        Iterator<SoundSource> iterator = this.usedSources.iterator();
        while (iterator.hasNext()) {
            SoundSource source = iterator.next();
            if (source.isPlaying()) continue;
            iterator.remove();
            source.setInactive();
            this.sourcePool.add(source);
        }
    }

    protected void cleanUp() {
        for (SoundSource source : this.sourcePool) {
            source.delete();
        }
    }
}


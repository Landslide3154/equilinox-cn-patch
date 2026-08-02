/*
 * Decompiled with CFR 0.152.
 */
package music;

import audio.SoundSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import music.MusicTrack;
import music.Playlist;
import music.QueueController;
import utils.BinaryReader;
import utils.BinaryWriter;

public class MusicPlayer {
    private static final float FADE_TIME = 1.0f;
    private float musicVolume = 0.3f;
    private SoundSource source;
    private Playlist currentPlaylist;
    private List<MusicTrack> musicQueue = new ArrayList<MusicTrack>();
    private QueueController manualQueue = new QueueController();
    private MusicTrack forcePlay = null;
    private MusicTrack currentlyPlaying = null;
    private boolean fadeOut = false;
    private float fadeFactor = 1.0f;
    private boolean shuffle;
    private boolean closing = false;

    public MusicPlayer() {
        this.source = new SoundSource();
        this.source.loop(false);
        this.source.setUndiminishing();
    }

    public void forcePlay(MusicTrack music, boolean fadeOutPrevious) {
        this.forcePlay = music;
        if (fadeOutPrevious) {
            if (!this.fadeOut) {
                this.fadeOutCurrentTrack();
            }
        } else {
            this.source.stop();
        }
    }

    public void setVolume(float volume) {
        this.musicVolume = volume;
        if (this.currentlyPlaying != null) {
            this.source.setVolume(this.musicVolume * this.fadeFactor);
        }
    }

    public float getVolume() {
        return this.musicVolume;
    }

    public void exportSettings(BinaryWriter writer) throws IOException {
        writer.writeFloat(this.musicVolume);
        this.manualQueue.export(writer);
    }

    public void loadSettings(BinaryReader reader) throws Exception {
        this.musicVolume = reader.readFloat();
        this.manualQueue.load(reader);
    }

    public QueueController getQueueController() {
        return this.manualQueue;
    }

    public void playMusicPlaylist(Playlist playlist, boolean shuffle) {
        this.shuffle = shuffle;
        this.currentPlaylist = playlist;
        this.fadeOutCurrentTrack();
        this.musicQueue.clear();
        this.fillQueue();
    }

    public MusicTrack getCurrentlyPlaying() {
        return this.currentlyPlaying;
    }

    public Playlist getPlayList() {
        return this.currentPlaylist;
    }

    public void update(float delta) {
        if (this.fadeOut) {
            this.updateFadeOut(delta);
        }
        if (!(this.source.isPlaying() || this.musicQueue.isEmpty() || this.closing)) {
            this.source.setInactive();
            this.playNextTrack();
        }
    }

    public void cleanUp() {
        this.source.delete();
    }

    private void updateFadeOut(float delta) {
        this.fadeFactor -= delta / 1.0f;
        this.source.setVolume(this.musicVolume * this.fadeFactor);
        if (this.fadeFactor <= 0.0f) {
            this.stopFadeOut();
            this.source.stop();
        }
    }

    private void playNextTrack() {
        this.stopFadeOut();
        MusicTrack nextTrack = this.getNextTrack();
        if (this.musicQueue.isEmpty()) {
            this.fillQueue();
        }
        this.currentlyPlaying = nextTrack;
        this.source.setVolume(this.musicVolume);
        this.source.playSound(this.currentlyPlaying.getSound(), 1.0f);
    }

    private void stopFadeOut() {
        this.fadeOut = false;
        this.fadeFactor = 1.0f;
    }

    public void fadeOutCurrentTrack() {
        if (this.currentlyPlaying != null) {
            this.fadeOut = true;
        }
    }

    public void fadeOutAndStopPlayer() {
        this.closing = true;
        if (this.currentlyPlaying != null) {
            this.fadeOut = true;
        }
    }

    private void fillQueue() {
        if (this.currentPlaylist == null) {
            return;
        }
        Collection<MusicTrack> allTracks = this.shuffle ? this.currentPlaylist.getShuffledMusicList(this.currentlyPlaying) : this.currentPlaylist.getOrderedTracks();
        for (MusicTrack track : allTracks) {
            if (track.isLocked()) continue;
            this.musicQueue.add(track);
        }
    }

    private MusicTrack getNextTrack() {
        if (this.forcePlay != null) {
            MusicTrack upNext = this.forcePlay;
            this.forcePlay = null;
            return upNext;
        }
        if (!this.manualQueue.isEmpty()) {
            return this.manualQueue.getNext();
        }
        return this.musicQueue.remove(0);
    }
}


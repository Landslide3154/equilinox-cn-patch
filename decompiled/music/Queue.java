/*
 * Decompiled with CFR 0.152.
 */
package music;

import java.util.ArrayList;
import java.util.List;
import music.MusicTrack;

public class Queue {
    private List<MusicTrack> tracks = new ArrayList<MusicTrack>();

    public void add(MusicTrack track) {
        this.tracks.add(track);
    }

    public MusicTrack remove(int i) {
        return this.tracks.remove(i);
    }

    public boolean isEmpty() {
        return this.tracks.isEmpty();
    }

    public List<MusicTrack> getTracks() {
        return this.tracks;
    }
}


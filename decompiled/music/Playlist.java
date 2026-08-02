/*
 * Decompiled with CFR 0.152.
 */
package music;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import music.MusicTrack;
import toolbox.Maths;

public class Playlist {
    private Map<Integer, MusicTrack> musics = new LinkedHashMap<Integer, MusicTrack>();

    public void addMusic(MusicTrack music) {
        this.musics.put(music.getId(), music);
    }

    public void clear() {
        this.musics.clear();
    }

    public boolean isLoaded() {
        for (MusicTrack sound : this.musics.values()) {
            if (sound.getSound().isLoaded()) continue;
            return false;
        }
        return true;
    }

    public Collection<MusicTrack> getOrderedTracks() {
        return this.musics.values();
    }

    public MusicTrack getTrack(int id) {
        return this.musics.get(id);
    }

    protected List<MusicTrack> getShuffledMusicList(MusicTrack previouslyPlayed) {
        ArrayList<MusicTrack> tempList = new ArrayList<MusicTrack>();
        tempList.addAll(this.musics.values());
        ArrayList<MusicTrack> shuffledList = new ArrayList<MusicTrack>();
        while (!tempList.isEmpty()) {
            MusicTrack track = this.removeRandomTrackFromList(tempList);
            if (track.isLocked()) continue;
            shuffledList.add(track);
        }
        this.ensurePreviousTrackNotRepeated(shuffledList, previouslyPlayed);
        return shuffledList;
    }

    private MusicTrack removeRandomTrackFromList(List<MusicTrack> listOfMusic) {
        int index = Maths.RANDOM.nextInt(listOfMusic.size());
        return listOfMusic.remove(index);
    }

    private void ensurePreviousTrackNotRepeated(List<MusicTrack> newPlaylist, MusicTrack previouslyPlayed) {
        if (!newPlaylist.isEmpty() && newPlaylist.get(0) == previouslyPlayed) {
            MusicTrack track = newPlaylist.remove(0);
            newPlaylist.add(track);
        }
    }
}


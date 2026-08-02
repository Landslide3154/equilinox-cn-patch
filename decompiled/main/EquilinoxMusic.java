/*
 * Decompiled with CFR 0.152.
 */
package main;

import audio.SoundMaestro;
import music.MusicPlayer;
import music.MusicTrack;
import music.Playlist;
import resourceManagement.SoundCache;

public class EquilinoxMusic {
    private static Playlist mainPlaylist;

    public static void loadMusic() {
        mainPlaylist = new Playlist();
        mainPlaylist.addMusic(new MusicTrack(1, SoundCache.CACHE.requestSound("song", true, true), "Sheep", false));
        mainPlaylist.addMusic(new MusicTrack(2, SoundCache.CACHE.requestSound("Ambience", true, true), "Ambience", false));
        mainPlaylist.addMusic(new MusicTrack(3, SoundCache.CACHE.requestSound("Poly dream", true, true), "Low Poly Dream", false));
        mainPlaylist.addMusic(new MusicTrack(4, SoundCache.CACHE.requestSound("Island paradise", true, true), "Island Paradise", false));
        mainPlaylist.addMusic(new MusicTrack(6, SoundCache.CACHE.requestSound("Zen", true, true), "Zen", false));
        mainPlaylist.addMusic(new MusicTrack(8, SoundCache.CACHE.requestSound("Wonderful world", true, true), "Wonderful World", false));
        mainPlaylist.addMusic(new MusicTrack(9, SoundCache.CACHE.requestSound("Tranquility", true, true), "Tranquility", false));
        mainPlaylist.addMusic(new MusicTrack(10, SoundCache.CACHE.requestSound("Sunrise", true, true), "Sunrise", false));
        mainPlaylist.addMusic(new MusicTrack(11, SoundCache.CACHE.requestSound("Green Fields", true, true), "Green Fields", false));
        mainPlaylist.addMusic(new MusicTrack(12, SoundCache.CACHE.requestSound("Ambition", true, true), "Ambition", false));
        mainPlaylist.addMusic(new MusicTrack(14, SoundCache.CACHE.requestSound("Main Theme Fixed", true, true), "Equilinox Theme", false));
        mainPlaylist.addMusic(new MusicTrack(16, SoundCache.CACHE.requestSound("Horizons", true, true), "Horizons", false));
        mainPlaylist.addMusic(new MusicTrack(17, SoundCache.CACHE.requestSound("Wander", true, true), "Wander", true));
        mainPlaylist.addMusic(new MusicTrack(18, SoundCache.CACHE.requestSound("Home", true, true), "Home", true));
        mainPlaylist.addMusic(new MusicTrack(19, SoundCache.CACHE.requestSound("Reminiscence", true, true), "Reminiscence", true));
        mainPlaylist.addMusic(new MusicTrack(20, SoundCache.CACHE.requestSound("Dusk", true, true), "From Dusk Til Dawn", true));
        mainPlaylist.addMusic(new MusicTrack(21, SoundCache.CACHE.requestSound("Waltz", true, true), "Wonderful Waltz", true));
    }

    public static boolean isLoaded() {
        return mainPlaylist.isLoaded();
    }

    public static void startPlayingPlaylist() {
        MusicPlayer player = SoundMaestro.getMusicPlayer();
        player.playMusicPlaylist(mainPlaylist, true);
    }

    public static Playlist getPlaylist() {
        return mainPlaylist;
    }

    public static void resetMusic() {
        for (MusicTrack track : mainPlaylist.getOrderedTracks()) {
            track.resetLock();
        }
    }

    public static MusicTrack getTrack(int id) {
        return mainPlaylist.getTrack(id);
    }
}


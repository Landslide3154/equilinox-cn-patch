/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.AudioController;
import audio.AudioListener;
import audio.PlayRequest;
import audio.Sound;
import audio.SoundLoader;
import audio.SourcePoolManager;
import audio.StreamManager;
import errors.ErrorManager;
import music.MusicPlayer;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;
import utils.FileUtils;
import utils.MyFile;

public class SoundMaestro {
    public static final MyFile SOUND_FOLDER = new MyFile(FileUtils.RES_FOLDER, "sounds");
    public static float SOUND_VOLUME = 1.0f;
    private static final int UI_SOURCES = 5;
    private static final int AMBIENT_SOURCES = 6;
    private static final int ENTITY_SOURCES = 13;
    private static SourcePoolManager uiSourcePool;
    private static SourcePoolManager ambientSourcePool;
    private static SourcePoolManager entitySourcePool;
    private static AudioListener listener;
    private static MusicPlayer musicPlayer;

    public static void init(AudioListener theListener) {
        try {
            AL.create();
            AL10.alGetError();
            AL10.alDistanceModel(53252);
            StreamManager.STREAMER.start();
            uiSourcePool = new SourcePoolManager(5);
            ambientSourcePool = new SourcePoolManager(6);
            entitySourcePool = new SourcePoolManager(13);
            listener = theListener;
            musicPlayer = new MusicPlayer();
        }
        catch (Exception e) {
            ErrorManager.crashWithUserAlert("No Audio Device Found!", "The game was unable to launch which was likely because no audio devices were found (headphones, speakers, etc.) Try plugging in headphones or turning on speakers to see if that fixes the problem. For more help, contact the dev at thinmatrix@gmail.com and copy-paste the following error message to the email:", e);
        }
    }

    public static AudioController playSystemSound(Sound sound) {
        if (!sound.isLoaded()) {
            return null;
        }
        return uiSourcePool.play(PlayRequest.newSystemPlayRequest(sound));
    }

    public static AudioController playSystemSound(Sound sound, float pitch) {
        if (!sound.isLoaded()) {
            return null;
        }
        return uiSourcePool.play(PlayRequest.newSystemPlayRequest(sound, pitch));
    }

    public static AudioController playAmbientSound(Sound sound, float volume) {
        if (!sound.isLoaded()) {
            return null;
        }
        return ambientSourcePool.play(PlayRequest.newAmbientPlayRequest(sound, volume));
    }

    public static AudioListener getListener() {
        return listener;
    }

    public static MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public static void update(float delta) {
        Vector3f position = listener.getListenerPosition();
        AL10.alListener3f(4100, position.x, position.y, position.z);
        musicPlayer.update(delta);
        uiSourcePool.update();
        ambientSourcePool.update();
        entitySourcePool.update();
    }

    public static void cleanUp() {
        StreamManager.STREAMER.kill();
        uiSourcePool.cleanUp();
        ambientSourcePool.cleanUp();
        entitySourcePool.cleanUp();
        musicPlayer.cleanUp();
        SoundLoader.cleanUp();
        AL.destroy();
    }

    protected static AudioController play3DSound(PlayRequest playRequest) {
        if (!playRequest.getSound().isLoaded()) {
            return null;
        }
        return entitySourcePool.play(playRequest);
    }
}


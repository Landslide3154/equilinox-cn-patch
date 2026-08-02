/*
 * Decompiled with CFR 0.152.
 */
package resourceManagement;

import audio.Sound;
import audio.SoundMaestro;
import java.util.HashMap;
import java.util.Map;
import utils.MyFile;

public class SoundCache {
    public static final SoundCache CACHE = new SoundCache();
    private static final String WAV_EXT = ".wav";
    private static final String OGG_EXT = ".ogg";
    private Map<String, CachedSound> soundCache = new HashMap<String, CachedSound>();

    private SoundCache() {
    }

    public synchronized Sound requestSound(String id, boolean loadNow) {
        return this.requestSound(id, loadNow, false);
    }

    public synchronized Sound requestSound(String id, boolean loadNow, boolean oggFile) {
        CachedSound cachedSound = this.soundCache.get(id);
        if (cachedSound == null) {
            return this.loadNewSound(id, loadNow, oggFile);
        }
        CachedSound cachedSound2 = cachedSound;
        cachedSound2.timesUsed = cachedSound2.timesUsed + 1;
        return cachedSound.sound;
    }

    public void releaseSound(Sound sound) {
        CachedSound cachedSound;
        CachedSound cachedSound2 = cachedSound = this.soundCache.get(sound.getId());
        cachedSound2.timesUsed = cachedSound2.timesUsed - 1;
        if (cachedSound.timesUsed == 0) {
            this.removeSoundFromCache(sound.getId());
        }
    }

    private Sound loadNewSound(String id, boolean loadNow, boolean oggFile) {
        String ext = oggFile ? OGG_EXT : WAV_EXT;
        Sound sound = !loadNow ? Sound.loadSoundInBackground(new MyFile(SoundMaestro.SOUND_FOLDER, String.valueOf(id) + ext)) : Sound.loadSoundNow(new MyFile(SoundMaestro.SOUND_FOLDER, String.valueOf(id) + ext));
        CachedSound cachedSound = new CachedSound(sound);
        this.soundCache.put(id, cachedSound);
        return sound;
    }

    private void removeSoundFromCache(String id) {
        CachedSound cachedSound = this.soundCache.remove(id);
        cachedSound.sound.delete();
    }

    private static class CachedSound {
        private int timesUsed = 1;
        private Sound sound;

        private CachedSound(Sound sound) {
            this.sound = sound;
        }
    }
}


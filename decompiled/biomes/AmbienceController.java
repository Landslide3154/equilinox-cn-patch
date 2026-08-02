/*
 * Decompiled with CFR 0.152.
 */
package biomes;

import audio.AudioController;
import audio.Sound;
import audio.SoundMaestro;
import basics.DisplayManager;
import biomes.Biome;
import biomes.BiomeGrid;
import biomes.BiomeGridSquare;
import gameManaging.GameManager;
import gameManaging.GameState;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class AmbienceController {
    private static final float SOUND_PAD = 0.45f;
    private static final float MAX_CHANGE_PER_SEC = 1.2f;
    private static final float Y_CUTOFF = 4.0f;
    private static final float Y_ROLLOFF = 5.0f;
    private static final float FADE_SPEED = 0.75f;
    private float masterVolume = 0.0f;
    private Map<Sound, AudioController> currentAmbience = new HashMap<Sound, AudioController>();
    private final BiomeGrid grid;

    public AmbienceController(BiomeGrid grid) {
        this.grid = grid;
    }

    public void update(Vector3f listener) {
        if (GameManager.getGameState() == GameState.GAME_MENU || GameManager.getGameState() == GameState.SPLASH_SCREEN) {
            this.masterVolume -= DisplayManager.getDeltaSeconds() * 0.75f;
        } else if (this.masterVolume < 1.0f) {
            this.masterVolume += DisplayManager.getDeltaSeconds() * 0.75f;
        }
        this.masterVolume = Maths.clamp(this.masterVolume, 0.0f, 1.0f);
        Map<Sound, Float> volumes = this.calculateIdealAmbienceVolumes(listener);
        this.updateAmbientVolumes(volumes);
        this.startNewSounds(volumes);
    }

    public void stopAll() {
        for (AudioController controller : this.currentAmbience.values()) {
            controller.stop();
        }
    }

    private Map<Sound, Float> calculateIdealAmbienceVolumes(Vector3f listenerPos) {
        float xCoord = listenerPos.x / 6.6666665f;
        float zCoord = listenerPos.z / 6.6666665f;
        float xFract = xCoord % 1.0f;
        float zFract = zCoord % 1.0f;
        int gridX = (int)xCoord;
        int gridZ = (int)zCoord;
        float yVolume = Maths.clamp(1.0f - (listenerPos.y - 4.0f) / 5.0f, 0.0f, 1.0f);
        HashMap<Sound, Float> volumes = new HashMap<Sound, Float>();
        this.addAmbience(volumes, (int)xCoord, (int)zCoord, yVolume);
        boolean inSideX = this.addSideAmbience(volumes, true, xFract, gridX, gridZ, yVolume);
        boolean inSideZ = this.addSideAmbience(volumes, false, zFract, gridX, gridZ, yVolume);
        if (inSideX && inSideZ) {
            this.addCornerAmbience(volumes, xFract, zFract, gridX, gridZ, yVolume);
        }
        return volumes;
    }

    private boolean addSideAmbience(Map<Sound, Float> volumes, boolean xDir, float fract, int gridX, int gridZ, float yVolume) {
        if (fract < 0.45f) {
            float volume = 1.0f - fract / 0.45f;
            this.addAmbience(volumes, xDir ? gridX - 1 : gridX, xDir ? gridZ : gridZ - 1, volume * yVolume);
            return true;
        }
        if (fract > 0.55f) {
            float volume = (fract - 0.55f) / 0.45f;
            this.addAmbience(volumes, xDir ? gridX + 1 : gridX, xDir ? gridZ : gridZ + 1, volume * yVolume);
            return true;
        }
        return false;
    }

    private void addAmbience(Map<Sound, Float> volumes, int gridX, int gridZ, float volume) {
        Sound ambientSound;
        Float currentVolume;
        BiomeGridSquare square = this.grid.getGridSquare(gridX, gridZ);
        if (square == null || volume <= 0.0f) {
            return;
        }
        Biome biome = square.getMajorityBiome();
        if (biome != null && biome.getSound() != null && ((currentVolume = volumes.get(ambientSound = biome.getSound())) == null || volume > currentVolume.floatValue())) {
            volumes.put(ambientSound, Float.valueOf(volume));
        }
    }

    private void addCornerAmbience(Map<Sound, Float> volumes, float xFract, float zFract, int gridX, int gridZ, float yVolume) {
        Vector2f listener;
        Vector2f corner = new Vector2f(!(xFract < 0.5f) ? 1 : 0, !(zFract < 0.5f) ? 1 : 0);
        float length = Vector2f.sub(corner, listener = new Vector2f(xFract, zFract), null).length();
        float volume = Math.max(1.0f - length / 0.45f, 0.0f);
        if (volume > 0.0f) {
            this.addAmbience(volumes, gridX + (xFract < 0.5f ? -1 : 1), gridZ + (zFract < 0.5f ? -1 : 1), volume * yVolume);
        }
    }

    private void updateAmbientVolumes(Map<Sound, Float> volumes) {
        Iterator<Map.Entry<Sound, AudioController>> iterator = this.currentAmbience.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Sound, AudioController> ambience = iterator.next();
            Float idealVolume = volumes.remove(ambience.getKey());
            if (idealVolume == null) {
                idealVolume = Float.valueOf(0.0f);
            }
            AudioController playingAmbience = ambience.getValue();
            playingAmbience.moveTowardIdealVolume(idealVolume.floatValue(), 1.2f, DisplayManager.getDeltaSeconds());
            if (playingAmbience.getLocalVolume() <= 0.0f) {
                playingAmbience.stop();
                iterator.remove();
                continue;
            }
            boolean stillPlaying = playingAmbience.update(DisplayManager.getDeltaSeconds(), SoundMaestro.SOUND_VOLUME * this.masterVolume);
            if (stillPlaying) continue;
            AudioController controller = SoundMaestro.playAmbientSound(ambience.getKey(), idealVolume.floatValue());
            this.currentAmbience.put(ambience.getKey(), controller);
        }
    }

    private void startNewSounds(Map<Sound, Float> newAmbiences) {
        for (Map.Entry<Sound, Float> newAmbience : newAmbiences.entrySet()) {
            AudioController controller = SoundMaestro.playAmbientSound(newAmbience.getKey(), newAmbience.getValue().floatValue());
            this.currentAmbience.put(newAmbience.getKey(), controller);
        }
    }
}


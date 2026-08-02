/*
 * Decompiled with CFR 0.152.
 */
package resourceManagement;

import java.util.HashMap;
import java.util.Map;
import particles.ParticleTexture;
import textures.Texture;
import utils.FileUtils;
import utils.MyFile;

public class ParticleAtlasCache {
    private static final MyFile ATLAS_FOLDER = new MyFile(FileUtils.RES_FOLDER, "particleAtlases");
    public static final Texture TRIANGLE = Texture.newTexture(new MyFile(ATLAS_FOLDER, "triangle.png")).clampEdges().create();
    public static Map<Integer, ParticleTexture> particleTextures = new HashMap<Integer, ParticleTexture>();

    public static ParticleTexture getAtlas(int id) {
        return particleTextures.get(id);
    }

    public static void loadAll() {
        ParticleAtlasCache.loadAtlas(1, "cosmic.png", 4, true);
        ParticleAtlasCache.loadAtlas(2, "blueHeal.png", 3, true);
        ParticleAtlasCache.loadAtlas(3, "diseased2.png", 2, false);
        ParticleAtlasCache.loadAtlas(4, "poison.png", 2, false);
        ParticleAtlasCache.loadAtlas(5, "pollen.png", 4, true);
        ParticleAtlasCache.loadAtlas(6, "snow.png", 4, true);
        ParticleAtlasCache.loadAtlas(7, "leaf.png", 2, false);
        ParticleAtlasCache.loadAtlas(8, "dust.png", 3, false);
        ParticleAtlasCache.loadAtlas(9, "rock.png", 2, false);
        ParticleAtlasCache.loadAtlas(10, "swamp.png", 2, false);
        ParticleAtlasCache.loadAtlas(11, "snore.png", 2, false);
        ParticleAtlasCache.loadAtlas(12, "floaty.png", 4, true);
        ParticleAtlasCache.loadAtlas(13, "fireFly.png", 2, true);
        ParticleAtlasCache.loadAtlas(14, "dusty.png", 3, false);
        ParticleAtlasCache.loadAtlas(15, "food.png", 1, false);
        ParticleAtlasCache.loadAtlas(16, "splash.png", 2, false);
        ParticleAtlasCache.loadAtlas(17, "splash2.png", 2, false);
        ParticleAtlasCache.loadAtlas(18, "cosmicPink.png", 4, true);
        ParticleAtlasCache.loadAtlas(19, "fireFlyPink.png", 3, true);
        ParticleAtlasCache.loadAtlas(20, "blood.png", 2, false);
        ParticleAtlasCache.loadAtlas(21, "smoky.png", 3, false);
    }

    private static void loadAtlas(int id, String textureFile, int rows, boolean additive) {
        Texture texture = Texture.newTexture(new MyFile(ATLAS_FOLDER, textureFile)).clampEdges().create();
        ParticleTexture atlas = new ParticleTexture(texture, rows, additive);
        particleTextures.put(id, atlas);
    }
}


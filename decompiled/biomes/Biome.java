/*
 * Decompiled with CFR 0.152.
 */
package biomes;

import audio.Sound;
import languages.GameText;
import particleSpawns.CuboidSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import resourceManagement.ParticleAtlasCache;
import resourceManagement.SoundCache;
import toolbox.Colour;

public enum Biome {
    GRASSLAND(GameText.getText(796), new Colour(131.0f, 185.0f, 98.0f, true), Biome.createGrasslandParticleSystem(5), Biome.getSound("birds", 0.4f)),
    FOREST(GameText.getText(797), new Colour(160.0f, 176.0f, 47.0f, true), null),
    RIVER_BED(GameText.getText(798), new Colour(217.0f, 191.0f, 113.0f, true), null),
    DESERT(GameText.getText(799), new Colour(232.0f, 224.0f, 149.0f, true), null),
    SNOW(GameText.getText(800), new Colour(240.0f, 240.0f, 245.0f, true), Biome.createSnowParticleSystem(), Biome.getSound("wind", 0.3f)),
    JUNGLE(GameText.getText(801), new Colour(86.0f, 125.0f, 80.0f, true), null, Biome.getSound("jungle", 0.4f)),
    SWAMP(GameText.getText(802), new Colour(115.0f, 100.0f, 99.0f, true), Biome.createSwampParticleSystem(), Biome.getSound("swamp", 0.8f)),
    LUSH(GameText.getText(803), new Colour(203.0f, 165.0f, 181.0f, true), null),
    WOODLAND(GameText.getText(804), new Colour(116.0f, 184.0f, 119.0f, true), Biome.createFireflyParticleSystem(13), Biome.getSound("birds2", 0.7f)),
    TROPICAL(GameText.getText(805), new Colour(188.0f, 204.0f, 129.0f, true), null, Biome.getSound("tropical", 0.55f));

    private Colour colour;
    private String name;
    private ParticleSystem particleSystem;
    private Sound sound;

    private Biome(String name, Colour colour, ParticleSystem particleSystem) {
        this.colour = colour;
        this.name = name;
        this.particleSystem = particleSystem;
    }

    private Biome(String name, Colour colour, ParticleSystem particleSystem, Sound sound) {
        this.colour = colour;
        this.name = name;
        this.particleSystem = particleSystem;
        this.sound = sound;
    }

    public Colour getColour() {
        return this.colour;
    }

    public Sound getSound() {
        return this.sound;
    }

    public ParticleSystem getParticleSystem() {
        return this.particleSystem;
    }

    public String toString() {
        return this.name;
    }

    private static ParticleSystem createGrasslandParticleSystem(int id) {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(id);
        atlas.setGlowy();
        return new ParticleSystem(atlas, new CuboidSpawn(6.6666665f, 1.0f, 6.6666665f), 3.0f, 0.2f, -0.0025f, 4.0f, 0.026f);
    }

    private static ParticleSystem createFireflyParticleSystem(int id) {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(id);
        return new ParticleSystem(atlas, new CuboidSpawn(6.6666665f, 1.0f, 6.6666665f), 5.0f, 0.1f, -0.0025f, 6.0f, 0.039f);
    }

    private static ParticleSystem createSnowParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(6);
        return new ParticleSystem(atlas, new CuboidSpawn(6.6666665f, 1.0f, 6.6666665f, 2.3f), 18.0f, 0.3f, 0.03f, 3.8f, 0.07f);
    }

    private static ParticleSystem createSwampParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(10);
        return new ParticleSystem(atlas, new CuboidSpawn(6.6666665f, 1.0f, 6.6666665f, 1.0f), 0.5f, 0.1f, 0.002f, 15.0f, 5.0f);
    }

    private static Sound getSound(String name, float volume) {
        return SoundCache.CACHE.requestSound(name, true).withVolume(volume);
    }
}


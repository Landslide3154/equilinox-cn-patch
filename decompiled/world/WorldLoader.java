/*
 * Decompiled with CFR 0.152.
 */
package world;

import generation.ColourCalculator;
import generation.NormalsGenerator;
import generation.PerlinNoise;
import instances.Entity;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import session.EntityLoad;
import terrains.Terrain;
import toolbox.Colour;
import utils.BinaryReader;
import world.World;
import world.WorldConfigs;

public class WorldLoader {
    private static final int MIN_WATER = 500;
    private static final int MIN_DEEPEST = -7;
    private static final int MAX_TRIES = 10;

    public static World generateWorld(WorldConfigs configs) {
        World world = WorldLoader.generateWorld(configs, null, true);
        return world;
    }

    public static World loadWorld(BinaryReader reader, EntityLoad entities) {
        try {
            WorldConfigs configs = WorldConfigs.loadConfigs(reader);
            World world = WorldLoader.generateWorld(configs, entities, false);
            WorldLoader.addBiomes(world, entities.getStaticBatches());
            return world;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static float generateHeights(float[][] heights, PerlinNoise noise, WorldConfigs configs, boolean newGen) {
        float maxHeight = 0.0f;
        int i = 0;
        while (i < 10) {
            maxHeight = 0.0f;
            int underwaterCount = 0;
            float deepest = 0.0f;
            int z = 0;
            while (z < heights.length) {
                int x = 0;
                while (x < heights[z].length) {
                    float height;
                    heights[z][x] = height = noise.getPerlinNoise(x, z);
                    if (height < configs.getWaterHeight()) {
                        deepest = Math.min(height, deepest);
                        ++underwaterCount;
                    }
                    maxHeight = Math.max(height, maxHeight);
                    ++x;
                }
                ++z;
            }
            float factor = 8.0f / (maxHeight - configs.getWaterHeight());
            float altitude = (deepest - configs.getWaterHeight()) * 10.0f * factor;
            System.out.println("depth: " + altitude + ", count: " + underwaterCount);
            if (!newGen || underwaterCount > 500 && altitude < -7.0f) break;
            noise.randomizeSeed();
            configs.setSeed(noise.getSeed());
            System.out.println("REDOING GENERATION - NOT ENOUGH WATER!");
            ++i;
        }
        return maxHeight;
    }

    private static void addBiomes(World world, List<List<Entity>> staticBatches) {
        WorldLoader.waitUntilTerrainsLoaded(world);
        for (List<Entity> statics : staticBatches) {
            for (Entity entity : statics) {
                world.updateBiome(entity, true);
            }
        }
    }

    private static void waitUntilTerrainsLoaded(World world) {
        while (!world.terrainsLoaded()) {
            try {
                Thread.sleep(1L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static World generateWorld(WorldConfigs configs, EntityLoad entities, boolean newGen) {
        PerlinNoise noise = new PerlinNoise(configs.getSeed(), configs.getSmoothness(), (float)configs.getVertexCount() - 1.0f, configs.getWaterHeight() + 0.5f);
        float[][] heights = new float[configs.getVertexCount()][configs.getVertexCount()];
        float maxHeight = WorldLoader.generateHeights(heights, noise, configs, newGen);
        configs.setMaxHeight(maxHeight);
        Vector3f[][] normals = NormalsGenerator.generateNormals(heights);
        Terrain[][] terrains = new Terrain[5][5];
        WorldLoader.initializeTerrains(configs, terrains, heights, normals, World.BACK_COLOUR);
        return new World(configs, terrains, heights, entities);
    }

    private static void initializeTerrains(WorldConfigs configs, Terrain[][] terrains, float[][] heights, Vector3f[][] normals, Colour backColour) {
        int offset = (heights.length - 1) / terrains.length;
        int chunkLength = offset + 1;
        int i = 0;
        while (i < terrains.length) {
            int j = 0;
            while (j < terrains.length) {
                WorldLoader.createTerrain(configs, i, j, offset, chunkLength, heights, normals, backColour, terrains);
                ++j;
            }
            ++i;
        }
    }

    private static void createTerrain(WorldConfigs configs, int i, int j, int offset, int chunkLength, float[][] heights, Vector3f[][] normals, Colour backColour, Terrain[][] terrains) {
        float[][] childHeights = new float[chunkLength][chunkLength];
        Vector3f[][] childNormals = new Vector3f[chunkLength][chunkLength];
        Colour[][] backgroundColour = new Colour[chunkLength][chunkLength];
        int startX = offset * j;
        int startZ = offset * i;
        int z = 0;
        while (z < chunkLength) {
            int x = 0;
            while (x < chunkLength) {
                childHeights[z][x] = heights[startZ + z][startX + x];
                childNormals[z][x] = normals[startZ + z][startX + x];
                backgroundColour[z][x] = ColourCalculator.getColour(childHeights[z][x], configs);
                ++x;
            }
            ++z;
        }
        terrains[i][j] = Terrain.createTerrain(j, i, childHeights, childNormals, backgroundColour);
    }
}


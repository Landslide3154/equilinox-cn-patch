/*
 * Decompiled with CFR 0.152.
 */
package biomes;

import biomes.Biome;
import biomes.BiomeGrid;
import java.util.HashMap;
import java.util.Map;
import main.Camera;
import objectPools.Vec3Pool;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import particles.ParticleSystem;
import terrains.TerrainVertex;
import world.World;

public class BiomeGridSquare {
    private static final float MAJOIRTY_GAIN = 0.5f;
    private static final float MAJOIRTY_LOSE = 0.35f;
    private static final float PARTICLE_RANGE = 50.0f;
    private static final float PARTICLE_RANGE_SQUARED = 2500.0f;
    public static final float SIZE = 6.6666665f;
    private static final int SAMPLE_ROW_COUNT = 4;
    private static final int SAMPLES = 16;
    private static final float SAMPLE_STEP = 1.6666666f;
    private static final float HALF_SAMPLE_STEP = 0.8333333f;
    private final World world;
    private final BiomeGrid grid;
    private final Vector2f topLeftPosition;
    private final float averageHeight;
    private Biome majorityBiome;

    protected BiomeGridSquare(World world, BiomeGrid grid, int col, int row) {
        this.topLeftPosition = new Vector2f((float)col * 6.6666665f, (float)row * 6.6666665f);
        this.world = world;
        this.grid = grid;
        this.calculateMajorityBiome();
        this.averageHeight = this.calcAverageHeight(world);
    }

    protected Biome getMajorityBiome() {
        return this.majorityBiome;
    }

    protected void calculateMajorityBiome() {
        Map<Biome, Float> averages = this.sampleBiomesInSquare();
        boolean previouslyHadMajority = this.majorityBiome != null;
        boolean majorityGained = this.checkIfMajorityGained(averages);
        if (majorityGained && !previouslyHadMajority) {
            this.grid.addToUpdateList(this);
        }
        if (!majorityGained && this.majorityBiome != null) {
            this.checkIfMajorityLost(averages);
        }
    }

    protected void update() {
        ParticleSystem system;
        if (this.majorityBiome != null && (system = this.majorityBiome.getParticleSystem()) != null && this.inParticleRange()) {
            system.generateParticles(new Vector3f(this.topLeftPosition.x, this.averageHeight, this.topLeftPosition.y), 1.0f);
        }
    }

    private Map<Biome, Float> sampleBiomesInSquare() {
        Vector2f samplePosition = new Vector2f();
        HashMap<Biome, Float> averages = new HashMap<Biome, Float>();
        int x = 0;
        while (x < 4) {
            int z = 0;
            while (z < 4) {
                this.updateSamplePosition(x, z, samplePosition);
                TerrainVertex vertex = this.world.getTerrainVertex(samplePosition.x, samplePosition.y);
                for (Biome biome : vertex.getBiomeAmounts().keySet()) {
                    float value = vertex.getBiomeDecimal(biome);
                    this.updateBiomeAverage(averages, biome, value);
                }
                ++z;
            }
            ++x;
        }
        return averages;
    }

    private boolean inParticleRange() {
        float centerX = this.topLeftPosition.x + 3.3333333f;
        float centerZ = this.topLeftPosition.y + 3.3333333f;
        Vector3f gridCenter = new Vector3f(centerX, this.averageHeight, centerZ);
        Vector3f camPos = Camera.getCamera().getPosition();
        Vector3f temp = Vec3Pool.get();
        float dis = Vector3f.sub(gridCenter, camPos, temp).lengthSquared();
        Vec3Pool.release(temp);
        return dis < 2500.0f;
    }

    private float calcAverageHeight(World world) {
        Vector2f samplePosition = new Vector2f();
        float totalHeight = 0.0f;
        int x = 0;
        while (x < 4) {
            int z = 0;
            while (z < 4) {
                this.updateSamplePosition(x, z, samplePosition);
                float sampleHeight = world.getHeightOfTerrain(samplePosition.x, samplePosition.y);
                totalHeight += sampleHeight;
                ++z;
            }
            ++x;
        }
        return totalHeight / 16.0f;
    }

    private void updateBiomeAverage(Map<Biome, Float> averages, Biome biome, float value) {
        Float average = averages.get((Object)biome);
        if (average == null) {
            average = Float.valueOf(0.0f);
        }
        average = Float.valueOf(average.floatValue() + value / 16.0f);
        averages.put(biome, average);
    }

    private boolean checkIfMajorityGained(Map<Biome, Float> averages) {
        Biome highestBiome = null;
        float highestAverage = 0.0f;
        for (Map.Entry<Biome, Float> biomeAverage : averages.entrySet()) {
            if (!(biomeAverage.getValue().floatValue() > highestAverage)) continue;
            highestBiome = biomeAverage.getKey();
            highestAverage = biomeAverage.getValue().floatValue();
        }
        return this.checkIfMajorityGained(highestBiome, highestAverage);
    }

    private void checkIfMajorityLost(Map<Biome, Float> averages) {
        Float value = averages.get((Object)this.majorityBiome);
        if (value == null || value.floatValue() < 0.35f) {
            this.loseMajorityBiome();
        }
    }

    private boolean checkIfMajorityGained(Biome highestBiome, float highestAverage) {
        if (highestAverage > 0.5f) {
            if (this.majorityBiome != highestBiome) {
                this.changeMajorityBiome(highestBiome);
            }
            return true;
        }
        return false;
    }

    private void changeMajorityBiome(Biome newMajorityBiome) {
        this.majorityBiome = newMajorityBiome;
    }

    private void loseMajorityBiome() {
        this.majorityBiome = null;
        this.grid.removeFromUpdateList(this);
    }

    private void updateSamplePosition(int x, int z, Vector2f samplePosition) {
        samplePosition.x = this.topLeftPosition.x + 0.8333333f + (float)x * 1.6666666f;
        samplePosition.y = this.topLeftPosition.y + 0.8333333f + (float)z * 1.6666666f;
    }
}


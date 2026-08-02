/*
 * Decompiled with CFR 0.152.
 */
package world;

import java.io.IOException;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;

public class WorldConfigs {
    public static final float EDGE_ABOVE_WATER = 0.5f;
    private int seed;
    private float smoothness;
    private int vertexCount;
    private float waterHeight;
    private float maxHeight;

    private WorldConfigs(int seed, float smoothness, int vertexCount, float waterHeight) {
        this.seed = seed;
        this.smoothness = smoothness;
        this.vertexCount = vertexCount;
        this.waterHeight = waterHeight;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void setMaxHeight(float height) {
        this.maxHeight = height;
    }

    public float getMaxHeight() {
        return this.maxHeight;
    }

    public static WorldConfigs loadConfigs(BinaryReader reader) throws Exception {
        int seed = reader.readInt();
        float smoothness = reader.readFloat();
        int vertexCount = reader.readInt();
        float waterHeight = reader.readFloat();
        return new WorldConfigs(seed, smoothness, vertexCount, waterHeight);
    }

    public static WorldConfigs createDefault() {
        return new WorldConfigs(Maths.RANDOM.nextInt(1000000), 1.0f, 136, -2.0f);
    }

    public static WorldConfigs create(float smoothness, float waterHeight) {
        return new WorldConfigs(Maths.RANDOM.nextInt(1000000), smoothness, 136, waterHeight);
    }

    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.seed);
        writer.writeFloat(this.smoothness);
        writer.writeInt(this.vertexCount);
        writer.writeFloat(this.waterHeight);
    }

    public float getWaterHeight() {
        return this.waterHeight;
    }

    public int getSeed() {
        return this.seed;
    }

    public float getSmoothness() {
        return this.smoothness;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }
}


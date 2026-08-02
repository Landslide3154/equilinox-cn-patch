/*
 * Decompiled with CFR 0.152.
 */
package particleSpawns;

import org.lwjgl.util.vector.Vector4f;
import particleSpawns.ParticleSpawn;
import toolbox.Maths;

public class CuboidSpawn
implements ParticleSpawn {
    private final float xScale;
    private final float yScale;
    private final float zScale;
    private final float yOffset;

    public CuboidSpawn(float xScale, float yScale, float zScale) {
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
        this.yOffset = 0.0f;
    }

    public CuboidSpawn(float xScale, float yScale, float zScale, float yOffset) {
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
        this.yOffset = yOffset;
    }

    @Override
    public Vector4f getBaseSpawnPosition() {
        float xOffset = Maths.RANDOM.nextFloat() * this.xScale;
        float yOffset = Maths.RANDOM.nextFloat() * this.yScale + this.yOffset;
        float zOffset = Maths.RANDOM.nextFloat() * this.zScale;
        return new Vector4f(xOffset, yOffset, zOffset, 1.0f);
    }
}


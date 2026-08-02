/*
 * Decompiled with CFR 0.152.
 */
package water;

import java.nio.ByteBuffer;
import terrains.HeightFinder;

public class WaterVertex {
    private final float xPos;
    private final float zPos;
    private final byte[] indicators;

    public WaterVertex(float xPos, float zPos, byte[] indicators) {
        this.xPos = xPos;
        this.zPos = zPos;
        this.indicators = indicators;
    }

    public boolean isUnderground(HeightFinder heightFinder, float waterHeight) {
        float terrainHeight = heightFinder.getHeight(this.xPos, this.zPos);
        return terrainHeight > waterHeight + 0.06f;
    }

    public void packData(ByteBuffer buffer) {
        buffer.putFloat(this.xPos);
        buffer.putFloat(this.zPos);
        buffer.put(this.indicators);
    }
}


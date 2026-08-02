/*
 * Decompiled with CFR 0.152.
 */
package blueprints;

import picking.AABB;

public class SubBlueprint {
    private float[] data;
    private AABB boundingBox;
    private AABB[] extraAabbs;
    private float increaseFactor;
    private float minGrowth;
    private float maxGrowth;

    public SubBlueprint(float[] data, AABB aabb, AABB[] aabbs, float increaseFactor) {
        this.boundingBox = aabb;
        this.data = data;
        this.extraAabbs = aabbs;
        this.increaseFactor = increaseFactor;
    }

    public AABB[] getExtraAabbs() {
        return this.extraAabbs;
    }

    public void calculateGrowths(boolean isFirst, SubBlueprint nextStage) {
        this.calculateMaxGrowth(nextStage);
        this.calculateMinGrowth(isFirst);
    }

    public float getIncreaseFactor() {
        return this.increaseFactor;
    }

    public float getMinGrowth() {
        return this.minGrowth;
    }

    public float getMaxGrowth() {
        return this.maxGrowth;
    }

    public float[] getFullModelData() {
        return this.data;
    }

    public float[] getUniqueStageData() {
        return this.data;
    }

    public int getDataLength() {
        return this.data.length;
    }

    public int getVertexCount() {
        return this.getDataLength() / 10;
    }

    public AABB getAABB() {
        return this.boundingBox;
    }

    private void calculateMaxGrowth(SubBlueprint nextStage) {
        if (nextStage == null) {
            float half = (this.increaseFactor - 1.0f) / 2.0f;
            float part = (1.0f + half) / this.increaseFactor;
            this.maxGrowth = 2.0f - part;
        } else {
            float half = (nextStage.increaseFactor - 1.0f) / 2.0f;
            this.maxGrowth = 1.0f + half;
        }
    }

    private void calculateMinGrowth(boolean isFirst) {
        if (isFirst) {
            float half = this.maxGrowth - 1.0f;
            this.minGrowth = 1.0f - half;
        } else {
            float half = (this.increaseFactor - 1.0f) / 2.0f;
            this.minGrowth = (1.0f + half) / this.increaseFactor;
        }
    }
}


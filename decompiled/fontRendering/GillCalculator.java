/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

import fontRendering.FontVariablesCalculator;

public class GillCalculator
implements FontVariablesCalculator {
    private static final float[] subOneAntialiasValues = new float[]{0.5f, 0.47f, 0.36f, 0.21f, 0.17f, 0.14f, 0.1297f, 0.126f, 0.0894f, 0.102f, 0.098f};
    private static final float[] subOneEdgeValues = new float[]{0.35f, 0.36f, 0.38f, 0.42f, 0.43f, 0.444f, 0.448f, 0.443f, 0.465f, 0.451f, 0.452f};

    @Override
    public float calculateAntialiasValue(float size) {
        if (size >= 1.0f) {
            size = (size - 1.0f) / (1.0f + size / 4.0f) + 1.0f;
            return 0.1f / size;
        }
        return this.lookupInterpolatedValue(subOneAntialiasValues, size);
    }

    @Override
    public float calculateEdgeValue(float size) {
        if (size >= 1.0f) {
            return 0.0033333334f * size + 0.45666668f;
        }
        return this.lookupInterpolatedValue(subOneEdgeValues, size);
    }

    private float lookupInterpolatedValue(float[] data, float size) {
        float value = size / 0.1f;
        int firstIndex = (int)value;
        float progress = value - (float)firstIndex;
        float lowerValue = data[firstIndex];
        float higherValue = data[firstIndex + 1];
        return this.linearInterpolate(lowerValue, higherValue, progress);
    }

    private float linearInterpolate(float lower, float higher, float progress) {
        float dif = higher - lower;
        return lower + dif * progress;
    }
}


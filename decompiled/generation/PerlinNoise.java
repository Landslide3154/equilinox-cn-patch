/*
 * Decompiled with CFR 0.152.
 */
package generation;

import java.util.Random;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Maths;

public class PerlinNoise {
    private static final float ROUGHNESS = 0.43f;
    private static final float OCTAVES = 5.0f;
    public static final float AMPLITUDE = 15.0f;
    private final float smoothness;
    private final float max;
    private final float halfMaxSquared;
    private final Vector2f center;
    private float edgeHeight = -1.0f;
    private static final float EDGE = 0.98f;
    private static final float TRANSITION = 0.5f;
    private boolean roundWorld = false;
    private int seed;

    public PerlinNoise(int seed, float smoothness, float size, float edgeHeight) {
        this.seed = seed;
        this.edgeHeight = edgeHeight;
        this.smoothness = smoothness;
        this.max = size;
        this.halfMaxSquared = this.max * 0.5f * (this.max * 0.5f);
        this.center = new Vector2f(this.max / 2.0f, this.max / 2.0f);
    }

    public PerlinNoise(float smoothness, float size, float edgeHeight) {
        this.seed = Maths.RANDOM.nextInt(1000000000);
        this.smoothness = smoothness;
        this.edgeHeight = edgeHeight;
        this.max = size;
        this.halfMaxSquared = this.max * 0.5f * (this.max * 0.5f);
        this.center = new Vector2f(this.max / 2.0f, this.max / 2.0f);
    }

    public void randomizeSeed() {
        this.seed = Maths.RANDOM.nextInt(1000000000);
    }

    public int getSeed() {
        return this.seed;
    }

    public float getPerlinNoise(float x, float y) {
        float total = 0.0f;
        float d = (float)Math.pow(2.0, 4.0);
        int i = 0;
        while ((float)i < 5.0f) {
            float freq = (float)(Math.pow(2.0, i) / (double)d);
            float amp = (float)Math.pow(0.43f, i) * 15.0f;
            total += this.getInterpolatedNoise(x * this.smoothness * freq, y * this.smoothness * freq) * amp;
            ++i;
        }
        float edgeFactor = this.roundWorld ? this.getEdgeFactor(x, y) : this.getEdgeFactorSquare(x, y);
        return total * edgeFactor + (1.0f - edgeFactor) * this.edgeHeight;
    }

    private float getSmoothNoise(int x, int y) {
        float corners = (this.getNoise(x - 1, y - 1) + this.getNoise(x + 1, y - 1) + this.getNoise(x - 1, y + 1) + this.getNoise(x + 1, y + 1)) / 16.0f;
        float sides = (this.getNoise(x - 1, y) + this.getNoise(x + 1, y) + this.getNoise(x, y - 1) + this.getNoise(x, y + 1)) / 8.0f;
        float center = this.getNoise(x, y) / 4.0f;
        return corners + sides + center;
    }

    private float getNoise(int x, int y) {
        return new Random(x * 49632 + y * 325176 + this.seed).nextFloat() * 2.0f - 1.0f;
    }

    private float getInterpolatedNoise(float x, float y) {
        int intX = (int)x;
        float fracX = x - (float)intX;
        int intY = (int)y;
        float fracY = y - (float)intY;
        float v1 = this.getSmoothNoise(intX, intY);
        float v2 = this.getSmoothNoise(intX + 1, intY);
        float v3 = this.getSmoothNoise(intX, intY + 1);
        float v4 = this.getSmoothNoise(intX + 1, intY + 1);
        float i1 = this.interpolate(v1, v2, fracX);
        float i2 = this.interpolate(v3, v4, fracX);
        return this.interpolate(i1, i2, fracY);
    }

    private float interpolate(float a, float b, float blend) {
        double theta = (double)blend * Math.PI;
        float f = (float)((1.0 - Math.cos(theta)) * 0.5);
        return a * (1.0f - f) + b * f;
    }

    private float getEdgeFactor(float x, float y) {
        Vector2f vector = new Vector2f(x, y);
        float disSquared = Vector2f.sub(vector, this.center, vector).lengthSquared();
        return 1.0f - Maths.smoothStep(0.48000002f, 0.98f, disSquared / this.halfMaxSquared);
    }

    private float getEdgeFactorSquare(float x, float y) {
        float nearEdge = this.max * 0.1f;
        float farEdge = this.max - nearEdge;
        float xFactorNear = Maths.clamp(x / nearEdge, 0.0f, 1.0f);
        float xFactor = Maths.clamp(1.0f - (x - farEdge) / nearEdge, 0.0f, xFactorNear);
        float yFactorNear = Maths.clamp(y / nearEdge, 0.0f, xFactor);
        return Maths.clamp(1.0f - (y - farEdge) / nearEdge, 0.0f, yFactorNear);
    }
}


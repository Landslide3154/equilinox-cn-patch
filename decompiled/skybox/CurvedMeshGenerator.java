/*
 * Decompiled with CFR 0.152.
 */
package skybox;

import basics.DisplayManager;
import basics.Loader;
import org.lwjgl.util.vector.Vector2f;

public class CurvedMeshGenerator {
    private final int segmentCount;
    private final float segmentTheta;
    private final float factor;

    public CurvedMeshGenerator(int segmentCount, float totalAngle) {
        this.segmentCount = segmentCount;
        this.segmentTheta = totalAngle / (float)segmentCount;
        this.factor = 1.0f / totalAngle;
    }

    public int generateMeshVao() {
        Vector2f[] xzPositions = this.getVertexPositions();
        float[] vertices = new float[xzPositions.length * 2 * 3];
        int pointer = 0;
        Vector2f[] vector2fArray = xzPositions;
        int n = xzPositions.length;
        int n2 = 0;
        while (n2 < n) {
            Vector2f pos = vector2fArray[n2];
            pointer = this.storeVertex(pos, 1.0f, vertices, pointer);
            pointer = this.storeVertex(pos, -1.0f, vertices, pointer);
            ++n2;
        }
        return Loader.createInterleavedVAO(xzPositions.length * 2, new float[][]{vertices});
    }

    private int storeVertex(Vector2f pos, float height, float[] vertices, int pointer) {
        vertices[pointer++] = pos.x * this.factor;
        vertices[pointer++] = height;
        vertices[pointer++] = pos.y * this.factor;
        return pointer;
    }

    private Vector2f[] getVertexPositions() {
        float startPoint = (float)this.segmentCount / 2.0f;
        float startingTheta = -startPoint * this.segmentTheta;
        Vector2f[] points = new Vector2f[this.segmentCount + 1];
        int i = 0;
        while (i < points.length) {
            points[i] = this.pointOnCircle(startingTheta + this.segmentTheta * (float)i);
            ++i;
        }
        return points;
    }

    private Vector2f pointOnCircle(float theta) {
        float x = (float)Math.sin(theta);
        float z = (float)(-Math.cos(theta)) + 1.0f;
        float extra = Math.max(1.0f, DisplayManager.getAspectRatio() / 1.8f);
        return new Vector2f(x * extra, z);
    }
}


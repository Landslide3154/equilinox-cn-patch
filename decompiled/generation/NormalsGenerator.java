/*
 * Decompiled with CFR 0.152.
 */
package generation;

import org.lwjgl.util.vector.Vector3f;

public class NormalsGenerator {
    public static Vector3f[][] generateNormals(float[][] heights) {
        Vector3f[][] normals = new Vector3f[heights.length][heights.length];
        int z = 0;
        while (z < normals.length) {
            int x = 0;
            while (x < normals[z].length) {
                normals[z][x] = NormalsGenerator.calculateNormal(x, z, heights);
                ++x;
            }
            ++z;
        }
        return normals;
    }

    private static Vector3f calculateNormal(int x, int z, float[][] heights) {
        float heightL = NormalsGenerator.getHeight(x - 1, z, heights);
        float heightR = NormalsGenerator.getHeight(x + 1, z, heights);
        float heightD = NormalsGenerator.getHeight(x, z - 1, heights);
        float heightU = NormalsGenerator.getHeight(x, z + 1, heights);
        Vector3f normal = new Vector3f(heightL - heightR, 2.0f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    private static float getHeight(int x, int z, float[][] heights) {
        x = x < 0 ? 0 : x;
        z = z < 0 ? 0 : z;
        x = x >= heights.length ? heights.length - 1 : x;
        z = z >= heights.length ? heights.length - 1 : z;
        return heights[z][x];
    }
}


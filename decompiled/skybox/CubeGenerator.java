/*
 * Decompiled with CFR 0.152.
 */
package skybox;

import basics.Loader;

public class CubeGenerator {
    private static final int VERTEX_COUNT = 8;
    private static final int[] INDICES;

    static {
        int[] nArray = new int[36];
        nArray[1] = 1;
        nArray[2] = 3;
        nArray[3] = 1;
        nArray[4] = 2;
        nArray[5] = 3;
        nArray[6] = 1;
        nArray[7] = 5;
        nArray[8] = 2;
        nArray[9] = 2;
        nArray[10] = 5;
        nArray[11] = 6;
        nArray[12] = 4;
        nArray[13] = 7;
        nArray[14] = 5;
        nArray[15] = 5;
        nArray[16] = 7;
        nArray[17] = 6;
        nArray[19] = 3;
        nArray[20] = 4;
        nArray[21] = 4;
        nArray[22] = 3;
        nArray[23] = 7;
        nArray[24] = 7;
        nArray[25] = 3;
        nArray[26] = 6;
        nArray[27] = 6;
        nArray[28] = 3;
        nArray[29] = 2;
        nArray[30] = 4;
        nArray[31] = 5;
        nArray[34] = 5;
        nArray[35] = 1;
        INDICES = nArray;
    }

    public static int generateCube(float size) {
        int vao = Loader.createInterleavedVAO(CubeGenerator.getVertexPositions(size), INDICES, new int[]{3});
        return vao;
    }

    private static float[] getVertexPositions(float size) {
        return new float[]{-size, size, size, size, size, size, size, -size, size, -size, -size, size, -size, size, -size, size, size, -size, size, -size, -size, -size, -size, -size};
    }
}


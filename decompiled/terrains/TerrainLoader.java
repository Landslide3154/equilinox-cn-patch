/*
 * Decompiled with CFR 0.152.
 */
package terrains;

import basics.Loader;
import generation.GridGenerator;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;

public class TerrainLoader {
    protected static void loadTerrain(Terrain terrain, int gridX, int gridZ, float[][] heights, Vector3f[][] normals) {
        int vao = Loader.createVAO();
        int[] indices = GridGenerator.generateGridIndexBuffer(28, (gridX + gridZ) % 2 == 0);
        Loader.createIndicesVBO(vao, indices);
        TerrainLoader.loadPositionNormalBuffer(vao, heights, normals, gridX, gridZ);
        int colourVbo = Loader.createEmptyInterleavedVBO(vao, 784, 2, 3, 4);
        terrain.setVboData(vao, colourVbo, indices.length);
    }

    private static void loadPositionNormalBuffer(int vao, float[][] heights, Vector3f[][] normalsGrid, int gridX, int gridZ) {
        float[] positions = TerrainLoader.generatePositionData(heights, gridX, gridZ);
        float[] normals = TerrainLoader.generateNormalData(normalsGrid);
        float[] interleavedData = Loader.interleaveFloatData(positions.length / 3, positions, normals);
        Loader.storeInterleavedDataInVAO(vao, interleavedData, 3, 3);
    }

    private static float[] generatePositionData(float[][] heights, int gridX, int gridZ) {
        float[] positions = new float[heights.length * heights.length * 3];
        float squareSize = 0.7407407f;
        float startX = 20.0f * (float)gridX;
        float startZ = 20.0f * (float)gridZ;
        int pointer = 0;
        int z = 0;
        while (z < heights.length) {
            int x = 0;
            while (x < heights.length) {
                positions[pointer++] = startX + (float)x * squareSize;
                positions[pointer++] = heights[z][x];
                positions[pointer++] = startZ + (float)z * squareSize;
                ++x;
            }
            ++z;
        }
        return positions;
    }

    private static float[] generateNormalData(Vector3f[][] normalsGrid) {
        float[] normals = new float[normalsGrid.length * normalsGrid.length * 3];
        int pointer = 0;
        int z = 0;
        while (z < normalsGrid.length) {
            int x = 0;
            while (x < normalsGrid.length) {
                Vector3f normal = normalsGrid[z][x];
                normals[pointer++] = normal.x;
                normals[pointer++] = normal.y;
                normals[pointer++] = normal.z;
                ++x;
            }
            ++z;
        }
        return normals;
    }
}


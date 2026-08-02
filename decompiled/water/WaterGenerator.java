/*
 * Decompiled with CFR 0.152.
 */
package water;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.util.vector.Vector2f;
import terrains.HeightFinder;
import toolbox.Maths;

public class WaterGenerator {
    private static final int VERTICES_PER_SQUARE = 6;
    public static final int VERTEX_SIZE_BYTES = 12;
    private static final float PAD = 0.09f;

    public static byte[] generate(int gridCount, HeightFinder heightFinder, float waterHeight) {
        int totalVertexCount = gridCount * gridCount * 6;
        return WaterGenerator.createMeshData(gridCount, totalVertexCount, heightFinder, waterHeight);
    }

    private static byte[] createMeshData(int gridCount, int totalVertexCount, HeightFinder heightFinder, float waterHeight) {
        int byteSize = 12 * totalVertexCount;
        ByteBuffer buffer = ByteBuffer.allocate(byteSize).order(ByteOrder.nativeOrder());
        int row = 0;
        while (row < gridCount) {
            int col = 0;
            while (col < gridCount) {
                WaterGenerator.storeGridSquare(col, row, buffer, heightFinder, waterHeight);
                ++col;
            }
            ++row;
        }
        return WaterGenerator.getDataFromBuffer(buffer);
    }

    private static byte[] getDataFromBuffer(ByteBuffer buffer) {
        buffer.flip();
        byte[] array = new byte[buffer.limit()];
        int i = 0;
        while (i < array.length) {
            array[i] = buffer.get();
            ++i;
        }
        return array;
    }

    private static void storeGridSquare(int col, int row, ByteBuffer buffer, HeightFinder heightFinder, float waterHeight) {
        Vector2f[] cornerPos = WaterGenerator.calculateCornerPositions(col, row);
        boolean even = (col + row) % 2 == 0;
        WaterGenerator.storeTriangle(cornerPos, buffer, true, even, heightFinder, waterHeight);
        WaterGenerator.storeTriangle(cornerPos, buffer, false, even, heightFinder, waterHeight);
    }

    private static void storeTriangle(Vector2f[] cornerPos, ByteBuffer buffer, boolean left, boolean normalSquare, HeightFinder heightFinder, float waterHeight) {
        int index2;
        int index0 = left ? 0 : 2;
        int index1 = !normalSquare && !left ? 0 : 1;
        int n = index2 = normalSquare && left ? 2 : 3;
        if (!WaterGenerator.isUnderGround(cornerPos[index0], cornerPos[index1], cornerPos[index2], heightFinder, waterHeight)) {
            WaterGenerator.storeVertex(cornerPos[index0], WaterGenerator.getIndicators(index0, cornerPos, index1, index2), buffer);
            WaterGenerator.storeVertex(cornerPos[index1], WaterGenerator.getIndicators(index1, cornerPos, index2, index0), buffer);
            WaterGenerator.storeVertex(cornerPos[index2], WaterGenerator.getIndicators(index2, cornerPos, index0, index1), buffer);
        }
    }

    private static boolean isUnderGround(Vector2f vert1, Vector2f vert2, Vector2f vert3, HeightFinder heightFinder, float waterHeight) {
        return WaterGenerator.isVertexUnderGround(vert1, heightFinder, waterHeight) && WaterGenerator.isVertexUnderGround(vert2, heightFinder, waterHeight) && WaterGenerator.isVertexUnderGround(vert3, heightFinder, waterHeight);
    }

    private static boolean isVertexUnderGround(Vector2f point, HeightFinder heightFinder, float waterHeight) {
        float terrainHeight = heightFinder.getHeight(point.x, point.y);
        return terrainHeight > waterHeight + 0.06f;
    }

    private static void storeVertex(Vector2f position, byte[] indicators, ByteBuffer buffer) {
        float posX = Maths.clamp(position.x, 0.09f, 99.91f);
        float posZ = Maths.clamp(position.y, 0.09f, 99.91f);
        buffer.putFloat(posX);
        buffer.putFloat(posZ);
        buffer.put(indicators);
    }

    private static Vector2f[] calculateCornerPositions(int col, int row) {
        Vector2f[] vertices = new Vector2f[]{new Vector2f(col, row), new Vector2f(col, row + 1), new Vector2f(col + 1, row), new Vector2f(col + 1, row + 1)};
        return vertices;
    }

    private static byte[] getIndicators(int currentVertex, Vector2f[] vertexPositions, int vertex1, int vertex2) {
        Vector2f currentVertexPos = vertexPositions[currentVertex];
        Vector2f vertex1Pos = vertexPositions[vertex1];
        Vector2f vertex2Pos = vertexPositions[vertex2];
        Vector2f offset1 = Vector2f.sub(vertex1Pos, currentVertexPos, null);
        Vector2f offset2 = Vector2f.sub(vertex2Pos, currentVertexPos, null);
        return new byte[]{(byte)offset1.x, (byte)offset1.y, (byte)offset2.x, (byte)offset2.y};
    }
}


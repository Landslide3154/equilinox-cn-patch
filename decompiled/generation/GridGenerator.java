/*
 * Decompiled with CFR 0.152.
 */
package generation;

public class GridGenerator {
    public static int[] generateGridIndexBuffer(int vertexCount, boolean evenTile) {
        int testBit = vertexCount % 2 == 0 && !evenTile ? 1 : 0;
        int indexCount = (vertexCount - 1) * (vertexCount - 1) * 6;
        int[] indices = new int[indexCount];
        int pointer = 0;
        int col = 0;
        while (col < vertexCount - 1) {
            int row = 0;
            while (row < vertexCount - 1) {
                int topLeft = row * vertexCount + col;
                int topRight = topLeft + 1;
                int bottomLeft = (row + 1) * vertexCount + col;
                int bottomRight = bottomLeft + 1;
                pointer = row % 2 == 0 ? GridGenerator.storeQuad1(indices, pointer, topLeft, topRight, bottomLeft, bottomRight, col % 2 == testBit) : GridGenerator.storeQuad2(indices, pointer, topLeft, topRight, bottomLeft, bottomRight, col % 2 == testBit);
                ++row;
            }
            ++col;
        }
        return indices;
    }

    private static int storeQuad1(int[] indices, int pointer, int topLeft, int topRight, int bottomLeft, int bottomRight, boolean mixed) {
        indices[pointer++] = topLeft;
        indices[pointer++] = bottomLeft;
        indices[pointer++] = mixed ? topRight : bottomRight;
        indices[pointer++] = bottomRight;
        indices[pointer++] = topRight;
        indices[pointer++] = mixed ? bottomLeft : topLeft;
        return pointer;
    }

    private static int storeQuad2(int[] indices, int pointer, int topLeft, int topRight, int bottomLeft, int bottomRight, boolean mixed) {
        indices[pointer++] = topRight;
        indices[pointer++] = topLeft;
        indices[pointer++] = mixed ? bottomRight : bottomLeft;
        indices[pointer++] = bottomLeft;
        indices[pointer++] = bottomRight;
        indices[pointer++] = mixed ? topLeft : topRight;
        return pointer;
    }
}


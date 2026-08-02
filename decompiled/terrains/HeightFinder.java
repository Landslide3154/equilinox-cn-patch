/*
 * Decompiled with CFR 0.152.
 */
package terrains;

import objectPools.Vec2Pool;
import objectPools.Vec3Pool;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class HeightFinder {
    private float gridSquareSize;
    private float[][] heights;
    private final float voidHeight;
    private VertexQuad quad = new VertexQuad();
    private final VertexTriangle vertexTriangle = new VertexTriangle();

    public HeightFinder(float[][] heights, float gridSize, float voidHeight) {
        this.heights = heights;
        this.voidHeight = voidHeight;
        this.gridSquareSize = gridSize / (float)(heights.length - 1);
    }

    public synchronized float getHeight(float x, float z) {
        VertexQuad quad = this.calculateVertexQuad(x, z);
        if (quad == null) {
            return this.voidHeight;
        }
        Vector2f gridCoords = this.calculateQuadCoords(x, z, Vec2Pool.get());
        VertexTriangle triangle = quad.getTriangle(gridCoords.x, gridCoords.y, this.vertexTriangle);
        float height = Maths.barryCentric(triangle.p0, triangle.p1, triangle.p2, gridCoords);
        Vec2Pool.release(gridCoords);
        return height;
    }

    public synchronized Vector3f getNormal(float x, float z) {
        VertexQuad quad = this.calculateVertexQuad(x, z);
        if (quad == null) {
            return new Vector3f(0.0f, 1.0f, 0.0f);
        }
        Vector2f gridCoords = this.calculateQuadCoords(x, z, Vec2Pool.get());
        VertexTriangle triangle = quad.getTriangle(gridCoords.x, gridCoords.y, this.vertexTriangle);
        Vec2Pool.release(gridCoords);
        try {
            return triangle.getNormal();
        }
        catch (Exception e) {
            System.err.println("COULDN'T CALC NORMAL");
            return new Vector3f(0.0f, 1.0f, 0.0f);
        }
    }

    private synchronized VertexQuad calculateVertexQuad(float x, float z) {
        int gridX = (int)(x / this.gridSquareSize);
        int gridZ = (int)(z / this.gridSquareSize);
        if (gridX >= this.heights.length - 1 || gridZ >= this.heights.length - 1 || gridX < 0 || gridZ < 0) {
            return null;
        }
        return this.quad.set(gridX, gridZ);
    }

    private synchronized Vector2f calculateQuadCoords(float x, float z, Vector2f vec) {
        float xCoord = x % this.gridSquareSize / this.gridSquareSize;
        float zCoord = z % this.gridSquareSize / this.gridSquareSize;
        vec.set(xCoord, zCoord);
        return vec;
    }

    private class VertexQuad {
        private final Vector3f topLeft = Vec3Pool.get();
        private final Vector3f topRight = Vec3Pool.get();
        private final Vector3f bottomLeft = Vec3Pool.get();
        private final Vector3f bottomRight = Vec3Pool.get();
        private boolean rightHanded;

        private VertexQuad() {
        }

        public VertexQuad set(int gridX, int gridZ) {
            this.topLeft.set(0.0f, HeightFinder.this.heights[gridZ][gridX], 0.0f);
            this.topRight.set(1.0f, HeightFinder.this.heights[gridZ][gridX + 1], 0.0f);
            this.bottomLeft.set(0.0f, HeightFinder.this.heights[gridZ + 1][gridX], 1.0f);
            this.bottomRight.set(1.0f, HeightFinder.this.heights[gridZ + 1][gridX + 1], 1.0f);
            this.rightHanded = (gridX + gridZ) % 2 != 0;
            return this;
        }

        private VertexTriangle getTriangle(float xCoord, float zCoord, VertexTriangle vertexTriangle) {
            if (this.rightHanded) {
                if (xCoord > zCoord) {
                    return vertexTriangle.set(this.topLeft, this.bottomRight, this.topRight);
                }
                return vertexTriangle.set(this.topLeft, this.bottomLeft, this.bottomRight);
            }
            if (xCoord < 1.0f - zCoord) {
                return vertexTriangle.set(this.topLeft, this.bottomLeft, this.topRight);
            }
            return vertexTriangle.set(this.topRight, this.bottomLeft, this.bottomRight);
        }
    }

    private class VertexTriangle {
        private Vector3f p0;
        private Vector3f p1;
        private Vector3f p2;

        private VertexTriangle() {
        }

        private VertexTriangle set(Vector3f p0, Vector3f p1, Vector3f p2) {
            this.p0 = p0;
            this.p1 = p1;
            this.p2 = p2;
            return this;
        }

        public Vector3f getNormal() {
            return Maths.calculateNormal(this.p0, this.p1, this.p2);
        }
    }
}


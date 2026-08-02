/*
 * Decompiled with CFR 0.152.
 */
package water;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import terrains.HeightFinder;
import toolbox.Maths;
import water.Water;

public class WaterMeshGenerator {
    public static float[] generateVertices(HeightFinder heightFinder, Water water) {
        ArrayList<Float> vertices = new ArrayList<Float>();
        int col = 0;
        while (col < water.pointCount - 1) {
            int row = 0;
            while (row < water.pointCount - 1) {
                WaterMeshGenerator.generateWaterQuad(col, row, water, vertices);
                ++row;
            }
            ++col;
        }
        List<Float> overgroundVertices = WaterMeshGenerator.removeUndergroundVertices(vertices, heightFinder, water.height);
        return Maths.floatListToArray(overgroundVertices);
    }

    private static void generateWaterQuad(int col, int row, Water water, List<Float> vertices) {
        int topLeft = row * water.pointCount + col;
        int topRight = topLeft + 1;
        int bottomLeft = (row + 1) * water.pointCount + col;
        int bottomRight = bottomLeft + 1;
        if (row % 2 == 0) {
            WaterMeshGenerator.storeQuad1(vertices, topLeft, topRight, bottomLeft, bottomRight, col % 2 == 0, water);
        } else {
            WaterMeshGenerator.storeQuad2(vertices, topLeft, topRight, bottomLeft, bottomRight, col % 2 == 0, water);
        }
    }

    private static void storeQuad1(List<Float> vertices, int topLeft, int topRight, int bottomLeft, int bottomRight, boolean mixed, Water water) {
        WaterMeshGenerator.storeVertex(vertices, topLeft, new Vector2f(0.0f, 1.0f), mixed ? new Vector2f(1.0f, 0.0f) : new Vector2f(1.0f, 1.0f), water);
        WaterMeshGenerator.storeVertex(vertices, bottomLeft, mixed ? new Vector2f(1.0f, -1.0f) : new Vector2f(1.0f, 0.0f), new Vector2f(0.0f, -1.0f), water);
        if (mixed) {
            WaterMeshGenerator.storeVertex(vertices, topRight, new Vector2f(-1.0f, 0.0f), new Vector2f(-1.0f, 1.0f), water);
        } else {
            WaterMeshGenerator.storeVertex(vertices, bottomRight, new Vector2f(-1.0f, -1.0f), new Vector2f(-1.0f, 0.0f), water);
        }
        WaterMeshGenerator.storeVertex(vertices, bottomRight, new Vector2f(0.0f, -1.0f), mixed ? new Vector2f(-1.0f, 0.0f) : new Vector2f(-1.0f, -1.0f), water);
        WaterMeshGenerator.storeVertex(vertices, topRight, mixed ? new Vector2f(-1.0f, 1.0f) : new Vector2f(-1.0f, 0.0f), new Vector2f(0.0f, 1.0f), water);
        if (mixed) {
            WaterMeshGenerator.storeVertex(vertices, bottomLeft, new Vector2f(1.0f, 0.0f), new Vector2f(1.0f, -1.0f), water);
        } else {
            WaterMeshGenerator.storeVertex(vertices, topLeft, new Vector2f(1.0f, 1.0f), new Vector2f(1.0f, 0.0f), water);
        }
    }

    private static void storeQuad2(List<Float> vertices, int topLeft, int topRight, int bottomLeft, int bottomRight, boolean mixed, Water water) {
        WaterMeshGenerator.storeVertex(vertices, topRight, new Vector2f(-1.0f, 0.0f), mixed ? new Vector2f(0.0f, 1.0f) : new Vector2f(-1.0f, 1.0f), water);
        WaterMeshGenerator.storeVertex(vertices, topLeft, mixed ? new Vector2f(1.0f, 1.0f) : new Vector2f(0.0f, 1.0f), new Vector2f(1.0f, 0.0f), water);
        if (mixed) {
            WaterMeshGenerator.storeVertex(vertices, bottomRight, new Vector2f(0.0f, -1.0f), new Vector2f(-1.0f, -1.0f), water);
        } else {
            WaterMeshGenerator.storeVertex(vertices, bottomLeft, new Vector2f(1.0f, -1.0f), new Vector2f(0.0f, -1.0f), water);
        }
        WaterMeshGenerator.storeVertex(vertices, bottomLeft, new Vector2f(1.0f, 0.0f), mixed ? new Vector2f(0.0f, -1.0f) : new Vector2f(1.0f, -1.0f), water);
        WaterMeshGenerator.storeVertex(vertices, bottomRight, mixed ? new Vector2f(-1.0f, -1.0f) : new Vector2f(0.0f, -1.0f), new Vector2f(-1.0f, 0.0f), water);
        if (mixed) {
            WaterMeshGenerator.storeVertex(vertices, topLeft, new Vector2f(0.0f, 1.0f), new Vector2f(1.0f, 1.0f), water);
        } else {
            WaterMeshGenerator.storeVertex(vertices, topRight, new Vector2f(-1.0f, 1.0f), new Vector2f(0.0f, 1.0f), water);
        }
    }

    private static void storeVertex(List<Float> vertices, int index, Vector2f otherPoint1, Vector2f otherPoint2, Water water) {
        int gridX = index % water.pointCount;
        int gridZ = index / water.pointCount;
        float x = (float)gridX * 1.0f;
        float z = (float)gridZ * 1.0f;
        vertices.add(Float.valueOf(x));
        vertices.add(Float.valueOf(z));
        vertices.add(Float.valueOf(WaterMeshGenerator.encode(otherPoint1.x, otherPoint1.y, otherPoint2.x, otherPoint2.y)));
    }

    private static float encode(float x, float z, float x2, float z2) {
        float p3 = (x + 1.0f) * 27.0f;
        float p2 = (z + 1.0f) * 9.0f;
        float p1 = (x2 + 1.0f) * 3.0f;
        float p0 = (z2 + 1.0f) * 1.0f;
        return p0 + p1 + p2 + p3;
    }

    private static List<Float> removeUndergroundVertices(List<Float> vertices, HeightFinder heightFinder, float waterHeight) {
        ArrayList<Float> overgroundVertices = new ArrayList<Float>();
        int i = 0;
        while (i < vertices.size()) {
            boolean triangleUnder;
            Vector3f point1 = WaterMeshGenerator.extractVertex(vertices, i);
            Vector3f point2 = WaterMeshGenerator.extractVertex(vertices, i + 3);
            Vector3f point3 = WaterMeshGenerator.extractVertex(vertices, i + 6);
            boolean bl = triangleUnder = WaterMeshGenerator.isUnderGround(point1, heightFinder, waterHeight) && WaterMeshGenerator.isUnderGround(point2, heightFinder, waterHeight) && WaterMeshGenerator.isUnderGround(point3, heightFinder, waterHeight);
            if (!triangleUnder) {
                WaterMeshGenerator.addVertexToList(overgroundVertices, point1);
                WaterMeshGenerator.addVertexToList(overgroundVertices, point2);
                WaterMeshGenerator.addVertexToList(overgroundVertices, point3);
            }
            i += 9;
        }
        return overgroundVertices;
    }

    private static void addVertexToList(List<Float> vertices, Vector3f point) {
        vertices.add(Float.valueOf(point.x));
        vertices.add(Float.valueOf(point.y));
        vertices.add(Float.valueOf(point.z));
    }

    private static Vector3f extractVertex(List<Float> vertices, int pointer) {
        float x = vertices.get(pointer).floatValue();
        float z = vertices.get(pointer + 1).floatValue();
        float code = vertices.get(pointer + 2).floatValue();
        return new Vector3f(x, z, code);
    }

    private static boolean isUnderGround(Vector3f point, HeightFinder heightFinder, float waterHeight) {
        float terrainHeight = heightFinder.getHeight(point.x, point.y);
        return terrainHeight > waterHeight + 0.06f;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package world;

import gameManaging.GameManager;
import main.Camera;
import main.IGameCam;
import objectPools.Vec3Pool;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class ClosestPointFinder {
    private static final int CENTER = 1;
    private static final int LEFT = 0;
    private static final int RIGHT = 2;
    private final Vector2f topLeft;
    private float size;
    private final IGameCam camera;

    public ClosestPointFinder(Vector2f topLeft, float size) {
        this.topLeft = topLeft;
        this.size = size;
        this.camera = Camera.getCamera();
    }

    public float getDistance() {
        int[] area = this.getArea();
        if (this.isCenter(area)) {
            return this.getDistanceFromPoint(this.camera.getPosition().x, this.camera.getPosition().z);
        }
        if (this.isCorner(area)) {
            return this.getDistanceFromCorner(area);
        }
        return this.getDistanceFromEdge(area);
    }

    private float getDistanceFromCorner(int[] corner) {
        corner[0] = corner[0] / 2;
        corner[1] = corner[1] / 2;
        float x = this.topLeft.x + (float)corner[0] * this.size;
        float z = this.topLeft.y + (float)corner[1] * this.size;
        return this.getDistanceFromPoint(x, z);
    }

    private float getDistanceFromEdge(int[] edge) {
        if (edge[0] == 1) {
            edge[1] = edge[1] / 2;
            float zPos = this.topLeft.y + (float)edge[1] * this.size;
            return this.getDistanceFromPoint(this.camera.getPosition().x, zPos);
        }
        edge[0] = edge[0] / 2;
        float xPos = this.topLeft.x + (float)edge[0] * this.size;
        return this.getDistanceFromPoint(xPos, this.camera.getPosition().z);
    }

    private float getDistanceFromPoint(float x, float z) {
        float height = GameManager.getWorld().getHeightOfTerrain(x, z);
        Vector3f terrainPoint = Vec3Pool.get(x, height, z);
        Vector3f camPos = this.camera.getPosition();
        Vector3f toPoint = Vector3f.sub(camPos, terrainPoint, Vec3Pool.get());
        float length = toPoint.length();
        Vec3Pool.release(terrainPoint, toPoint);
        return length;
    }

    private int[] getArea() {
        int gridX = this.getGridNumber(this.camera.getPosition().x, this.topLeft.x);
        int gridZ = this.getGridNumber(this.camera.getPosition().z, this.topLeft.y);
        return new int[]{gridX, gridZ};
    }

    private int getGridNumber(float camPos, float chunkPos) {
        if (camPos > chunkPos + this.size) {
            return 2;
        }
        if (camPos > chunkPos) {
            return 1;
        }
        return 0;
    }

    private boolean isCenter(int[] area) {
        return area[0] == 1 && area[1] == 1;
    }

    private boolean isCorner(int[] area) {
        return !(area[0] != 0 && area[0] != 2 || area[1] != 0 && area[1] != 2);
    }
}


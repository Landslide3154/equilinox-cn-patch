/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import basics.CameraInterface;
import basics.MasterRenderer;
import gameManaging.GameManager;
import objectPools.Vec3Pool;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import world.World;

public class MousePicker {
    private static final int RECURSION_COUNT = 40;
    private static final float RAY_RANGE = 120.0f;
    private static final float RAY_SECTION = 2.0f;
    private static final int NO_POINT = -1;
    private Vector3f currentRay = new Vector3f();
    private Matrix4f viewMatrix;
    private CameraInterface camera;
    private Vector3f currentTerrainPoint;
    private Vector3f currentOffsetPoint;
    private boolean pickCenterScreen;
    private float offsetPointHeight;
    private float terrainDistance;
    private boolean rayUpToDate = false;
    private boolean terrainPointUpToDate = false;
    private boolean offsetPointUpToDate = false;
    private float offset = 0.0f;
    private boolean testWater = false;

    public MousePicker(CameraInterface cam, boolean pickCenter) {
        this.camera = cam;
        this.viewMatrix = this.camera.getViewMatrix();
        this.pickCenterScreen = pickCenter;
    }

    public MousePicker(CameraInterface cam, boolean pickCenter, boolean water) {
        this.camera = cam;
        this.testWater = water;
        this.viewMatrix = this.camera.getViewMatrix();
        this.pickCenterScreen = pickCenter;
    }

    public Vector3f getCurrentTerrainPoint() {
        if (!this.terrainPointUpToDate) {
            this.updateTerrainPoint();
        }
        return this.currentTerrainPoint;
    }

    public float getTerrainDistance() {
        if (!this.terrainPointUpToDate) {
            this.updateTerrainPoint();
        }
        return this.terrainDistance;
    }

    public Vector3f getIntersectionWithPlane(float planeHeight) {
        float d = -this.camera.getPosition().y / this.currentRay.y;
        return this.getPointOnRay(this.currentRay, d);
    }

    public void setOffsetPointHeight(float height) {
        this.offsetPointHeight = height;
    }

    public Vector3f getCurrentOffsetPoint() {
        if (!this.offsetPointUpToDate) {
            this.updateOffsetPoint();
        }
        return this.currentOffsetPoint;
    }

    public Vector3f getRayPoint(float disFromCam) {
        return this.getPointOnRay(this.getCurrentRay(), disFromCam);
    }

    public Vector3f getCurrentRay() {
        if (!this.rayUpToDate) {
            this.updateMouseRay();
        }
        return this.currentRay;
    }

    public void update() {
        this.viewMatrix = this.camera.getViewMatrix();
        this.rayUpToDate = false;
        this.terrainPointUpToDate = false;
        this.offsetPointUpToDate = false;
    }

    private void updateTerrainPoint() {
        this.offset = 0.0f;
        Vector3f ray = this.getCurrentRay();
        float section = this.getSectionID(ray, this.testWater);
        if (section == -1.0f) {
            this.currentTerrainPoint = null;
            return;
        }
        this.currentTerrainPoint = this.binarySearch(0, section * 2.0f, (section + 1.0f) * 2.0f, ray, this.testWater);
        this.terrainPointUpToDate = true;
    }

    private void updateOffsetPoint() {
        this.offset = this.offsetPointHeight;
        Vector3f ray = this.getCurrentRay();
        float section = this.getSectionID(ray, true);
        if (section == -1.0f) {
            this.currentTerrainPoint = null;
            return;
        }
        this.currentOffsetPoint = this.binarySearch(0, section * 2.0f, (section + 1.0f) * 2.0f, ray, true);
        this.offsetPointUpToDate = true;
    }

    private void updateMouseRay() {
        Vector3f worldRay;
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();
        Vector2f normalizedCoords = this.getNormalisedDeviceCoordinates(mouseX, mouseY);
        if (this.pickCenterScreen) {
            normalizedCoords.set(0.0f, -0.0f);
        }
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
        Vector4f eyeCoords = this.toEyeCoords(clipCoords);
        this.currentRay = worldRay = this.toWorldCoords(eyeCoords);
        this.rayUpToDate = true;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = Matrix4f.invert(this.viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = Matrix4f.invert(MasterRenderer.getProjectionMatrix(), null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1.0f, 0.0f);
    }

    private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
        float x = 2.0f * mouseX / (float)Display.getWidth() - 1.0f;
        float y = 2.0f * mouseY / (float)Display.getHeight() - 1.0f;
        return new Vector2f(x, y);
    }

    private int getSectionID(Vector3f ray, boolean testWater) {
        int i = 0;
        while ((float)i < 60.0f) {
            if (this.intersectionInRange((float)i * 2.0f, (float)(i + 1) * 2.0f, ray, testWater)) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = this.camera.getPosition();
        Vector3f start = Vec3Pool.get(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = Vec3Pool.get(ray.x * distance, ray.y * distance, ray.z * distance);
        Vector3f point = Vector3f.add(start, scaledRay, Vec3Pool.get());
        Vec3Pool.release(start, scaledRay);
        return point;
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray, boolean testWater) {
        float half = start + (finish - start) / 2.0f;
        if (count >= 40) {
            Vector3f endPoint = this.getPointOnRay(ray, half);
            World terrain = this.getTerrain();
            if (terrain != null) {
                this.terrainDistance = half;
                return endPoint;
            }
            this.terrainDistance = 0.0f;
            return null;
        }
        if (this.intersectionInRange(start, half, ray, testWater)) {
            return this.binarySearch(count + 1, start, half, ray, testWater);
        }
        return this.binarySearch(count + 1, half, finish, ray, testWater);
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray, boolean testWater) {
        Vector3f startPoint = this.getPointOnRay(ray, start);
        Vector3f endPoint = this.getPointOnRay(ray, finish);
        if (!this.isUnderGround(startPoint, testWater) && this.isUnderGround(endPoint, testWater)) {
            Vec3Pool.release(startPoint, endPoint);
            return true;
        }
        Vec3Pool.release(startPoint, endPoint);
        return false;
    }

    private boolean isUnderGround(Vector3f testPoint, boolean testWater) {
        World terrain = this.getTerrain();
        float height = 0.0f;
        if (terrain != null && terrain.isOnWorld(testPoint)) {
            height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
            if (testWater) {
                height = Math.max(GameManager.getWorld().getWaterHeight(), height);
            }
        } else {
            return false;
        }
        return testPoint.y < height + this.offset;
    }

    private World getTerrain() {
        return GameManager.getWorld();
    }
}


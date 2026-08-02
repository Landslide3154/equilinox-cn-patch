/*
 * Decompiled with CFR 0.152.
 */
package shadows;

import basics.CameraInterface;
import basics.DisplayManager;
import basics.EngineMaster;
import frustumCulling.FrustumCuller;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

public class ShadowBox {
    private static final float OFFSET = 2.0f;
    private static final Vector4f UP = new Vector4f(0.0f, 1.0f, 0.0f, 0.0f);
    private static final Vector4f FORWARD = new Vector4f(0.0f, 0.0f, -1.0f, 0.0f);
    private float minX;
    private float maxX;
    private float minY;
    private float maxY;
    private float minZ;
    private float maxZ;
    private Matrix4f lightViewMatrix;
    private CameraInterface cam;
    private float shadowDistance;
    private FrustumCuller boxCuller = new FrustumCuller();
    private float farHeight;
    private float farWidth;
    private float nearHeight;
    private float nearWidth;

    protected ShadowBox(Matrix4f lightViewMatrix, CameraInterface camera) {
        this.lightViewMatrix = lightViewMatrix;
        this.cam = camera;
    }

    public boolean isInBox(Vector3f mins, Vector3f maxs) {
        return this.boxCuller.isInFrustum(mins, maxs);
    }

    protected float getShadowDistance() {
        return this.shadowDistance;
    }

    protected void update() {
        this.updateShadowDistance();
        this.updateWidthsAndHeights();
        Matrix4f rotation = this.calculateCameraRotationMatrix();
        Vector3f forwardVector = new Vector3f(Matrix4f.transform(rotation, FORWARD, null));
        Vector3f toFar = new Vector3f(forwardVector);
        toFar.scale(this.shadowDistance);
        Vector3f toNear = new Vector3f(forwardVector);
        toNear.scale(this.cam.getNearPlane());
        Vector3f centerNear = Vector3f.add(toNear, this.cam.getPosition(), null);
        Vector3f centerFar = Vector3f.add(toFar, this.cam.getPosition(), null);
        Vector4f[] points = this.calculateFrustumVertices(rotation, forwardVector, centerNear, centerFar);
        boolean first = true;
        Vector4f[] vector4fArray = points;
        int n = points.length;
        int n2 = 0;
        while (n2 < n) {
            Vector4f point = vector4fArray[n2];
            if (first) {
                this.minX = point.x;
                this.maxX = point.x;
                this.minY = point.y;
                this.maxY = point.y;
                this.minZ = point.z;
                this.maxZ = point.z;
                first = false;
            } else {
                this.maxX = Math.max(point.x, this.maxX);
                this.maxY = Math.max(point.y, this.maxY);
                this.maxZ = Math.max(point.z, this.maxZ);
                this.minX = Math.min(point.x, this.minX);
                this.minY = Math.min(point.y, this.minY);
                this.minZ = Math.min(point.z, this.minZ);
            }
            ++n2;
        }
        this.maxZ += 2.0f;
        this.updateBoxCuller();
    }

    private void updateBoxCuller() {
        Vector4f[] lightSpaceVerts = this.getLightSpaceVertices();
        Vector4f[] frustumVerts = this.convertVerticesToWorldSpace(lightSpaceVerts);
        this.boxCuller.update(frustumVerts);
    }

    private Vector4f[] getLightSpaceVertices() {
        Vector4f[] verts = new Vector4f[]{new Vector4f(this.maxX, this.maxY, this.maxZ, 1.0f), new Vector4f(this.minX, this.maxY, this.maxZ, 1.0f), new Vector4f(this.maxX, this.minY, this.maxZ, 1.0f), new Vector4f(this.minX, this.minY, this.maxZ, 1.0f), new Vector4f(this.maxX, this.maxY, this.minZ, 1.0f), new Vector4f(this.minX, this.maxY, this.minZ, 1.0f), new Vector4f(this.maxX, this.minY, this.minZ, 1.0f), new Vector4f(this.minX, this.minY, this.minZ, 1.0f)};
        return verts;
    }

    private Vector4f[] convertVerticesToWorldSpace(Vector4f[] lightSpaceVerts) {
        Matrix4f toWorldSpace = Matrix4f.invert(this.lightViewMatrix, null);
        Vector4f[] frustumVertices = new Vector4f[lightSpaceVerts.length];
        int i = 0;
        while (i < lightSpaceVerts.length) {
            frustumVertices[i] = Matrix4f.transform(toWorldSpace, lightSpaceVerts[i], null);
            ++i;
        }
        return frustumVertices;
    }

    private void updateShadowDistance() {
        this.shadowDistance = EngineMaster.getCamera().getAimDistance() * 2.5f;
    }

    private void updateWidthsAndHeights() {
        this.farWidth = (float)((double)this.shadowDistance * Math.tan(Math.toRadians(this.cam.getFOV())));
        this.nearWidth = (float)((double)this.cam.getNearPlane() * Math.tan(Math.toRadians(this.cam.getFOV())));
        this.farHeight = this.farWidth / DisplayManager.getAspectRatio();
        this.nearHeight = this.nearWidth / DisplayManager.getAspectRatio();
    }

    protected Vector3f getCenter() {
        float x = (this.minX + this.maxX) / 2.0f;
        float y = (this.minY + this.maxY) / 2.0f;
        float z = (this.minZ + this.maxZ) / 2.0f;
        Vector4f cen = new Vector4f(x, y, z, 1.0f);
        Matrix4f invertedLight = new Matrix4f();
        Matrix4f.invert(this.lightViewMatrix, invertedLight);
        return new Vector3f(Matrix4f.transform(invertedLight, cen, null));
    }

    protected float getWidth() {
        return this.maxX - this.minX;
    }

    protected float getHeight() {
        return this.maxY - this.minY;
    }

    protected float getLength() {
        return this.maxZ - this.minZ;
    }

    private static float clampValue(float value, float min, float max) {
        if (value > max) {
            return max;
        }
        if (value < min) {
            return min;
        }
        return value;
    }

    private Vector4f[] calculateFrustumVertices(Matrix4f rotation, Vector3f forwardVector, Vector3f centerNear, Vector3f centerFar) {
        Vector3f upVector = new Vector3f(Matrix4f.transform(rotation, UP, null));
        Vector3f rightVector = Vector3f.cross(forwardVector, upVector, null);
        Vector3f downVector = new Vector3f(-upVector.x, -upVector.y, -upVector.z);
        Vector3f leftVector = new Vector3f(-rightVector.x, -rightVector.y, -rightVector.z);
        Vector3f farTop = Vector3f.add(centerFar, new Vector3f(upVector.x * this.farHeight, upVector.y * this.farHeight, upVector.z * this.farHeight), null);
        Vector3f farBottom = Vector3f.add(centerFar, new Vector3f(downVector.x * this.farHeight, downVector.y * this.farHeight, downVector.z * this.farHeight), null);
        Vector3f nearTop = Vector3f.add(centerNear, new Vector3f(upVector.x * this.nearHeight, upVector.y * this.nearHeight, upVector.z * this.nearHeight), null);
        Vector3f nearBottom = Vector3f.add(centerNear, new Vector3f(downVector.x * this.nearHeight, downVector.y * this.nearHeight, downVector.z * this.nearHeight), null);
        Vector4f[] points = new Vector4f[]{this.calculateLightSpaceFrustumCorner(farTop, rightVector, this.farWidth), this.calculateLightSpaceFrustumCorner(farTop, leftVector, this.farWidth), this.calculateLightSpaceFrustumCorner(farBottom, rightVector, this.farWidth), this.calculateLightSpaceFrustumCorner(farBottom, leftVector, this.farWidth), this.calculateLightSpaceFrustumCorner(nearTop, rightVector, this.nearWidth), this.calculateLightSpaceFrustumCorner(nearTop, leftVector, this.nearWidth), this.calculateLightSpaceFrustumCorner(nearBottom, rightVector, this.nearWidth), this.calculateLightSpaceFrustumCorner(nearBottom, leftVector, this.nearWidth)};
        return points;
    }

    private Vector4f calculateLightSpaceFrustumCorner(Vector3f startPoint, Vector3f direction, float width) {
        Vector3f point = Vector3f.add(startPoint, new Vector3f(direction.x * width, direction.y * width, direction.z * width), null);
        Vector4f point4f = new Vector4f(point.x, point.y, point.z, 1.0f);
        Matrix4f.transform(this.lightViewMatrix, point4f, point4f);
        return point4f;
    }

    private Matrix4f calculateCameraRotationMatrix() {
        Matrix4f rotation = new Matrix4f();
        rotation.rotate(Maths.degreesToRadians(this.cam.getYaw()), new Vector3f(0.0f, 1.0f, 0.0f));
        rotation.rotate(Maths.degreesToRadians(-this.cam.getPitch()), new Vector3f(1.0f, 0.0f, 0.0f));
        return rotation;
    }
}


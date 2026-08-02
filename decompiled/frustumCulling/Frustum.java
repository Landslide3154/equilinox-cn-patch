/*
 * Decompiled with CFR 0.152.
 */
package frustumCulling;

import basics.CameraInterface;
import basics.DisplayManager;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

public class Frustum {
    public static final int VERTEX_COUNT = 8;
    private Vector4f[] originalVertices = new Vector4f[8];
    private Vector4f[] frustumVertices = new Vector4f[8];
    private CameraInterface camera;
    private float frustumLength;
    private float farWidth;
    private float farHeight;
    private float nearWidth;
    private float nearHeight;
    private Matrix4f cameraTransform = new Matrix4f();

    public Frustum(CameraInterface camera) {
        this.camera = camera;
        this.frustumLength = camera.getFarPlane();
        this.initFrusutmVertices();
        this.calculateOriginalVertices();
        this.update();
    }

    public Frustum() {
    }

    public void update() {
        this.updateCameraTransform();
        int i = 0;
        while (i < this.frustumVertices.length) {
            Matrix4f.transform(this.cameraTransform, this.originalVertices[i], this.frustumVertices[i]);
            ++i;
        }
    }

    public void update(float limitedDistance) {
        if (this.frustumLength != limitedDistance) {
            this.frustumLength = limitedDistance;
            this.calculateOriginalVertices();
        }
        this.update();
    }

    public void update(Vector4f[] newVertices) {
        this.frustumVertices = newVertices;
    }

    public Vector3f getVertex(int i) {
        return new Vector3f(this.frustumVertices[i]);
    }

    private void calculateWidthsAndHeights() {
        this.farHeight = (float)((double)this.frustumLength * Math.tan(Math.toRadians(this.camera.getFOV() / 2.0f)));
        this.nearHeight = (float)((double)this.camera.getNearPlane() * Math.tan(Math.toRadians(this.camera.getFOV() / 2.0f)));
        this.farWidth = this.farHeight * DisplayManager.getAspectRatio();
        this.nearWidth = this.nearHeight * DisplayManager.getAspectRatio();
    }

    private void calculateOriginalVertices() {
        this.calculateWidthsAndHeights();
        int i = 0;
        while (i < this.originalVertices.length) {
            this.originalVertices[i] = this.getVertex(i / 4 % 2 == 0, i % 2 == 0, i / 2 % 2 == 0);
            ++i;
        }
    }

    private Vector4f getVertex(boolean isNear, boolean positiveX, boolean positiveY) {
        Vector4f vertex = new Vector4f();
        vertex.z = isNear ? -this.camera.getNearPlane() : -this.frustumLength;
        Vector2f sizes = isNear ? new Vector2f(this.nearWidth, this.nearHeight) : new Vector2f(this.farWidth, this.farHeight);
        vertex.x = positiveX ? sizes.x : -sizes.x;
        vertex.y = positiveY ? sizes.y : -sizes.y;
        vertex.w = 1.0f;
        return vertex;
    }

    private void initFrusutmVertices() {
        int i = 0;
        while (i < this.frustumVertices.length) {
            this.frustumVertices[i] = new Vector4f();
            ++i;
        }
    }

    private void updateCameraTransform() {
        this.cameraTransform.setIdentity();
        this.cameraTransform.translate(this.camera.getPosition(), this.cameraTransform);
        this.cameraTransform.rotate(Maths.degreesToRadians(this.camera.getYaw()), new Vector3f(0.0f, 1.0f, 0.0f));
        this.cameraTransform.rotate(Maths.degreesToRadians(-this.camera.getPitch()), new Vector3f(1.0f, 0.0f, 0.0f));
    }
}


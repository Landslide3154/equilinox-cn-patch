/*
 * Decompiled with CFR 0.152.
 */
package frustumCulling;

import basics.CameraInterface;
import frustumCulling.Frustum;
import frustumCulling.Plane;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class FrustumCuller {
    private static final int NUM_PLANES = 5;
    private Frustum frustum;
    private Plane[] planes = new Plane[5];

    public FrustumCuller(CameraInterface camera) {
        this.frustum = new Frustum(camera);
        this.initPlanes();
        this.constructPlanes();
    }

    public FrustumCuller() {
        this.frustum = new Frustum();
        this.initPlanes();
    }

    public void update() {
        this.frustum.update();
        this.constructPlanes();
    }

    public void update(Vector4f[] frustumVertices) {
        this.frustum.update(frustumVertices);
        this.constructPlanes();
    }

    public boolean isInFrustum(Vector3f center, float radius) {
        Plane[] planeArray = this.planes;
        int n = this.planes.length;
        int n2 = 0;
        while (n2 < n) {
            Plane plane = planeArray[n2];
            if (plane.getSignedDistance(center) < -radius) {
                return false;
            }
            ++n2;
        }
        return true;
    }

    public boolean isInFrustum(Vector3f mins, Vector3f maxs) {
        int i = 0;
        while (i < 5) {
            float pZ;
            float pY;
            Plane plane;
            float pX = plane.getNormal().x > 0.0f ? maxs.x : mins.x;
            float pointDistance = plane.getNormal().x * pX + plane.getNormal().y * (pY = plane.getNormal().y > 0.0f ? maxs.y : mins.y) + plane.getNormal().z * (pZ = plane.getNormal().z > 0.0f ? maxs.z : mins.z);
            if (pointDistance < -(plane = this.planes[i]).getConstant()) {
                return false;
            }
            ++i;
        }
        return true;
    }

    private void initPlanes() {
        int i = 0;
        while (i < this.planes.length) {
            this.planes[i] = new Plane();
            ++i;
        }
    }

    private void constructPlanes() {
        this.planes[0].setPlane(this.frustum.getVertex(0), this.frustum.getVertex(4), this.frustum.getVertex(5));
        this.planes[1].setPlane(this.frustum.getVertex(0), this.frustum.getVertex(2), this.frustum.getVertex(6));
        this.planes[2].setPlane(this.frustum.getVertex(1), this.frustum.getVertex(3), this.frustum.getVertex(2));
        this.planes[3].setPlane(this.frustum.getVertex(7), this.frustum.getVertex(6), this.frustum.getVertex(2));
        this.planes[4].setPlane(this.frustum.getVertex(1), this.frustum.getVertex(5), this.frustum.getVertex(7));
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package frustumCulling;

import org.lwjgl.util.vector.Vector3f;

public class Plane {
    private Vector3f origin;
    private Vector3f normal;
    private float constant;

    public Plane() {
    }

    public Plane(Vector3f point0, Vector3f point1, Vector3f point2) {
        this.setPlane(point0, point1, point2);
    }

    public Plane(Vector3f normal, Vector3f origin) {
        this.normal = normal;
        this.origin = origin;
        this.constant = -(normal.x * origin.x + normal.y * origin.y + normal.z * origin.z);
    }

    public Vector3f getNormal() {
        return this.normal;
    }

    public float getConstant() {
        return this.constant;
    }

    public void setPlane(Vector3f point0, Vector3f point1, Vector3f point2) {
        this.normal = Vector3f.cross(Vector3f.sub(point2, point0, null), Vector3f.sub(point1, point0, null), null);
        this.normal.normalise();
        this.origin = new Vector3f(point0.x, point0.y, point0.z);
        this.constant = -(this.normal.x * this.origin.x + this.normal.y * this.origin.y + this.normal.z * this.origin.z);
    }

    public float getSignedDistance(Vector3f point) {
        return Vector3f.dot(this.normal, point) + this.constant;
    }

    public boolean isFrontFacingTo(Vector3f direction) {
        float dot = Vector3f.dot(this.normal, direction);
        return dot <= 0.0f;
    }
}


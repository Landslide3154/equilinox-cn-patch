/*
 * Decompiled with CFR 0.152.
 */
package interpolation;

import org.lwjgl.util.vector.Vector3f;

public class SmoothVector {
    private final float agility;
    private Vector3f current = new Vector3f();
    private Vector3f target = new Vector3f();

    public SmoothVector(Vector3f target, float agility) {
        this.target.set(target);
        this.current.set(target);
        this.agility = agility;
    }

    public void update(float delta) {
        Vector3f diff = Vector3f.sub(this.target, this.current, null);
        float factor = delta * this.agility;
        if (factor > 1.0f) {
            this.current.set(this.target);
        } else {
            diff.scale(factor);
            Vector3f.add(this.current, diff, this.current);
        }
    }

    public void cancelTarget() {
        this.target.set(this.current);
    }

    public void invertCurrent(float waterHeight) {
        this.current.y -= 2.0f * (this.current.y - waterHeight);
    }

    public Vector3f getTarget() {
        return this.target;
    }

    public void setTarget(Vector3f newTarget) {
        this.target.set(newTarget);
    }

    public void setTarget(float x, float y, float z) {
        this.target.set(x, y, z);
    }

    public void increaseTarget(float dx, float dy, float dz) {
        this.target.x += dx;
        this.target.y += dy;
        this.target.z += dz;
    }

    public void force(Vector3f newValue) {
        this.current.set(newValue);
        this.target.set(newValue);
    }

    public void force(float x, float y, float z) {
        this.current.set(x, y, z);
        this.target.set(x, y, z);
    }

    public void forceOnlyActualValue(Vector3f newValue) {
        Vector3f difference = Vector3f.sub(this.target, this.current, null);
        this.current.set(newValue);
        Vector3f.add(this.current, difference, this.target);
    }

    public boolean reached() {
        float diffSquared = Vector3f.sub(this.target, this.current, null).lengthSquared();
        return diffSquared < 1.0E-5f;
    }

    public void increaseAll(float dx, float dy, float dz) {
        this.current.x += dx;
        this.current.y += dy;
        this.current.z += dz;
        this.target.x += dx;
        this.target.y += dy;
        this.target.z += dz;
    }

    public Vector3f get() {
        return this.current;
    }
}


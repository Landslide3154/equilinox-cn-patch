/*
 * Decompiled with CFR 0.152.
 */
package picking;

import objectPools.Vec3Pool;
import objectPools.Vec4Pool;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import picking.AABB;
import toolbox.Maths;
import toolbox.Transformation;

public class Box {
    private static final float MIN_WIDTH_VALUES = 0.15f;
    private Matrix4f modelMatrix = new Matrix4f();
    private Vector3f scale = new Vector3f();
    private AABB aabb;
    private Transformation transform;
    private boolean dirty = true;

    public Box(AABB aabb, Transformation transform) {
        this.aabb = aabb;
        this.transform = transform;
    }

    public Vector3f getSizes() {
        if (this.isDirty()) {
            this.recalculate();
        }
        return this.scale;
    }

    public Matrix4f getModelMatrix() {
        if (this.isDirty()) {
            this.recalculate();
        }
        return this.modelMatrix;
    }

    public float getHeight() {
        return this.aabb.getHeight() * this.transform.getScale();
    }

    public float getMaxWidth() {
        Vector3f sizes = this.getSizes();
        return Math.max(sizes.x, sizes.z);
    }

    public float getMaxSize() {
        return Math.max(this.getMaxWidth(), this.getSizes().y);
    }

    protected boolean isDirty() {
        return this.dirty;
    }

    public AABB getAabb() {
        return this.aabb;
    }

    protected void setAabb(AABB newAabb) {
        if (newAabb != this.aabb) {
            this.aabb = newAabb;
            this.dirty = true;
        }
    }

    public void setDirty() {
        this.dirty = true;
    }

    private void recalculate() {
        Vector4f position = Matrix4f.transform(this.transform.getModelMatrix(), this.aabb.getOffset(), Vec4Pool.get());
        this.scale.set(this.aabb.getScale());
        this.scale.scale(this.transform.getScale());
        this.buffScale(this.scale);
        Vector3f pos3f = Vec3Pool.get(position);
        Vec4Pool.release(position);
        Maths.updateModelMatrix(this.modelMatrix, pos3f, this.transform.getRotX(), this.transform.getRotY(), this.transform.getRotZ(), this.scale);
        Vec3Pool.release(pos3f);
        this.dirty = false;
    }

    private void buffScale(Vector3f scale) {
        scale.x = Math.max(scale.x, 0.15f);
        scale.z = Math.max(scale.z, 0.15f);
    }
}


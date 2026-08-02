/*
 * Decompiled with CFR 0.152.
 */
package picking;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class AABB {
    private Vector3f scale;
    private Vector4f offset;

    public AABB(Vector3f min, Vector3f max) {
        this.scale = Vector3f.sub(max, min, null);
        Vector3f middle = Vector3f.add(min, new Vector3f(this.scale.x / 2.0f, this.scale.y / 2.0f, this.scale.z / 2.0f), null);
        this.offset = new Vector4f(middle.x, middle.y, middle.z, 1.0f);
    }

    public Vector3f getScale() {
        return this.scale;
    }

    public float getHeight() {
        return this.scale.y * 0.5f + this.offset.y;
    }

    public Vector4f getOffset() {
        return this.offset;
    }

    public float getMaxDimension() {
        return Math.max(this.scale.y, this.getMaxWidth());
    }

    public float getMaxWidth() {
        return Math.max(this.scale.x, this.scale.z);
    }
}


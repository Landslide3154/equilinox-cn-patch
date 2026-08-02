/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import org.lwjgl.util.vector.Vector3f;

public class GridAABB {
    public final Vector3f mins;
    public final Vector3f maxs;
    public final Vector3f center;

    public GridAABB(Vector3f mins, Vector3f maxs) {
        this.mins = mins;
        this.maxs = maxs;
        this.center = this.calculateCenter();
    }

    public float getSizeX() {
        return this.maxs.x - this.mins.x;
    }

    public float getSizeY() {
        return this.maxs.y - this.mins.y;
    }

    public float getSizeZ() {
        return this.maxs.z - this.mins.z;
    }

    private Vector3f calculateCenter() {
        float x = this.mins.x + this.getSizeX() * 0.5f;
        float y = this.mins.y + this.getSizeY() * 0.5f;
        float z = this.mins.z + this.getSizeZ() * 0.5f;
        return new Vector3f(x, y, z);
    }
}


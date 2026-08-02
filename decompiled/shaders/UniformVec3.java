/*
 * Decompiled with CFR 0.152.
 */
package shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;
import shaders.Uniform;

public class UniformVec3
extends Uniform {
    private float currentX;
    private float currentY;
    private float currentZ;
    private boolean used = false;

    public UniformVec3(String name) {
        super(name);
    }

    public void loadVec3(Vector3f vector) {
        this.loadVec3(vector.x, vector.y, vector.z);
    }

    public void loadVec3(float x, float y, float z) {
        if (!this.used || x != this.currentX || y != this.currentY || z != this.currentZ) {
            this.currentX = x;
            this.currentY = y;
            this.currentZ = z;
            this.used = true;
            GL20.glUniform3f(super.getLocation(), x, y, z);
        }
    }
}


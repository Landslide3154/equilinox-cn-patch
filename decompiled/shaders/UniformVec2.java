/*
 * Decompiled with CFR 0.152.
 */
package shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector2f;
import shaders.Uniform;

public class UniformVec2
extends Uniform {
    private float currentX;
    private float currentY;
    private boolean used = false;

    public UniformVec2(String name) {
        super(name);
    }

    public void loadVec2(Vector2f vector) {
        this.loadVec2(vector.x, vector.y);
    }

    public void loadVec2(float x, float y) {
        if (!this.used || x != this.currentX || y != this.currentY) {
            this.currentX = x;
            this.currentY = y;
            this.used = true;
            GL20.glUniform2f(super.getLocation(), x, y);
        }
    }
}


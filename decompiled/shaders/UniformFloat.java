/*
 * Decompiled with CFR 0.152.
 */
package shaders;

import org.lwjgl.opengl.GL20;
import shaders.Uniform;

public class UniformFloat
extends Uniform {
    private float currentValue;
    private boolean used = false;

    public UniformFloat(String name) {
        super(name);
    }

    public void loadFloat(float value) {
        if (!this.used || this.currentValue != value) {
            GL20.glUniform1f(super.getLocation(), value);
            this.used = true;
            this.currentValue = value;
        }
    }
}


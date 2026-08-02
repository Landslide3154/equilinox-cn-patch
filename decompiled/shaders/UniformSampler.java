/*
 * Decompiled with CFR 0.152.
 */
package shaders;

import org.lwjgl.opengl.GL20;
import shaders.Uniform;

public class UniformSampler
extends Uniform {
    private int currentValue;
    private boolean used = false;

    public UniformSampler(String name) {
        super(name);
    }

    public void loadTexUnit(int texUnit) {
        if (!this.used || this.currentValue != texUnit) {
            GL20.glUniform1i(super.getLocation(), texUnit);
            this.used = true;
            this.currentValue = texUnit;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package shaders;

import org.lwjgl.opengl.GL20;
import shaders.Uniform;

public class UniformBoolean
extends Uniform {
    private boolean currentBool;
    private boolean used = false;

    public UniformBoolean(String name) {
        super(name);
    }

    public void loadBoolean(boolean bool) {
        if (!this.used || this.currentBool != bool) {
            GL20.glUniform1f(super.getLocation(), bool ? 1.0f : 0.0f);
            this.used = true;
            this.currentBool = bool;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package shaders;

import org.lwjgl.opengl.GL20;

public abstract class Uniform {
    private static final int NOT_FOUND = -1;
    private String name;
    private int location;

    protected Uniform(String name) {
        this.name = name;
    }

    protected void storeUniformLocation(int programID) {
        this.location = GL20.glGetUniformLocation(programID, this.name);
    }

    protected int getLocation() {
        return this.location;
    }
}


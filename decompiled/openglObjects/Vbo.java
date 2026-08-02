/*
 * Decompiled with CFR 0.152.
 */
package openglObjects;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL15;

public class Vbo {
    private final int vboId;
    private final int type;
    private final int usage;

    private Vbo(int vboId, int type, int usage) {
        this.vboId = vboId;
        this.type = type;
        this.usage = usage;
        this.bind();
    }

    public static Vbo create(int type, int usage) {
        int id = GL15.glGenBuffers();
        return new Vbo(id, type, usage);
    }

    public void bind() {
        GL15.glBindBuffer(this.type, this.vboId);
    }

    public void unbind() {
        GL15.glBindBuffer(this.type, 0);
    }

    public void allocateData(long sizeInBytes) {
        GL15.glBufferData(this.type, sizeInBytes, this.usage);
    }

    public void refill(ByteBuffer data) {
        this.bind();
        GL15.glBufferData(34962, data.capacity(), this.usage);
        GL15.glBufferSubData(34962, 0L, data);
        this.unbind();
    }

    public void storeData(long startInBytes, IntBuffer data) {
        GL15.glBufferSubData(this.type, startInBytes, data);
    }

    public void storeData(long startInBytes, FloatBuffer data) {
        GL15.glBufferSubData(this.type, startInBytes, data);
    }

    public void storeData(long startInBytes, ByteBuffer data) {
        GL15.glBufferSubData(this.type, startInBytes, data);
    }

    public void delete() {
        GL15.glDeleteBuffers(this.vboId);
    }
}


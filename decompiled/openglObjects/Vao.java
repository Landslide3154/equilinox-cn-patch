/*
 * Decompiled with CFR 0.152.
 */
package openglObjects;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import openglObjects.Attribute;
import openglObjects.Vbo;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Vao {
    private static final int BYTES_IN_FLOAT = 4;
    private static final int BYTES_IN_INT = 4;
    private List<Vbo> relatedVbos = new ArrayList<Vbo>();
    private Vbo indexBuffer;
    private List<Attribute> attributes = new ArrayList<Attribute>();
    public final int id;

    public static Vao create() {
        int id = GL30.glGenVertexArrays();
        return new Vao(id);
    }

    private Vao(int id) {
        this.id = id;
    }

    public void bind() {
        GL30.glBindVertexArray(this.id);
    }

    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void enableAttributes() {
        for (Attribute attribute : this.attributes) {
            attribute.enable(true);
        }
    }

    public void disableAttribs(int ... attribs) {
        int[] nArray = attribs;
        int n = attribs.length;
        int n2 = 0;
        while (n2 < n) {
            int i = nArray[n2];
            GL20.glDisableVertexAttribArray(i);
            ++n2;
        }
    }

    public Vbo createDataFeed(int maxVertexCount, int usage, Attribute ... newAttributes) {
        int bytesPerVertex = this.getVertexDataTotalBytes(newAttributes);
        Vbo vbo = Vbo.create(34962, usage);
        this.relatedVbos.add(vbo);
        vbo.allocateData(bytesPerVertex * maxVertexCount);
        this.linkAttributes(bytesPerVertex, newAttributes);
        vbo.unbind();
        return vbo;
    }

    public Vbo initDataFeed(FloatBuffer data, int usage, Attribute ... newAttributes) {
        int bytesPerVertex = this.getVertexDataTotalBytes(newAttributes);
        Vbo vbo = Vbo.create(34962, usage);
        this.relatedVbos.add(vbo);
        vbo.allocateData(data.limit() * 4);
        vbo.storeData(0L, data);
        this.linkAttributes(bytesPerVertex, newAttributes);
        vbo.unbind();
        return vbo;
    }

    public Vbo initDataFeed(ByteBuffer data, int usage, Attribute ... newAttributes) {
        int bytesPerVertex = this.getVertexDataTotalBytes(newAttributes);
        Vbo vbo = Vbo.create(34962, usage);
        this.relatedVbos.add(vbo);
        vbo.allocateData(data.limit());
        vbo.storeData(0L, data);
        this.linkAttributes(bytesPerVertex, newAttributes);
        vbo.unbind();
        return vbo;
    }

    public void linkBoundVbo(Vbo vbo, Attribute ... newAttributes) {
        int bytesPerVertex = this.getVertexDataTotalBytes(newAttributes);
        this.linkAttributes(bytesPerVertex, newAttributes);
        this.relatedVbos.add(vbo);
    }

    public Vbo createIndexBuffer(IntBuffer indices) {
        this.indexBuffer = Vbo.create(34963, 35044);
        this.indexBuffer.allocateData(indices.limit() * 4);
        this.indexBuffer.storeData(0L, indices);
        return this.indexBuffer;
    }

    public void delete(boolean deleteVbos) {
        GL30.glDeleteVertexArrays(this.id);
        if (deleteVbos) {
            for (Vbo vbo : this.relatedVbos) {
                vbo.delete();
            }
        }
    }

    private void linkAttributes(int bytesPerVertex, Attribute ... newAttributes) {
        int offset = 0;
        Attribute[] attributeArray = newAttributes;
        int n = newAttributes.length;
        int n2 = 0;
        while (n2 < n) {
            Attribute attribute = attributeArray[n2];
            attribute.link(offset, bytesPerVertex);
            offset += attribute.bytesPerVertex;
            attribute.enable(true);
            this.attributes.add(attribute);
            ++n2;
        }
    }

    private int getVertexDataTotalBytes(Attribute ... newAttributes) {
        int total = 0;
        Attribute[] attributeArray = newAttributes;
        int n = newAttributes.length;
        int n2 = 0;
        while (n2 < n) {
            Attribute attribute = attributeArray[n2];
            total += attribute.bytesPerVertex;
            ++n2;
        }
        return total;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package water;

import glRequestProcessing.GlRequest;
import glRequestProcessing.GlRequestProcessor;
import java.nio.ByteBuffer;
import openglObjects.Attribute;
import openglObjects.Vao;
import org.lwjgl.BufferUtils;
import terrains.HeightFinder;
import water.WaterGenerator;

public class Water {
    public static final float WAVE_SPEED = 0.9f;
    public static final float SQUARE_SIZE = 1.0f;
    public static final float AMPLITUDE = 0.06f;
    public final int pointCount;
    public final float size;
    public final float height;
    private Vao vao;
    private int vertexCount;
    private boolean loaded = false;

    public Water(float height, float size, HeightFinder heightFinder) {
        this.height = height;
        this.size = size;
        this.pointCount = (int)(size / 1.0f + 1.0f);
        this.generateMesh(heightFinder);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void delete() {
        GlRequestProcessor.sendRequest(new GlRequest(){

            @Override
            public void executeGlRequest() {
                Water.this.loaded = false;
                Water.this.vao.delete(true);
            }
        });
    }

    protected int getVao() {
        return this.vao.id;
    }

    protected int getVertexCount() {
        return this.vertexCount;
    }

    private void generateMesh(HeightFinder heightFinder) {
        byte[] vertexData = WaterGenerator.generate(this.pointCount - 1, heightFinder, this.height);
        final ByteBuffer buffer = BufferUtils.createByteBuffer(vertexData.length);
        buffer.put(vertexData);
        buffer.flip();
        this.vertexCount = vertexData.length / 12;
        GlRequestProcessor.sendRequest(new GlRequest(){

            @Override
            public void executeGlRequest() {
                Water.this.vao = Vao.create();
                Water.this.vao.bind();
                Water.this.vao.initDataFeed(buffer, 35044, new Attribute(0, 5126, 2), new Attribute(1, 5120, 4));
                Water.this.vao.unbind();
                Water.this.loaded = true;
            }
        });
    }
}


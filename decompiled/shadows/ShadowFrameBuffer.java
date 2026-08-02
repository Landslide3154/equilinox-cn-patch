/*
 * Decompiled with CFR 0.152.
 */
package shadows;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class ShadowFrameBuffer {
    private final int WIDTH;
    private final int HEIGHT;
    private int fbo;
    private int shadowMap;

    protected ShadowFrameBuffer(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.initialiseFrameBuffer();
    }

    protected void cleanUp() {
        GL30.glDeleteFramebuffers(this.fbo);
        GL11.glDeleteTextures(this.shadowMap);
    }

    protected void bindFrameBuffer() {
        ShadowFrameBuffer.bindFrameBuffer(this.fbo, this.WIDTH, this.HEIGHT);
    }

    protected void unbindFrameBuffer() {
        GL30.glBindFramebuffer(36160, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    protected int getShadowMap() {
        return this.shadowMap;
    }

    private void initialiseFrameBuffer() {
        this.fbo = ShadowFrameBuffer.createFrameBuffer();
        this.shadowMap = ShadowFrameBuffer.createDepthBufferAttachment(this.WIDTH, this.HEIGHT);
        this.unbindFrameBuffer();
    }

    private static void bindFrameBuffer(int frameBuffer, int width, int height) {
        GL11.glBindTexture(3553, 0);
        GL30.glBindFramebuffer(36009, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }

    private static int createFrameBuffer() {
        int frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(36160, frameBuffer);
        GL11.glDrawBuffer(0);
        return frameBuffer;
    }

    private static int createDepthBufferAttachment(int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(3553, texture);
        GL11.glTexImage2D(3553, 0, 33189, width, height, 0, 6402, 5126, null);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        GL32.glFramebufferTexture(36160, 36096, texture, 0);
        return texture;
    }
}


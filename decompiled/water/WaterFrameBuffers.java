/*
 * Decompiled with CFR 0.152.
 */
package water;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class WaterFrameBuffers {
    protected static final int REFLECTION_WIDTH = 1280;
    private static final int REFLECTION_HEIGHT = 720;
    protected static final int REFRACTION_WIDTH = 640;
    private static final int REFRACTION_HEIGHT = 360;
    private int reflectionFrameBuffer;
    private int reflectionTexture;
    private int reflectionDepthBuffer;
    private int refractionFrameBuffer;
    private int refractionDepthTexture;

    public WaterFrameBuffers() {
        this.initialiseReflectionFrameBuffer();
        this.initialiseRefractionFrameBuffer();
    }

    public void cleanUp() {
        GL30.glDeleteFramebuffers(this.reflectionFrameBuffer);
        GL11.glDeleteTextures(this.reflectionTexture);
        GL30.glDeleteRenderbuffers(this.reflectionDepthBuffer);
        GL30.glDeleteFramebuffers(this.refractionFrameBuffer);
        GL11.glDeleteTextures(this.refractionDepthTexture);
    }

    public void bindReflectionFrameBuffer() {
        this.bindFrameBuffer(this.reflectionFrameBuffer, 1280, 720);
    }

    public void bindRefractionFrameBuffer() {
        this.bindFrameBuffer(this.refractionFrameBuffer, 640, 360);
    }

    public void unbindCurrentFrameBuffer() {
        GL30.glBindFramebuffer(36160, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public int getReflectionTexture() {
        return this.reflectionTexture;
    }

    public int getRefractionDepthTexture() {
        return this.refractionDepthTexture;
    }

    private void initialiseReflectionFrameBuffer() {
        this.reflectionFrameBuffer = this.createFrameBuffer();
        this.reflectionTexture = this.createTextureAttachment(1280, 720);
        this.reflectionDepthBuffer = this.createDepthBufferAttachment(1280, 720);
        this.unbindCurrentFrameBuffer();
    }

    private void initialiseRefractionFrameBuffer() {
        this.refractionFrameBuffer = this.createFrameBuffer();
        GL11.glDrawBuffer(0);
        this.refractionDepthTexture = this.createDepthTextureAttachment(640, 360);
        this.unbindCurrentFrameBuffer();
    }

    private void bindFrameBuffer(int frameBuffer, int width, int height) {
        GL11.glBindTexture(3553, 0);
        GL30.glBindFramebuffer(36160, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }

    private int createFrameBuffer() {
        int frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(36160, frameBuffer);
        GL11.glDrawBuffer(36064);
        return frameBuffer;
    }

    private int createTextureAttachment(int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(3553, texture);
        GL11.glTexImage2D(3553, 0, 6407, width, height, 0, 6407, 5121, null);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL32.glFramebufferTexture(36160, 36064, texture, 0);
        return texture;
    }

    private int createDepthTextureAttachment(int width, int height) {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(3553, texture);
        GL11.glTexImage2D(3553, 0, 33191, width, height, 0, 6402, 5126, null);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL32.glFramebufferTexture(36160, 36096, texture, 0);
        return texture;
    }

    private int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(36161, depthBuffer);
        GL30.glRenderbufferStorage(36161, 6402, width, height);
        GL30.glFramebufferRenderbuffer(36160, 36096, 36161, depthBuffer);
        return depthBuffer;
    }
}


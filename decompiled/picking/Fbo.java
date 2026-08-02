/*
 * Decompiled with CFR 0.152.
 */
package picking;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import picking.FboBuilder;

public class Fbo {
    private final int width;
    private final int height;
    private final boolean alpha;
    private int frameBuffer;
    private int colourTexture;
    private int depthTexture;
    private int depthBuffer;
    private int colourBuffer;
    private final boolean antialiased;

    public static FboBuilder newFbo(int width, int height) {
        return new FboBuilder(width, height);
    }

    protected Fbo(int width, int height, FboBuilder.DepthBufferType depthType, boolean useColourBuffer, boolean linear, boolean clamp, boolean alpha, boolean antialiased, int samples) {
        this.width = width;
        this.height = height;
        this.alpha = alpha;
        this.antialiased = antialiased;
        this.initialiseFrameBuffer(depthType, useColourBuffer, linear, clamp, samples);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void cleanUp() {
        GL30.glDeleteFramebuffers(this.frameBuffer);
        GL11.glDeleteTextures(this.colourTexture);
        GL11.glDeleteTextures(this.depthTexture);
        GL30.glDeleteRenderbuffers(this.depthBuffer);
        GL30.glDeleteRenderbuffers(this.colourBuffer);
    }

    public void bindFrameBuffer() {
        GL11.glBindTexture(3553, 0);
        GL30.glBindFramebuffer(36009, this.frameBuffer);
        GL11.glViewport(0, 0, this.width, this.height);
    }

    public void unbindFrameBuffer() {
        GL30.glBindFramebuffer(36160, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public void bindToRead() {
        GL11.glBindTexture(3553, 0);
        GL30.glBindFramebuffer(36008, this.frameBuffer);
        GL11.glReadBuffer(36064);
    }

    public int getColourTexture() {
        return this.colourTexture;
    }

    public int getDepthTexture() {
        return this.depthTexture;
    }

    public void resolveMultisampledFbo(Fbo outputFbo) {
        GL30.glBindFramebuffer(36009, outputFbo.frameBuffer);
        GL30.glBindFramebuffer(36008, this.frameBuffer);
        GL30.glBlitFramebuffer(0, 0, this.width, this.height, 0, 0, outputFbo.width, outputFbo.height, 16640, 9728);
        this.unbindFrameBuffer();
    }

    public void blitToScreen() {
        GL30.glBindFramebuffer(36009, 0);
        GL11.glDrawBuffer(1029);
        GL30.glBindFramebuffer(36008, this.frameBuffer);
        GL30.glBlitFramebuffer(0, 0, this.width, this.height, 0, 0, Display.getWidth(), Display.getHeight(), 16384, 9728);
    }

    private void initialiseFrameBuffer(FboBuilder.DepthBufferType type, boolean useColourBuffer, boolean linear, boolean clamp, int samples) {
        this.createFrameBuffer(useColourBuffer);
        if (!this.antialiased) {
            if (useColourBuffer) {
                this.createTextureAttachment(linear, clamp);
            }
            if (type == FboBuilder.DepthBufferType.RENDER_BUFFER) {
                this.createDepthBufferAttachment(samples);
            } else if (type == FboBuilder.DepthBufferType.TEXTURE) {
                this.createDepthTextureAttachment();
            }
        } else {
            this.attachMutlisampleColourBuffer(samples);
            this.createDepthBufferAttachment(samples);
        }
        this.unbindFrameBuffer();
    }

    private void createFrameBuffer(boolean useColourBuffer) {
        this.frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(36160, this.frameBuffer);
        GL11.glDrawBuffer(useColourBuffer ? 36064 : 0);
    }

    public void createTextureAttachment(boolean linear, boolean clamp) {
        this.colourTexture = GL11.glGenTextures();
        GL11.glBindTexture(3553, this.colourTexture);
        GL11.glTexImage2D(3553, 0, this.alpha ? 32856 : 32849, this.width, this.height, 0, this.alpha ? 6408 : 6407, 5121, null);
        GL11.glTexParameteri(3553, 10240, linear ? 9729 : 9728);
        GL11.glTexParameteri(3553, 10241, linear ? 9729 : 9728);
        GL11.glTexParameteri(3553, 10242, clamp ? 33071 : 10497);
        GL11.glTexParameteri(3553, 10243, clamp ? 33071 : 10497);
        GL30.glFramebufferTexture2D(36160, 36064, 3553, this.colourTexture, 0);
    }

    private void createDepthTextureAttachment() {
        this.depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(3553, this.depthTexture);
        GL11.glTexImage2D(3553, 0, 33190, this.width, this.height, 0, 6402, 5126, null);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL30.glFramebufferTexture2D(36160, 36096, 3553, this.depthTexture, 0);
    }

    private void createDepthBufferAttachment(int samples) {
        this.depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(36161, this.depthBuffer);
        if (this.antialiased) {
            GL30.glRenderbufferStorageMultisample(36161, samples, 33190, this.width, this.height);
        } else {
            GL30.glRenderbufferStorage(36161, 33190, this.width, this.height);
        }
        GL30.glFramebufferRenderbuffer(36160, 36096, 36161, this.depthBuffer);
    }

    private void attachMutlisampleColourBuffer(int samples) {
        this.colourBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(36161, this.colourBuffer);
        GL30.glRenderbufferStorageMultisample(36161, samples, this.alpha ? 32856 : 32849, this.width, this.height);
        GL30.glFramebufferRenderbuffer(36160, 36064, 36161, this.colourBuffer);
    }
}


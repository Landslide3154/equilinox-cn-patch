/*
 * Decompiled with CFR 0.152.
 */
package fbos;

import fbos.Attachment;
import fbos.FboBuilder;
import fbos.FboMsBuilder;
import java.util.Map;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Fbo {
    private final int fboId;
    private final int width;
    private final int height;
    private final Map<Integer, Attachment> colourAttachments;
    private final Attachment depthAttachment;

    protected Fbo(int fboId, int width, int height, Map<Integer, Attachment> attachments, Attachment depthAttachment) {
        this.fboId = fboId;
        this.width = width;
        this.height = height;
        this.colourAttachments = attachments;
        this.depthAttachment = depthAttachment;
    }

    public void blitToScreen(int colourIndex) {
        GL30.glBindFramebuffer(36009, 0);
        GL11.glDrawBuffer(1029);
        GL30.glBindFramebuffer(36008, this.fboId);
        GL11.glReadBuffer(36064 + colourIndex);
        GL30.glBlitFramebuffer(0, 0, this.width, this.height, 0, 0, Display.getWidth(), Display.getHeight(), 16384, 9728);
        GL30.glBindFramebuffer(36160, 0);
    }

    public void blitToFbo(int srcColourIndex, Fbo target, int targetColourIndex) {
        GL30.glBindFramebuffer(36009, target.fboId);
        GL11.glDrawBuffer(36064 + targetColourIndex);
        GL30.glBindFramebuffer(36008, this.fboId);
        GL11.glReadBuffer(36064 + srcColourIndex);
        int bufferBit = this.depthAttachment != null && target.depthAttachment != null ? 16640 : 16384;
        GL30.glBlitFramebuffer(0, 0, this.width, this.height, 0, 0, target.width, target.height, bufferBit, 9728);
        GL30.glBindFramebuffer(36160, 0);
    }

    public int getColourBuffer(int colourIndex) {
        return this.colourAttachments.get(colourIndex).getBufferId();
    }

    public int getDepthBuffer() {
        return this.depthAttachment.getBufferId();
    }

    public boolean isComplete() {
        GL30.glBindFramebuffer(36160, this.fboId);
        boolean complete = GL30.glCheckFramebufferStatus(36160) == 36053;
        GL30.glBindFramebuffer(36160, 0);
        return complete;
    }

    public void bindForRender(int colourIndex) {
        GL30.glBindFramebuffer(36009, this.fboId);
        GL11.glDrawBuffer(36064 + colourIndex);
        GL11.glViewport(0, 0, this.width, this.height);
    }

    public void bindForReading(int colourIndex) {
        GL30.glBindFramebuffer(36008, this.fboId);
        GL11.glReadBuffer(36064 + colourIndex);
    }

    public void unbindAfterReading() {
        GL30.glBindFramebuffer(36008, 0);
    }

    public void unbindAfterRender() {
        GL30.glBindFramebuffer(36009, 0);
        GL11.glDrawBuffer(1029);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public void delete() {
        for (Attachment attachment : this.colourAttachments.values()) {
            attachment.delete();
        }
        if (this.depthAttachment != null) {
            this.depthAttachment.delete();
        }
    }

    public static FboBuilder newFbo(int width, int height) {
        return new FboBuilder(width, height, 0);
    }

    public static FboMsBuilder newMultisampledFbo(int width, int height, int samples) {
        return new FboMsBuilder(new FboBuilder(width, height, samples));
    }
}


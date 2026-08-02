/*
 * Decompiled with CFR 0.152.
 */
package fbos;

import fbos.Attachment;
import fbos.Fbo;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL30;

public class FboBuilder {
    private final int width;
    private final int height;
    private final int samples;
    private Map<Integer, Attachment> colourAttachments = new HashMap<Integer, Attachment>();
    private Attachment depthAttachment;

    protected FboBuilder(int width, int height, int samples) {
        this.width = width;
        this.height = height;
        this.samples = samples;
    }

    public FboBuilder addColourAttachment(int index, Attachment attachment) {
        this.colourAttachments.put(index, attachment);
        return this;
    }

    public FboBuilder addDepthAttachment(Attachment attachment) {
        this.depthAttachment = attachment;
        attachment.setAsDepthAttachment();
        return this;
    }

    public Fbo init() {
        int fboId = this.createFbo();
        this.createColourAttachments();
        this.createDepthAttachment();
        return new Fbo(fboId, this.width, this.height, this.colourAttachments, this.depthAttachment);
    }

    private int createFbo() {
        int fboId = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(36160, fboId);
        return fboId;
    }

    private void createColourAttachments() {
        for (Map.Entry<Integer, Attachment> entry : this.colourAttachments.entrySet()) {
            Attachment attachment = entry.getValue();
            int attachmentId = 36064 + entry.getKey();
            attachment.init(attachmentId, this.width, this.height, this.samples);
        }
    }

    private void createDepthAttachment() {
        if (this.depthAttachment != null) {
            this.depthAttachment.init(36096, this.width, this.height, this.samples);
        }
    }
}


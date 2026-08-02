/*
 * Decompiled with CFR 0.152.
 */
package fbos;

import fbos.Attachment;
import org.lwjgl.opengl.GL30;

public class RenderBufferAttachment
extends Attachment {
    private final int format;

    public RenderBufferAttachment(int format) {
        this.format = format;
    }

    @Override
    public void init(int attachment, int width, int height, int samples) {
        int buffer = GL30.glGenRenderbuffers();
        super.setBufferId(buffer);
        GL30.glBindRenderbuffer(36161, buffer);
        GL30.glRenderbufferStorageMultisample(36161, samples, this.format, width, height);
        GL30.glFramebufferRenderbuffer(36160, attachment, 36161, buffer);
    }

    @Override
    public void delete() {
        GL30.glDeleteRenderbuffers(super.getBufferId());
    }
}


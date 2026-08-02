/*
 * Decompiled with CFR 0.152.
 */
package fbos;

import fbos.Fbo;
import fbos.FboBuilder;
import fbos.RenderBufferAttachment;

public class FboMsBuilder {
    private final FboBuilder fboBuilder;

    protected FboMsBuilder(FboBuilder fboBuilder) {
        this.fboBuilder = fboBuilder;
    }

    public FboMsBuilder addColourAttachment(int index, RenderBufferAttachment attachment) {
        this.fboBuilder.addColourAttachment(index, attachment);
        return this;
    }

    public FboMsBuilder addDepthAttachment(RenderBufferAttachment attachment) {
        this.fboBuilder.addDepthAttachment(attachment);
        return this;
    }

    public Fbo init() {
        return this.fboBuilder.init();
    }
}


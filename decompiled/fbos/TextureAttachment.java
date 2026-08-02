/*
 * Decompiled with CFR 0.152.
 */
package fbos;

import fbos.Attachment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class TextureAttachment
extends Attachment {
    private final int format;
    private final boolean nearestFiltering;
    private final boolean clampEdges;

    public TextureAttachment(int format) {
        this.format = format;
        this.nearestFiltering = false;
        this.clampEdges = false;
    }

    public TextureAttachment(int format, boolean nearestFiltering, boolean clampEdges) {
        this.format = format;
        this.nearestFiltering = nearestFiltering;
        this.clampEdges = clampEdges;
    }

    @Override
    public void delete() {
        GL11.glDeleteTextures(this.getBufferId());
    }

    @Override
    public void init(int attachment, int width, int height, int samples) {
        int texture = GL11.glGenTextures();
        super.setBufferId(texture);
        GL11.glBindTexture(3553, texture);
        this.indicateStorageType(width, height);
        this.setTextureParams();
        GL30.glFramebufferTexture2D(36160, attachment, 3553, texture, 0);
    }

    private void indicateStorageType(int width, int height) {
        if (this.isDepthAttachment()) {
            GL11.glTexImage2D(3553, 0, this.format, width, height, 0, 6402, 5126, null);
        } else {
            GL11.glTexImage2D(3553, 0, this.format, width, height, 0, 6408, 5121, null);
        }
    }

    private void setTextureParams() {
        int filterType = this.nearestFiltering ? 9728 : 9729;
        GL11.glTexParameteri(3553, 10240, filterType);
        GL11.glTexParameteri(3553, 10241, filterType);
        int wrapType = this.clampEdges ? 33071 : 10497;
        GL11.glTexParameteri(3553, 10242, wrapType);
        GL11.glTexParameteri(3553, 10243, wrapType);
    }
}


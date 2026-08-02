/*
 * Decompiled with CFR 0.152.
 */
package textures;

import glRequestProcessing.GlRequest;
import glRequestProcessing.GlRequestProcessor;
import resourceProcessing.ResourceRequest;
import textures.Texture;
import textures.TextureBuilder;
import textures.TextureData;
import textures.TextureManager;

public class TextureLoadRequest
implements ResourceRequest,
GlRequest {
    private Texture texture;
    private TextureBuilder builder;
    private TextureData data;

    protected TextureLoadRequest(Texture texture, TextureBuilder builder) {
        this.texture = texture;
        this.builder = builder;
    }

    @Override
    public void doResourceRequest() {
        this.data = TextureManager.decodeTextureFile(this.builder.getFile());
        GlRequestProcessor.sendRequest(this);
    }

    @Override
    public void executeGlRequest() {
        int texID = TextureManager.loadTextureToOpenGL(this.data, this.builder);
        this.texture.setTextureID(texID);
    }
}


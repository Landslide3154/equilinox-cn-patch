/*
 * Decompiled with CFR 0.152.
 */
package textures;

import glRequestProcessing.GlRequest;
import textures.TextureManager;

public class TextureDeleteRequest
implements GlRequest {
    private int textureID;

    public TextureDeleteRequest(int textureID) {
        this.textureID = textureID;
    }

    @Override
    public void executeGlRequest() {
        TextureManager.deleteTexture(this.textureID);
    }
}


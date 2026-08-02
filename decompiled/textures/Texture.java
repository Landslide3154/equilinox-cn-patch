/*
 * Decompiled with CFR 0.152.
 */
package textures;

import glRequestProcessing.GlRequestProcessor;
import textures.TextureBuilder;
import textures.TextureDeleteRequest;
import utils.MyFile;

public class Texture {
    private boolean loaded = false;
    private int textureID;

    protected Texture() {
    }

    public void setTextureID(int id) {
        this.textureID = id;
        this.loaded = true;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public int getID() {
        return this.textureID;
    }

    public void delete() {
        this.loaded = false;
        GlRequestProcessor.sendRequest(new TextureDeleteRequest(this.textureID));
    }

    public static TextureBuilder newTexture(MyFile file) {
        return new TextureBuilder(file);
    }

    public static Texture getEmptyTexture() {
        return new Texture();
    }
}


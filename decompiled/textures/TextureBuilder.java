/*
 * Decompiled with CFR 0.152.
 */
package textures;

import glRequestProcessing.GlRequestProcessor;
import resourceProcessing.RequestProcessor;
import textures.Texture;
import textures.TextureLoadRequest;
import toolbox.Colour;
import utils.MyFile;

public class TextureBuilder {
    private boolean clampEdges = false;
    private boolean mipmap = true;
    private boolean anisotropic = true;
    private boolean nearest = false;
    private boolean clampToBorder = false;
    private Colour borderColour = new Colour(0.0f, 0.0f, 0.0f, 0.0f);
    private MyFile file;

    protected TextureBuilder(MyFile textureFile) {
        this.file = textureFile;
    }

    public TextureBuilder clampEdges() {
        this.clampEdges = true;
        this.clampToBorder = false;
        return this;
    }

    public TextureBuilder clampToBorder(float r, float g, float b, float a) {
        this.borderColour.setColour(r, g, b, a);
        this.clampToBorder = true;
        this.clampEdges = false;
        return this;
    }

    public TextureBuilder noMipMap() {
        this.mipmap = false;
        this.anisotropic = false;
        return this;
    }

    public TextureBuilder nearestFiltering() {
        this.mipmap = false;
        this.nearest = true;
        return this;
    }

    public TextureBuilder noFiltering() {
        this.anisotropic = false;
        return this;
    }

    protected boolean isClampEdges() {
        return this.clampEdges;
    }

    protected Colour getBorderColour() {
        return this.borderColour;
    }

    protected boolean isClampToBorder() {
        return this.clampToBorder;
    }

    protected boolean isMipmap() {
        return this.mipmap;
    }

    protected boolean isAnisotropic() {
        return this.anisotropic;
    }

    protected boolean isNearest() {
        return this.nearest;
    }

    protected MyFile getFile() {
        return this.file;
    }

    public Texture createInBackground() {
        Texture texture = new Texture();
        RequestProcessor.sendRequest(new TextureLoadRequest(texture, this));
        return texture;
    }

    public Texture createInSecondThread() {
        Texture texture = new Texture();
        TextureLoadRequest request = new TextureLoadRequest(texture, this);
        request.doResourceRequest();
        GlRequestProcessor.sendRequest(request);
        return texture;
    }

    public Texture create() {
        Texture texture = new Texture();
        TextureLoadRequest request = new TextureLoadRequest(texture, this);
        request.doResourceRequest();
        request.executeGlRequest();
        return texture;
    }
}


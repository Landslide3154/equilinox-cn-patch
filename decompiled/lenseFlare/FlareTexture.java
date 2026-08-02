/*
 * Decompiled with CFR 0.152.
 */
package lenseFlare;

import textures.Texture;

public class FlareTexture {
    protected final Texture texture;
    protected final float relativeScale;

    public FlareTexture(Texture texture, float relativeScale) {
        this.texture = texture;
        this.relativeScale = relativeScale;
    }

    public FlareTexture(Texture texture) {
        this.texture = texture;
        this.relativeScale = 1.0f;
    }
}


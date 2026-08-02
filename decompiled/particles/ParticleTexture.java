/*
 * Decompiled with CFR 0.152.
 */
package particles;

import textures.Texture;

public class ParticleTexture {
    private Texture texture;
    private int numberOfRows;
    private boolean additive;
    private boolean glows = false;

    public ParticleTexture(Texture texture, int numberOfRows, boolean additive) {
        this.texture = texture;
        this.numberOfRows = numberOfRows;
        this.additive = additive;
    }

    public ParticleTexture setGlowy() {
        this.glows = true;
        return this;
    }

    public boolean glows() {
        return this.glows;
    }

    protected int getTextureID() {
        return this.texture.getID();
    }

    protected int getNumberOfRows() {
        return this.numberOfRows;
    }

    protected boolean isAdditive() {
        return this.additive;
    }
}


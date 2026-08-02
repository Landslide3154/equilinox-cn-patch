/*
 * Decompiled with CFR 0.152.
 */
package guis;

import basics.DisplayManager;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.ValueDriver;

public class GuiTexture {
    private Texture texture;
    private Vector2f position = new Vector2f();
    private Vector2f scale = new Vector2f();
    private boolean usesBlur = false;
    private int[] scissorTestInfo = null;
    private ValueDriver alphaDriver = new ConstantDriver(1.0f);
    private float alpha = 1.0f;
    private boolean additive = false;
    private boolean flipTexture = false;
    private Colour overrideColour;

    public GuiTexture(Texture texture) {
        this.texture = texture;
    }

    public GuiTexture(Texture texture, boolean flip) {
        this.texture = texture;
        this.flipTexture = flip;
    }

    public boolean usesBlur() {
        return this.usesBlur;
    }

    public void setAdditive() {
        this.additive = true;
    }

    public void update() {
        this.alpha = this.alphaDriver.update(DisplayManager.getDeltaSeconds());
    }

    public boolean hasOverrideColour() {
        return this.overrideColour != null;
    }

    public boolean isAdditive() {
        return this.additive;
    }

    public void setBlurry(boolean blur) {
        this.usesBlur = blur;
    }

    public void flip(boolean flip) {
        this.flipTexture = flip;
    }

    public void setClippingBounds(int[] clippingBounds) {
        this.scissorTestInfo = clippingBounds;
    }

    public Colour getOverrideColour() {
        return this.overrideColour;
    }

    public void setOverrideColour(Colour colour) {
        this.overrideColour = colour;
    }

    public int[] getClippingBounds() {
        return this.scissorTestInfo;
    }

    public void setAlphaDriver(ValueDriver driver) {
        this.alphaDriver = driver;
    }

    public void setPosition(float x, float y, float width, float height) {
        this.position.set(x, y);
        this.scale.set(width, height);
    }

    public void setWidth(float scaleX) {
        this.scale.x = scaleX;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public boolean isFlipTexture() {
        return this.flipTexture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2f getPosition() {
        return this.position;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public ValueDriver getAlphaDriver() {
        return this.alphaDriver;
    }

    public Vector2f getScale() {
        return this.scale;
    }
}


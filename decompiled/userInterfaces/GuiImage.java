/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;

public class GuiImage
extends GuiComponent {
    private GuiTexture image;

    public GuiImage(GuiTexture image) {
        this.image = image;
    }

    public GuiImage(Texture image) {
        this.image = new GuiTexture(image);
    }

    public GuiTexture getTexture() {
        return this.image;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.image.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void setTextureClippingBounds(int[] bounds) {
        this.image.setClippingBounds(bounds);
    }

    @Override
    protected void updateSelf() {
        this.image.update();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.image);
    }
}


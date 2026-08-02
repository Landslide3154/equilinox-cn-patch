/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import visualFxDrivers.ConstantDriver;

public class IconButtonUi
extends GuiClickable {
    private GuiTexture iconTexture;

    public IconButtonUi(Texture icon) {
        super(1.0f);
        this.iconTexture = new GuiTexture(icon);
        this.addListener(ColourPalette.WHITE, ColourPalette.GREEN);
    }

    public IconButtonUi(Texture icon, Colour original, Colour moused) {
        super(1.0f);
        this.iconTexture = new GuiTexture(icon);
        this.iconTexture.setOverrideColour(original);
        this.addListener(original, moused);
    }

    public void setAlpha(float alpha) {
        this.iconTexture.setAlphaDriver(new ConstantDriver(alpha));
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.iconTexture.update();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.iconTexture.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.iconTexture);
    }

    @Override
    protected void setTextureClippingBounds(int[] bounds) {
        this.iconTexture.setClippingBounds(bounds);
    }

    private void addListener(final Colour original, final Colour moused) {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    IconButtonUi.this.iconTexture.setOverrideColour(moused);
                } else if (event.isMouseOff()) {
                    IconButtonUi.this.iconTexture.setOverrideColour(original);
                }
            }
        });
    }
}


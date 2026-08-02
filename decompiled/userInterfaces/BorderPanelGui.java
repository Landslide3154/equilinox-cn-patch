/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;

public class BorderPanelGui
extends GuiComponent {
    private int pixels;
    private GuiTexture left;
    private GuiTexture right;
    private GuiTexture top;
    private GuiTexture bottom;

    public BorderPanelGui(int pixels, Colour colour) {
        this.pixels = pixels;
        this.left = new GuiTexture(GuiRepository.BLOCK);
        this.left.setOverrideColour(colour);
        this.right = new GuiTexture(GuiRepository.BLOCK);
        this.right.setOverrideColour(colour);
        this.top = new GuiTexture(GuiRepository.BLOCK);
        this.top.setOverrideColour(colour);
        this.bottom = new GuiTexture(GuiRepository.BLOCK);
        this.bottom.setOverrideColour(colour);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        float pixelHeight = (float)this.pixels / (float)DisplayManager.getUiHeight();
        float pixelWidth = (float)this.pixels / (float)DisplayManager.getUiWidth();
        this.left.setPosition(position.x, position.y, pixelWidth, scale.y);
        this.top.setPosition(position.x, position.y, scale.x, pixelHeight);
        this.right.setPosition(position.x + scale.x - pixelWidth, position.y, pixelWidth, scale.y);
        this.bottom.setPosition(position.x, position.y + scale.y - pixelHeight, scale.x, pixelHeight);
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.left);
        data.addTexture(this.getLevel(), this.right);
        data.addTexture(this.getLevel(), this.top);
        data.addTexture(this.getLevel(), this.bottom);
    }
}


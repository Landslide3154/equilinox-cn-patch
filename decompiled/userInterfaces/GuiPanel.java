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
import textures.Texture;
import toolbox.Colour;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class GuiPanel
extends GuiComponent {
    private GuiTexture inner;
    private GuiTexture outer;
    private boolean border = false;
    private boolean hideBorder = false;
    private int borderPixels;
    private boolean plain = false;

    public GuiPanel() {
        this.plain = true;
    }

    public GuiPanel(Colour colour) {
        this.outer = new GuiTexture(GuiRepository.BLOCK);
        this.outer.setOverrideColour(colour);
    }

    public GuiPanel(Texture innerTexture, Colour colour) {
        this.outer = new GuiTexture(innerTexture);
        this.outer.setOverrideColour(colour);
    }

    public GuiPanel(Colour colour, float alpha) {
        this.outer = new GuiTexture(GuiRepository.BLOCK);
        this.outer.setOverrideColour(colour);
        this.outer.setAlphaDriver(new ConstantDriver(alpha));
    }

    public GuiPanel(Colour colour, int borderPixels) {
        this.inner = new GuiTexture(GuiRepository.BLOCK);
        this.inner.setOverrideColour(colour);
        this.borderPixels = borderPixels;
        this.hideBorder = true;
        this.border = true;
    }

    public GuiPanel(Colour colour, int borderPixels, Colour borderColour) {
        this.inner = new GuiTexture(GuiRepository.BLOCK);
        this.inner.setOverrideColour(colour);
        this.outer = new GuiTexture(GuiRepository.BLOCK);
        this.outer.setOverrideColour(borderColour);
        this.borderPixels = borderPixels;
        this.border = true;
    }

    public GuiPanel(Colour colour, int borderPixels, Colour borderColour, boolean blurry) {
        this.inner = new GuiTexture(GuiRepository.BLOCK);
        this.inner.setOverrideColour(colour);
        this.inner.setBlurry(true);
        this.inner.setAlphaDriver(new ConstantDriver(0.75f));
        this.outer = new GuiTexture(GuiRepository.BLOCK);
        this.outer.setOverrideColour(borderColour);
        this.borderPixels = borderPixels;
        this.border = true;
    }

    public GuiPanel(Texture innerTexture, Colour colour, int borderPixels, Colour borderColour) {
        this.inner = new GuiTexture(innerTexture);
        this.inner.setOverrideColour(colour);
        this.outer = new GuiTexture(GuiRepository.BLOCK);
        this.outer.setOverrideColour(borderColour);
        this.borderPixels = borderPixels;
        this.border = true;
    }

    public void setBlurry() {
        this.outer.setBlurry(true);
    }

    public void setAlphaDriver(ValueDriver driver) {
        if (this.inner != null) {
            this.inner.setAlphaDriver(driver);
        }
        if (this.outer != null) {
            this.outer.setAlphaDriver(driver);
        }
    }

    public void fadeOut(float time) {
        if (this.inner != null) {
            this.inner.setAlphaDriver(new SlideDriver(this.inner.getAlpha(), 0.0f, time));
        }
        if (this.outer != null) {
            this.outer.setAlphaDriver(new SlideDriver(this.outer.getAlpha(), 0.0f, time));
        }
    }

    public void showBorder(boolean show) {
        this.hideBorder = !show;
    }

    public void setColour(Colour colour) {
        if (this.border) {
            this.inner.setOverrideColour(colour.duplicate());
        } else {
            this.outer.setOverrideColour(colour);
        }
    }

    public void setBorderColour(Colour colour) {
        if (!this.hideBorder) {
            this.outer.setOverrideColour(colour);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        if (this.plain) {
            return;
        }
        if (!this.hideBorder) {
            this.outer.setPosition(position.x, position.y, scale.x, scale.y);
        }
        if (this.border) {
            float borderWidth = (float)this.borderPixels / ((float)DisplayManager.getUiWidth() * scale.x);
            float borderHeight = (float)this.borderPixels / ((float)DisplayManager.getUiHeight() * scale.y);
            this.inner.setPosition(position.x + borderWidth * scale.x, position.y + borderHeight * scale.y, (1.0f - borderWidth * 2.0f) * scale.x, (1.0f - borderHeight * 2.0f) * scale.y);
        }
    }

    @Override
    protected void updateSelf() {
        if (this.plain) {
            return;
        }
        if (!this.hideBorder) {
            this.outer.update();
        }
        if (this.border) {
            this.inner.update();
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (this.plain) {
            return;
        }
        if (!this.hideBorder) {
            data.addTexture(this.getLevel(), this.outer);
        }
        if (this.border) {
            data.addTexture(this.getLevel(), this.inner);
        }
    }
}


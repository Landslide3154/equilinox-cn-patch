/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Maths;
import toolbox.MyMouse;
import userInterfaces.GuiBar;

public class GuiScrollBar
extends GuiComponent {
    private static final int PX_SCROLL_PER_TICK = 28;
    private final GuiTexture background = new GuiTexture(GuiRepository.BLOCK);
    private float currentY = 0.0f;
    private float scaleFactor = 1.0f;
    private GuiBar bar;
    private float scrollWheel = 0.0f;

    public GuiScrollBar(float scaleFactor) {
        this.background.setOverrideColour(ColourPalette.LIGHT_GREY);
        this.setScaleValue(scaleFactor);
    }

    @Override
    protected void init() {
        super.init();
        this.bar = new GuiBar();
        super.addComponent(this.bar, 0.0f, this.currentY, 1.0f, 1.0f / this.scaleFactor);
    }

    protected void setScrollWheelAmount(float scrollWheel) {
        this.scrollWheel = scrollWheel;
    }

    protected void setScaleFactor(float scaleFactor) {
        this.setScaleValue(scaleFactor);
        this.currentY = 0.0f;
        if (this.bar != null) {
            this.updateBarPosition();
        }
    }

    protected float getDesiredRelativeY() {
        return -this.currentY * this.scaleFactor;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        this.checkBarMoving();
    }

    private void setScaleValue(float factor) {
        this.scaleFactor = Math.max(factor, 1.0f);
        this.show(factor > 1.0f);
    }

    private void updateBarPosition() {
        this.bar.setRelativeY(this.currentY);
        this.bar.setRelativeScaleY(1.0f / this.scaleFactor);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    private void checkBarMoving() {
        if (this.bar.isGrabbed()) {
            this.determineScroll(this.bar.getGrabPosition());
        } else if (super.isMouseOver() && MyMouse.getActiveMouse().isLeftClick() && !this.bar.isMouseOver()) {
            this.determineScroll(0.5f);
        } else {
            this.doMouseWheelScroll();
        }
    }

    private void determineScroll(float grabPosition) {
        float mousePosition = this.getRelativeMouseY();
        float barPosition = mousePosition - grabPosition * this.bar.getRelativeScaleY();
        this.currentY = Maths.clamp(barPosition, 0.0f, 1.0f - 1.0f / this.scaleFactor);
        this.updateBarPosition();
    }

    private void doMouseWheelScroll() {
        this.currentY -= this.scrollWheel * 28.0f / (super.getPixelHeight() * this.scaleFactor);
        this.currentY = Maths.clamp(this.currentY, 0.0f, 1.0f - 1.0f / this.scaleFactor);
        this.updateBarPosition();
    }
}


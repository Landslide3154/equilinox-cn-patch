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
import toolbox.MyMouse;
import userInterfaces.GuiClippingPanel;
import userInterfaces.GuiScrollBar;
import visualFxDrivers.ConstantDriver;

public class GuiScrollPanel
extends GuiClippingPanel {
    public static final int BAR_WIDTH_PIXELS = 10;
    private GuiComponent contentPanel;
    private GuiTexture background;
    private GuiScrollBar scrollBar;
    private boolean selfScroll = false;
    private float selfScrollY = 0.0f;
    private float selfScrollSpeed;
    private float contentRelY;

    public GuiScrollPanel(Colour colour) {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(colour);
    }

    public GuiScrollPanel(Colour colour, float alpha) {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(colour);
        this.background.setAlphaDriver(new ConstantDriver(alpha));
    }

    public GuiScrollPanel(Colour colour, float alpha, float selfScrollSpeed) {
        this.selfScrollSpeed = selfScrollSpeed;
        this.selfScroll = true;
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(colour);
        this.background.setAlphaDriver(new ConstantDriver(alpha));
    }

    @Override
    protected void init() {
        super.init();
        float width = 10.0f / super.getPixelWidth();
        if (!this.selfScroll) {
            this.scrollBar = new GuiScrollBar(1.0f);
            super.addComponent(this.scrollBar, 1.0f - width, 0.0f, width, 1.0f);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    public void setContents(GuiComponent contents, float relHeight) {
        this.removeContents();
        this.contentRelY = relHeight;
        this.contentPanel = contents;
        if (!this.selfScroll) {
            this.scrollBar.setScaleFactor(relHeight);
        }
        super.addComponent(contents, 0.0f, 0.0f, 1.0f, relHeight);
    }

    public void resize(float relHeight) {
        this.contentRelY = relHeight;
        if (!this.selfScroll) {
            this.scrollBar.setScaleFactor(relHeight);
        }
    }

    @Override
    protected void updateSelf() {
        this.background.update();
        if (this.contentPanel == null) {
            return;
        }
        if (!this.selfScroll) {
            this.contentPanel.setRelativeY(this.scrollBar.getDesiredRelativeY());
            if (super.isMouseOver()) {
                this.scrollBar.setScrollWheelAmount(MyMouse.getActiveMouse().getDWheelSigned());
            } else {
                this.scrollBar.setScrollWheelAmount(0.0f);
            }
        } else {
            this.selfScrollY -= DisplayManager.getDeltaSeconds() * this.selfScrollSpeed;
            if (this.selfScrollY < -this.contentRelY) {
                this.selfScrollY = 1.0f;
            }
            this.contentPanel.setRelativeY(this.selfScrollY);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    private void removeContents() {
        if (this.contentPanel != null) {
            this.contentPanel.remove();
            this.contentPanel = null;
        }
    }
}


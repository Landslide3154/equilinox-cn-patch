/*
 * Decompiled with CFR 0.152.
 */
package toolTips;

import basics.DisplayManager;
import guis.GuiMaster;
import mainGuis.ColourPalette;
import toolTips.DescriptionPanel;
import toolTips.TitledPanelGui;
import toolTips.ToolTipInfo;
import toolbox.MyMouse;

public class ToolTip
extends TitledPanelGui {
    private static final float FADE_TIME = 0.1f;
    private static final int WIDTH_PIXELS = 240;
    private static final float WIDTH = 240.0f / (float)DisplayManager.getUiWidth();
    private static final int HEIGHT_PIXELS = 120;
    private static final float HEIGHT = 120.0f / (float)DisplayManager.getUiHeight();
    private static final int SAFETY_PADDING = 20;
    private static final float SAFETY_PADDING_X = 20.0f / (float)DisplayManager.getUiWidth();
    private static final float SAFETY_PADDING_Y = 20.0f / (float)DisplayManager.getUiHeight();
    private static final int OFFSET_PIXELS = 5;
    private static final float X_OFFSET = 5.0f / (float)DisplayManager.getUiWidth();
    private static final float Y_OFFSET = 5.0f / (float)DisplayManager.getUiHeight();
    private final ToolTipInfo tipInfo;
    private final DescriptionPanel descPanel;
    private boolean bottom = false;
    private boolean right = false;
    private boolean fading = false;
    private float fadeTime = 0.0f;

    protected ToolTip(ToolTipInfo info) {
        super(info.title, ColourPalette.DARK_GREY, ColourPalette.BASE_BLUE);
        this.descPanel = new DescriptionPanel(info.description, this);
        super.setContent(this.descPanel);
        this.tipInfo = info;
        MyMouse mouse = MyMouse.getActiveMouse();
        super.setRenderLevel(1);
        GuiMaster.addComponent(this, mouse.getX(), mouse.getY(), WIDTH, HEIGHT);
    }

    public void fadeOut() {
        if (this.fading) {
            return;
        }
        this.fading = true;
        this.descPanel.fadeOut(0.1f);
        super.fadeOut(0.1f);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void resize(float textHeight) {
        super.resize(textHeight);
        this.calculateRelativePosition();
        this.determinePosition();
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.calculateRelativePosition();
        if (this.fading) {
            this.updateFade();
        }
    }

    private void updateFade() {
        this.fadeTime += DisplayManager.getDeltaSeconds();
        if (this.fadeTime >= 0.1f) {
            this.remove();
        }
    }

    private void calculateRelativePosition() {
        MyMouse mouse = MyMouse.getActiveMouse();
        float xPos = this.right ? mouse.getX() + X_OFFSET : mouse.getX() - super.getScale().x - X_OFFSET;
        float yPos = this.bottom ? mouse.getY() + Y_OFFSET : mouse.getY() - super.getScale().y - Y_OFFSET;
        super.setRelativePosition(xPos, yPos);
    }

    private void determinePosition() {
        this.bottom = this.tipInfo.prefersBottom == this.isVerticalClear(this.tipInfo.prefersBottom);
        this.right = this.tipInfo.prefersRight == this.isHorizontalClear(this.tipInfo.prefersRight);
    }

    private boolean isHorizontalClear(boolean right) {
        MyMouse mouse = MyMouse.getActiveMouse();
        if (right) {
            return mouse.getX() + super.getScale().x + X_OFFSET < 1.0f - SAFETY_PADDING_X;
        }
        return mouse.getX() - (super.getScale().x + X_OFFSET) > SAFETY_PADDING_X;
    }

    private boolean isVerticalClear(boolean bottom) {
        MyMouse mouse = MyMouse.getActiveMouse();
        if (bottom) {
            return mouse.getY() + super.getScale().y + Y_OFFSET < 1.0f - SAFETY_PADDING_Y;
        }
        return mouse.getY() - (super.getScale().y + Y_OFFSET) > SAFETY_PADDING_Y;
    }
}


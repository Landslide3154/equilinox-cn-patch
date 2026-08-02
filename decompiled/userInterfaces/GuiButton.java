/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolTips.ToolTip;
import toolTips.ToolTipInfo;
import toolbox.MyKeyboard;
import toolbox.MyMouse;
import userInterfaces.Listener;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SinWaveDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class GuiButton
extends GuiComponent {
    private static final float TOOL_TIP_TIME = 0.5f;
    private static final int NONE = -1;
    private static final float MAX_SIZE = 1.15f;
    private static final float CHANGE_TIME = 0.07f;
    private int keyBinding = -1;
    private boolean mustHoldKey = false;
    private GuiTexture texture;
    private boolean mouseOver = false;
    private ValueDriver scaleDriver = new ConstantDriver(1.0f);
    private float currentScale = 1.0f;
    private boolean toggleButton = false;
    private boolean toggledOn = false;
    private boolean manualTurnOffDisabled = false;
    private boolean firstUpdate = true;
    private boolean locked = false;
    private ToolTipInfo toolTipInfo;
    private boolean awaitingTip = false;
    private float toolTipTime = 0.5f;
    private ToolTip currentToolTip;
    private List<Listener> listeners = new ArrayList<Listener>();

    public GuiButton(Texture texture) {
        this.texture = new GuiTexture(texture);
    }

    public GuiButton(Texture texture, boolean toggleButton) {
        this.texture = new GuiTexture(texture);
        this.toggleButton = toggleButton;
    }

    public void setToolTip(ToolTipInfo toolTipInfo) {
        this.toolTipInfo = toolTipInfo;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void wobble(float factor, float time) {
        this.scaleDriver = new SinWaveDriver(1.0f, factor, time);
    }

    public void stopWobble() {
        this.scaleDriver = new SlideDriver(this.currentScale, 1.0f, 0.07f);
    }

    public void lock(boolean locked) {
        if (this.locked == locked) {
            return;
        }
        this.locked = locked;
        if (locked) {
            this.mouseOffOccurred();
            this.texture.setOverrideColour(ColourPalette.DARK_GREY);
            this.texture.setAlphaDriver(new ConstantDriver(0.2f));
        } else {
            this.texture.setOverrideColour(null);
            this.texture.setAlphaDriver(new ConstantDriver(1.0f));
        }
    }

    @Override
    public void show(boolean visible) {
        super.show(visible);
        if (!visible) {
            this.mouseOver = false;
            this.scaleDriver = new ConstantDriver(1.0f);
        }
    }

    public boolean isToggledOn() {
        return this.toggledOn;
    }

    public void setKeyBinding(int key, boolean mustHold) {
        this.keyBinding = key;
        this.mustHoldKey = mustHold;
    }

    public GuiTexture getGuiTexture() {
        return this.texture;
    }

    public void disableManualTurnOff() {
        this.manualTurnOffDisabled = true;
    }

    public void toggle() {
        this.toggledOn = !this.toggledOn;
        this.changeButtonState();
        for (Listener listener : this.listeners) {
            listener.eventOccurred(this.toggledOn);
        }
    }

    public void forceToggleOnNoEvent() {
        if (!this.toggledOn) {
            this.toggledOn = true;
            this.changeButtonState();
        }
    }

    @Override
    protected void updateSelf() {
        this.texture.update();
        if (this.firstUpdate) {
            this.firstUpdate = false;
            return;
        }
        if (!this.locked) {
            this.checkKeyBinding();
        }
        if (!this.toggledOn && !this.locked) {
            this.dealWithMouseOverEvents();
        }
        if (!this.locked) {
            this.checkClickEvent();
        }
        this.currentScale = this.scaleDriver.update(DisplayManager.getDeltaSeconds());
        this.updatePositions();
        this.updateToolTip();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.updatePositions();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.texture);
    }

    private void updateToolTip() {
        if (this.awaitingTip) {
            this.toolTipTime -= DisplayManager.getDeltaSeconds();
            if (this.toolTipTime <= 0.0f) {
                this.awaitingTip = false;
                this.currentToolTip = this.toolTipInfo.createToolTip();
            }
        }
    }

    private void changeButtonState() {
        if (this.toggledOn) {
            this.scaleDriver = new ConstantDriver(1.15f);
            this.texture.setOverrideColour(ColourPalette.GREEN);
        } else {
            this.scaleDriver = new SlideDriver(this.currentScale, 1.0f, 0.07f);
            this.texture.setOverrideColour(null);
        }
    }

    private void checkClickEvent() {
        if (this.isMouseOver() && MyMouse.getActiveMouse().isLeftClick()) {
            this.removeToolTip();
            this.dealWithButtonAction();
        }
    }

    private void dealWithMouseOverEvents() {
        if (this.isMouseOver() && !this.mouseOver) {
            this.mouseOverOccurred();
        } else if (!this.isMouseOver() && this.mouseOver) {
            this.mouseOffOccurred();
        }
    }

    private void dealWithButtonAction() {
        if (this.toggleButton) {
            if (!this.toggledOn || !this.manualTurnOffDisabled) {
                this.toggle();
            }
        } else {
            for (Listener listener : this.listeners) {
                listener.eventOccurred(true);
            }
        }
    }

    private void mouseOverOccurred() {
        this.mouseOver = true;
        this.awaitingTip = this.toolTipInfo != null;
        this.scaleDriver = new SlideDriver(this.currentScale, 1.15f, 0.07f);
    }

    private void mouseOffOccurred() {
        this.mouseOver = false;
        this.scaleDriver = new SlideDriver(this.currentScale, 1.0f, 0.07f);
        this.removeToolTip();
    }

    private void removeToolTip() {
        this.toolTipTime = 0.5f;
        this.awaitingTip = false;
        if (this.currentToolTip != null) {
            this.currentToolTip.fadeOut();
            this.currentToolTip = null;
        }
    }

    private void updatePositions() {
        Vector2f position = super.getPosition();
        Vector2f scale = super.getScale();
        float currentX = this.calculateScaledPosition(position.x, scale.x);
        float currentY = this.calculateScaledPosition(position.y, scale.y);
        float currentScaleX = scale.x * this.currentScale;
        float currentScaleY = scale.y * this.currentScale;
        this.texture.setPosition(currentX, currentY, currentScaleX, currentScaleY);
    }

    private float calculateScaledPosition(float originalPos, float originalScale) {
        float change = originalScale * this.currentScale - originalScale;
        return originalPos - change / 2.0f;
    }

    private void checkKeyBinding() {
        if (this.keyBinding == -1) {
            return;
        }
        if (!(!MyKeyboard.getKeyboard().keyDownEventOccurred(this.keyBinding) || this.toggledOn && this.mustHoldKey)) {
            this.dealWithButtonAction();
        }
        if (this.mustHoldKey && MyKeyboard.getKeyboard().keyUpEventOccurred(this.keyBinding) && this.toggledOn) {
            this.dealWithButtonAction();
        }
    }
}


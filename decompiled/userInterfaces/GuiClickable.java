/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import audio.SoundMaestro;
import basics.DisplayManager;
import guis.GuiComponent;
import java.util.ArrayList;
import java.util.List;
import mainGuis.GuiSounds;
import org.lwjgl.util.vector.Vector2f;
import toolTips.ToolTip;
import toolTips.ToolTipInfo;
import toolbox.MyKeyboard;
import toolbox.MyMouse;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import visualFxDrivers.BounceDriver;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SinWaveDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public abstract class GuiClickable
extends GuiComponent {
    private static final float TOOL_TIP_TIME = 0.5f;
    protected static final float MAX_SIZE = 1.15f;
    protected static final float CHANGE_TIME = 0.07f;
    private float scaleFactor = 1.15f;
    private boolean mouseOver;
    private ValueDriver scaleDriver = new ConstantDriver(1.0f);
    private float currentScale = 1.0f;
    private Vector2f originalPosition;
    private Vector2f originalScale;
    private List<ClickListener> listenersToAdd = new ArrayList<ClickListener>();
    private List<ClickListener> listeners = new ArrayList<ClickListener>();
    private Vector2f unclickRelPos;
    private Vector2f unclickRelScale;
    private Vector2f unclickPos = new Vector2f();
    private Vector2f unclickScale = new Vector2f();
    private boolean maintainPosX = false;
    private boolean blocked = false;
    private boolean toggleButton = false;
    private boolean toggledOn = false;
    private boolean firingListeners = false;
    private boolean manualTurnOffDisabled = false;
    private ToolTipInfo toolTipInfo;
    private boolean awaitingTip = false;
    private float toolTipTime = 0.5f;
    private ToolTip currentToolTip;
    private Integer hotkey = null;
    private boolean muted = false;

    public GuiClickable() {
    }

    public void mute() {
        this.muted = true;
    }

    public GuiClickable(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public GuiClickable(boolean toggleButton) {
        this.toggleButton = toggleButton;
    }

    public GuiClickable(boolean toggleButton, float scaleFactor) {
        this.toggleButton = toggleButton;
        this.scaleFactor = scaleFactor;
    }

    public void setToolTip(ToolTipInfo toolTipInfo) {
        this.toolTipInfo = toolTipInfo;
    }

    public void setScaleFactor(float scale) {
        this.scaleFactor = scale;
    }

    public void setMaintainPositionX(boolean maintain) {
        this.maintainPosX = maintain;
    }

    public void addListener(ClickListener listener) {
        if (this.firingListeners) {
            this.listenersToAdd.add(listener);
        } else {
            this.listeners.add(listener);
        }
    }

    public void setUnclickableRegion(Vector2f pos, Vector2f scale) {
        this.unclickRelPos = pos;
        this.unclickRelScale = scale;
        this.calculateUnclickRegion(super.getPosition(), super.getScale());
    }

    @Override
    public void setRelativeX(float x) {
        this.originalPosition.x = x;
        this.updatePositions();
    }

    public float getOriginalRelativeX() {
        return this.originalPosition.x;
    }

    public void block(boolean block) {
        if (this.blocked == block) {
            return;
        }
        this.blocked = block;
        if (block && this.mouseOver) {
            this.mouseOffOccurred();
        }
    }

    public void wobble(float factor, float time) {
        this.scaleDriver = new SinWaveDriver(1.0f, factor, time);
    }

    public void bounce(float time, float factor) {
        this.scaleDriver = new BounceDriver(this.currentScale, factor * this.currentScale, time);
    }

    public void cancelScaleEffect() {
        this.scaleDriver = new SlideDriver(this.currentScale, 1.0f, 0.07f);
    }

    public void toggle() {
        this.toggledOn = !this.toggledOn;
        this.changeButtonState(this.toggledOn);
        this.notifyListeners(GuiClickEvent.newToggleEvent(this.toggledOn));
    }

    public void disableManualTurnOff() {
        this.manualTurnOffDisabled = true;
    }

    public void release() {
        if (this.toggledOn) {
            this.toggledOn = false;
            this.scaleDriver = new SlideDriver(this.currentScale, 1.0f, 0.07f);
        }
    }

    public void setHotkey(int key) {
        this.hotkey = key;
    }

    public boolean isToggledOn() {
        return this.toggledOn;
    }

    @Override
    protected void updateSelf() {
        if (!this.blocked) {
            if (!this.toggledOn) {
                this.checkMouseOverEvents();
            }
            this.checkClickEvent();
            this.checkHotkey();
        }
        this.currentScale = this.scaleDriver.update(DisplayManager.getDeltaSeconds());
        this.updatePositions();
        this.updateToolTip();
    }

    private void checkHotkey() {
        if (this.hotkey != null) {
            if (MyKeyboard.getKeyboard().keyDownEventOccurred(this.hotkey)) {
                if (!this.toggledOn) {
                    this.toggle();
                }
            } else if (MyKeyboard.getKeyboard().keyUpEventOccurred(this.hotkey) && this.toggledOn) {
                this.toggle();
            }
        }
    }

    protected boolean isBlocked() {
        return this.blocked;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        if (this.originalPosition == null) {
            this.originalPosition = new Vector2f(this.getRelativeX(), this.getRelativeY());
            this.originalScale = new Vector2f(this.getRelativeScaleX(), this.getRelativeScaleY());
        }
        this.calculateUnclickRegion(position, scale);
    }

    protected void setOn() {
        this.toggledOn = true;
        this.scaleDriver = new ConstantDriver(this.scaleFactor);
    }

    private void checkClickEvent() {
        if (this.isMouseOverClickableRegion()) {
            if (MyMouse.getActiveMouse().isLeftClick()) {
                this.removeToolTip();
                if (!(!this.toggleButton || this.toggledOn && this.manualTurnOffDisabled)) {
                    if (!this.muted) {
                        SoundMaestro.playSystemSound(GuiSounds.getClickSound());
                    }
                    this.toggle();
                } else {
                    this.notifyListeners(GuiClickEvent.newLeftClickEvent(true));
                }
            } else if (MyMouse.getActiveMouse().isLeftClickRelease()) {
                this.notifyListeners(GuiClickEvent.newLeftClickEvent(false));
            }
        }
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

    private void checkMouseOverEvents() {
        if (this.isMouseOver() && !this.mouseOver) {
            this.mouseOverOccurred();
        } else if (!this.isMouseOver() && this.mouseOver) {
            this.mouseOffOccurred();
        }
    }

    protected void changeButtonState(boolean toggleOn) {
        this.scaleDriver = toggleOn ? new ConstantDriver(this.scaleFactor) : new SlideDriver(this.currentScale, 1.0f, 0.07f);
    }

    protected void mouseOverOccurred() {
        this.mouseOver = true;
        this.scaleDriver = new SlideDriver(this.currentScale, this.scaleFactor, 0.07f);
        this.notifyListeners(GuiClickEvent.newMouseOverEvent(true));
        this.awaitingTip = this.toolTipInfo != null;
    }

    protected void mouseOffOccurred() {
        this.mouseOver = false;
        this.scaleDriver = new SlideDriver(this.currentScale, 1.0f, 0.07f);
        this.notifyListeners(GuiClickEvent.newMouseOverEvent(false));
        this.removeToolTip();
    }

    @Override
    protected void delete() {
        super.delete();
        this.removeToolTip();
    }

    private void updatePositions() {
        float currentX = this.maintainPosX ? this.originalPosition.x : this.calculateScaledPosition(this.originalPosition.x, this.originalScale.x);
        float currentY = this.calculateScaledPosition(this.originalPosition.y, this.originalScale.y);
        float currentScaleX = this.originalScale.x * this.currentScale;
        float currentScaleY = this.originalScale.y * this.currentScale;
        super.setRelativePosition(currentX, currentY);
        super.setRelativeScale(currentScaleX, currentScaleY);
    }

    private float calculateScaledPosition(float originalPos, float originalScale) {
        float change = originalScale * this.currentScale - originalScale;
        return originalPos - change / 2.0f;
    }

    private void addNewListeners() {
        for (ClickListener toAdd : this.listenersToAdd) {
            this.listeners.add(toAdd);
        }
        this.listenersToAdd.clear();
    }

    private void removeToolTip() {
        this.toolTipTime = 0.5f;
        this.awaitingTip = false;
        if (this.currentToolTip != null) {
            this.currentToolTip.fadeOut();
            this.currentToolTip = null;
        }
    }

    private void notifyListeners(GuiClickEvent event) {
        this.firingListeners = true;
        for (ClickListener listener : this.listeners) {
            listener.eventOccurred(event);
        }
        this.addNewListeners();
        this.firingListeners = false;
    }

    private boolean isMouseOverClickableRegion() {
        boolean mouseOver = super.isMouseOver();
        if (this.unclickRelPos == null || !mouseOver) {
            return mouseOver;
        }
        return !this.mouseOverUnclickableRegion();
    }

    private void calculateUnclickRegion(Vector2f position, Vector2f scale) {
        if (this.unclickRelPos != null) {
            this.unclickPos.x = position.x + this.unclickRelPos.x * scale.x;
            this.unclickPos.y = position.y + this.unclickRelPos.y * scale.y;
            this.unclickScale.x = scale.x * this.unclickRelScale.x;
            this.unclickScale.y = scale.y * this.unclickRelScale.y;
        }
    }

    private boolean mouseOverUnclickableRegion() {
        MyMouse mouse = MyMouse.getActiveMouse();
        return mouse.getX() >= this.unclickPos.x && mouse.getX() <= this.unclickPos.x + this.unclickScale.x && mouse.getY() >= this.unclickPos.y && mouse.getY() <= this.unclickPos.y + this.unclickScale.y;
    }
}


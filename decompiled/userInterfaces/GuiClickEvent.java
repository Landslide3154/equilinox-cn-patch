/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

public class GuiClickEvent {
    public final boolean leftClick;
    public final boolean rightClick;
    public final boolean mouseOver;
    public final boolean toggleChange;
    public final boolean eventState;

    private GuiClickEvent(boolean leftClick, boolean rightClick, boolean mouseOver, boolean toggle, boolean state) {
        this.leftClick = leftClick;
        this.rightClick = rightClick;
        this.mouseOver = mouseOver;
        this.eventState = state;
        this.toggleChange = toggle;
    }

    public static GuiClickEvent newRightClickEvent(boolean state) {
        GuiClickEvent event = new GuiClickEvent(false, true, false, false, state);
        return event;
    }

    public static GuiClickEvent newLeftClickEvent(boolean state) {
        GuiClickEvent event = new GuiClickEvent(true, false, false, false, state);
        return event;
    }

    public static GuiClickEvent newMouseOverEvent(boolean state) {
        GuiClickEvent event = new GuiClickEvent(false, false, true, false, state);
        return event;
    }

    public static GuiClickEvent newToggleEvent(boolean state) {
        GuiClickEvent event = new GuiClickEvent(false, false, false, true, state);
        return event;
    }

    public boolean isToggleOn() {
        return this.toggleChange && this.eventState;
    }

    public boolean isToggleOff() {
        return this.toggleChange && !this.eventState;
    }

    public boolean isLeftClick() {
        return this.leftClick && this.eventState;
    }

    public boolean isLeftClickRelease() {
        return this.leftClick && !this.eventState;
    }

    public boolean isRightClick() {
        return this.rightClick && this.eventState;
    }

    public boolean isMouseOver() {
        return this.mouseOver && this.eventState;
    }

    public boolean isMouseOff() {
        return this.mouseOver && !this.eventState;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import basics.DisplayManager;
import mainGuis.MyCursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class MyMouse {
    private static MyMouse ACTIVE_MOUSE = new MyMouse();
    private boolean leftClick = false;
    private boolean leftClickRelease = false;
    private boolean rightClick = false;
    private boolean rightClickRelease = false;
    private boolean middleClick = false;
    private boolean middleClickRelease = false;
    private boolean leftButtonDown = false;
    private boolean rightButtonDown = false;
    private boolean middleButtonDown = false;
    private float x = 0.0f;
    private float y = 0.0f;
    private int dX = 0;
    private int dY = 0;
    private int dWheel = 0;
    private static final float MOVE_THRESHOLD = 0.23f;
    private float rightButtonTime = 0.0f;
    private float middleButtonTime = 0.0f;

    private MyMouse() {
        ACTIVE_MOUSE = this;
    }

    public void initCursor() {
        MyCursor.setCursor(MyCursor.NORMAL);
    }

    public static MyMouse getActiveMouse() {
        return ACTIVE_MOUSE;
    }

    public void update() {
        this.updateClickCheck();
        this.resetFlags();
        this.checkForEvents();
        this.setPosition();
        this.updateButtonStates();
        this.updateMovementValues();
    }

    public boolean isLeftClick() {
        return this.leftClick;
    }

    public boolean isLeftClickRelease() {
        return this.leftClickRelease;
    }

    public boolean isRightClick() {
        return this.rightClick;
    }

    public boolean isRightClickRelease() {
        return this.rightClickRelease;
    }

    public boolean isMiddleClick() {
        return this.middleClick;
    }

    public boolean isMiddleClickRelease() {
        return this.middleClickRelease;
    }

    public boolean isMouseWheelDown() {
        return this.middleButtonDown;
    }

    public boolean isLeftButtonDown() {
        return this.leftButtonDown;
    }

    public boolean isRightButtonDown() {
        return this.rightButtonDown;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getDX() {
        return this.dX;
    }

    public int getDY() {
        return this.dY;
    }

    public int getDWheel() {
        return this.dWheel;
    }

    public boolean shortRightClickOccurred() {
        return this.isRightClickRelease() && this.rightButtonTime < 0.23f;
    }

    public boolean shortMiddleClickOccurred() {
        return this.isMiddleClickRelease() && this.middleButtonTime < 0.23f;
    }

    private void updateClickCheck() {
        this.rightButtonTime = this.isRightButtonDown() ? (this.rightButtonTime += DisplayManager.getDeltaSeconds() + (float)(Math.abs(this.getDX()) + Math.abs(this.getDY())) * 0.001f) : 0.0f;
        this.middleButtonTime = this.isMouseWheelDown() ? (this.middleButtonTime += DisplayManager.getDeltaSeconds() + (float)(Math.abs(this.getDX()) + Math.abs(this.getDY())) * 0.001f) : 0.0f;
    }

    public float getDWheelSigned() {
        return Math.signum(this.dWheel);
    }

    private void resetFlags() {
        this.leftClick = false;
        this.rightClick = false;
        this.leftClickRelease = false;
        this.middleClick = false;
        this.middleClickRelease = false;
        this.rightClickRelease = false;
        this.middleButtonDown = false;
    }

    private void checkForEvents() {
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                if (Mouse.getEventButton() == 0) {
                    this.leftClick = true;
                }
                if (Mouse.getEventButton() == 1) {
                    this.rightClick = true;
                }
                if (Mouse.getEventButton() != 2) continue;
                this.middleClick = true;
                continue;
            }
            if (Mouse.getEventButton() == 0) {
                this.leftClickRelease = true;
            }
            if (Mouse.getEventButton() == 1) {
                this.rightClickRelease = true;
            }
            if (Mouse.getEventButton() != 2) continue;
            this.middleClickRelease = true;
        }
    }

    private void setPosition() {
        this.x = (float)Mouse.getX() / (float)Display.getWidth();
        this.y = 1.0f - (float)Mouse.getY() / (float)Display.getHeight();
    }

    private void updateButtonStates() {
        this.leftButtonDown = Mouse.isButtonDown(0);
        this.rightButtonDown = Mouse.isButtonDown(1);
        this.middleButtonDown = Mouse.isButtonDown(2);
    }

    private void updateMovementValues() {
        this.dX = Mouse.getDX();
        this.dY = Mouse.getDY();
        this.dWheel = Mouse.getDWheel();
    }
}


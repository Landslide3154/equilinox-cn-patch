/*
 * Decompiled with CFR 0.152.
 */
package bottomBar;

import basics.DisplayManager;
import bottomBar.DayNightControlUi;
import bottomBar.SpeedUi;
import bottomBar.TimeDisplay;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import org.lwjgl.util.vector.Vector2f;
import session.GameMode;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class BottomBarUi
extends GuiComponent {
    public static final int GAP = 1;
    public static final int HEIGHT_PIXELS = 20;
    public static final float HEIGHT = 20.0f / (float)DisplayManager.getUiHeight();
    public static final int WIDTH_PIXELS = 181;
    private static final float WIDTH = 181.0f / (float)DisplayManager.getUiWidth();
    private static final int TIME_UI_WIDTH_PIXELS = 160;
    private static final float TIME_UI_WIDTH = 160.0f / (float)DisplayManager.getUiWidth();
    public static final float X_POS = 0.5f - 0.5f * TIME_UI_WIDTH;
    public static final int DAY_CONTROL_WIDTH = 65;
    private static final float Y_POS = 1.0f - HEIGHT;
    private ValueDriver yDriver = new ConstantDriver(1.0f);
    private boolean displayed = false;
    private TimeDisplay calendarUi;
    private SpeedUi speedUi;
    private DayNightControlUi dayNightUi;

    public BottomBarUi() {
        GuiMaster.addComponent(this, 0.5f - 0.5f * TIME_UI_WIDTH, 1.0f, WIDTH, HEIGHT);
        this.show(false);
    }

    @Override
    protected void init() {
        super.init();
        this.addCalendarUi();
        this.addSpeedButton();
        this.addDayControls();
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        boolean overDayControl;
        boolean bl = overDayControl = this.dayNightUi.isShown() && this.dayNightUi.isMouseOver();
        return super.isMouseOverFocusIrrelevant() || overDayControl;
    }

    public void display() {
        if (!this.displayed) {
            this.displayed = true;
            this.show(true);
            this.dayNightUi.show(GameManager.getGameMode() != GameMode.NORMAL);
            this.yDriver = new SlideDriver(this.getRelativeY(), Y_POS, 0.2f);
            this.calendarUi.resetTime();
        }
    }

    public void undisplay() {
        if (this.displayed) {
            this.displayed = false;
            this.yDriver = new SlideDriver(this.getRelativeY(), 1.0f, 0.2f);
        }
    }

    public void reset() {
        this.dayNightUi.reset();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        float yPos = this.yDriver.update(DisplayManager.getDeltaSeconds());
        super.setRelativeY(yPos);
        if (yPos >= 1.0f) {
            this.show(false);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addSpeedButton() {
        this.speedUi = new SpeedUi();
        float xPos = 0.88950276f;
        super.addComponent(this.speedUi, xPos, 0.0f, 1.0f - xPos, 1.0f);
    }

    private void addCalendarUi() {
        this.calendarUi = new TimeDisplay();
        super.addComponent(this.calendarUi, 0.0f, 0.0f, 0.8839779f, 1.0f);
    }

    private void addDayControls() {
        this.dayNightUi = new DayNightControlUi();
        float width = super.pixelsToRelativeX(65.0f);
        super.addComponent(this.dayNightUi, -super.pixelsToRelativeX(66.0f), 0.0f, width, 1.0f);
    }
}


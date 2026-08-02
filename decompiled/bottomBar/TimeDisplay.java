/*
 * Decompiled with CFR 0.152.
 */
package bottomBar;

import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import time.Calendar;
import visualFxDrivers.ConstantDriver;

public class TimeDisplay
extends GuiComponent {
    private static final int INTERVAL = 5;
    private static final String TITLE = GameText.getText(79);
    private static final String DAY = GameText.getText(80);
    private static final String YEAR = GameText.getText(81);
    private Text text = Text.newText(TITLE).center().setFontSize(UiSettings.NORM_FONT).create();
    private int displayedMinutes = -1;
    private GuiTexture background;

    public TimeDisplay() {
        this.text.setColour(ColourPalette.WHITE);
        super.addText(this.text, 0.0f, 0.0f, 1.0f);
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.DARK_GREY);
        this.background.setAlphaDriver(new ConstantDriver(0.75f));
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        return false;
    }

    protected void resetTime() {
        this.displayedMinutes = -1;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        this.background.update();
        Calendar calendar = GameManager.getSession().getStats().getCalendar();
        int minutes = calendar.getTimeMinutesNearest(5);
        if (this.displayedMinutes != minutes) {
            this.text.setText(this.getTimeAndDateString(minutes, calendar));
            this.displayedMinutes = minutes;
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    private String getTimeAndDateString(int minutes, Calendar calendar) {
        int hours = calendar.getTimeHours();
        String time = (hours < 10 ? "0" + hours : Integer.valueOf(hours)) + ":" + (minutes < 10 ? "0" + minutes : Integer.valueOf(minutes));
        return String.valueOf(time) + " - " + DAY + " " + calendar.getDay() + ", " + YEAR + " " + calendar.getYear();
    }
}


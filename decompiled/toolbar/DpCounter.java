/*
 * Decompiled with CFR 0.152.
 */
package toolbar;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import session.GameMode;
import toolbar.DpText;
import toolbar.Toolbar;
import toolbox.Colour;
import toolbox.Maths;
import userInterfaces.GuiImage;
import visualFxDrivers.BounceDriver;

public class DpCounter
extends GuiComponent {
    private static final Colour COLOUR = ColourPalette.BEIGE;
    private static final String DP_TEXT = "dp";
    private static final int FRAMES_PER_CHANGE = 3;
    private static final float DP_TEXT_X = 0.84f;
    private static final float COUNT_TEXT_X = 0.18f;
    private static final float ICON_X = 0.02f;
    private static final float ICON_SCALE_Y = 0.6f;
    private static final float ICON_Y = 0.25f;
    private static final float PULSE_SIZE = 0.25f;
    private static final float PULSE_TIME = 0.7f;
    private GuiImage icon;
    private Text dpText;
    private Text countText;
    private int count = 0;
    private double displayedCount = 0.0;
    private double timeTillCorrect = 0.0;
    private List<DpText> popUpTexts = new ArrayList<DpText>();
    private Toolbar toolbar;
    private int frameCounter = 2;
    private float timePassed = 0.0f;

    protected DpCounter(Toolbar toolbar, int initialCount) {
        this.toolbar = toolbar;
        this.addIcon();
        this.addDpText();
        this.addCount();
        this.setCount(initialCount, true);
    }

    public void setModeName(GameMode mode) {
        this.countText.setText(mode.toString());
        this.timeTillCorrect = 0.0;
    }

    public void showDpText(boolean show) {
        if (show && this.dpText == null) {
            this.addDpText();
        } else if (!show) {
            this.removeDpText();
        }
    }

    public void setCount(int count, boolean instant) {
        this.count = count;
        if (instant) {
            this.displayedCount = count;
            String stringCount = Maths.formatNumber(count);
            this.countText.setText(stringCount);
            this.timeTillCorrect = 0.0;
        } else {
            this.timeTillCorrect = 0.7f;
        }
    }

    public synchronized void increaseCount(int increase) {
        this.count += increase;
        this.setCount(this.count, false);
        if (this.toolbar.isDisplayed() && increase != 0) {
            float change = increase > 0 ? 0.25f : -0.083333336f;
            this.countText.setScaleDriver(new BounceDriver(this.countText.getCurrentEffectScale(), 1.0f + change, 1.0f, 0.7f));
            this.countText.setColourDriver(increase > 0 ? ColourPalette.GREEN : ColourPalette.BRIGHT_RED, COLOUR, 0.7f);
        }
    }

    protected void removeIndicatorTexts() {
        for (DpText text : this.popUpTexts) {
            text.remove();
        }
        this.popUpTexts.clear();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        if (this.timeTillCorrect > 0.0) {
            this.increaseTiming();
            if (this.frameCounter == 0) {
                this.increaseDisplayedCount();
            }
        }
    }

    private void increaseDisplayedCount() {
        double factor = (double)this.timePassed / this.timeTillCorrect;
        this.timeTillCorrect -= (double)this.timePassed;
        this.calculateDisplayedTime(factor);
        this.updateDisplayedText();
    }

    private void increaseTiming() {
        ++this.frameCounter;
        this.frameCounter %= 3;
        this.timePassed += DisplayManager.getDeltaSeconds();
    }

    private void calculateDisplayedTime(double increaseFactor) {
        if (this.timeTillCorrect <= 0.0) {
            this.timeTillCorrect = 0.0;
            this.displayedCount = this.count;
        } else {
            double difference = (double)this.count - this.displayedCount;
            this.displayedCount += increaseFactor * difference;
        }
    }

    private void updateDisplayedText() {
        String stringCount = Maths.formatNumber(Math.round(this.displayedCount));
        this.countText.setText(stringCount);
        this.timePassed = 0.0f;
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addIcon() {
        this.icon = new GuiImage(GuiRepository.DP_ICON);
        this.icon.getTexture().setOverrideColour(COLOUR);
        super.addComponentY(this.icon, 0.02f, 0.25f, 0.6f);
    }

    private void addCount() {
        this.countText = Text.newText("0").setFontSize(UiSettings.LARGE_FONT).create();
        this.countText.setColour(COLOUR.duplicate());
        super.addText(this.countText, 0.18f, 0.19f, 1.0f);
    }

    private void removeDpText() {
        if (this.dpText == null) {
            return;
        }
        super.deleteText(this.dpText);
        this.dpText = null;
    }

    private void addDpText() {
        this.dpText = Text.newText(DP_TEXT).setFontSize(UiSettings.LARGE_FONT).create();
        this.dpText.setColour(COLOUR);
        super.addText(this.dpText, 0.84f, 0.19f, 1.0f);
    }
}


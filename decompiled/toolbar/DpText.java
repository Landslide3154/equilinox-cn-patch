/*
 * Decompiled with CFR 0.152.
 */
package toolbar;

import basics.DisplayManager;
import fontRendering.Text;
import guis.GuiMaster;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import toolbox.Maths;
import visualFxDrivers.FadeDriver;

public class DpText {
    private static final String PLUS_TEXT = "+";
    private static final String DP_TEXT = " dp";
    private static final float X_POS = 0.03f;
    private static final float DURATION = 1.0f;
    private static final float INIT_SPEED = 0.14f;
    private static final float DECELERATION = 0.14f;
    private static final float FADE_IN = 0.2f;
    private static final float FADE_OUT = 0.8f;
    private float time = 0.0f;
    private Text text;
    private float speed = 0.14f;
    private float yPos = 0.05f;

    public DpText(int amount) {
        String prefix = amount >= 0 ? PLUS_TEXT : "";
        this.text = Text.newText(String.valueOf(prefix) + Maths.formatNumber(amount) + DP_TEXT).setFontSize(UiSettings.LARGE_FONT).create();
        if (amount >= 0) {
            this.text.setColour(ColourPalette.GREEN);
        } else {
            this.text.setColour(ColourPalette.BRIGHT_RED);
        }
        this.text.setAlphaDriver(new FadeDriver(1.0f, 0.2f, 0.8f, 1.0f));
        GuiMaster.addText(this.text, 0.03f, this.yPos, 1.0f);
    }

    public boolean update() {
        this.time += DisplayManager.getDeltaSeconds();
        this.speed -= DisplayManager.getDeltaSeconds() * 0.14f;
        this.yPos += DisplayManager.getDeltaSeconds() * this.speed;
        this.text.setAbsPosition(0.03f, this.yPos);
        if (this.time > 1.0f) {
            GuiMaster.removeText(this.text);
            return false;
        }
        return true;
    }

    public void remove() {
        GuiMaster.removeText(this.text);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package worldOptions;

import basics.DisplayManager;
import fontRendering.Text;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import userInterfaces.GuiPanel;

public class ModeDescUi
extends GuiPanel {
    public static final float BUFF = 0.05f;
    private String message;

    public ModeDescUi(String message) {
        super(GuiRepository.BLOCK, ColourPalette.MIDDLE_GREY, 2, ColourPalette.LIGHT_GREY);
        this.message = message;
    }

    @Override
    protected void init() {
        super.init();
        float maxSize = 750.0f / (float)DisplayManager.getUiHeight();
        Text text = Text.newText(this.message).justify().setFontSize(Math.min(0.7f, maxSize)).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.05f, 0.05f, 0.9f);
    }
}


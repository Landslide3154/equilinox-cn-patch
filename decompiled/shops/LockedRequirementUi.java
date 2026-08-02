/*
 * Decompiled with CFR 0.152.
 */
package shops;

import basics.DisplayManager;
import fontRendering.Text;
import gameManaging.GameManager;
import guis.GuiMaster;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import session.GameMode;
import toolbox.Colour;
import toolbox.MyMouse;
import userInterfaces.GuiPanel;

public class LockedRequirementUi
extends GuiPanel {
    private static final int PIXEL_WIDTH = 185;
    private static final int PIXEL_HEIGHT = 80;
    private static final int BORDER_PIXELS = 1;
    private static final Colour MAIN_COLOUR = ColourPalette.MIDDLE_GREY;
    private static final Colour BORDER_COLOUR = ColourPalette.BRIGHT_GREY;
    private static final float TEXT_X = 0.07f;
    private static final float CREATIVE_TEXT_X = 0.05f;
    private static final float TEXT1_Y = 0.06f;
    private static final float TEXT2_Y = 0.28f;
    private static final float TEXT3_Y = 0.69f;
    private static final float CREATIVE_TITLE_Y = 0.04f;
    private static final float CREATIVE_TEXT_Y = 0.4f;
    private static final float HEIGHT = 80.0f / (float)DisplayManager.getUiHeight();
    private final String name;
    private final String desc;

    public LockedRequirementUi(String desc, String name) {
        super(MAIN_COLOUR, 1, BORDER_COLOUR);
        this.name = name;
        this.desc = desc;
        MyMouse mouse = MyMouse.getActiveMouse();
        super.setRenderLevel(1);
        float width = GameManager.getGameMode() == GameMode.NORMAL ? 185.0f / (float)DisplayManager.getUiWidth() : 190.0f / (float)DisplayManager.getUiWidth();
        GuiMaster.addComponent(this, mouse.getX(), mouse.getY(), width, HEIGHT);
    }

    @Override
    protected void init() {
        super.init();
        if (GameManager.getGameMode() == GameMode.NORMAL) {
            this.addText();
        } else {
            this.addCreativeText();
        }
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        MyMouse mouse = MyMouse.getActiveMouse();
        super.setRelativePosition(mouse.getX(), mouse.getY());
    }

    private void addCreativeText() {
        float length = 0.9f;
        this.addSpecificText("Locked!", UiSettings.NORM_FONT * 1.4f, ColourPalette.WHITE, 0.04f, 0.05f, length);
        this.addSpecificText("Species must be unlocked in the \"Normal\" game mode first.", UiSettings.NORM_FONT, ColourPalette.BRIGHT_GREY, 0.4f, 0.05f, length);
    }

    private void addText() {
        this.addSpecificText(String.valueOf(this.desc) + ":", UiSettings.NORM_FONT, ColourPalette.BRIGHT_GREY, 0.06f, 0.07f, 1.0f);
        this.addSpecificText(this.name, UiSettings.NORM_FONT * 1.4f, ColourPalette.WHITE, 0.28f, 0.07f, 1.0f);
        this.addSpecificText("Click for more info...", UiSettings.NORM_FONT, ColourPalette.BLUE_TEXT, 0.69f, 0.07f, 1.0f);
    }

    private void addSpecificText(String message, float size, Colour colour, float yPos, float textX, float length) {
        Text text = Text.newText(message).setFontSize(size).create();
        text.setColour(colour);
        super.addText(text, textX, yPos, length);
    }
}


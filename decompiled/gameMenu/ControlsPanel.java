/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import fontRendering.Text;
import gameMenu.GameMenuBackground;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import userInterfaces.GuiImage;
import userInterfaces.GuiPanel;

public class ControlsPanel
extends GuiPanel {
    private static final float LEFT_PAD = 0.08f;
    private static final float TITLE_FONT = 1.3f;
    private static final float SECTION_GAP = 0.04f;
    private static final float INFO_FONT = 0.8f;
    private static final float TITLE_HEIGHT = 0.13f;
    private static final float INFO_HEIGHT = 0.05f;
    private static final float Y_START = 0.02f;
    private static final float CENTER = 0.3f;
    private static final float LINE_Y_PAD = 0.09f;
    private static final int OTHER_GAP_PIXELS = 135;
    private float yPos = 0.02f;

    public ControlsPanel() {
        super(GameMenuBackground.getStandardColour(), 0.65f);
    }

    @Override
    protected void init() {
        super.init();
        this.addTitle(GameText.getText(38));
        this.addInfo(GameText.getText(39), GameText.getText(40), GameText.getText(1081));
        this.addInfo(GameText.getText(41), GameText.getText(42), GameText.getText(1082));
        this.addInfo(GameText.getText(43), GameText.getText(44), GameText.getText(1083));
        this.addInfo(GameText.getText(887), "R", 0);
        this.yPos += 0.04f;
        this.addTitle(GameText.getText(45));
        this.addInfo(GameText.getText(46), GameText.getText(47), 0);
        this.addInfo(GameText.getText(50), GameText.getText(47), 0);
        this.yPos += 0.04f;
        this.addTitle(GameText.getText(48));
        float sectionTop = this.yPos;
        this.addInfo(GameText.getText(49), GameText.getText(51), 0);
        this.addInfo(GameText.getText(886), "H", 0);
        this.addInfo(GameText.getText(1114), GameText.getText(1115), 0);
        this.yPos = sectionTop;
        this.addInfo(GameText.getText(1070), "DEL", 1);
        this.addInfo(GameText.getText(1071), "F", 1);
        this.addInfo(GameText.getText(1072), "P", 1);
    }

    private void addTitle(String title) {
        Text text = Text.newText(title).setFontSize(1.3f).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.08f, this.yPos, 1.0f);
        GuiImage image = new GuiImage(GuiRepository.BLOCK);
        super.addComponent(image, 0.08f, this.yPos + 0.09f, 0.84000003f, super.pixelsToRelativeY(1.0f));
        this.yPos += 0.13f;
    }

    private void addInfo(String name, String info, int col) {
        float xPos = (float)col * 0.5f;
        Text text = Text.newText(name).setFontSize(0.8f).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, xPos + 0.08f, this.yPos, 1.0f);
        text = Text.newText(info).setFontSize(0.8f).create();
        text.setColour(ColourPalette.GREEN);
        super.addText(text, xPos + 0.3f, this.yPos, 1.0f);
        this.yPos += 0.05f;
    }

    private void addInfo(String name, String info, String info2) {
        Text text = Text.newText(name).setFontSize(0.8f).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.08f, this.yPos, 1.0f);
        text = Text.newText(info).setFontSize(0.8f).create();
        text.setColour(ColourPalette.GREEN);
        super.addText(text, 0.3f, this.yPos, 1.0f);
        text = Text.newText(info2).setFontSize(0.8f).create();
        text.setColour(ColourPalette.GREEN);
        super.addText(text, 0.5f, this.yPos, 1.0f);
        this.yPos += 0.05f;
    }
}


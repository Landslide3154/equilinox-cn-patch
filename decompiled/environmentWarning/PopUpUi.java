/*
 * Decompiled with CFR 0.152.
 */
package environmentWarning;

import basics.DisplayManager;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiMaster;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import session.GameMode;
import toolbox.Colour;
import toolbox.MyMouse;
import userInterfaces.GuiPanel;

public abstract class PopUpUi
extends GuiPanel {
    public static final Colour HEADING_COLOUR = ColourPalette.BEIGE;
    public static final Colour NORMAL_COLOUR = ColourPalette.WHITE;
    public static final Colour NEGATIVE_COLOUR = ColourPalette.BRIGHT_RED;
    private static final String NO_FACTORS = GameText.getText(1090);
    private static final int WIDTH_PIXELS = 230;
    private static final float WIDTH = 230.0f / (float)DisplayManager.getUiWidth();
    private static final float RIGHT_START = 0.6f;
    private static final float LEFT_PAD = 0.05f;
    private static final float Y_PAD = 0.05f;
    private static final float GAP = 0.17f;
    private float totalY;
    private Text[][] texts;
    private float yPos = 0.05f;
    private String[] headers;
    private int pixelOffsetX = 0;
    private int pixelOffsetY = 0;
    private Text message = null;

    public PopUpUi(String[] headers) {
        super(ColourPalette.MIDDLE_GREY, 1, ColourPalette.LIGHT_GREY);
        this.headers = headers;
        int lines = Math.max(headers.length, 1);
        this.totalY = 0.1f + (float)lines * 0.17f;
        super.setRenderLevel(2);
        GuiMaster.addComponent(this, 0.0f, 0.0f, WIDTH, WIDTH * this.totalY * DisplayManager.getAspectRatio() / 1.7777778f);
    }

    public void setPixelOffset(int offsetX, int offsetY) {
        this.pixelOffsetX = offsetX;
        this.pixelOffsetY = offsetY;
    }

    public abstract List<StatData> getData();

    protected int getLineCount() {
        return this.headers.length;
    }

    public void setOneLineText(String line, Colour lineColour) {
        this.deleteEnviroText();
        this.addMessageText(line, lineColour);
    }

    public void showEnviro() {
        if (this.texts == null) {
            if (this.headers.length == 0) {
                this.setOneLineText(GameManager.getGameMode() == GameMode.BUILD ? "Hold M to place multiple" : NO_FACTORS, ColourPalette.WHITE);
            } else {
                this.addEnviroText();
                this.deleteMessageText();
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        if (this.headers.length == 0) {
            this.setOneLineText(GameManager.getGameMode() == GameMode.BUILD ? "Hold M to place many" : NO_FACTORS, ColourPalette.WHITE);
        } else {
            this.addEnviroText();
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        MyMouse mouse = MyMouse.getActiveMouse();
        float offsetX = (float)this.pixelOffsetX / (float)DisplayManager.getUiWidth();
        float offsetY = (float)this.pixelOffsetY / (float)DisplayManager.getUiHeight();
        super.setRelativePosition(mouse.getX() + offsetX, mouse.getY() + offsetY);
        if (this.texts != null) {
            List<StatData> data = this.getData();
            int pointer = 0;
            for (StatData dataEntry : data) {
                this.updateText(pointer, dataEntry.value, dataEntry.colour);
                ++pointer;
            }
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        super.getGuiTextures(data);
    }

    private void addMessageText(String newMessage, Colour colour) {
        if (this.message != null) {
            this.message.setText(newMessage);
            this.message.setColour(colour);
        } else {
            this.message = Text.newText(newMessage).center().setFontSize(UiSettings.NORM_FONT).create();
            this.message.setColour(colour);
            super.addText(this.message, 0.0f, 0.05f / this.totalY, 1.0f);
        }
    }

    private void deleteMessageText() {
        if (this.message != null) {
            super.deleteText(this.message);
            this.message = null;
        }
    }

    private void addEnviroText() {
        this.yPos = 0.05f;
        List<StatData> data = this.getData();
        this.texts = new Text[this.headers.length][2];
        int pointer = 0;
        for (StatData dataEntry : data) {
            this.addTextLine(pointer, this.headers[pointer], dataEntry.value, dataEntry.colour);
            ++pointer;
        }
    }

    private void addTextLine(int index, String name, String value, Colour colour) {
        Text text = Text.newText(name).setFontSize(UiSettings.NORM_FONT).create();
        super.addText(text, 0.05f, this.yPos / this.totalY, 1.0f);
        text.setColour(colour);
        Text valueText = Text.newText(value).setFontSize(UiSettings.NORM_FONT).create();
        super.addText(valueText, 0.6f, this.yPos / this.totalY, 1.0f);
        valueText.setColour(colour);
        this.yPos += 0.17f;
        this.texts[index][0] = text;
        this.texts[index][1] = valueText;
    }

    private void deleteEnviroText() {
        if (this.texts != null) {
            int i = 0;
            while (i < this.texts.length) {
                super.deleteText(this.texts[i][0]);
                super.deleteText(this.texts[i][1]);
                ++i;
            }
            this.texts = null;
        }
    }

    private void updateText(int index, String newValue, Colour newColour) {
        Text value = this.texts[index][1];
        if (!newValue.equals(value.getTextString())) {
            value.setText(newValue);
        }
        this.texts[index][0].setColour(newColour);
        this.texts[index][1].setColour(newColour);
    }

    public static class StatData {
        public final Colour colour;
        public final String value;

        public StatData(String value, Colour colour) {
            this.value = value;
            this.colour = colour;
        }
    }
}


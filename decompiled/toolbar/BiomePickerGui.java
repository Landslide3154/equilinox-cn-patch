/*
 * Decompiled with CFR 0.152.
 */
package toolbar;

import basics.DisplayManager;
import biomes.Biome;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbar.BiomeData;
import toolbox.MyMouse;
import userInterfaces.GuiImage;
import visualFxDrivers.ConstantDriver;

public class BiomePickerGui
extends GuiComponent {
    private static final String DRY_TEXT = GameText.getText(152);
    private static final String WATER_TEXT = GameText.getText(153);
    private static final String BARREN_LAND = GameText.getText(154);
    private static final String ALTITUDE = GameText.getText(155);
    private static final float Y_PADDING = 0.05f;
    private static final float SECTION_GAP = 0.09f;
    private static final float BOTTOM_BUF = 0.05f;
    private static final float GAP = 0.162f;
    private static final float MOUSE_OFFSET = 5.0f;
    private static final float BORDER_WIDTH = 1.0f;
    private static final float CENTER_GAP = 0.3f;
    private static final float LINE_LENGTH = 0.9f;
    private static final float HALF_CENTER_GAP = 0.15f;
    private static final int PIXELS_HEIGHT = 138;
    private static final int PIXELS_WIDTH = 192;
    private static final float X_SCALE = 192.0f / (float)DisplayManager.getUiWidth();
    private static final float Y_SCALE = 138.0f / (float)DisplayManager.getUiHeight();
    private GuiTexture background;
    private GuiTexture border;
    private List<BiomeData> infos = new ArrayList<BiomeData>();
    private Text barrenText = null;
    private Text heightText;
    private Text waterText;
    private int displayedHeight = 0;

    protected BiomePickerGui() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.DARK_GREY);
        this.background.setAlphaDriver(new ConstantDriver(0.75f));
        this.background.setBlurry(true);
        this.border = new GuiTexture(GuiRepository.BLOCK);
        this.border.setOverrideColour(ColourPalette.BRIGHT_GREY);
        super.setRenderLevel(1);
        GuiMaster.addComponent(this, 0.0f, 0.0f, X_SCALE, Y_SCALE);
        this.initHeightText();
        this.initWaterText();
        this.show(false);
    }

    @Override
    protected void init() {
        super.init();
        this.addLine();
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        return false;
    }

    protected void displayInfo(Map<Biome, Integer> data, int height, boolean underwater, Vector3f terrainPos) {
        int pointer = 0;
        if (data.isEmpty()) {
            this.displayBarrenText();
        } else {
            this.hideBarrenText();
        }
        for (Biome newInfo : data.keySet()) {
            this.displayText(pointer, newInfo, data.get((Object)newInfo));
            ++pointer;
        }
        this.removeExcessTexts(pointer);
        this.updateHeight(height);
        this.updateWaterText(underwater);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        float borderHeight = 0.007246377f;
        float borderWidth = 0.0052083335f;
        this.border.setPosition(position.x, position.y, scale.x, scale.y);
        this.background.setPosition(position.x + borderWidth * scale.x, position.y + borderHeight * scale.y, (1.0f - borderWidth * 2.0f) * scale.x, (1.0f - borderHeight * 2.0f) * scale.y);
    }

    @Override
    protected void updateSelf() {
        this.background.update();
        MyMouse mouse = MyMouse.getActiveMouse();
        super.setRelativePosition(mouse.getX() + 5.0f / (float)DisplayManager.getUiWidth(), mouse.getY() + 5.0f / (float)DisplayManager.getUiHeight());
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.border);
        data.addTexture(this.getLevel(), this.background);
    }

    private void displayText(int pointer, Biome biome, int count) {
        if (this.infos.size() > pointer) {
            this.infos.get(pointer).update(biome, count);
        } else {
            this.addNewText(biome, count);
        }
    }

    private void addNewText(Biome biome, int count) {
        BiomeData data = new BiomeData(biome, count);
        float yPos = 0.05f + 0.162f * (float)this.infos.size();
        super.addText(data.getBiomeText(), 0.0f, yPos, 0.35f);
        super.addText(data.getDashText(), 0.0f, yPos, 1.0f);
        super.addText(data.getCountText(), 0.65f, yPos, 1.0f);
        this.infos.add(data);
    }

    private void removeExcessTexts(int pointer) {
        while (pointer < this.infos.size()) {
            BiomeData data = this.infos.remove(pointer);
            super.deleteText(data.getBiomeText());
            super.deleteText(data.getDashText());
            super.deleteText(data.getCountText());
        }
    }

    private void hideBarrenText() {
        if (this.barrenText != null) {
            super.deleteText(this.barrenText);
            this.barrenText = null;
        }
    }

    private void addLine() {
        GuiImage image = new GuiImage(GuiRepository.BLOCK);
        image.getTexture().setOverrideColour(ColourPalette.MIDDLE_GREY);
        float yPos = 0.581f;
        super.addComponent(image, 0.050000012f, yPos, 0.9f, super.pixelsToRelativeY(1.0f));
    }

    private void displayBarrenText() {
        if (this.barrenText == null) {
            this.barrenText = Text.newText(BARREN_LAND).center().setFontSize(UiSettings.NORM_FONT).create();
            this.barrenText.setColour(ColourPalette.WHITE);
            super.addText(this.barrenText, 0.0f, 0.05f, 1.0f);
        }
    }

    private void updateHeight(int height) {
        if (this.displayedHeight != height) {
            this.displayedHeight = height;
            this.heightText.setText(String.valueOf(this.displayedHeight) + "m");
        }
    }

    private void initHeightText() {
        float yPos = 0.626f;
        Text text = Text.newText(ALTITUDE).setFontSize(UiSettings.NORM_FONT).rightAlign().create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, yPos, 0.35f);
        this.heightText = Text.newText(String.valueOf(this.displayedHeight) + "m").setFontSize(UiSettings.NORM_FONT).create();
        this.heightText.setColour(ColourPalette.WHITE);
        super.addText(this.heightText, 0.65f, yPos, 1.0f);
        Text dash = Text.newText("-").center().setFontSize(UiSettings.NORM_FONT).create();
        dash.setColour(ColourPalette.WHITE);
        super.addText(dash, 0.0f, yPos, 1.0f);
    }

    private void updateWaterText(boolean underwater) {
        String correctText;
        String string = correctText = underwater ? WATER_TEXT : DRY_TEXT;
        if (!this.waterText.getTextString().equals(correctText)) {
            this.waterText.setText(correctText);
        }
    }

    private void initWaterText() {
        this.waterText = Text.newText(DRY_TEXT).center().setFontSize(UiSettings.NORM_FONT).create();
        this.waterText.setColour(ColourPalette.BEIGE);
        super.addText(this.waterText, 0.0f, 0.788f, 1.0f);
    }
}


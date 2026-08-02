/*
 * Decompiled with CFR 0.152.
 */
package speciesInformation;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import speciesInformation.SpeciesInfoGui;
import speciesInformation.SpeciesInfoLine;
import toolbox.Colour;

public class StatsGui
extends GuiComponent {
    private static final String NO_ABILITY = GameText.getText(208);
    private static final float ENTRY_PAD = 0.015f;
    private static final float CENTER_PAD = 0.06f;
    private static final float LINE_SIZE = 0.47f;
    private static final float RIGHT_START = 0.53f;
    private static final float Y_START = 0.03f;
    private static final float X_PAD = 0.03f;
    private List<SpeciesInfoLine> information;
    private final boolean hasTitles;

    public StatsGui(List<SpeciesInfoLine> information, boolean hasTitles) {
        this.information = information;
        this.hasTitles = hasTitles;
    }

    @Override
    protected void init() {
        super.init();
        this.addStats(this.information);
    }

    private void addStats(List<SpeciesInfoLine> information) {
        float yPos = 0.03f;
        if (information.isEmpty() && !this.hasTitles) {
            this.addNoAbilityLine();
        }
        int i = 0;
        while (i < information.size()) {
            float height = this.addInfoLine(information.get(i), yPos);
            yPos += height + 0.015f;
            ++i;
        }
    }

    private float addInfoLine(SpeciesInfoLine info, float y) {
        float textHeight = 0.0f;
        if (this.hasTitles) {
            textHeight = this.addNameText(info, y);
        }
        if (!this.hasTitles) {
            textHeight = this.addFullLine("- " + info.getValueText(), y);
        } else if (info.usesTextValue()) {
            float valueHeight = this.addValueText(info, y);
            textHeight = Math.max(valueHeight, textHeight);
        } else {
            this.addComponentValue(info.getValueComponent(), y, textHeight);
        }
        return textHeight;
    }

    private void addComponentValue(GuiComponent component, float yPos, float height) {
        super.addComponent(component, 0.53f, yPos, 0.47f, height);
    }

    private float addNameText(SpeciesInfoLine info, float y) {
        Text nameText = Text.newText(String.valueOf(info.getName()) + ":").rightAlign().setFontSize(SpeciesInfoGui.FONT_SIZE).create();
        Colour colour = info.getOverrideColour();
        nameText.setColour(colour == null ? ColourPalette.WHITE : colour);
        super.addText(nameText, 0.0f, y, 0.47f);
        return nameText.getHeight() / super.getScale().y;
    }

    private float addValueText(SpeciesInfoLine info, float y) {
        Text value = Text.newText(info.getValueText()).setFontSize(SpeciesInfoGui.FONT_SIZE).create();
        Colour colour = info.getOverrideColour();
        value.setColour(colour == null ? ColourPalette.BEIGE : colour);
        super.addText(value, 0.53f, y, 0.47f);
        return value.getHeight() / super.getScale().y;
    }

    private float addFullLine(String line, float y) {
        Text value = Text.newText(line).setFontSize(SpeciesInfoGui.FONT_SIZE).indent().create();
        value.setColour(ColourPalette.WHITE);
        super.addText(value, 0.03f, y, 0.94f);
        return value.getHeight() / super.getScale().y;
    }

    private float addNoAbilityLine() {
        Text value = Text.newText(NO_ABILITY).setFontSize(SpeciesInfoGui.FONT_SIZE).center().create();
        value.setColour(ColourPalette.BEIGE);
        super.addText(value, 0.03f, 0.03f, 0.94f);
        return value.getHeight() / super.getScale().y;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }
}


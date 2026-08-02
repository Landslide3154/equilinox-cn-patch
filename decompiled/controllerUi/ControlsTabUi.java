/*
 * Decompiled with CFR 0.152.
 */
package controllerUi;

import basics.DisplayManager;
import componentArchitecture.ControlBehaviour;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import speciesInformation.SpeciesInfoGui;

public class ControlsTabUi
extends GuiComponent {
    private static final String MOVE_NAME = GameText.getText(24);
    private static final String MOVE_VAL = GameText.getText(25);
    private static final String SPRINT_NAME = GameText.getText(26);
    private static final String SPRINT_VAL = GameText.getText(27);
    private final List<ControlBehaviour> behaviours;

    public ControlsTabUi(List<ControlBehaviour> behaviours) {
        this.behaviours = behaviours;
    }

    @Override
    protected void init() {
        float yPos = 0.0f;
        float lineHeight = 20.0f / (super.getScale().y * (float)DisplayManager.getUiHeight());
        this.addLine(yPos, MOVE_NAME, MOVE_VAL);
        this.addLine(yPos += lineHeight, SPRINT_NAME, SPRINT_VAL);
        yPos += lineHeight;
        for (ControlBehaviour behaviour : this.behaviours) {
            this.addLine(yPos, behaviour.getName(), behaviour.getKeyName());
            yPos += lineHeight;
        }
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

    private void addLine(float yPos, String name, String key) {
        Text nameText = Text.newText(String.valueOf(name) + ":").setFontSize(SpeciesInfoGui.FONT_SIZE).create();
        nameText.setColour(ColourPalette.WHITE);
        super.addText(nameText, 0.0f, yPos, 1.0f);
        Text keyText = Text.newText(key).setFontSize(SpeciesInfoGui.FONT_SIZE).create();
        keyText.setColour(ColourPalette.BEIGE);
        super.addText(keyText, 0.54f, yPos, 1.0f);
    }
}


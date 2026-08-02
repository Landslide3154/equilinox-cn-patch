/*
 * Decompiled with CFR 0.152.
 */
package materials;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import materials.ColourTraitBlueprint;
import materials.MaterialComponent;
import org.lwjgl.util.vector.Vector2f;
import speciesInformation.SpeciesInfoGui;
import toolbox.Colour;
import userInterfaces.GuiPanel;

public class NaturalColoursGui
extends GuiComponent {
    private static final float COLOUR_HEIGHT = 1.0f;
    private static final float GAP = 0.3f;
    private ColourTraitBlueprint colourTrait;

    public NaturalColoursGui(MaterialComponent.MaterialCompBlueprint materialBlueprint) {
        this.colourTrait = materialBlueprint.getTrait();
    }

    @Override
    protected void init() {
        super.init();
        this.addColour(this.colourTrait.getNaturalColour1(), 0.0f);
        if (this.colourTrait.hasSecondNaturalColour()) {
            this.addText();
            this.addColour(this.colourTrait.getNaturalColour2(), 0.3f);
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

    private void addText() {
        Text text = Text.newText("to").center().setFontSize(SpeciesInfoGui.FONT_SIZE).create();
        text.setColour(ColourPalette.BEIGE);
        float startX = super.getRelativeWidthCoords(1.0f);
        super.addText(text, startX, 0.0f, 0.3f - startX);
    }

    private void addColour(Colour colour, float xPos) {
        GuiPanel panel = new GuiPanel(GuiRepository.BLOCK, colour, 2, ColourPalette.BEIGE);
        super.addCenteredComponentYScaleY(panel, 0.5f, xPos, 1.0f);
    }
}


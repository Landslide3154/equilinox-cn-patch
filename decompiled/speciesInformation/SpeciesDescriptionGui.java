/*
 * Decompiled with CFR 0.152.
 */
package speciesInformation;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import speciesInformation.SpeciesInfoGui;

public class SpeciesDescriptionGui
extends GuiComponent {
    private static final float PADDING_Y = 0.05f;
    private static final float PADDING_X = 0.03f;

    public SpeciesDescriptionGui(String description) {
        Text text = Text.newText(description).justify().setFontSize(SpeciesInfoGui.FONT_SIZE).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.03f, 0.05f, 0.94f);
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


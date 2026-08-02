/*
 * Decompiled with CFR 0.152.
 */
package colourSelector;

import colourSelector.ColourSelectorGui;
import colourSelector.SetColourUi;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;

public class RecentColoursUi
extends GuiComponent {
    private static final int GAP_PIXELS = 4;
    private final List<Colour> colours;
    private final ColourSelectorGui mainUi;

    public RecentColoursUi(ColourSelectorGui mainUi, List<Colour> colours) {
        this.colours = colours;
        this.mainUi = mainUi;
    }

    @Override
    protected void init() {
        float yPos = 0.0f;
        float boxHeight = super.getRelativeHeightCoords(1.0f);
        float gap = super.pixelsToRelativeY(4.0f);
        for (Colour colour : this.colours) {
            this.addColourBox(yPos, colour, boxHeight);
            yPos += boxHeight + gap;
        }
    }

    private void addColourBox(float yPos, Colour colour, float boxHeight) {
        SetColourUi box = new SetColourUi(this.mainUi, colour);
        super.addComponent(box, 0.0f, yPos, 1.0f, boxHeight);
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }
}


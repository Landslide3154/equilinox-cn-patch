/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;

public class GuiProgressBar
extends GuiComponent {
    private static final int BORDER_SIZE = 1;
    private static final Colour GOOD_COLOUR = ColourPalette.GREEN;
    private static final Colour BAD_COLOUR = ColourPalette.FLAT_RED;
    private float progress;
    private GuiTexture outlineTexture;
    private GuiTexture barTexture;
    private GuiTexture emptyTexture = new GuiTexture(GuiRepository.BLOCK);

    public GuiProgressBar(float progress) {
        this.emptyTexture.setOverrideColour(ColourPalette.MIDDLE_GREY);
        this.outlineTexture = new GuiTexture(GuiRepository.BLOCK);
        this.outlineTexture.setOverrideColour(ColourPalette.BEIGE);
        this.barTexture = new GuiTexture(GuiRepository.BLOCK);
        this.barTexture.setOverrideColour(Colour.interpolateColours(BAD_COLOUR, GOOD_COLOUR, progress, null));
        this.progress = progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.outlineTexture.setPosition(position.x, position.y, scale.x, scale.y);
        float padX = super.pixelsToRelativeX(1.0f);
        float padY = super.pixelsToRelativeY(1.0f);
        float height = 1.0f - 2.0f * padY;
        this.barTexture.setPosition(this.getBarPos(position.x, scale.x, padX), this.getBarPos(position.y, scale.y, padY), this.calculateBarWidth(), scale.y * height);
        this.emptyTexture.setPosition(this.getBarPos(position.x, scale.x, padX), this.getBarPos(position.y, scale.y, padY), scale.x * (1.0f - 2.0f * padX), scale.y * height);
    }

    @Override
    protected void updateSelf() {
        this.barTexture.setOverrideColour(Colour.interpolateColours(BAD_COLOUR, GOOD_COLOUR, this.progress, null));
        this.barTexture.setWidth(this.calculateBarWidth());
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.outlineTexture);
        data.addTexture(this.getLevel(), this.emptyTexture);
        data.addTexture(this.getLevel(), this.barTexture);
    }

    private float getBarPos(float pos, float scale, float pad) {
        return pos + scale * pad;
    }

    private float calculateBarWidth() {
        float padX = super.pixelsToRelativeX(1.0f);
        float width = (1.0f - 2.0f * padX) * this.progress;
        return super.getScale().x * width;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.Maths;
import userInterfaces.GuiImage;

public class ColourMenuItem
extends GuiComponent {
    private static final float COLOUR_HEIGHT = 0.7f;
    private static final float TEXT_X = 0.45f;
    private static final float TEXT_WIDTH = 0.32f;
    private static final float TEXT_Y = 0.1f;
    private static final float COL_TEXT_X = 0.15f;
    private static final float DP_TEXT_X = 0.8f;
    private final Colour colour;
    private final int price;
    private final String name;

    public ColourMenuItem(Colour colour, int price, String name) {
        this.colour = colour;
        this.price = price;
        this.name = name;
    }

    @Override
    protected void init() {
        super.init();
        GuiImage image = new GuiImage(GuiRepository.BLOCK);
        image.getTexture().setOverrideColour(this.colour);
        float width = super.getRelativeWidthCoords(0.7f);
        super.addComponent(image, 0.0f, 0.15f, width, 0.7f);
        this.addText();
        if (this.name != null) {
            this.addName();
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

    private void addName() {
        Text text = Text.newText(this.name).setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.BRIGHT_GREY);
        super.addText(text, 0.15f, 0.1f, 1.0f);
    }

    private void addText() {
        Text text = Text.newText(Maths.formatAbreviatedNumber(this.price)).rightAlign().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.BEIGE);
        super.addText(text, 0.45f, 0.1f, 0.32f);
        Text dpText = Text.newText("dp").setFontSize(UiSettings.NORM_FONT).create();
        dpText.setColour(ColourPalette.WHITE);
        super.addText(dpText, 0.8f, 0.1f, 1.0f);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.InfoType;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;

public abstract class PopUpInfoGui
extends GuiComponent {
    public static final float CENTER = 0.5f;
    public static final float CENTER_PAD = 0.08f;
    public static final float LEFT_SIZE = 0.46f;
    public static final float RIGHT_START = 0.54f;
    private static final String COLON = ":";
    private final String name;
    private final Colour colour;
    private final Colour nameColour;
    private final InfoType type;
    private final float font;

    public PopUpInfoGui(String name, InfoType type, float font) {
        this.name = String.valueOf(name) + COLON;
        this.colour = ColourPalette.BEIGE;
        this.nameColour = ColourPalette.WHITE;
        this.type = type;
        this.font = font;
    }

    public PopUpInfoGui(String name, Colour colour, InfoType type, float font) {
        this.name = String.valueOf(name) + COLON;
        this.colour = colour;
        this.type = type;
        this.font = font;
        this.nameColour = colour;
    }

    public String getNameText() {
        return this.name;
    }

    public Colour getTextColour() {
        return this.colour;
    }

    public InfoType getType() {
        return this.type;
    }

    public float getFontSize() {
        return this.font;
    }

    @Override
    protected void init() {
        this.initNameText();
        this.initValueGui();
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

    protected abstract void initValueGui();

    private void initNameText() {
        Text nameText = Text.newText(this.name).setFontSize(this.font).create();
        nameText.setColour(this.nameColour);
        super.addText(nameText, 0.0f, 0.0f, 1.0f);
    }
}


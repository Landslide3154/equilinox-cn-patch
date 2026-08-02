/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import visualFxDrivers.ConstantDriver;

public class TextButtonUi
extends GuiClickable {
    private final GuiTexture background;
    private final Text text;
    private final Colour colour;
    private final Colour mouseoverColour;
    private final Colour toggledColour;
    private Colour blockBackColour = ColourPalette.DARK_GREY;
    private Colour blockTextColour = ColourPalette.MIDDLE_GREY;
    private Colour textColour = ColourPalette.WHITE;
    private float textY = 0.0f;
    private float alphaNormal = -1.0f;

    public TextButtonUi(String text, Colour colour, float font) {
        super(1.0f);
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(colour);
        this.colour = colour;
        this.toggledColour = null;
        this.mouseoverColour = colour.duplicate().scale(1.3f);
        this.text = Text.newText(text).setFontSize(font).center().create();
        this.addMouseoverListener();
    }

    public TextButtonUi(String text, Colour colour, float font, Colour toggledColour) {
        super(true, 1.0f);
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(colour);
        this.colour = colour;
        this.toggledColour = toggledColour;
        this.mouseoverColour = colour.duplicate().scale(1.3f);
        this.text = Text.newText(text).setFontSize(font).center().create();
        this.addMouseoverListener();
    }

    public TextButtonUi(String text, Colour colour, float font, Colour toggledColour, float alphaNormal, float alphaTog) {
        super(true, 1.0f);
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(colour);
        this.alphaNormal = alphaNormal;
        this.background.setAlphaDriver(new ConstantDriver(alphaNormal));
        this.colour = colour;
        this.toggledColour = toggledColour;
        this.mouseoverColour = colour.duplicate().scale(1.3f);
        this.text = Text.newText(text).setFontSize(font).center().create();
        this.addMouseoverListener();
    }

    public TextButtonUi(String text, Colour colour, float font, Colour textColour, float textY) {
        super(1.0f);
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(colour);
        this.colour = colour;
        this.toggledColour = null;
        this.mouseoverColour = colour.duplicate().scale(1.3f);
        this.text = Text.newText(text).setFontSize(font).center().create();
        this.textY = textY;
        this.textColour = textColour;
        this.addMouseoverListener();
    }

    public void setBlockColours(Colour blockBackground, Colour blockText) {
        this.blockBackColour = blockBackground;
        this.blockTextColour = blockText;
    }

    public void setText(String newText) {
        this.text.setText(newText);
    }

    @Override
    public void block(boolean block) {
        if (super.isBlocked() == block) {
            return;
        }
        super.block(block);
        this.background.setOverrideColour(block ? this.blockBackColour : this.colour);
        this.text.setColour(block ? this.blockTextColour : this.textColour);
    }

    @Override
    protected void init() {
        super.init();
        this.text.setColour(this.textColour);
        super.addText(this.text, 0.0f, this.textY, 1.0f);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.background.update();
    }

    private void addMouseoverListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.mouseOver) {
                    TextButtonUi.this.background.setOverrideColour(event.eventState ? TextButtonUi.this.mouseoverColour : TextButtonUi.this.colour);
                }
                if (event.isToggleOn()) {
                    TextButtonUi.this.background.setOverrideColour(TextButtonUi.this.toggledColour);
                    if (TextButtonUi.this.alphaNormal >= 0.0f) {
                        ((ConstantDriver)TextButtonUi.this.background.getAlphaDriver()).setValue(1.0f);
                    }
                } else if (event.isToggleOff()) {
                    TextButtonUi.this.background.setOverrideColour(TextButtonUi.this.colour);
                    if (TextButtonUi.this.alphaNormal >= 0.0f) {
                        ((ConstantDriver)TextButtonUi.this.background.getAlphaDriver()).setValue(TextButtonUi.this.alphaNormal);
                    }
                }
            }
        });
    }
}


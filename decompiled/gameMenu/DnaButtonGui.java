/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import fontRendering.Text;
import gameMenu.GameMenuBackground;
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

public class DnaButtonGui
extends GuiClickable {
    private static final float FONT_SIZE = 1.0f;
    private static final float TEXT_X = 0.05f;
    private static final float TEXT_Y = 0.013f;
    private static final float MAX_SIZE = 1.15f;
    private final GuiTexture line;
    private final GuiTexture background;
    private Text text;

    public DnaButtonGui(GuiTexture dnaLine, String text) {
        super(1.15f);
        this.line = dnaLine;
        super.setMaintainPositionX(true);
        this.background = this.initBackgroundTexture();
        this.addMouseoverListener();
        this.addText(text);
    }

    protected void notifyOpening() {
        Colour colour = super.isBlocked() ? GameMenuBackground.getBlockedColour() : GameMenuBackground.getStandardColour();
        this.line.setOverrideColour(colour);
        this.background.setOverrideColour(colour);
    }

    public void setText(String newText) {
        this.text.setText(newText);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.background.update();
    }

    @Override
    public void block(boolean block) {
        super.block(block);
        Colour colour = super.isBlocked() ? GameMenuBackground.getBlockedColour() : GameMenuBackground.getStandardColour();
        this.line.setOverrideColour(colour);
        this.background.setOverrideColour(colour);
        this.text.setColour(block ? ColourPalette.LIGHT_GREY : ColourPalette.WHITE);
    }

    public void setColour(Colour colour) {
        this.text.setColour(colour);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    private GuiTexture initBackgroundTexture() {
        GuiTexture background = new GuiTexture(GuiRepository.DNA_BUTTON);
        background.setOverrideColour(GameMenuBackground.getStandardColour());
        background.setAlphaDriver(new ConstantDriver(0.7f));
        return background;
    }

    private void addMouseoverListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    DnaButtonGui.this.background.setOverrideColour(ColourPalette.GREEN);
                    DnaButtonGui.this.line.setOverrideColour(ColourPalette.GREEN);
                } else if (event.isMouseOff()) {
                    DnaButtonGui.this.background.setOverrideColour(GameMenuBackground.getStandardColour());
                    DnaButtonGui.this.line.setOverrideColour(GameMenuBackground.getStandardColour());
                }
            }
        });
    }

    private void addText(String name) {
        this.text = Text.newText(name).setFontSize(1.0f).create();
        this.text.setColour(ColourPalette.WHITE);
        super.addText(this.text, 0.05f, 0.013f, 1.0f);
    }
}


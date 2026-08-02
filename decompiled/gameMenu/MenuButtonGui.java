/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiMaster;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import userInterfaces.GuiImage;
import utils.MyFile;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;

public class MenuButtonGui
extends GuiClickable {
    private static final Texture FOCUS_LINES = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "focusLines.png")).noFiltering().create();
    private static final float FADE_TIME = 0.15f;
    private static final float LINE_SIZE = 2.3f;
    private GuiTexture backgroundTexture;
    private GuiImage lines;
    private Text text;
    private float fontSize;
    private float textY;

    public MenuButtonGui(String text, boolean showLines, boolean toggle, float fontSize, float textY) {
        super(toggle);
        this.fontSize = fontSize;
        this.textY = textY;
        this.initBackground(toggle);
        this.initLines();
        this.initText(text);
        this.addClickListener(showLines);
    }

    @Override
    public void setOn() {
        this.backgroundTexture.setOverrideColour(ColourPalette.GREEN);
        super.setOn();
    }

    public void setText(String newText) {
        this.text.setText(newText);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.backgroundTexture.update();
    }

    @Override
    public void block(boolean block) {
        super.block(block);
        this.backgroundTexture.setOverrideColour(block ? ColourPalette.MIDDLE_GREY : ColourPalette.GREEN);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.backgroundTexture.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.backgroundTexture);
    }

    private void initBackground(boolean toggle) {
        this.backgroundTexture = new GuiTexture(GuiRepository.FADE);
        this.backgroundTexture.setOverrideColour(toggle ? ColourPalette.LIGHT_GREY : ColourPalette.GREEN);
    }

    private void initLines() {
        this.lines = new GuiImage(FOCUS_LINES);
        this.lines.setPreferredAspectRatio(16.0f);
        this.lines.getTexture().setAlphaDriver(new ConstantDriver(0.0f));
        super.addCenteredComponent(this.lines, 0.5f, 0.5f, 2.3f);
    }

    private void initText(String name) {
        this.text = Text.newText(name).center().setFontSize(this.fontSize).create();
        this.text.setColour(ColourPalette.WHITE);
        super.addText(this.text, -1.0f, this.textY, 3.0f);
    }

    private void addClickListener(final boolean showLines) {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    if (showLines) {
                        GuiTexture lineTexture = MenuButtonGui.this.lines.getTexture();
                        lineTexture.setAlphaDriver(new SlideDriver(lineTexture.getAlpha(), 1.0f, 0.15f));
                    }
                } else if (showLines) {
                    GuiTexture lineTexture = MenuButtonGui.this.lines.getTexture();
                    lineTexture.setAlphaDriver(new SlideDriver(lineTexture.getAlpha(), 0.0f, 0.15f));
                }
                if (event.isToggleOn()) {
                    MenuButtonGui.this.backgroundTexture.setOverrideColour(ColourPalette.GREEN);
                } else if (event.isToggleOff()) {
                    MenuButtonGui.this.backgroundTexture.setOverrideColour(ColourPalette.LIGHT_GREY);
                }
            }
        });
    }
}


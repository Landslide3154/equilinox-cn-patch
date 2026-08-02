/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import visualFxDrivers.ConstantDriver;

public class PageButtonUi
extends GuiClickable {
    private static final Colour ON_COL = ColourPalette.GREEN;
    private static final Colour OFF_COL = ColourPalette.LIGHT_GREY;
    private static final ConstantDriver ON_ALPHA = new ConstantDriver(1.0f);
    private static final ConstantDriver OFF_ALPHA = new ConstantDriver(0.3f);
    private static final float TEXT_Y = 0.03f;
    private final int index;
    private GuiTexture background;

    public PageButtonUi(int number) {
        super(true, 1.0f);
        this.index = number;
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(OFF_COL);
        this.background.setAlphaDriver(OFF_ALPHA);
        this.addListener();
    }

    @Override
    protected void init() {
        super.init();
        Text text = Text.newText(Integer.toString(this.index)).center().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, 0.03f, 1.0f);
    }

    @Override
    public void toggle() {
        super.toggle();
        if (super.isToggledOn()) {
            this.background.setOverrideColour(ON_COL);
            this.background.setAlphaDriver(ON_ALPHA);
        } else {
            this.background.setOverrideColour(OFF_COL);
            this.background.setAlphaDriver(OFF_ALPHA);
        }
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.background.update();
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

    private void addListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    PageButtonUi.this.background.setAlphaDriver(ON_ALPHA);
                } else if (event.isMouseOff()) {
                    PageButtonUi.this.background.setAlphaDriver(OFF_ALPHA);
                }
            }
        });
    }
}


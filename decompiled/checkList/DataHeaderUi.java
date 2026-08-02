/*
 * Decompiled with CFR 0.152.
 */
package checkList;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.GuiClickable;
import visualFxDrivers.ConstantDriver;

public class DataHeaderUi
extends GuiClickable {
    private static final float TEXT_PAD_X = 0.05f;
    private static final float TEXT_PAD_Y = 0.1f;
    private GuiTexture background;
    private final String header;

    public DataHeaderUi(String header) {
        super(true, 1.0f);
        this.header = header;
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setAlphaDriver(new ConstantDriver(0.2f));
        this.background.setOverrideColour(ColourPalette.LIGHT_GREY);
    }

    @Override
    protected void init() {
        super.init();
        this.addText();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.background.update();
        if (super.isToggledOn()) {
            this.background.setOverrideColour(ColourPalette.GREEN);
            ((ConstantDriver)this.background.getAlphaDriver()).setValue(1.0f);
        } else if (super.isMouseOver()) {
            this.background.setOverrideColour(ColourPalette.BRIGHT_GREY);
            ((ConstantDriver)this.background.getAlphaDriver()).setValue(0.2f);
        } else {
            this.background.setOverrideColour(ColourPalette.LIGHT_GREY);
            ((ConstantDriver)this.background.getAlphaDriver()).setValue(0.2f);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    private void addText() {
        Text text = Text.newText(this.header).setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.05f, 0.1f, 1.0f);
    }
}


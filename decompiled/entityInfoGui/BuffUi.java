/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.BuffPanelUi;
import entityInfoGui.EntityInfoGui;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.TextStatInfo;
import visualFxDrivers.ConstantDriver;

public class BuffUi
extends GuiComponent {
    private final GuiTexture background;
    private final TextStatInfo info;
    private final BuffPanelUi buffPanel;

    public BuffUi(TextStatInfo info, BuffPanelUi buffPanel) {
        this.info = info;
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.LIGHT_GREY);
        this.background.setAlphaDriver(new ConstantDriver(0.2f));
        this.buffPanel = buffPanel;
    }

    @Override
    protected void init() {
        super.init();
        float xPos = super.pixelsToRelativeX(8.0f);
        this.addInfo(xPos);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        if (super.isMouseOver()) {
            this.buffPanel.notifyMouseOver(this.info);
        }
        this.background.update();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (super.isMouseOver()) {
            data.addTexture(this.getLevel(), this.background);
        }
    }

    private void addInfo(float xPos) {
        this.addName(this.info.name, this.info.nameColour, xPos);
        this.addValue(this.info.value, this.info.valueColour, xPos);
    }

    private void addName(String name, Colour colour, float xPos) {
        Text nameText = Text.newText(String.valueOf(name) + ":").setFontSize(EntityInfoGui.FONT_SIZE).create();
        nameText.setColour(colour);
        super.addText(nameText, xPos, 0.0f, 1.0f);
    }

    private void addValue(String value, Colour colour, float xPos) {
        Text valueText = Text.newText(value).setFontSize(EntityInfoGui.FONT_SIZE).create();
        valueText.setColour(colour);
        super.addText(valueText, 0.54f, 0.0f, 1.0f);
    }
}


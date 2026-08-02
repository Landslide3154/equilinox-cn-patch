/*
 * Decompiled with CFR 0.152.
 */
package toolTips;

import entityInfoGui.EntityInfoGui;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import toolTips.ToolTip;
import visualFxDrivers.SlideDriver;

public class DescriptionPanel
extends GuiComponent {
    private String desc;
    private ToolTip toolTip;
    private Text text;

    protected DescriptionPanel(String desc, ToolTip toolTip) {
        this.desc = desc;
        this.toolTip = toolTip;
    }

    public void fadeOut(float time) {
        this.text.setAlphaDriver(new SlideDriver(1.0f, 0.0f, time));
    }

    @Override
    protected void init() {
        super.init();
        this.text = Text.newText(this.desc).setFontSize(EntityInfoGui.FONT_SIZE).create();
        this.text.setColour(ColourPalette.WHITE);
        super.addText(this.text, 0.0f, 0.0f, 1.0f);
        this.toolTip.resize(this.text.getHeight());
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
}


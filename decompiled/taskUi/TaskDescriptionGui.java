/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;

public class TaskDescriptionGui
extends GuiComponent {
    private String description;

    public TaskDescriptionGui(String desc) {
        this.description = desc;
    }

    @Override
    protected void init() {
        super.init();
        Text text = Text.newText(this.description).justify().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, 0.0f, 1.0f);
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


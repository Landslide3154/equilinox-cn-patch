/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.EntityInfoGui;
import entityInfoGui.EntityPopUpPanel;
import fontRendering.Text;
import mainGuis.ColourPalette;

public class BuffDescriptionUi
extends EntityPopUpPanel {
    private final Text text;

    public BuffDescriptionUi(String description) {
        super(ColourPalette.DARK_GREY, 0.75f);
        this.text = Text.newText(description).setFontSize(EntityInfoGui.FONT_SIZE).create();
        this.text.setColour(ColourPalette.BEIGE);
        super.setBlurry();
        super.addText(this.text, 0.03f, 0.04f, 0.9f);
    }

    protected void setDescription(String newText) {
        this.text.setText(newText);
    }

    @Override
    public float getMaxY() {
        return 0.0f;
    }

    @Override
    public float getMinY() {
        return 0.0f;
    }

    @Override
    public void addToParentPanel(EntityInfoGui parentPanel) {
        float pad = 5.0f / parentPanel.getPixelWidth();
        float titleHeight = parentPanel.getTitleBarHeight();
        parentPanel.addComponent(this, 1.0f + pad, titleHeight, 1.0f, 1.0f - titleHeight);
    }
}


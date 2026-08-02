/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.InfoType;
import entityInfoGui.PopUpInfoGui;
import fontRendering.Text;
import mainGuis.ColourPalette;
import toolbox.Colour;

public abstract class TextInfo
extends PopUpInfoGui {
    private Colour colour = ColourPalette.BEIGE;
    private Text valueText;

    public TextInfo(String name, float font) {
        super(name, InfoType.TEXT, font);
    }

    public TextInfo(String name, float font, boolean header) {
        super(name, header ? InfoType.HEADER : InfoType.TEXT, font);
    }

    public TextInfo(String name, float font, Colour colour) {
        super(name, colour, InfoType.TEXT, font);
        this.colour = colour;
    }

    public TextInfo(String name, float font, Colour colour, InfoType type) {
        super(name, colour, type, font);
        this.colour = colour;
    }

    public TextInfo(String name, float font, InfoType type) {
        super(name, type, font);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        String newValue = this.getValue();
        if (!newValue.equals(this.valueText.getTextString())) {
            this.valueText.setText(newValue);
        }
    }

    @Override
    protected void initValueGui() {
        this.valueText = Text.newText(this.getValue()).setFontSize(super.getFontSize()).create();
        this.valueText.setColour(this.colour);
        super.addText(this.valueText, 0.54f, 0.0f, 1.0f);
    }

    public abstract String getValue();
}


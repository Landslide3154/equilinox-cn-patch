/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.InfoType;
import entityInfoGui.PopUpInfoGui;
import languages.GameText;
import mainGuis.ColourPalette;
import toolbox.Colour;
import userInterfaces.GuiPanel;

public class ColourInfoGui
extends PopUpInfoGui {
    private static final String NAME = GameText.getText(37);
    private Colour colour;

    public ColourInfoGui(Colour colour, float font) {
        super(NAME, InfoType.COLOUR, font);
        this.colour = colour;
    }

    @Override
    protected void initValueGui() {
        super.addCenteredComponentYScaleY(new GuiPanel(this.colour, 2, ColourPalette.BEIGE), 0.5f, 0.54f, 0.8f);
    }
}


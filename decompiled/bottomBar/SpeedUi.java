/*
 * Decompiled with CFR 0.152.
 */
package bottomBar;

import bottomBar.SpeedButtonUi;
import languages.GameText;
import mainGuis.ColourPalette;
import userInterfaces.GuiPanel;

public class SpeedUi
extends GuiPanel {
    private static final String TIME_TIP = GameText.getText(172);
    private static final String TIME_DESC = GameText.getText(173);

    public SpeedUi() {
        super(ColourPalette.DARK_GREY, 0.75f);
    }

    @Override
    protected void init() {
        super.init();
        this.addButton();
    }

    private void addButton() {
        super.addComponent(new SpeedButtonUi(), 0.15f, 0.15f, 0.7f, 0.7f);
    }
}


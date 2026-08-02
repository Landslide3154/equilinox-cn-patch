/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import gameMenu.ControlsPanel;
import gameMenu.GameMenuGui;
import gameMenu.SecondPanelUi;

public class ControlsScreenGui
extends SecondPanelUi {
    private static final float Y_SIZE = 0.9f;
    private static final float X_SIZE = 0.6f;

    protected ControlsScreenGui(GameMenuGui mainMenu) {
        super(mainMenu);
    }

    @Override
    protected void init() {
        super.init();
        ControlsPanel panel = new ControlsPanel();
        super.addComponent(panel, 0.19999999f, 0.050000012f, 0.6f, 0.9f);
    }
}


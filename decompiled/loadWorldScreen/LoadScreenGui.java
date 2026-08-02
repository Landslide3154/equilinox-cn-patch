/*
 * Decompiled with CFR 0.152.
 */
package loadWorldScreen;

import gameMenu.GameMenuBackground;
import gameMenu.GameMenuGui;
import gameMenu.SecondPanelUi;
import loadWorldScreen.SaveInfoPanel;
import loadWorldScreen.SaveSlotsPanel;
import saves.SaveSlot;

public class LoadScreenGui
extends SecondPanelUi {
    private static final float PANELS_Y = 0.1f;
    private static final float PANELS_HEIGHT = 0.8f;
    private static final float LEFT_PANEL_X = 0.2f;
    private static final float LEFT_PANEL_WIDTH = 0.2f;
    private static final float GAP = 0.01f;
    private static final float RIGHT_PANEL_X = 0.41f;
    private static final float RIGHT_PANEL_WIDTH = 0.39000002f;
    private GameMenuGui gameMenu;
    private GameMenuBackground superGameMenu;
    private SaveSlotsPanel slotsPanel;
    private SaveInfoPanel infoPanel;

    public LoadScreenGui(GameMenuBackground superGameMenu, GameMenuGui menu) {
        super(menu);
        this.gameMenu = menu;
        this.superGameMenu = superGameMenu;
        this.addLeftPanel();
    }

    protected void updateSlotsPanel(SaveSlot slot) {
        this.slotsPanel.updateText(slot);
    }

    protected SaveInfoPanel addInfoPanel(SaveSlot slot) {
        if (this.infoPanel != null) {
            this.removeComponent(this.infoPanel);
        }
        this.infoPanel = new SaveInfoPanel(slot, this.superGameMenu, this.gameMenu, this);
        this.addComponent(this.infoPanel, 0.41f, 0.1f, 0.39000002f, 0.8f);
        return this.infoPanel;
    }

    protected void reset() {
        super.removeComponent(this.slotsPanel);
        this.addLeftPanel();
    }

    private void addLeftPanel() {
        this.slotsPanel = new SaveSlotsPanel(this);
        this.addComponent(this.slotsPanel, 0.2f, 0.1f, 0.2f, 0.8f);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import gameMenu.GameMenuBackground;
import gameMenu.GameMenuGui;
import gameMenu.MenuPanelGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import org.lwjgl.util.vector.Vector2f;

public class MainMenuGui
extends GuiComponent {
    public static final float BACK_BUTTON_Y = 0.97f;
    private MenuPanelGui panel;

    protected MainMenuGui(GameMenuBackground superMenu, GameMenuGui menu) {
        this.panel = new MenuPanelGui(menu, superMenu);
    }

    @Override
    protected void init() {
        super.addComponent(this.panel, 0.04f, 0.0f, 1.0f, 1.0f);
    }

    @Override
    protected void updateSelf() {
    }

    protected void notifyBackOnScreen() {
        this.panel.updateNewWorldButton();
    }

    protected void notifyOpening() {
        this.panel.notifyOpening();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }
}


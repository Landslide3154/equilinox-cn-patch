/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import gameMenu.GameMenuGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.IconButtonUi;

public class SecondPanelUi
extends GuiComponent {
    private final GameMenuGui gameMenu;

    public SecondPanelUi(GameMenuGui gameMenu) {
        this.gameMenu = gameMenu;
    }

    @Override
    protected void init() {
        super.init();
        this.createBackOption();
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

    private void createBackOption() {
        IconButtonUi backButton = new IconButtonUi(GameMenuGui.BACK_ICON, ColourPalette.DARK_GREY, ColourPalette.MIDDLE_GREY);
        backButton.setAlpha(0.75f);
        super.addComponentY(backButton, GameMenuGui.BACK_BUTTON_POS.x, GameMenuGui.BACK_BUTTON_POS.y, 0.12f);
        ClickListener listener = new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    SecondPanelUi.this.gameMenu.closeSecondaryScreen();
                }
            }
        };
        backButton.addListener(listener);
    }
}


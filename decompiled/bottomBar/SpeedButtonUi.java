/*
 * Decompiled with CFR 0.152.
 */
package bottomBar;

import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;

public class SpeedButtonUi
extends GuiClickable {
    private final GuiTexture texture = new GuiTexture(GuiRepository.FAST_FORWARD);

    public SpeedButtonUi() {
        this.addTurnOnListener();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.texture.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
    }

    private void addTurnOnListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    SpeedButtonUi.this.texture.setOverrideColour(ColourPalette.BASE_BLUE);
                    GameManager.getGameSpeed().fastSpeed();
                } else if (event.isMouseOff() || event.isLeftClickRelease()) {
                    SpeedButtonUi.this.texture.setOverrideColour(ColourPalette.WHITE);
                    GameManager.getGameSpeed().normalSpeed();
                }
            }
        });
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.texture);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package bottomBar;

import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;

public class ArrowButtonUi
extends GuiClickable {
    private final GuiTexture icon;
    private boolean forwards;
    private boolean active = false;
    private static final float DAY_SECONDS = 8.0f;

    public ArrowButtonUi(Texture icon, boolean forwards) {
        super(1.0f);
        this.forwards = forwards;
        this.icon = new GuiTexture(icon);
    }

    @Override
    protected void init() {
        super.init();
        this.addListener();
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        if (this.active) {
            this.changeTime();
        }
    }

    private void addListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    ArrowButtonUi.this.icon.setOverrideColour(ColourPalette.LIGHT_GREY);
                } else if (event.isMouseOff()) {
                    ArrowButtonUi.this.icon.setOverrideColour(ColourPalette.WHITE);
                    ArrowButtonUi.this.active = false;
                }
                if (event.isLeftClick()) {
                    ArrowButtonUi.this.active = true;
                    ArrowButtonUi.this.icon.setOverrideColour(ColourPalette.GREEN);
                } else if (event.isLeftClickRelease()) {
                    ArrowButtonUi.this.active = false;
                    ArrowButtonUi.this.icon.setOverrideColour(ColourPalette.LIGHT_GREY);
                }
            }
        });
    }

    private void changeTime() {
        float delta = GameManager.getGameSeconds();
        if (this.forwards) {
            GameManager.getSession().getStats().getCalendar().increaseTime(delta / 8.0f);
        } else {
            GameManager.getSession().getStats().getCalendar().increaseTime(-delta / 8.0f);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.icon.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.icon);
    }
}


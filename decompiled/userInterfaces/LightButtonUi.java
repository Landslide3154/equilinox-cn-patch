/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;

public class LightButtonUi
extends GuiClickable {
    private static final float LIGHT_VALUE = 1.3f;
    private final Colour colour;
    private final Colour lightColour;
    private GuiTexture guiTexture;

    public LightButtonUi(Texture texture, Colour colour) {
        super(1.0f);
        this.guiTexture = new GuiTexture(texture);
        this.guiTexture.setOverrideColour(colour);
        this.colour = colour;
        this.lightColour = colour.duplicate().scale(1.3f);
        this.addMouseOverEffect();
    }

    @Override
    public void block(boolean block) {
        super.block(block);
        if (block) {
            this.guiTexture.setOverrideColour(ColourPalette.MIDDLE_GREY);
        } else {
            this.guiTexture.setOverrideColour(this.colour);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.guiTexture.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.guiTexture);
    }

    private void addMouseOverEffect() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    LightButtonUi.this.guiTexture.setOverrideColour(LightButtonUi.this.lightColour);
                } else if (event.isMouseOff()) {
                    LightButtonUi.this.guiTexture.setOverrideColour(LightButtonUi.this.colour);
                }
            }
        });
    }
}


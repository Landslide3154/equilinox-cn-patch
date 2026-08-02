/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import toolbox.MyKeyboard;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;

public class SearchButtonUi
extends GuiClickable {
    private static final float BRIGHT_FACTOR = 1.2f;
    private final Texture icon;
    private final Colour onColour;
    private final Colour offColour;
    private final Colour onColourBright;
    private final Colour offColourBright;
    private GuiTexture background;
    private GuiTexture image;

    public SearchButtonUi(Texture icon, Colour off, Colour on, boolean active) {
        super(true, 1.0f);
        super.setHotkey(28);
        if (active) {
            super.setOn();
        }
        this.icon = icon;
        this.offColour = off;
        this.onColour = on;
        this.onColourBright = this.onColour.duplicate().scale(1.2f);
        this.offColourBright = this.offColour.duplicate().scale(1.2f);
        this.initTextures();
        this.addListener();
    }

    @Override
    protected void updateSelf() {
        if (MyKeyboard.getKeyboard().keyDownEventOccurredIgnoreBlock(1) && super.isToggledOn()) {
            super.toggle();
        }
        super.updateSelf();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        this.image.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.image);
    }

    private void initTextures() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(this.isToggledOn() ? this.onColour : this.offColour);
        this.image = new GuiTexture(this.icon);
        this.image.setOverrideColour(ColourPalette.WHITE);
    }

    private void addListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    SearchButtonUi.this.background.setOverrideColour(SearchButtonUi.super.isToggledOn() ? SearchButtonUi.this.onColourBright : SearchButtonUi.this.offColourBright);
                } else if (event.isMouseOff()) {
                    SearchButtonUi.this.background.setOverrideColour(SearchButtonUi.super.isToggledOn() ? SearchButtonUi.this.onColour : SearchButtonUi.this.offColour);
                } else if (event.isToggleOn()) {
                    SearchButtonUi.this.background.setOverrideColour(SearchButtonUi.this.onColour);
                } else if (event.isToggleOff()) {
                    SearchButtonUi.this.background.setOverrideColour(SearchButtonUi.this.offColour);
                }
            }
        });
    }
}


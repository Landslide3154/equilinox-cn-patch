/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import audio.SoundMaestro;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiSounds;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import visualFxDrivers.ConstantDriver;

public class Tab2ButtonUi
extends GuiClickable {
    private final GuiTexture offIcon;
    private final GuiTexture onIcon;
    private Colour blockedColour = ColourPalette.MIDDLE_GREY;
    private Colour normalColour = ColourPalette.WHITE;
    private Colour moColour;
    private Colour onColour = this.moColour = ColourPalette.GREEN;
    private boolean transparentBlock = false;

    public Tab2ButtonUi(Texture offIcon, Texture onIcon) {
        super(true, 1.0f);
        this.offIcon = new GuiTexture(offIcon);
        this.onIcon = new GuiTexture(onIcon);
    }

    public Tab2ButtonUi(Texture offIcon, Texture onIcon, Colour normalColour, boolean toggle) {
        super(toggle, 1.0f);
        this.normalColour = normalColour;
        this.offIcon = new GuiTexture(offIcon);
        this.offIcon.setOverrideColour(normalColour);
        this.onIcon = new GuiTexture(onIcon);
    }

    public void setBlockColour(Colour colour, boolean transBlock) {
        this.blockedColour = colour;
        this.transparentBlock = transBlock;
    }

    public void setOnColour(Colour colour) {
        this.onColour = colour;
    }

    public void setMouseOverColour(Colour colour) {
        this.onColour = this.moColour = colour;
    }

    @Override
    protected void init() {
        super.init();
        this.onIcon.setOverrideColour(super.isBlocked() ? ColourPalette.BASE_BLUE : this.onColour);
        this.offIcon.setOverrideColour(super.isBlocked() ? this.blockedColour : this.normalColour);
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    SoundMaestro.playSystemSound(GuiSounds.getClickSound());
                }
            }
        });
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.offIcon.update();
    }

    @Override
    public void toggle() {
        super.toggle();
    }

    @Override
    public void block(boolean block) {
        super.block(block);
        if (block) {
            this.offIcon.setOverrideColour(this.blockedColour);
            this.onIcon.setOverrideColour(ColourPalette.BASE_BLUE);
            if (this.transparentBlock) {
                this.offIcon.setAlphaDriver(new ConstantDriver(0.2f));
            }
        } else {
            this.offIcon.setOverrideColour(this.normalColour);
            this.onIcon.setOverrideColour(this.onColour);
            if (this.transparentBlock) {
                this.offIcon.setAlphaDriver(new ConstantDriver(1.0f));
            }
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.offIcon.setPosition(position.x, position.y, scale.x, scale.y);
        this.onIcon.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void mouseOverOccurred() {
        super.mouseOverOccurred();
        this.offIcon.setOverrideColour(this.moColour);
    }

    @Override
    protected void mouseOffOccurred() {
        super.mouseOffOccurred();
        this.offIcon.setOverrideColour(this.normalColour);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (super.isToggledOn()) {
            data.addTexture(this.getLevel(), this.onIcon);
        } else {
            data.addTexture(this.getLevel(), this.offIcon);
        }
    }
}


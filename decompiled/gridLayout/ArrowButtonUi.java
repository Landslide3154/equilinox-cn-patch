/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import audio.SoundMaestro;
import gridLayout.PageSelectionGui;
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

public class ArrowButtonUi
extends GuiClickable {
    private static final Colour ON_COL = ColourPalette.GREEN;
    private static final Colour OFF_COL = ColourPalette.LIGHT_GREY;
    private static final ConstantDriver ON_ALPHA = new ConstantDriver(1.0f);
    private static final ConstantDriver OFF_ALPHA = new ConstantDriver(0.3f);
    private static final ConstantDriver BLOCK_ALPHA = new ConstantDriver(0.1f);
    private final GuiTexture icon;
    private final boolean right;
    private boolean clicking = false;
    private final PageSelectionGui selection;

    public ArrowButtonUi(Texture texture, PageSelectionGui selection, boolean right) {
        super(1.0f);
        this.right = right;
        this.selection = selection;
        this.icon = new GuiTexture(texture);
        this.icon.setOverrideColour(OFF_COL);
        this.icon.setAlphaDriver(OFF_ALPHA);
        this.addListener();
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

    @Override
    public void block(boolean block) {
        if (super.isBlocked() == block) {
            return;
        }
        super.block(block);
        this.icon.setAlphaDriver(block ? BLOCK_ALPHA : OFF_ALPHA);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.icon.update();
    }

    private void addListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    ArrowButtonUi.this.icon.setOverrideColour(ON_COL);
                    ArrowButtonUi.this.clicking = true;
                } else if (event.isLeftClickRelease()) {
                    ArrowButtonUi.this.icon.setOverrideColour(OFF_COL);
                    if (ArrowButtonUi.this.clicking) {
                        ArrowButtonUi.this.selection.notifyNext(ArrowButtonUi.this.right);
                        SoundMaestro.playSystemSound(GuiSounds.getClickSound());
                        ArrowButtonUi.this.clicking = false;
                    }
                }
                if (event.isMouseOver()) {
                    ArrowButtonUi.this.icon.setAlphaDriver(ON_ALPHA);
                } else if (event.isMouseOff()) {
                    ArrowButtonUi.this.icon.setAlphaDriver(OFF_ALPHA);
                    ArrowButtonUi.this.icon.setOverrideColour(OFF_COL);
                    ArrowButtonUi.this.clicking = false;
                }
            }
        });
    }
}


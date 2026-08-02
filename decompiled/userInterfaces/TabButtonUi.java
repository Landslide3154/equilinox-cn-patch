/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import userInterfaces.GuiClickable;
import userInterfaces.GuiImage;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SinWaveDriver;
import visualFxDrivers.ValueDriver;

public class TabButtonUi
extends GuiClickable {
    private static final float FLASH_TIME = 0.6f;
    private final int iconPixels;
    private final GuiImage icon;
    private GuiTexture background;
    private boolean flashing = false;
    private ValueDriver colourDriver = new ConstantDriver(0.0f);

    public TabButtonUi(Texture iconImage, int iconPixels) {
        super(true, 1.0f);
        this.iconPixels = iconPixels;
        this.icon = new GuiImage(iconImage);
        this.initBackground();
    }

    @Override
    protected void init() {
        super.init();
        this.addIcon();
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        if (this.flashing && !super.isMouseOver()) {
            this.icon.getTexture().setOverrideColour(Colour.interpolateColours(ColourPalette.WHITE, ColourPalette.GREEN, this.colourDriver.update(DisplayManager.getDeltaSeconds()), null));
        }
    }

    public void flash(boolean flash) {
        this.flashing = flash;
        if (this.flashing) {
            this.colourDriver = new SinWaveDriver(0.0f, 1.0f, 0.6f);
        } else {
            this.icon.getTexture().setOverrideColour(ColourPalette.WHITE);
            this.colourDriver = new ConstantDriver(0.0f);
        }
    }

    @Override
    public void toggle() {
        super.toggle();
        this.flash(false);
        if (super.isToggledOn()) {
            this.icon.getTexture().setOverrideColour(ColourPalette.WHITE);
        } else {
            this.icon.getTexture().setOverrideColour(super.isMouseOver() ? ColourPalette.GREEN : ColourPalette.WHITE);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void mouseOverOccurred() {
        super.mouseOverOccurred();
        this.icon.getTexture().setOverrideColour(ColourPalette.GREEN);
    }

    @Override
    protected void mouseOffOccurred() {
        super.mouseOffOccurred();
        this.icon.getTexture().setOverrideColour(ColourPalette.WHITE);
    }

    private void addIcon() {
        this.icon.setPreferredPixelSize(this.iconPixels);
        super.addPixelCompCenter(this.icon, 0.5f, 0.5f);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (super.isToggledOn()) {
            data.addTexture(this.getLevel(), this.background);
        }
    }

    private void initBackground() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.GREEN);
    }
}


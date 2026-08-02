/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import interpolation.SmoothFloat;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.Maths;
import visualFxDrivers.ConstantDriver;

public class PlainProgressBarUi
extends GuiComponent {
    private static final float AGILITY = 5.0f;
    private GuiTexture background;
    private GuiTexture foreground;
    private SmoothFloat progress;

    public PlainProgressBarUi(Colour foregroundColour, Colour backgroundColour, float progress) {
        this.progress = new SmoothFloat(Maths.clamp(progress, 0.0f, 1.0f), 5.0f);
        this.foreground = new GuiTexture(GuiRepository.BLOCK);
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setAlphaDriver(new ConstantDriver(0.5f));
        this.foreground.setOverrideColour(foregroundColour);
        this.background.setOverrideColour(backgroundColour);
    }

    public void setBarColour(Colour colour) {
        this.foreground.setOverrideColour(colour);
    }

    public void setProgress(float progress) {
        this.progress.setTarget(Maths.clamp(progress, 0.0f, 1.0f));
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        this.foreground.setPosition(position.x, position.y, scale.x * this.progress.get(), scale.y);
    }

    @Override
    protected void updateSelf() {
        this.progress.update(DisplayManager.getDeltaSeconds());
        this.background.update();
        this.foreground.setWidth(super.getScale().x * this.progress.get());
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.foreground);
    }
}


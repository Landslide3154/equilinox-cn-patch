/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import visualFxDrivers.ConstantDriver;

public class LoadingBar
extends GuiComponent {
    private static final float ALPHA = 0.4f;
    private GuiTexture barTexture;
    private GuiTexture barOutlineTexture;
    private float barScaleX;
    private float value = 0.6f;

    public LoadingBar() {
        this.initBarTextures();
    }

    public void setValue(float value) {
        this.value = value > 1.0f ? 1.0f : value;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.barScaleX = scale.x;
        this.barOutlineTexture.setPosition(position.x, position.y, scale.x, scale.y);
        this.barTexture.setPosition(position.x, position.y, this.calculateBarScaleX(), scale.y);
    }

    @Override
    protected void updateSelf() {
        this.barTexture.getScale().x = this.calculateBarScaleX();
        this.barOutlineTexture.update();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.barOutlineTexture);
        data.addTexture(this.getLevel(), this.barTexture);
    }

    private float calculateBarScaleX() {
        return this.barScaleX * this.value;
    }

    private void initBarTextures() {
        this.barTexture = new GuiTexture(GuiRepository.BLOCK);
        this.barTexture.setOverrideColour(ColourPalette.GREEN);
        this.barOutlineTexture = new GuiTexture(GuiRepository.BLOCK);
        this.barOutlineTexture.setOverrideColour(ColourPalette.DARK_GREY);
        this.barOutlineTexture.setAlphaDriver(new ConstantDriver(0.4f));
    }
}


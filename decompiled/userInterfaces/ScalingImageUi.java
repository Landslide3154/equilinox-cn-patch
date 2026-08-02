/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import guis.GuiTexture;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import userInterfaces.GuiImage;
import visualFxDrivers.SinWaveDriver;
import visualFxDrivers.ValueDriver;

public class ScalingImageUi
extends GuiImage {
    private static final float SIZE_UP_X = 0.02f;
    private static final float SIZE_UP_Y = 0.04f;
    private ValueDriver sinDriver = new SinWaveDriver(0.0f, 1.0f, 3.0f);
    private float xScaleValue = 1.0f;
    private float yScaleValue = 1.0f;

    public ScalingImageUi(GuiTexture image) {
        super(image);
    }

    public ScalingImageUi(Texture texture) {
        super(texture);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        float x = position.x - (this.xScaleValue - 1.0f) * 0.5f * scale.x;
        float y = position.y - (this.yScaleValue - 1.0f) * 0.5f * scale.y;
        super.getTexture().setPosition(x, y, scale.x * this.xScaleValue, scale.y * this.yScaleValue);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        float value = this.sinDriver.update(DisplayManager.getDeltaSeconds());
        this.xScaleValue = 1.0f + value * 0.02f;
        this.yScaleValue = 1.0f + (1.0f - value) * 0.04f;
        Vector2f pos = super.getPosition();
        Vector2f scale = super.getScale();
        this.updateGuiTexturePositions(pos, scale);
    }
}


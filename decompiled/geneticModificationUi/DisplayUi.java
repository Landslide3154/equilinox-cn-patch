/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import basics.DisplayManager;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.GuiPanel;
import visualFxDrivers.BounceDriver;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.ValueDriver;

public abstract class DisplayUi
extends GuiPanel {
    public static final float PULSE_TIME = 0.27f;
    public static final float PULSE_SIZE = 1.3f;
    private Vector2f originalScale;
    private Vector2f originalPosition;
    private ValueDriver scaleDriver = new ConstantDriver(1.0f);
    private float currentScale = 1.0f;

    public DisplayUi(Colour colour) {
        super(colour);
    }

    public DisplayUi(Colour colour, int pixels, Colour borderCol) {
        super(colour, pixels, borderCol);
    }

    public abstract void block(boolean var1);

    public void pulse() {
        this.scaleDriver = new BounceDriver(1.0f, 1.3f, 0.27f);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.currentScale = this.scaleDriver.update(DisplayManager.getDeltaSeconds());
        this.updatePositions();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        if (this.originalPosition == null) {
            this.originalPosition = new Vector2f(this.getRelativeX(), this.getRelativeY());
            this.originalScale = new Vector2f(this.getRelativeScaleX(), this.getRelativeScaleY());
        }
    }

    private void updatePositions() {
        float currentX = this.calculateScaledPosition(this.originalPosition.x, this.originalScale.x);
        float currentY = this.calculateScaledPosition(this.originalPosition.y, this.originalScale.y);
        float currentScaleX = this.originalScale.x * this.currentScale;
        float currentScaleY = this.originalScale.y * this.currentScale;
        super.setRelativePosition(currentX, currentY);
        super.setRelativeScale(currentScaleX, currentScaleY);
    }

    private float calculateScaledPosition(float originalPos, float originalScale) {
        float change = originalScale * this.currentScale - originalScale;
        return originalPos - change / 2.0f;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package inventory;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import visualFxDrivers.BounceDriver;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.ValueDriver;

public class CountGui
extends GuiComponent {
    private static final Colour BACKGROUND = new Colour(91.0f, 116.0f, 98.0f, true);
    private GuiTexture blob;
    private Text countText;
    private final float fontSize;
    private final float textY;
    private Vector2f originalScale;
    private Vector2f originalPosition;
    private ValueDriver scaleDriver = new ConstantDriver(1.0f);
    private float currentScale = 1.0f;
    private int currentCount;
    private int nextCount;

    public CountGui(int count, float yPos) {
        this.fontSize = 0.8f;
        this.textY = yPos;
        this.addIcon();
        this.addText(count);
    }

    public CountGui(int count, int fontFactor, float yPos) {
        this.fontSize = (float)fontFactor / (float)DisplayManager.getUiHeight();
        this.textY = yPos;
        this.addIcon();
        this.addText(count);
    }

    public void setCount(int count) {
        this.nextCount = count;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.blob.setPosition(position.x, position.y, scale.x, scale.y);
        if (this.originalPosition == null) {
            this.originalPosition = new Vector2f(this.getRelativeX(), this.getRelativeY());
            this.originalScale = new Vector2f(this.getRelativeScaleX(), this.getRelativeScaleY());
        }
    }

    @Override
    protected void updateSelf() {
        if (this.currentCount != this.nextCount) {
            this.updateCount();
        }
        this.currentScale = this.scaleDriver.update(DisplayManager.getDeltaSeconds());
        this.updatePositions();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.blob);
    }

    private void updateCount() {
        this.currentCount = this.nextCount;
        this.scaleDriver = new BounceDriver(1.0f, 1.4f, 0.3f);
        this.countText.setText(Integer.toString(this.currentCount));
    }

    private void addIcon() {
        this.blob = new GuiTexture(GuiRepository.BLOB);
        this.blob.setOverrideColour(BACKGROUND);
    }

    private void addText(int count) {
        this.currentCount = this.nextCount = count;
        this.countText = Text.newText(Integer.toString(count)).center().setFontSize(this.fontSize).create();
        this.countText.setColour(ColourPalette.WHITE);
        super.addText(this.countText, -1.0f, this.textY, 3.0f);
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


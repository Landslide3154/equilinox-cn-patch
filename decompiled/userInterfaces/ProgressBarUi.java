/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.Maths;
import userInterfaces.GuiImage;
import visualFxDrivers.ConstantDriver;

public class ProgressBarUi
extends GuiComponent {
    private static final String PROGRESS = GameText.getText(863);
    private static final int ARROW_COUNT = 3;
    private static final int ARROW_SIZE_PX = 6;
    private static final float ARROW_GAP = 0.04f;
    private static final float ARROW_START_X = 0.2f;
    private static final float ARROW_TIME_DIF = 0.1f;
    private static final float ARROW_FADE_TIME = 0.5f;
    private static final float ARROW_HALF_FADE_TIME = 0.25f;
    private final GuiTexture barTexture;
    private final GuiTexture backgroundTexture;
    private Text text;
    private float progress;
    private boolean showPercent = true;
    private int max;
    private Colour barColour;
    private boolean flash = false;
    private float alphaProgress = 0.0f;
    private GuiImage[][] arrows;

    public ProgressBarUi(float progress) {
        this.setProgress(progress);
        this.barTexture = new GuiTexture(GuiRepository.BLOCK);
        this.barColour = ColourPalette.GREEN.duplicate();
        this.barTexture.setOverrideColour(this.barColour);
        this.backgroundTexture = new GuiTexture(GuiRepository.BLOCK);
        this.backgroundTexture.setOverrideColour(ColourPalette.LIGHT_GREY);
    }

    public ProgressBarUi(float progress, GuiTexture barTexture) {
        this.setProgress(progress);
        this.barTexture = barTexture;
        this.barColour = ColourPalette.GREEN.duplicate();
        this.barTexture.setOverrideColour(this.barColour);
        this.backgroundTexture = new GuiTexture(GuiRepository.BLOCK);
        this.backgroundTexture.setOverrideColour(ColourPalette.LIGHT_GREY);
    }

    public void showCountingText(int max, Colour colour, float fontSize, float y) {
        this.max = max;
        this.showPercent = false;
        this.showText(colour, fontSize, y);
    }

    public void showText(Colour colour, float fontSize, float y) {
        this.text = Text.newText(this.getTextString()).center().setFontSize(fontSize).create();
        this.text.setColour(colour);
        super.addText(this.text, 0.0f, y, 1.0f);
    }

    public void setBarColour(Colour colour) {
        this.barColour = colour.duplicate();
        this.barTexture.setOverrideColour(this.barColour);
    }

    public void setBackgroundColour(Colour colour) {
        this.backgroundTexture.setOverrideColour(colour);
    }

    public void setBackgroundAlpha(float alpha) {
        this.backgroundTexture.setAlphaDriver(new ConstantDriver(alpha));
    }

    public void setProgress(float progress) {
        this.progress = Math.min(1.0f, progress);
        if (this.text != null) {
            this.text.setText(this.getTextString());
        }
    }

    public void flashArrows(boolean flash) {
        this.flash = flash;
        if (super.isInitialized()) {
            this.showArrows();
        }
    }

    private void showArrows() {
        int i = 0;
        while (i < this.arrows.length) {
            int j = 0;
            while (j < this.arrows[i].length) {
                this.arrows[i][j].show(this.flash);
                ++j;
            }
            ++i;
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.backgroundTexture.setPosition(position.x, position.y, scale.x, scale.y);
        this.barTexture.setPosition(position.x, position.y, scale.x * this.progress, scale.y);
    }

    @Override
    protected void updateSelf() {
        this.alphaProgress += DisplayManager.getDeltaSeconds() * 0.7f;
        this.alphaProgress %= 1.0f;
        this.barTexture.update();
        this.backgroundTexture.update();
        this.barTexture.setWidth(this.calculateBarWidth());
        if (this.flash) {
            this.updateArrows();
        }
    }

    @Override
    protected void init() {
        super.init();
        this.arrows = new GuiImage[2][3];
        int i = 0;
        while (i < this.arrows.length) {
            GuiImage[] arrowRow = this.arrows[i];
            int j = 0;
            while (j < arrowRow.length) {
                this.arrows[i][j] = new GuiImage(GuiRepository.RIGHT_ARROW);
                super.addCenteredComponentY(this.arrows[i][j], 0.5f, 0.5f * (float)i + 0.2f + (float)j * 0.04f, super.pixelsToRelativeX(6.0f));
                ++j;
            }
            ++i;
        }
        this.showArrows();
    }

    private void updateArrows() {
        int i = 0;
        while (i < this.arrows.length) {
            GuiImage[] arrowRow = this.arrows[i];
            int j = 0;
            while (j < arrowRow.length) {
                float offset = 0.1f * (float)j;
                ConstantDriver alpha = (ConstantDriver)this.arrows[i][j].getTexture().getAlphaDriver();
                alpha.setValue(this.calcAlpha(0.0f + offset, 0.25f + offset, 0.5f + offset));
                ++j;
            }
            ++i;
        }
    }

    private float calcAlpha(float start, float peak, float end) {
        if (this.alphaProgress <= start) {
            return 0.0f;
        }
        if (this.alphaProgress >= end) {
            return 0.0f;
        }
        if (this.alphaProgress < peak) {
            float blend = (this.alphaProgress - start) / (peak - start);
            return Maths.smoothInterpolate(0.0f, 1.0f, blend);
        }
        float blend = (this.alphaProgress - peak) / (end - peak);
        return Maths.smoothInterpolate(1.0f, 0.0f, blend);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.backgroundTexture);
        data.addTexture(this.getLevel(), this.barTexture);
    }

    private int getPercent() {
        return (int)(this.progress * 100.0f);
    }

    private String getTextString() {
        if (this.showPercent) {
            return String.valueOf(PROGRESS) + ": " + this.getPercent() + "%";
        }
        int current = (int)((float)this.max * this.progress);
        return String.valueOf(current) + "/" + this.max;
    }

    private float calculateBarWidth() {
        return this.getScale().x * this.progress;
    }
}


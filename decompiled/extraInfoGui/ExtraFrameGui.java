/*
 * Decompiled with CFR 0.152.
 */
package extraInfoGui;

import basics.DisplayManager;
import extraInfoGui.ExtraInfoContent;
import extraInfoGui.ExtraInfoGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class ExtraFrameGui
extends GuiComponent {
    protected static final int HEIGHT_PIXELS = 504;
    private static final float HEIGHT = 504.0f / (float)DisplayManager.getUiHeight();
    private static final float Y_POS = 36.0f / (float)DisplayManager.getUiHeight() + 0.01f;
    private static final float BAR_HEIGHT = 15.0f / (HEIGHT * (float)DisplayManager.getUiHeight());
    private ExtraInfoContent currentContents;
    private boolean displayed = false;
    private ValueDriver xDriver = new ConstantDriver(1.0f);
    private GuiTexture bar;
    private GuiTexture background = new GuiTexture(GuiRepository.BLOCK);

    protected ExtraFrameGui() {
        this.background.setBlurry(true);
        this.background.setAlphaDriver(new ConstantDriver(0.75f));
        this.background.setOverrideColour(ColourPalette.DARK_GREY);
        this.bar = new GuiTexture(GuiRepository.BLOCK);
        this.bar.setOverrideColour(ColourPalette.GREEN);
        GuiMaster.addComponent(this, 1.0f, Y_POS, ExtraInfoGui.WIDTH, HEIGHT);
        this.show(false);
    }

    public void display(ExtraInfoContent contents) {
        if (!this.displayed) {
            this.show(true);
            this.slide(true);
        }
        this.replaceContents(contents);
    }

    protected void undisplay() {
        if (this.displayed) {
            this.currentContents.close();
            this.slide(false);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        this.bar.setPosition(position.x, position.y, scale.x, scale.y * BAR_HEIGHT);
    }

    @Override
    protected void updateSelf() {
        this.background.update();
        float xPos = this.xDriver.update(DisplayManager.getDeltaSeconds());
        super.setRelativeX(xPos);
        if (!this.displayed) {
            this.checkOffScreen();
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.bar);
    }

    private void checkOffScreen() {
        if (super.getRelativeX() >= 1.0f) {
            this.removeContents();
            this.show(false);
        }
    }

    private void slide(boolean in) {
        this.displayed = in;
        this.xDriver = new SlideDriver(super.getRelativeX(), in ? 1.0f - ExtraInfoGui.WIDTH : 1.0f, 0.2f);
    }

    private void removeContents() {
        if (this.currentContents != null) {
            this.currentContents.remove();
            this.currentContents.close();
            this.currentContents = null;
        }
    }

    private void replaceContents(ExtraInfoContent contents) {
        this.removeContents();
        this.currentContents = contents;
        super.addComponent(this.currentContents, 0.0f, BAR_HEIGHT, 1.0f, 1.0f - BAR_HEIGHT);
    }
}


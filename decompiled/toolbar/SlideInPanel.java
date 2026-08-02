/*
 * Decompiled with CFR 0.152.
 */
package toolbar;

import basics.DisplayManager;
import gameManaging.GameManager;
import gameManaging.GameState;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbar.Toolbar;
import userInterfaces.GuiPanel;
import userInterfaces.Listener;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class SlideInPanel
extends GuiPanel {
    private static final float ALPHA = 0.75f;
    private static final float SLIDE_TIME = 0.3f;
    public static final float HEIGHT = 0.9f;
    private final float barHeight;
    private final float barWidth;
    private final boolean horizontal;
    private final Toolbar toolbar;
    private ValueDriver xDriver = new ConstantDriver(0.0f);
    private boolean displayed = false;
    private GuiTexture bar;
    private GuiTexture bar2;
    private GuiComponent currentContents;
    private boolean intense;
    private List<Listener> closeListeners = new ArrayList<Listener>();

    public SlideInPanel(Toolbar toolbar, float y, float scaleX, boolean intense, boolean doubleBarred, boolean horizontal) {
        super(ColourPalette.DARK_GREY, 0.75f);
        super.setBlurry();
        this.toolbar = toolbar;
        this.horizontal = horizontal;
        this.bar = new GuiTexture(GuiRepository.BLOCK);
        this.bar.setOverrideColour(ColourPalette.GREEN);
        if (doubleBarred) {
            this.bar2 = new GuiTexture(GuiRepository.BLOCK);
            this.bar2.setOverrideColour(ColourPalette.GREEN);
        }
        this.barHeight = 15.0f / (0.9f * (float)DisplayManager.getUiHeight());
        this.barWidth = 15.0f / (scaleX * (float)DisplayManager.getUiWidth());
        GuiMaster.addComponent(this, -scaleX, y, scaleX, 0.9f);
        this.intense = intense;
        this.show(false);
    }

    public SlideInPanel(Toolbar toolbar, float y, float scaleX, float scaleY, boolean intense, boolean doubleBarred, boolean horizontal) {
        super(ColourPalette.DARK_GREY, 0.75f);
        super.setBlurry();
        this.toolbar = toolbar;
        this.horizontal = horizontal;
        this.bar = new GuiTexture(GuiRepository.BLOCK);
        this.bar.setOverrideColour(ColourPalette.GREEN);
        if (doubleBarred) {
            this.bar2 = new GuiTexture(GuiRepository.BLOCK);
            this.bar2.setOverrideColour(ColourPalette.GREEN);
        }
        this.barHeight = 15.0f / (scaleY * (float)DisplayManager.getUiHeight());
        this.barWidth = 15.0f / (scaleX * (float)DisplayManager.getUiWidth());
        GuiMaster.addComponent(this, -scaleX, y, scaleX, scaleY);
        this.intense = intense;
        this.show(false);
    }

    public void display(GuiComponent contents) {
        if (!this.displayed) {
            this.displayPanel();
        }
        this.removeCurrentContents();
        this.addNewContents(contents);
    }

    public void addCloseListener(Listener listener) {
        this.closeListeners.add(listener);
    }

    public void undisplayPanel() {
        if (this.displayed) {
            this.displayed = false;
            this.xDriver = new SlideDriver(this.getRelativeX(), -super.getRelativeScaleX(), 0.3f);
            for (Listener listener : this.closeListeners) {
                listener.eventOccurred(true);
            }
        }
    }

    public boolean isDisplayed() {
        return this.displayed;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        if (!this.horizontal) {
            this.bar.setPosition(position.x, position.y, scale.x, scale.y * this.barHeight);
        } else {
            this.bar.setPosition(position.x, position.y, scale.x * this.barWidth, scale.y);
        }
        if (this.bar2 != null) {
            this.bar2.setPosition(position.x, position.y + (1.0f - this.barHeight) * scale.y, scale.x, scale.y * this.barHeight);
        }
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        float xPos = this.xDriver.update(DisplayManager.getDeltaSeconds());
        super.setRelativeX(xPos);
        if (xPos <= -super.getRelativeScaleX()) {
            this.unshow();
        }
        if (this.displayed && this.intense) {
            this.closeIfClickedOff();
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        super.getGuiTextures(data);
        data.addTexture(this.getLevel(), this.bar);
        if (this.bar2 != null) {
            data.addTexture(this.getLevel(), this.bar2);
        }
    }

    private void displayPanel() {
        if (!this.displayed) {
            this.displayed = true;
            this.show(true);
            this.xDriver = new SlideDriver(this.getRelativeX(), 0.0f, 0.3f);
            if (this.intense) {
                GameManager.gameState.setState(GameState.INTENSE_GUI);
            }
        }
    }

    private void unshow() {
        this.removeCurrentContents();
        this.show(false);
        if (this.intense) {
            GameManager.gameState.endState(GameState.INTENSE_GUI);
        }
    }

    private void removeCurrentContents() {
        if (this.currentContents != null) {
            this.currentContents.remove();
            this.currentContents = null;
        }
    }

    private void addNewContents(GuiComponent contents) {
        this.currentContents = contents;
        if (this.horizontal) {
            super.addComponent(contents, this.barWidth, 0.0f, 1.0f - this.barWidth, 1.0f);
        } else {
            float height = this.bar2 == null ? 1.0f - this.barHeight : 1.0f - 2.0f * this.barHeight;
            super.addComponent(contents, 0.0f, this.barHeight, 1.0f, height);
        }
    }

    private void closeIfClickedOff() {
        if (GuiMaster.isInFocus(this) && GuiMaster.clickedOffGui()) {
            this.toolbar.turnOffButtonOptions();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package colourSelector;

import basics.DisplayManager;
import guiRendering.GuiRenderData;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.Maths;
import toolbox.MyMouse;
import userInterfaces.GuiImage;
import userInterfaces.GuiPanel;
import userInterfaces.Listener;

public class BrightnessSelectorGui
extends GuiPanel {
    private static final int BAR_HEIGHT_PIXELS = 4;
    private static final int POINTER_PIXELS = 8;
    private float value;
    private final float originalBrightness;
    private GuiImage selectorBar;
    private float barHeight;
    private boolean grabbed = false;
    private List<Listener> listeners = new ArrayList<Listener>();

    protected BrightnessSelectorGui(Colour startColour, float startBrightness, float originalBrightness) {
        super(GuiRepository.BRIGHT_CONTROL, startColour, 1, ColourPalette.BRIGHT_GREY);
        this.originalBrightness = originalBrightness;
        this.value = startBrightness;
        super.setPreferredAspectRatio(0.125f);
    }

    @Override
    protected void init() {
        super.init();
        this.initBar();
    }

    public void set(float brightness) {
        this.value = brightness;
        this.selectorBar.setRelativeY(1.0f - this.value - this.barHeight / 2.0f);
        this.notifyListeners();
    }

    protected void reset() {
        this.value = this.originalBrightness;
        this.selectorBar.setRelativeY(1.0f - this.value - this.barHeight / 2.0f);
    }

    protected float getBrightness() {
        return this.value;
    }

    protected void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        MyMouse mouse = MyMouse.getActiveMouse();
        this.checkGrabbed(mouse);
        if (this.grabbed) {
            this.calculateValue(mouse);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        super.getGuiTextures(data);
    }

    private void initBar() {
        this.selectorBar = new GuiImage(GuiRepository.SELECT_BAR);
        this.barHeight = 4.0f / ((float)DisplayManager.getUiHeight() * super.getScale().y);
        super.addComponent(this.selectorBar, 0.0f, 1.0f - this.value - this.barHeight / 2.0f, 1.0f, this.barHeight);
    }

    private void checkGrabbed(MyMouse mouse) {
        if (mouse.isLeftClick() && super.isMouseOver()) {
            this.grabbed = true;
        } else if (mouse.isLeftClickRelease()) {
            this.grabbed = false;
        }
    }

    private void calculateValue(MyMouse mouse) {
        this.value = (mouse.getY() - super.getPosition().y) / super.getScale().y;
        this.value = Maths.clamp(this.value, 0.0f, 1.0f);
        this.value = 1.0f - this.value;
        this.selectorBar.setRelativeY(1.0f - this.value - this.barHeight / 2.0f);
        this.notifyListeners();
    }

    private void notifyListeners() {
        for (Listener listener : this.listeners) {
            listener.eventOccurred(true);
        }
    }
}


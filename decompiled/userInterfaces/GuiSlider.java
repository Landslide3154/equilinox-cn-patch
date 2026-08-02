/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.Maths;
import toolbox.MyMouse;
import userInterfaces.ChangeListener;
import userInterfaces.GuiImage;

public class GuiSlider
extends GuiComponent {
    private static final float POINTER_WIDTH = 0.4f;
    private static final float SCROLL_WHEEL_SPEED = 0.035f;
    private final int pixelHeight;
    private float progress;
    private boolean grabbed = false;
    private GuiImage pointer;
    private boolean equality = false;
    private ProgressUi progressUi;
    private final Colour pointerColour;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    public GuiSlider(float startingValue) {
        this.progress = startingValue;
        this.pixelHeight = 3;
        this.pointerColour = ColourPalette.BRIGHT_GREY;
    }

    public GuiSlider(float startingValue, int pixelHeight, Colour pointerColour) {
        this.progress = startingValue;
        this.pixelHeight = pixelHeight;
        this.pointerColour = pointerColour;
    }

    public void addChangeListener(ChangeListener listener) {
        this.listeners.add(listener);
    }

    public void setEqualitySlider() {
        this.equality = true;
    }

    public void setProgress(float prog) {
        this.progress = Maths.clamp(prog, 0.0f, 1.0f);
        this.pointer.setRelativeX(this.progress - super.getRelativeWidthCoords(1.0f) * 0.4f * 0.5f);
        this.progressUi.progress = this.progress;
        this.notifyListeners();
    }

    public float getProgress() {
        return this.progress;
    }

    @Override
    protected void init() {
        super.init();
        this.pointer = new GuiImage(GuiRepository.BLOCK);
        this.pointer.getTexture().setOverrideColour(this.pointerColour);
        this.progressUi = new ProgressUi(this.progress);
        this.progressUi.equalitySlider = this.equality;
        float height = super.pixelsToRelativeY(this.pixelHeight);
        super.addComponent(this.progressUi, 0.0f, (1.0f - height) / 2.0f, 1.0f, height);
        this.pointer.setPreferredAspectRatio(0.4f);
        super.addCenteredComponentX(this.pointer, this.progress, 0.0f, 1.0f);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        MyMouse mouse = MyMouse.getActiveMouse();
        boolean mouseOver = super.isMouseOver();
        if (mouseOver && mouse.isLeftClick()) {
            this.grabbed = true;
        } else if (mouse.isLeftClickRelease()) {
            this.grabbed = false;
        }
        if (this.grabbed) {
            float mouseX = MyMouse.getActiveMouse().getX();
            this.setProgress((mouseX - super.getPosition().x) / super.getScale().x);
        } else if (mouseOver) {
            float change = -mouse.getDWheelSigned();
            this.setProgress(this.progress + change * 0.035f);
        }
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        if (!GuiMaster.isMouseInteractionEnabled()) {
            return false;
        }
        MyMouse mouse = MyMouse.getActiveMouse();
        Vector2f position = super.getPosition();
        Vector2f scale = super.getScale();
        float extraX = super.getRelativeWidthCoords(1.2f) * 0.4f * 0.5f * scale.x;
        return mouse.getX() >= position.x - extraX && mouse.getX() <= position.x + scale.x + extraX && mouse.getY() >= position.y && mouse.getY() <= position.y + scale.y;
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void notifyListeners() {
        for (ChangeListener listener : this.listeners) {
            listener.eventOccurred(this.progress);
        }
    }

    protected static class ProgressUi
    extends GuiComponent {
        private GuiTexture background = new GuiTexture(GuiRepository.BLOCK);
        private GuiTexture foreground;
        private float progress;
        private boolean equalitySlider;

        public ProgressUi(float startingValue) {
            this.background.setOverrideColour(ColourPalette.MIDDLE_GREY);
            this.foreground = new GuiTexture(GuiRepository.BLOCK);
            this.foreground.setOverrideColour(ColourPalette.GREEN);
            this.progress = startingValue;
        }

        @Override
        protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
            this.background.setPosition(position.x, position.y, scale.x, scale.y);
            this.foreground.setPosition(position.x, position.y, scale.x * (this.equalitySlider ? 1.0f : this.progress), scale.y);
        }

        @Override
        protected void updateSelf() {
            this.foreground.setWidth(super.getScale().x * (this.equalitySlider ? 1.0f : this.progress));
        }

        @Override
        protected void getGuiTextures(GuiRenderData data) {
            data.addTexture(this.getLevel(), this.background);
            data.addTexture(this.getLevel(), this.foreground);
        }
    }
}


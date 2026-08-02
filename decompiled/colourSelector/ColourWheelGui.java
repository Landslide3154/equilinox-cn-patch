/*
 * Decompiled with CFR 0.152.
 */
package colourSelector;

import basics.DisplayManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.HsvColour;
import toolbox.Maths;
import toolbox.MyMouse;
import userInterfaces.GuiImage;
import userInterfaces.Listener;

public class ColourWheelGui
extends GuiComponent {
    private static final int SELECTOR_PIXEL_SIZE = 16;
    private static final float MAX_SAT = 0.55f;
    private GuiTexture colourWheelTexture;
    private GuiImage selectionPoint;
    private final HsvColour originalColour;
    private HsvColour hsvColour = new HsvColour(0.0f, 0.0f, 1.0f);
    private Colour currentColour = new Colour();
    private boolean grabbed = false;
    private List<Listener> listeners = new ArrayList<Listener>();

    protected ColourWheelGui(HsvColour originalColour, HsvColour startingColour) {
        this.colourWheelTexture = new GuiTexture(GuiRepository.COLOUR_WHEEL);
        this.originalColour = originalColour;
        this.hsvColour.setHue(startingColour.getHue());
        this.hsvColour.setSaturation(startingColour.getSaturation());
        this.currentColour.setColour(this.hsvColour);
    }

    protected void reset() {
        this.hsvColour.setHue(this.originalColour.getHue());
        this.hsvColour.setSaturation(this.originalColour.getSaturation());
        this.currentColour.setColour(this.hsvColour);
        Vector2f pos = this.convertToPosition(this.originalColour.getHue(), this.originalColour.getSaturation());
        this.selectionPoint.setRelativePosition(pos.x - this.selectionPoint.getRelativeScaleX() / 2.0f, pos.y - this.selectionPoint.getRelativeScaleY() / 2.0f);
    }

    protected void set(HsvColour newColour) {
        this.hsvColour.setHue(newColour.getHue());
        this.hsvColour.setSaturation(newColour.getSaturation());
        this.currentColour.setColour(this.hsvColour);
        Vector2f pos = this.convertToPosition(newColour.getHue(), newColour.getSaturation() / 0.55f);
        this.selectionPoint.setRelativePosition(pos.x - this.selectionPoint.getRelativeScaleX() / 2.0f, pos.y - this.selectionPoint.getRelativeScaleY() / 2.0f);
        this.notifyListeners();
    }

    @Override
    protected void init() {
        this.initSelectionPoint();
    }

    protected void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    protected HsvColour getHsvColour() {
        return this.hsvColour;
    }

    protected Colour getRgbColour() {
        return this.currentColour;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.colourWheelTexture.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        MyMouse mouse = MyMouse.getActiveMouse();
        Vector2f colourVector = this.getVectorFromCenter(mouse);
        float radius = colourVector.length();
        this.checkGrabbed(radius);
        if (this.grabbed) {
            this.updateSelectedColour(colourVector, radius);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(super.getLevel(), this.colourWheelTexture);
    }

    private Vector2f convertToPosition(float hue, float saturation) {
        saturation = Math.min(saturation, 1.0f);
        float posX = (float)((double)(-saturation) * Math.sin((double)(-hue * 2.0f) * Math.PI));
        float posY = (float)((double)saturation * Math.cos((double)(-hue * 2.0f) * Math.PI));
        return new Vector2f(posX / 2.0f + 0.5f, 1.0f - (posY / 2.0f + 0.5f));
    }

    private void setSelectionPointPosition(Vector2f colourVector) {
        float x = colourVector.x / 2.0f + 0.5f;
        float y = 1.0f - (colourVector.y / 2.0f + 0.5f);
        this.selectionPoint.setRelativePosition(x - this.selectionPoint.getRelativeScaleX() / 2.0f, y - this.selectionPoint.getRelativeScaleY() / 2.0f);
    }

    private Vector2f getVectorFromCenter(MyMouse mouse) {
        float mousePixelX = mouse.getX() * (float)DisplayManager.getUiWidth();
        float mousePixelY = mouse.getY() * (float)DisplayManager.getUiHeight();
        float centerX = (this.getPosition().x + this.getScale().x / 2.0f) * (float)DisplayManager.getUiWidth();
        float centerY = (this.getPosition().y + this.getScale().y / 2.0f) * (float)DisplayManager.getUiHeight();
        float radius = this.getScale().y * (float)DisplayManager.getUiHeight() / 2.0f;
        return new Vector2f((mousePixelX - centerX) / radius, -(mousePixelY - centerY) / radius);
    }

    private void initSelectionPoint() {
        this.selectionPoint = new GuiImage(GuiRepository.CROSS_HAIR);
        Vector2f pos = this.convertToPosition(this.hsvColour.getHue(), this.hsvColour.getSaturation() / 0.55f);
        float sizeX = 16.0f / (super.getScale().x * (float)DisplayManager.getUiWidth());
        super.addCenteredComponent(this.selectionPoint, pos.x, pos.y, sizeX);
    }

    private void initOriginalPoint() {
        GuiImage originalPoint = new GuiImage(GuiRepository.CROSS);
        Vector2f pos = this.convertToPosition(this.originalColour.getHue(), this.originalColour.getSaturation() / 0.55f);
        super.addCenteredComponent(originalPoint, pos.x, pos.y, 0.05f);
    }

    private void notifyListeners() {
        for (Listener listener : this.listeners) {
            listener.eventOccurred(true);
        }
    }

    private void updateSelectedColour(Vector2f colourVector, float radius) {
        if (radius > 1.0f) {
            colourVector.normalise();
            radius = 1.0f;
        }
        this.setSelectionPointPosition(colourVector);
        float angle = 0.0f;
        try {
            angle = Maths.calculateVectorAngle(colourVector);
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.hsvColour.setHue(angle / 360.0f);
        this.hsvColour.setSaturation(radius * 0.55f);
        this.currentColour.setColour(this.hsvColour);
        this.notifyListeners();
    }

    private void checkGrabbed(float radius) {
        MyMouse mouse = MyMouse.getActiveMouse();
        if (radius < 1.0f && mouse.isLeftClick()) {
            this.grabbed = true;
        } else if (mouse.isLeftClickRelease()) {
            this.grabbed = false;
        }
    }
}


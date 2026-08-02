/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import toolbox.MyMouse;
import userInterfaces.Listener;

public class GuiCheckBox
extends GuiComponent {
    private GuiTexture checkInner;
    private GuiTexture checkOuter;
    private Colour backColour;
    private List<Listener> listeners = new ArrayList<Listener>();
    private boolean mousedOver = false;
    private boolean on = false;

    public GuiCheckBox(boolean on) {
        this.on = on;
        this.backColour = ColourPalette.LIGHT_GREY;
        this.checkInner = new GuiTexture(GuiRepository.TICK_FILL);
        this.checkInner.setOverrideColour(on ? ColourPalette.GREEN : this.backColour);
        this.checkOuter = new GuiTexture(GuiRepository.TICK_EMPTY);
        this.checkOuter.setOverrideColour(this.backColour);
        super.setPreferredPixelSize(18);
    }

    public GuiCheckBox(boolean on, Texture fillTexture, Texture emptyTexture, Colour col) {
        this.on = on;
        this.backColour = col;
        this.checkInner = new GuiTexture(fillTexture);
        this.checkInner.setOverrideColour(on ? ColourPalette.GREEN : this.backColour);
        this.checkOuter = new GuiTexture(emptyTexture);
        this.checkOuter.setOverrideColour(this.backColour);
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.checkInner.setPosition(position.x, position.y, scale.x, scale.y);
        this.checkOuter.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        this.mousedOver = super.isMouseOver();
        if (this.mousedOver) {
            this.checkClick();
        }
        this.setColours();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (this.on || this.mousedOver) {
            data.addTexture(this.getLevel(), this.checkInner);
        } else {
            data.addTexture(this.getLevel(), this.checkOuter);
        }
    }

    private void setColours() {
        if (this.on) {
            this.checkInner.setOverrideColour(ColourPalette.GREEN);
        } else if (this.mousedOver) {
            this.checkInner.setOverrideColour(this.backColour);
        }
    }

    private void checkClick() {
        MyMouse mouse = MyMouse.getActiveMouse();
        if (mouse.isLeftClick()) {
            this.set(!this.on);
        }
    }

    public void set(boolean checked) {
        this.on = checked;
        this.notifyListeners(this.on);
    }

    public void setSilently(boolean checked) {
        this.on = checked;
    }

    private void notifyListeners(boolean on) {
        for (Listener listener : this.listeners) {
            listener.eventOccurred(on);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.MyMouse;

public class GuiBar
extends GuiComponent {
    private final Colour BAR_COLOUR_1 = ColourPalette.BASE_BLUE;
    private final Colour BAR_COLOUR_2 = ColourPalette.BASE_BLUE.duplicate().scale(1.2f);
    private final Colour BAR_COLOUR_3 = this.BAR_COLOUR_2.duplicate().scale(1.2f);
    private final GuiTexture bar = new GuiTexture(GuiRepository.BLOCK);
    private boolean grabbed = false;
    private float grabPosition;

    protected GuiBar() {
        this.bar.setOverrideColour(this.BAR_COLOUR_1);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.bar.setPosition(position.x, position.y, scale.x, scale.y);
    }

    protected boolean isGrabbed() {
        return this.grabbed;
    }

    protected float getGrabPosition() {
        return this.grabPosition;
    }

    @Override
    protected void updateSelf() {
        this.determineGrabbed();
        this.determineColour();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.bar);
    }

    private void determineGrabbed() {
        MyMouse mouse = MyMouse.getActiveMouse();
        if (mouse.isLeftClick() && super.isMouseOver()) {
            this.grabbed = true;
            this.grabPosition = super.getRelativeMouseY();
        }
        if (mouse.isLeftClickRelease()) {
            this.grabbed = false;
        }
    }

    private void determineColour() {
        if (this.grabbed) {
            this.bar.setOverrideColour(this.BAR_COLOUR_3);
        } else if (super.isMouseOver() && !MyMouse.getActiveMouse().isLeftButtonDown()) {
            this.bar.setOverrideColour(this.BAR_COLOUR_2);
        } else {
            this.bar.setOverrideColour(ColourPalette.BASE_BLUE);
        }
    }
}


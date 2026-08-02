/*
 * Decompiled with CFR 0.152.
 */
package usefulUis;

import guis.GuiComponent;
import toolbox.Colour;
import userInterfaces.GuiPanel;

public class PaddedPanelUi
extends GuiPanel {
    private int padLeft;
    private int padRight;
    private int padTop;
    private int padBottom = 0;
    private float left;
    private float right;
    private float top;
    private float bottom;
    private GuiComponent component = new GuiPanel();

    public PaddedPanelUi(Colour colour, float alpha) {
        super(colour, alpha);
    }

    public PaddedPanelUi(Colour colour) {
        super(colour, 1);
    }

    public PaddedPanelUi(Colour colour, int borderSize, Colour borderColour) {
        super(colour, borderSize, borderColour);
    }

    public GuiComponent getContent() {
        return this.component;
    }

    public void setPadding(int left, int right, int top, int bottom) {
        this.padLeft = left;
        this.padRight = right;
        this.padTop = top;
        this.padBottom = bottom;
    }

    public void setPadding(int value) {
        this.padLeft = value;
        this.padRight = value;
        this.padTop = value;
        this.padBottom = value;
    }

    public void displayComponent(GuiComponent component) {
        this.removeCurrentComponent();
        this.component = component;
        if (super.isInitialized()) {
            super.addComponent(component, this.left, this.top, 1.0f - (this.right + this.left), 1.0f - (this.top + this.bottom));
        }
    }

    @Override
    protected void init() {
        super.init();
        this.left = super.pixelsToRelativeX(this.padLeft);
        this.right = super.pixelsToRelativeX(this.padRight);
        this.top = super.pixelsToRelativeY(this.padTop);
        this.bottom = super.pixelsToRelativeY(this.padBottom);
        if (this.component != null) {
            super.addComponent(this.component, this.left, this.top, 1.0f - (this.right + this.left), 1.0f - (this.top + this.bottom));
        }
    }

    private void removeCurrentComponent() {
        if (this.component != null && super.isInitialized()) {
            this.component.remove();
            this.component = null;
        }
    }
}


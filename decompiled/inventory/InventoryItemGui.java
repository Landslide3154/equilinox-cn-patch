/*
 * Decompiled with CFR 0.152.
 */
package inventory;

import basics.DisplayManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import inventory.CountGui;
import inventory.ItemsPanelGui;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import userInterfaces.GuiButton;
import userInterfaces.Listener;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class InventoryItemGui
extends GuiComponent {
    private static final float COUNT_POS = 0.6f;
    private static final float COUNT_SIZE = 0.39999998f;
    private static final float SHIFT_TIME = 0.3f;
    private final ItemsPanelGui panel;
    private CountGui countGui;
    private GuiButton iconButton;
    private int listIndex;
    private float offsetFromTop;
    private ValueDriver yDriver;
    private float relativeY;
    private boolean initialized = false;

    protected InventoryItemGui(int index, Texture icon, boolean flip, int count, ItemsPanelGui panel) {
        this.panel = panel;
        this.listIndex = index;
        this.addIcon(icon, flip);
        this.addCount(count);
    }

    protected int getListIndex() {
        return this.listIndex;
    }

    protected void setCount(int count) {
        this.countGui.setCount(count);
    }

    protected void shiftUp() {
        --this.listIndex;
        this.yDriver = new SlideDriver(this.offsetFromTop, this.panel.calculateOffsetFromTop(this.listIndex), 0.3f);
    }

    protected void addListener(Listener listener) {
        this.iconButton.addListener(listener);
    }

    protected void calculateRelativeYPos(float topOffset) {
        this.offsetFromTop = this.yDriver.update(DisplayManager.getDeltaSeconds());
        this.relativeY = this.offsetFromTop - topOffset;
        if (this.relativeY < 0.0f || this.relativeY > 1.0f - this.panel.getItemHeight()) {
            super.show(false);
        } else {
            this.show(true);
        }
        if (super.isShown()) {
            super.setRelativeY(this.offsetFromTop - topOffset);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        if (!this.initialized) {
            this.initialized = true;
            this.yDriver = new ConstantDriver(this.panel.calculateOffsetFromTop(this.listIndex));
            this.relativeY = super.getRelativeY();
        }
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addIcon(Texture icon, boolean flip) {
        this.iconButton = new GuiButton(icon);
        this.iconButton.getGuiTexture().flip(flip);
        super.addComponent(this.iconButton, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    private void addCount(int count) {
        this.countGui = new CountGui(count, 576, 0.0f);
        super.addComponentX(this.countGui, 0.6f, 0.6f, 0.39999998f);
    }
}


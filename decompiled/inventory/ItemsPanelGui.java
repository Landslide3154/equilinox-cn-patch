/*
 * Decompiled with CFR 0.152.
 */
package inventory;

import basics.DisplayManager;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import inventory.InventoryItem;
import inventory.InventoryItemGui;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import toolbox.MyMouse;

public class ItemsPanelGui
extends GuiComponent {
    private static final float SMOOTHING_FACTOR = 0.05f;
    private static final int ITEM_HEIGHT_PIXELS = 100;
    private static final int VISIBLE_COUNT = DisplayManager.getUiHeight() / 100;
    private static final float GAP_FACTOR = 0.2f;
    private boolean initialized = false;
    private float itemHeight;
    private float gapHeight;
    private int topIndex = 0;
    private float currentTopY = 0.0f;
    private float targetTopY = 0.0f;
    private List<InventoryItemGui> itemGuis = new ArrayList<InventoryItemGui>();

    protected ItemsPanelGui(List<InventoryItem> items) {
        int i = 0;
        while (i < items.size()) {
            InventoryItem item = items.get(i);
            InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)item.blueprint.getComponent(ComponentType.INFO);
            this.itemGuis.add(new InventoryItemGui(i, info.getIcon(), info.isFlipTexture(), item.getCount(), this));
            ++i;
        }
    }

    protected void updateCountForItem(int index, int newCount) {
        this.itemGuis.get(index).setCount(newCount);
    }

    protected void removeItem(int index) {
        InventoryItemGui itemGui = this.itemGuis.remove(index);
        itemGui.remove();
        int i = index;
        while (i < this.itemGuis.size()) {
            this.itemGuis.get(i).shiftUp();
            ++i;
        }
    }

    protected List<InventoryItemGui> getItemGuis() {
        return this.itemGuis;
    }

    protected boolean canScrollUp() {
        return this.topIndex > 0;
    }

    protected boolean canScrollDown() {
        return this.topIndex + VISIBLE_COUNT < this.itemGuis.size();
    }

    protected float calculateOffsetFromTop(int index) {
        return this.gapHeight + (float)index * (this.gapHeight + this.itemHeight);
    }

    protected float getItemHeight() {
        return this.itemHeight;
    }

    protected void scroll(boolean up) {
        int change = up ? -1 : 1;
        this.setTopIndex(this.topIndex + change);
    }

    protected void setTopIndex(int index) {
        this.topIndex = Math.max(0, Math.min(index, this.getMaxTopIndex()));
        this.targetTopY = (float)this.topIndex * (this.gapHeight + this.itemHeight);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        if (!this.initialized) {
            this.initialized = true;
            float itemWidth = this.calculateWidth();
            this.itemHeight = super.getRelativeHeightCoords(itemWidth);
            float gapWidth = this.calculateGap(1, itemWidth);
            this.gapHeight = this.calculateGap(VISIBLE_COUNT, this.itemHeight);
            int i = 0;
            while (i < this.itemGuis.size()) {
                float yPos = this.calculateOffsetFromTop(i);
                super.addComponent(this.itemGuis.get(i), gapWidth, yPos, itemWidth, this.itemHeight);
                ++i;
            }
        }
    }

    @Override
    protected void updateSelf() {
        float difference = this.targetTopY - this.currentTopY;
        this.currentTopY += difference * DisplayManager.getDeltaSeconds() / 0.05f;
        for (InventoryItemGui itemGui : this.itemGuis) {
            itemGui.calculateRelativeYPos(this.currentTopY);
        }
        this.checkScrolling();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private float calculateWidth() {
        float potentialWidth1 = this.calculateFittingValue(1.0f);
        float potentialWidth2 = this.calculateWidthFromFittingHeight();
        return Math.min(potentialWidth1, potentialWidth2);
    }

    private float calculateWidthFromFittingHeight() {
        float perfectHeight = this.calculateFittingValue(VISIBLE_COUNT);
        float width = super.getRelativeWidthCoords(perfectHeight);
        return width;
    }

    private float calculateFittingValue(float count) {
        return 1.0f / (count + count * 0.2f + 0.2f);
    }

    private float calculateGap(int count, float size) {
        float totalGap = 1.0f - (float)count * size;
        return totalGap / (float)(count + 1);
    }

    private void checkScrolling() {
        if (super.isMouseOver()) {
            int dWheel = MyMouse.getActiveMouse().getDWheel();
            if (dWheel > 0) {
                this.scroll(true);
            } else if (dWheel < 0) {
                this.scroll(false);
            }
        }
    }

    private int getMaxTopIndex() {
        return Math.max(this.itemGuis.size() - VISIBLE_COUNT, 0);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import gridLayout.GridComponent;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import org.lwjgl.util.vector.Vector2f;

public class ItemPageGui
extends GuiComponent {
    public static final int SIDES_PAD = 15;
    public static final int TOP_PAD = 12;
    protected static final float GAP_FACTOR = 0.15f;
    private int itemsPerRow;
    private int itemsPerColumn;
    private int pageId;
    private GridComponent[] items;
    private float aspectRatio;
    private Object currentMouseoverOwner;
    private GuiComponent mouseover;

    protected ItemPageGui(int pageId, GridComponent[] items, int itemsPerRow, int itemsPerColumn, float aspectRatio) {
        this.aspectRatio = aspectRatio;
        this.pageId = pageId;
        this.itemsPerRow = itemsPerRow;
        this.itemsPerColumn = itemsPerColumn;
        this.items = items;
    }

    @Override
    protected void delete() {
        super.delete();
        this.removeMouseover();
    }

    public void registerMouseover(Object owner, GuiComponent mouseover) {
        this.removeMouseover();
        this.mouseover = mouseover;
        this.currentMouseoverOwner = owner;
    }

    public void unregisterMouseover(Object owner) {
        if (this.currentMouseoverOwner == owner) {
            this.removeMouseover();
        }
    }

    protected int getPageId() {
        return this.pageId;
    }

    @Override
    protected void init() {
        float itemWidth = this.calculateWidth();
        float itemHeight = super.getRelativeHeightCoords(itemWidth) / this.aspectRatio;
        float sidePad = super.pixelsToRelativeX(15.0f);
        float gapWidth = this.calculateGap(this.itemsPerRow, itemWidth, sidePad);
        float gapHeight = this.calculateYGap(this.itemsPerColumn, itemHeight);
        int i = 0;
        while (i < this.items.length) {
            int row = i / this.itemsPerRow;
            int col = i % this.itemsPerRow;
            GuiComponent itemGui = this.items[i].getComponentGui(this);
            float x = sidePad + (gapWidth + itemWidth) * (float)col;
            float y = gapHeight + (gapHeight + itemHeight) * (float)row;
            super.addComponent(itemGui, x, y, itemWidth, itemHeight);
            ++i;
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private float calculateWidth() {
        float potentialWidth1 = this.calculateFittingValue(this.itemsPerRow, super.pixelsToRelativeX(15.0f));
        float potentialWidth2 = this.calculateWidthFromFittingHeight();
        return Math.min(potentialWidth1, potentialWidth2);
    }

    private float calculateWidthFromFittingHeight() {
        float perfectHeight = this.calculateFittingValue(this.itemsPerColumn, super.pixelsToRelativeY(12.0f));
        float width = super.getRelativeWidthCoords(perfectHeight) * this.aspectRatio;
        return width;
    }

    private float calculateFittingValue(float count, float edgePadding) {
        float totalSize = 1.0f - edgePadding;
        return totalSize / (count + (count - 1.0f) * 0.15f);
    }

    private float calculateGap(int count, float itemSize, float pad) {
        float totalGap = 1.0f - (2.0f * pad + (float)count * itemSize);
        return totalGap / (float)(count - 1);
    }

    private float calculateYGap(int count, float size) {
        float totalGap = 1.0f - (float)count * size;
        return totalGap / (float)(count + 1);
    }

    private void removeMouseover() {
        if (this.mouseover != null) {
            this.mouseover.remove();
            this.currentMouseoverOwner = null;
            this.mouseover = null;
        }
    }
}


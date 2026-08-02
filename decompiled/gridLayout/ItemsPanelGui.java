/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import gridLayout.GridComponent;
import gridLayout.ItemPageGui;
import gridLayout.PageTracker;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;

public class ItemsPanelGui
extends GuiComponent {
    private GridComponent[][] pages;
    private ItemPageGui currentPage;
    private int itemsPerRow;
    private int itemsPerColumn;
    private int itemsPerPage;
    private List<? extends GridComponent> items;
    private float itemAspectRatio;
    private int itemMinWidth;
    private int itemMinHeight;
    private PageTracker tracker;

    protected ItemsPanelGui(List<? extends GridComponent> items, float itemAspectRatio, int itemPixelWidth, PageTracker tracker) {
        this.items = items;
        this.tracker = tracker;
        this.itemAspectRatio = itemAspectRatio;
        this.itemMinWidth = itemPixelWidth;
        this.itemMinHeight = (int)((float)this.itemMinWidth / itemAspectRatio);
    }

    protected int getPageCount() {
        return this.pages.length;
    }

    protected void changePage(int page) {
        if (page == this.currentPage.getPageId()) {
            return;
        }
        this.currentPage.remove();
        this.showPage(page);
    }

    @Override
    protected void init() {
        this.determinePageCount(super.getScale());
        this.addItemsToPages();
        this.showPage(this.tracker.page);
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

    private void showPage(int i) {
        this.currentPage = new ItemPageGui(i, this.pages[i], this.itemsPerRow, this.itemsPerColumn, this.itemAspectRatio);
        super.addComponent(this.currentPage, 0.0f, 0.0f, 1.0f, 1.0f);
        this.tracker.page = i;
    }

    private void determinePageCount(Vector2f scale) {
        this.itemsPerRow = this.determineGridCount(super.getPixelWidth(), this.itemMinWidth, 15);
        this.itemsPerColumn = this.determineGridCount(super.getPixelHeight(), this.itemMinHeight, 12);
        this.itemsPerPage = this.itemsPerRow * this.itemsPerColumn;
        int pageCount = (this.items.size() - 1) / this.itemsPerPage + 1;
        this.pages = new GridComponent[pageCount][];
    }

    private void addItemsToPages() {
        int pointer = 0;
        if (this.items.isEmpty()) {
            this.pages[0] = new GridComponent[0];
            return;
        }
        int i = 0;
        while (i < this.pages.length) {
            int count;
            GridComponent[] pageItems = i < this.pages.length - 1 ? new GridComponent[this.itemsPerPage] : new GridComponent[(count = this.items.size() % this.itemsPerPage) == 0 ? this.itemsPerPage : count];
            int j = 0;
            while (j < pageItems.length) {
                pageItems[j] = this.items.get(pointer++);
                ++j;
            }
            this.pages[i] = pageItems;
            ++i;
        }
    }

    private int determineGridCount(float totalPixelSize, int minPixels, int pad) {
        totalPixelSize -= (float)(2 * pad);
        int itemWidth = (int)((float)minPixels * 1.15f);
        return 1 + (int)((totalPixelSize -= (float)minPixels) / (float)itemWidth);
    }
}


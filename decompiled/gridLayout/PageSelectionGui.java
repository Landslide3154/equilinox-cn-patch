/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import gridLayout.ArrowButtonUi;
import gridLayout.ItemsPanelGui;
import gridLayout.PageButtonUi;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import userInterfaces.GuiClickableGroup;

public class PageSelectionGui
extends GuiComponent {
    private static final float SIZE_X = 0.035f;
    private static final float GAP = 0.007f;
    private static final float CENTER_Y = 0.5f;
    private GuiClickable[] buttons;
    private ItemsPanelGui itemsPanel;
    private GuiClickable rightArrow;
    private GuiClickable leftArrow;
    private int currentPage;

    protected PageSelectionGui(ItemsPanelGui itemsPanel, int startPage) {
        this.itemsPanel = itemsPanel;
        this.currentPage = startPage;
    }

    @Override
    protected void init() {
        super.init();
        this.buttons = new GuiClickable[this.itemsPanel.getPageCount()];
        this.createButtons(this.itemsPanel.getPageCount());
        this.addListeners();
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

    protected void notifyNext(boolean right) {
        if (right && this.currentPage + 1 < this.buttons.length) {
            this.buttons[this.currentPage + 1].toggle();
        } else if (!right && this.currentPage > 0) {
            this.buttons[this.currentPage - 1].toggle();
        }
    }

    private void addListeners() {
        int i = 0;
        while (i < this.itemsPanel.getPageCount()) {
            final int pageNumber = i++;
            this.buttons[pageNumber].addListener(new ClickListener(){

                @Override
                public void eventOccurred(GuiClickEvent event) {
                    if (event.isToggleOn()) {
                        PageSelectionGui.this.currentPage = pageNumber;
                        PageSelectionGui.this.itemsPanel.changePage(pageNumber);
                        PageSelectionGui.this.updateArrowBlocks();
                    }
                }
            });
        }
    }

    private void updateArrowBlocks() {
        if (this.buttons.length == 1) {
            return;
        }
        this.rightArrow.block(this.currentPage == this.buttons.length - 1);
        this.leftArrow.block(this.currentPage == 0);
    }

    private void createButtons(int pageCount) {
        GuiClickableGroup group = new GuiClickableGroup(true);
        float xPos = 0.5f - this.calculateStartX(pageCount);
        this.leftArrow = this.addArrow(false, xPos - 0.042f);
        int i = 0;
        while (i < this.buttons.length) {
            PageButtonUi button = new PageButtonUi(i + 1);
            this.buttons[i] = button;
            group.addButton(button, i == this.currentPage);
            super.addCenteredComponentY(button, 0.5f, xPos, 0.035f);
            xPos += 0.042f;
            ++i;
        }
        this.rightArrow = this.addArrow(true, xPos);
        this.updateArrowBlocks();
    }

    private GuiClickable addArrow(boolean rightArrow, float xPos) {
        if (this.buttons.length == 1) {
            return null;
        }
        ArrowButtonUi button = new ArrowButtonUi(rightArrow ? GuiRepository.RIGHT_ARROW : GuiRepository.LEFT_ARROW, this, rightArrow);
        super.addCenteredComponentY(button, 0.5f, xPos, 0.035f);
        return button;
    }

    private float calculateStartX(int count) {
        if (count % 2 == 0) {
            return (float)(count - 1) * 0.0035f + (float)(count / 2) * 0.035f;
        }
        return (float)count * 0.0175f + (float)((count - 1) / 2) * 0.007f;
    }
}


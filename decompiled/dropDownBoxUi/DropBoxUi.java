/*
 * Decompiled with CFR 0.152.
 */
package dropDownBoxUi;

import basics.DisplayManager;
import dropDownBoxUi.ComboBoxObject;
import dropDownBoxUi.ComboBoxUi;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.MyMouse;
import userInterfaces.GuiImage;
import userInterfaces.GuiPanel;

public class DropBoxUi
extends GuiPanel {
    private static final float ARROW_X = 0.88f;
    private static final float ARROW_SCALE = 0.7f;
    private final ComboBoxUi comboBox;
    private final ComboBoxObject[] items;
    private GuiTexture selectBox;
    private int selected = -1;
    private int parentSelection = -1;
    private GuiComponent subMenu = null;

    protected DropBoxUi(ComboBoxObject[] items, ComboBoxUi comboBox) {
        super(GuiRepository.BLOCK, ComboBoxUi.BACK_COLOUR, 1, ComboBoxUi.TINT_COL_2);
        this.items = items;
        this.comboBox = comboBox;
        this.selectBox = new GuiTexture(GuiRepository.BLOCK);
        this.selectBox.setOverrideColour(ComboBoxUi.TINT_COL);
        super.setRenderLevel(1);
    }

    protected DropBoxUi(ComboBoxObject[] items, ComboBoxUi comboBox, int parentSelection) {
        super(GuiRepository.BLOCK, ComboBoxUi.BACK_COLOUR, 1, ComboBoxUi.TINT_COL_2);
        this.parentSelection = parentSelection;
        this.items = items;
        this.comboBox = comboBox;
        this.selectBox = new GuiTexture(GuiRepository.BLOCK);
        this.selectBox.setOverrideColour(ComboBoxUi.TINT_COL);
        super.setRenderLevel(1);
    }

    @Override
    protected void init() {
        super.init();
        float yGap = 1.0f / (float)this.items.length;
        float yPos = 0.0f;
        float startX = 5.0f / ((float)DisplayManager.getUiWidth() * super.getScale().x);
        float startY = 2.0f / ((float)DisplayManager.getUiHeight() * super.getScale().y);
        ComboBoxObject[] comboBoxObjectArray = this.items;
        int n = this.items.length;
        int n2 = 0;
        while (n2 < n) {
            ComboBoxObject item = comboBoxObjectArray[n2];
            this.addItem(item, startX, startY, yPos, yGap);
            yPos += yGap;
            ++n2;
        }
    }

    private void addItem(ComboBoxObject item, float startX, float startY, float yPos, float yGap) {
        if (item.hasUiComponent()) {
            super.addComponent(item.createUiComponent(), startX, yPos, 1.0f - 2.0f * startX, yGap);
        } else {
            Text text = Text.newText(item.toString()).setFontSize(this.comboBox.getFontSize()).create();
            text.setColour(ComboBoxUi.TEXT_COLOUR);
            super.addText(text, startX, yPos + startY, 1.0f);
        }
        if (item.hasSubObjects()) {
            this.addArrow(yPos, yGap);
        }
    }

    private void addArrow(float yPos, float yGap) {
        GuiImage image = new GuiImage(GuiRepository.DROP_MENU_ARROW);
        super.addCenteredComponentYScaleY(image, yPos + yGap * 0.5f, 0.88f, yGap * 0.7f);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        float itemHeight = scale.y / (float)this.items.length;
        this.selectBox.setPosition(position.x, position.y + (float)this.selected * itemHeight, scale.x, scale.y / (float)this.items.length);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        if (super.isMouseOver()) {
            float mouseY = super.getRelativeMouseY();
            int newSelected = (int)(mouseY / (1.0f / (float)this.items.length));
            if (newSelected < this.items.length && newSelected != this.selected) {
                this.updateSelection(newSelected);
            }
            if (MyMouse.getActiveMouse().isLeftClick()) {
                if (this.parentSelection >= 0) {
                    this.comboBox.select(this.parentSelection, this.selected);
                } else {
                    this.comboBox.select(this.selected, null);
                }
            }
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        super.getGuiTextures(data);
        if (this.selected >= 0) {
            data.addTexture(this.getLevel(), this.selectBox);
        }
    }

    private void updateSelection(int newSelection) {
        this.selected = newSelection;
        super.updateScreenSpacePosition();
        this.removeSubMenu();
        this.selectBox.setOverrideColour(this.items[this.selected].isSelectable() ? ComboBoxUi.TINT_COL : ColourPalette.LIGHT_GREY);
        if (this.items[this.selected].hasSubObjects()) {
            this.subMenu = new DropBoxUi(this.getSubCategories(this.items[this.selected].getSubObjects()), this.comboBox, this.selected);
            float gap = 2.0f / ((float)DisplayManager.getUiHeight() * this.comboBox.getScale().y);
            float gapX = 2.0f / ((float)DisplayManager.getUiWidth() * this.comboBox.getScale().x);
            this.comboBox.addComponent(this.subMenu, 1.0f + gapX, (float)(1 + this.selected) + gap, 1.0f, this.items[this.selected].getSubObjects().length);
        }
    }

    @Override
    public void remove() {
        super.remove();
        this.removeSubMenu();
    }

    private void removeSubMenu() {
        if (this.subMenu != null) {
            this.subMenu.remove();
            this.subMenu = null;
        }
    }

    private ComboBoxObject[] getSubCategories(Object[] subCats) {
        ComboBoxObject[] array = new ComboBoxObject[subCats.length];
        int i = 0;
        while (i < array.length) {
            array[i] = new ComboBoxObject(subCats[i]);
            ++i;
        }
        return array;
    }
}


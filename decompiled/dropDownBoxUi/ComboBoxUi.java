/*
 * Decompiled with CFR 0.152.
 */
package dropDownBoxUi;

import basics.DisplayManager;
import dropDownBoxUi.ComboBoxObject;
import dropDownBoxUi.DropBoxUi;
import dropDownBoxUi.NameBoxUi;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.Listener;

public class ComboBoxUi
extends GuiComponent {
    public static final int PIXEL_GAP = 2;
    protected static final int PIXEL_GAP_LEFT = 5;
    protected static final int PIXEL_GAP_TOP = 2;
    protected static final Colour BACK_COLOUR = ColourPalette.MIDDLE_GREY;
    protected static final Colour TEXT_COLOUR = ColourPalette.WHITE;
    protected static final Colour TINT_COL_2 = new Colour(104.0f, 136.0f, 148.0f, true);
    protected static final Colour TINT_COL = new Colour(101.0f, 163.0f, 186.0f, true);
    private float fontSize = UiSettings.NORM_FONT;
    private ComboBoxObject[] comboObjects;
    private int selected;
    private Integer subCatSelected = null;
    private NameBoxUi nameBox;
    private DropBoxUi dropBox;
    private String overrideName;
    private Colour nameColour = TEXT_COLOUR;
    private List<Listener> listeners = new ArrayList<Listener>();

    public ComboBoxUi(Object[] objects, int selected) {
        this.initComboBoxObjects(objects);
        this.selected = selected;
    }

    public ComboBoxUi(ComboBoxObject[] objects, int selected, Integer subCatSelected) {
        this.comboObjects = objects;
        this.selected = selected;
        this.subCatSelected = subCatSelected;
    }

    public void setOverrideName(String name) {
        if (this.nameBox != null) {
            this.nameBox.setOverrideName(name);
        } else {
            this.overrideName = name;
        }
    }

    public void setNameBoxColour(Colour colour) {
        this.nameColour = colour;
        if (this.nameBox != null) {
            this.nameBox.setNameColour(this.nameColour);
        }
    }

    public void setFontSize(float size) {
        this.fontSize = size;
    }

    protected float getFontSize() {
        return this.fontSize;
    }

    public void setObjects(ComboBoxObject[] objects) {
        this.comboObjects = objects;
        this.selected = 0;
        this.subCatSelected = null;
        this.nameBox.setCurrentItem(objects[0]);
    }

    public void addSelectionListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void select(int index, Integer subIndex) {
        ComboBoxObject object = this.getSelectedObject(index, subIndex);
        if (!object.isSelectable()) {
            return;
        }
        this.selected = index;
        this.subCatSelected = subIndex;
        this.nameBox.setCurrentItem(this.getSelectedObject());
        this.fireEvent();
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        if (this.dropBox != null) {
            return super.isMouseOverFocusIrrelevant() || this.dropBox.isMouseOverFocusIrrelevant();
        }
        return super.isMouseOverFocusIrrelevant();
    }

    public ComboBoxObject getSelectedObject() {
        return this.getSelectedObject(this.selected, this.subCatSelected);
    }

    private ComboBoxObject getSelectedObject(int mainCat, Integer subCat) {
        if (subCat == null) {
            return this.comboObjects[mainCat];
        }
        ComboBoxObject newObject = new ComboBoxObject(this.comboObjects[mainCat].getSubObjects()[subCat]);
        newObject.setExtraPrefix(this.comboObjects[mainCat].getExtraPrefix());
        return newObject;
    }

    public int getSelectedIndex() {
        return this.selected;
    }

    public Integer getSelectedSubIndex() {
        return this.subCatSelected;
    }

    @Override
    protected void init() {
        this.nameBox = new NameBoxUi(this.getSelectedObject(), this);
        if (this.overrideName != null) {
            this.nameBox.setOverrideName(this.overrideName);
        }
        this.nameBox.setNameColour(this.nameColour);
        super.addComponent(this.nameBox, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    @Override
    protected void delete() {
        GuiMaster.releaseFocus(this);
        super.delete();
    }

    protected void openDropBox(boolean open) {
        if (open) {
            this.dropBox = new DropBoxUi(this.comboObjects, this);
            float gap = 2.0f / ((float)DisplayManager.getUiHeight() * super.getScale().y);
            super.addComponent(this.dropBox, 0.0f, 1.0f + gap, 1.0f, this.comboObjects.length);
            GuiMaster.focusOn(this);
        } else if (this.dropBox != null) {
            this.dropBox.remove();
            GuiMaster.releaseFocus(this);
            this.dropBox = null;
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

    private void fireEvent() {
        for (Listener listener : this.listeners) {
            listener.eventOccurred(true);
        }
    }

    private void initComboBoxObjects(Object[] objects) {
        this.comboObjects = new ComboBoxObject[objects.length];
        int i = 0;
        while (i < this.comboObjects.length) {
            this.comboObjects[i] = new ComboBoxObject(objects[i]);
            ++i;
        }
    }
}


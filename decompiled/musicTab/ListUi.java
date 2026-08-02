/*
 * Decompiled with CFR 0.152.
 */
package musicTab;

import basics.DisplayManager;
import fontRendering.Text;
import gridLayout.GridGui;
import guis.GuiComponent;
import interpolation.SmoothFloat;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import userInterfaces.GuiPanel;

public class ListUi
extends GuiPanel {
    private static final float X_PAD = 0.04f;
    private static final float TITLE_HEIGHT_PIXELS = 45.0f;
    protected static final int TITLE_Y_PIXELS = 5;
    private static final float TITLE_FONT = GridGui.FONT_SIZE;
    private static final float AGILITY = 15.0f;
    private final String title;
    private final int elementPixelHeight;
    private final int gapPixelHeight;
    private float heightPixels;
    private float gap;
    private float height;
    private List<GuiComponent> listElements;
    private Text text;
    private SmoothFloat yCursor;
    private boolean hideTitle = false;

    public ListUi(String title, int pixelsHigh, int gap) {
        super(ColourPalette.LIGHT_GREY, 0.2f);
        this.title = title;
        this.listElements = new ArrayList<GuiComponent>();
        this.elementPixelHeight = pixelsHigh;
        this.gapPixelHeight = gap;
    }

    public ListUi(String title, int pixelsHigh, int gap, List<GuiComponent> components, boolean hideTitleIfSmall) {
        super(ColourPalette.LIGHT_GREY, 0.2f);
        this.title = title;
        this.hideTitle = hideTitleIfSmall;
        this.elementPixelHeight = pixelsHigh;
        this.listElements = components;
        this.gapPixelHeight = gap;
    }

    public void addElement(GuiComponent component) {
        this.listElements.add(component);
        super.addComponent(component, 0.04f, this.yCursor.get(), 0.92f, this.height);
        this.yCursor.increaseAll(this.height + this.gap);
    }

    public void removeElement(int index) {
        this.listElements.remove(index).remove();
        this.yCursor.increaseTarget(-(this.height + this.gap));
    }

    public void setTitle(String newTitle) {
        if (this.text != null) {
            this.text.setText(newTitle);
        }
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.yCursor.update(DisplayManager.getDeltaSeconds());
        this.shiftElements();
    }

    @Override
    protected void init() {
        super.init();
        this.heightPixels = super.getScale().y * (float)DisplayManager.getUiHeight();
        this.gap = (float)this.gapPixelHeight / this.heightPixels;
        this.height = (float)this.elementPixelHeight / this.heightPixels;
        if (!this.hideTitle || !DisplayManager.isMinitureHeight()) {
            this.addTitle();
        }
        this.initList();
    }

    private void addTitle() {
        this.text = Text.newText(this.title).center().setFontSize(TITLE_FONT).create();
        this.text.setColour(ColourPalette.WHITE);
        super.addText(this.text, 0.0f, 5.0f / this.heightPixels, 1.0f);
    }

    private void initList() {
        float startPixel = DisplayManager.isMinitureHeight() && this.hideTitle ? 5.0f : 45.0f;
        this.yCursor = new SmoothFloat(startPixel / this.heightPixels, 15.0f);
        for (GuiComponent element : this.listElements) {
            super.addComponent(element, 0.04f, this.yCursor.get(), 0.92f, this.height);
            this.yCursor.force(this.yCursor.get() + this.height + this.gap);
        }
    }

    private void shiftElements() {
        int i = 0;
        while (i < this.listElements.size()) {
            GuiComponent element = this.listElements.get(i);
            float yPos = this.yCursor.get() - (this.gap + this.height) * (float)(this.listElements.size() - i);
            if (element.getRelativeY() > yPos) {
                element.setRelativeY(yPos);
            }
            ++i;
        }
    }
}


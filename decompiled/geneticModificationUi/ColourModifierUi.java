/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import dropDownBoxUi.ComboBoxObject;
import dropDownBoxUi.ComboBoxUi;
import dropDownBoxUi.UiProvider;
import geneticModificationUi.ColourMenuItem;
import geneticModificationUi.ColourModifierManager;
import geneticModificationUi.GeneticsPanelUi;
import geneticModificationUi.ModifierUI;
import guis.GuiComponent;
import languages.GameText;
import mainGuis.ColourPalette;
import materials.NaturalColour;
import toolbox.Colour;
import userInterfaces.ChangeListener;
import userInterfaces.GuiPanel;
import userInterfaces.GuiSlider;
import userInterfaces.Listener;

public class ColourModifierUi
extends ModifierUI {
    private static final String TARGET = GameText.getText(935);
    private static final float DISPLAY_PX = 15.0f;
    private static final float DISPLAY__PAD_PX = 7.0f;
    private static final float BUTTON_HEIGHT = 22.0f;
    private static final float MENU_HEIGHT = 0.15f;
    private final ColourModifierManager traitManager;
    private final NaturalColour[] naturalColours;
    private Colour currentNaturalColour;
    private int currentNaturalColourPrice;
    private Colour currentChoice;
    private GuiSlider slider;
    private ComboBoxUi menu;

    public ColourModifierUi(GeneticsPanelUi mainPanel, ColourModifierManager traitManager) {
        super(mainPanel, 165);
        this.traitManager = traitManager;
        this.naturalColours = traitManager.getTrait().blueprint.getNaturalColours();
        this.currentNaturalColour = this.naturalColours[0].colour.duplicate();
        this.currentNaturalColourPrice = this.naturalColours[0].price;
    }

    @Override
    protected void init() {
        super.init();
        this.currentChoice = this.traitManager.getTrait().getModifier().duplicate();
        this.addSlider();
        this.addValueBoxes();
        this.addDropDownMenu();
    }

    @Override
    protected void confirm() {
        this.traitManager.updateModifier(this.currentChoice);
    }

    private void addSlider() {
        this.slider = new GuiSlider(0.0f);
        float pad = super.pixelsToRelativeX(22.0f) + 0.1f;
        float yPos = super.pixelsToRelativeY(58.0f);
        super.addComponent(this.slider, pad, yPos, 1.0f - 2.0f * pad, super.pixelsToRelativeY(15.0f));
        this.slider.addChangeListener(new ChangeListener(){

            @Override
            public void eventOccurred(float value) {
                ColourModifierUi.this.updateChosenColour(value);
            }
        });
        this.addColourDisplay(this.traitManager.getTrait().getModifier().duplicate(), 0.1f, yPos);
        this.addColourDisplay(this.currentNaturalColour, 1.0f - (0.1f + super.pixelsToRelativeX(15.0f)), yPos);
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        return super.isMouseOverFocusIrrelevant() || this.menu != null && this.menu.isMouseOverFocusIrrelevant();
    }

    private void updateChosenColour(float progress) {
        Colour.interpolateColours(this.traitManager.getTrait().getModifier(), this.currentNaturalColour, progress, this.currentChoice);
        float dis = Colour.sub(this.currentChoice, this.traitManager.getTrait().getModifier(), null).length();
        super.updatePrice((int)(dis * (float)this.currentNaturalColourPrice));
    }

    private void addDropDownMenu() {
        ComboBoxObject[] items = this.getColourMenuItems();
        this.menu = new ComboBoxUi(items, 0, null);
        float yPos = super.pixelsToRelativeY(7.0f);
        super.addComponent(this.menu, 0.1f, yPos, 0.8f, 0.15f);
        this.menu.addSelectionListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                ColourModifierUi.this.currentNaturalColour.setColour(((ColourModifierUi)ColourModifierUi.this).naturalColours[((ColourModifierUi)ColourModifierUi.this).menu.getSelectedIndex()].colour);
                ColourModifierUi.this.currentNaturalColourPrice = ((ColourModifierUi)ColourModifierUi.this).naturalColours[((ColourModifierUi)ColourModifierUi.this).menu.getSelectedIndex()].price;
                ColourModifierUi.this.slider.setProgress(1.0f);
            }
        });
    }

    private ComboBoxObject[] getColourMenuItems() {
        ComboBoxObject[] array = new ComboBoxObject[this.naturalColours.length];
        int i = 0;
        while (i < array.length) {
            final int num = i;
            array[i] = new ComboBoxObject((Object)"X", new UiProvider(){

                @Override
                public GuiComponent createUi() {
                    Colour naturalCol = ((ColourModifierUi)ColourModifierUi.this).naturalColours[num].colour;
                    float dis = Colour.sub(naturalCol, ColourModifierUi.this.traitManager.getTrait().getModifier(), null).length();
                    int naturalPrice = ((ColourModifierUi)ColourModifierUi.this).naturalColours[num].price;
                    int price = (int)(dis * (float)naturalPrice);
                    return new ColourMenuItem(((ColourModifierUi)ColourModifierUi.this).naturalColours[num].colour, price, ((ColourModifierUi)ColourModifierUi.this).naturalColours[num].name);
                }
            });
            ++i;
        }
        return array;
    }

    private void addColourDisplay(Colour colour, float relX, float relY) {
        GuiPanel display = new GuiPanel(colour, 2, ColourPalette.MIDDLE_GREY);
        super.addComponent(display, relX, relY, this.pixelsToRelativeX(15.0f), this.pixelsToRelativeY(15.0f));
    }

    private void addValueBoxes() {
        float yPos = 1.0f - super.pixelsToRelativeY(82.0f);
        this.addLabelText(String.valueOf(TARGET) + ":", yPos);
        GuiPanel colourDisplay = new GuiPanel(this.currentChoice, 2, ColourPalette.MIDDLE_GREY);
        super.addComponent(colourDisplay, 0.4f, yPos, 0.5f, super.pixelsToRelativeY(20.0f));
    }
}


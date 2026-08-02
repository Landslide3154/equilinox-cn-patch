/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import colourSelector.ColourSelectorGui;
import fontRendering.Text;
import gameManaging.GameManager;
import geneticModificationUi.ColourModifierManager;
import geneticModificationUi.GeneticsPanelUi;
import geneticModificationUi.ModifierUI;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import session.GameMode;
import toolbox.Colour;
import userInterfaces.GuiPanel;
import userInterfaces.Listener;

public class FreeColourModifierUi
extends ModifierUI {
    private static final int LARGE_HEIGHT = 285;
    private static final String TRAIT = GameText.getText(981);
    private static final float BUTTON_HEIGHT = 22.0f;
    private final ColourModifierManager traitManager;
    private Colour currentChoice;

    public FreeColourModifierUi(GeneticsPanelUi mainPanel, ColourModifierManager traitManager) {
        super(mainPanel, 285);
        this.traitManager = traitManager;
    }

    @Override
    protected void init() {
        super.init();
        this.currentChoice = this.traitManager.getTrait().getModifier().duplicate();
        this.addValueBoxes();
        this.addTitle();
        this.addColourSelector();
    }

    @Override
    protected void confirm() {
        this.traitManager.updateModifier(this.currentChoice);
        if (GameManager.getGameMode() == GameMode.BUILD) {
            GameManager.getSession().getStats().addRecentColour(this.currentChoice);
        }
    }

    private void addTitle() {
        Text text = Text.newText(String.valueOf(this.traitManager.getTrait().blueprint.getName()) + " " + TRAIT).center().setFontSize(UiSettings.LARGE_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, super.pixelsToRelativeY(7.0f), 1.0f);
    }

    private void addColourSelector() {
        ColourSelectorGui selectorUi = new ColourSelectorGui(this.currentChoice, new Colour(1.0f, 1.0f, 0.9f));
        super.addCenteredComponentX(selectorUi, 0.5f, 0.19f, 0.67f);
        selectorUi.addChangeListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                FreeColourModifierUi.super.updatePrice(100);
            }
        });
    }

    private void addValueBoxes() {
        float yPos = 1.0f - super.pixelsToRelativeY(82.0f);
        this.addLabelText("Colour:", yPos);
        GuiPanel colourDisplay = new GuiPanel(this.currentChoice, 2, ColourPalette.MIDDLE_GREY);
        super.addComponent(colourDisplay, 0.4f, yPos, 0.5f, super.pixelsToRelativeY(20.0f));
    }
}


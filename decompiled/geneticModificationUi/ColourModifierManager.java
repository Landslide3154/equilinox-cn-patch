/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import gameManaging.GameManager;
import geneticModificationUi.ColourModifierDisplayUi;
import geneticModificationUi.ColourModifierUi;
import geneticModificationUi.FreeColourModifierUi;
import geneticModificationUi.GeneticsPanelUi;
import geneticModificationUi.ModifierUI;
import geneticModificationUi.TraitModificationManager;
import guis.GuiComponent;
import materials.ColourTrait;
import session.GameMode;
import toolbox.Colour;

public class ColourModifierManager
implements TraitModificationManager {
    private final ColourTrait trait;
    private ColourModifierDisplayUi currentDisplay;

    public ColourModifierManager(ColourTrait trait) {
        this.trait = trait;
    }

    @Override
    public GuiComponent createModifierDisplay() {
        this.currentDisplay = new ColourModifierDisplayUi(this.trait);
        return this.currentDisplay;
    }

    @Override
    public ModifierUI createModifierUi(GeneticsPanelUi mainPanel) {
        if (GameManager.getGameMode() == GameMode.BUILD) {
            return new FreeColourModifierUi(mainPanel, this);
        }
        return new ColourModifierUi(mainPanel, this);
    }

    @Override
    public ColourTrait getTrait() {
        return this.trait;
    }

    public void updateModifier(Colour colour) {
        this.trait.setModifier(colour);
        this.currentDisplay.updateValue();
    }

    @Override
    public void block(boolean blocked) {
        if (this.currentDisplay != null) {
            this.currentDisplay.block(blocked);
        }
    }
}


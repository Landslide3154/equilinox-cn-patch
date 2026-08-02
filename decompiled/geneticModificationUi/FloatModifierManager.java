/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import breedingTraits.FloatTrait;
import geneticModificationUi.FloatModifierDisplayUi;
import geneticModificationUi.FloatModifierUi;
import geneticModificationUi.GeneticsPanelUi;
import geneticModificationUi.ModifierUI;
import geneticModificationUi.TraitModificationManager;
import guis.GuiComponent;

public class FloatModifierManager
implements TraitModificationManager {
    private final FloatTrait trait;
    private FloatModifierDisplayUi currentDisplay;

    public FloatModifierManager(FloatTrait trait) {
        this.trait = trait;
    }

    @Override
    public GuiComponent createModifierDisplay() {
        this.currentDisplay = new FloatModifierDisplayUi(this.trait);
        return this.currentDisplay;
    }

    @Override
    public ModifierUI createModifierUi(GeneticsPanelUi mainPanel) {
        return new FloatModifierUi(mainPanel, this);
    }

    @Override
    public FloatTrait getTrait() {
        return this.trait;
    }

    public void updateModifier(int increase) {
        this.trait.increaseModifier(increase);
        this.currentDisplay.updateValue();
    }

    @Override
    public void block(boolean blocked) {
        if (this.currentDisplay != null) {
            this.currentDisplay.block(blocked);
        }
    }
}


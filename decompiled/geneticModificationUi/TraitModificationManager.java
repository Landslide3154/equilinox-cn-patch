/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import breedingTraits.Trait;
import geneticModificationUi.GeneticsPanelUi;
import geneticModificationUi.ModifierUI;
import guis.GuiComponent;

public interface TraitModificationManager {
    public GuiComponent createModifierDisplay();

    public Trait getTrait();

    public ModifierUI createModifierUi(GeneticsPanelUi var1);

    public void block(boolean var1);
}


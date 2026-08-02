/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import geneticModificationUi.DisplayUi;
import mainGuis.ColourPalette;
import materials.ColourTrait;
import visualFxDrivers.ConstantDriver;

public class ColourModifierDisplayUi
extends DisplayUi {
    private static final int BORDER_PIXELS = 2;
    private final ColourTrait trait;

    public ColourModifierDisplayUi(ColourTrait trait) {
        super(trait.getModifier(), 2, trait.hasModifier() ? ColourPalette.BASE_BLUE : ColourPalette.MIDDLE_GREY);
        this.trait = trait;
    }

    @Override
    protected void init() {
        super.init();
    }

    protected void updateValue() {
        super.setBorderColour(this.trait.hasModifier() ? ColourPalette.BASE_BLUE : ColourPalette.MIDDLE_GREY);
        super.setColour(this.trait.getModifier());
        super.pulse();
    }

    @Override
    public void block(boolean blocked) {
        if (blocked) {
            super.setBorderColour(ColourPalette.DARK_GREY);
            super.setColour(ColourPalette.MIDDLE_GREY);
            super.setAlphaDriver(new ConstantDriver(0.2f));
        } else {
            super.setBorderColour(this.trait.hasModifier() ? ColourPalette.BASE_BLUE : ColourPalette.MIDDLE_GREY);
            super.setColour(this.trait.getModifier());
            super.setAlphaDriver(new ConstantDriver(1.0f));
        }
    }
}


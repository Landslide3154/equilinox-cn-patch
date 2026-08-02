/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import breedingTraits.FloatTrait;
import fontRendering.Text;
import geneticModificationUi.DisplayUi;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import visualFxDrivers.ConstantDriver;

public class FloatModifierDisplayUi
extends DisplayUi {
    private static final float TEXT_Y = -0.1f;
    private final FloatTrait trait;
    private Text text;

    public FloatModifierDisplayUi(FloatTrait trait) {
        super(trait.getModifier() != 0 ? ColourPalette.BASE_BLUE : ColourPalette.MIDDLE_GREY);
        this.trait = trait;
        this.text = Text.newText(this.getTextString()).center().setFontSize(UiSettings.NORM_FONT).create();
        this.text.setColour(ColourPalette.WHITE);
    }

    @Override
    protected void init() {
        super.init();
        super.addText(this.text, 0.0f, -0.1f, 1.0f);
    }

    protected void updateValue() {
        super.setColour(this.trait.getModifier() != 0 ? ColourPalette.BASE_BLUE : ColourPalette.MIDDLE_GREY);
        this.text.setText(this.getTextString());
        super.pulse();
    }

    private String getTextString() {
        return String.valueOf(this.trait.getModifier() >= 0 ? "+" : "") + this.trait.getModifier() + "%";
    }

    @Override
    public void block(boolean blocked) {
        this.text.setColour(blocked ? ColourPalette.MIDDLE_GREY : ColourPalette.WHITE);
        if (blocked) {
            super.setColour(ColourPalette.DARK_GREY);
            super.setAlphaDriver(new ConstantDriver(0.2f));
        } else {
            super.setColour(this.trait.getModifier() != 0 ? ColourPalette.BASE_BLUE : ColourPalette.MIDDLE_GREY);
            super.setAlphaDriver(new ConstantDriver(1.0f));
        }
    }
}


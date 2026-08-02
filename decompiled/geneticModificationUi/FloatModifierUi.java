/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import breedingTraits.FloatTrait;
import fontRendering.Text;
import geneticModificationUi.FloatModifierManager;
import geneticModificationUi.GeneticsPanelUi;
import geneticModificationUi.ModifierUI;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import userInterfaces.ChangeListener;
import userInterfaces.GuiSlider;

public class FloatModifierUi
extends ModifierUI {
    private static final float MAX_TRAIT_CHANGE = 50.0f;
    private static final String INCREASE = GameText.getText(980);
    private static final String TRAIT = GameText.getText(981);
    private final FloatModifierManager traitManager;
    private int increase = 0;
    private float yPos = 7.0f;
    private Text modifierText;

    public FloatModifierUi(GeneticsPanelUi mainPanel, FloatModifierManager traitManager) {
        super(mainPanel, 165);
        this.traitManager = traitManager;
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
    }

    @Override
    protected void init() {
        super.init();
        this.addTitle();
        this.addSlider();
        this.addValueBoxes();
    }

    @Override
    protected void confirm() {
        this.traitManager.updateModifier(this.increase);
    }

    private void addTitle() {
        Text text = Text.newText(String.valueOf(this.traitManager.getTrait().blueprint.getName()) + " " + TRAIT).center().setFontSize(UiSettings.LARGE_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, super.pixelsToRelativeY(this.yPos), 1.0f);
    }

    private void addSlider() {
        GuiSlider slider = new GuiSlider(0.5f);
        slider.setEqualitySlider();
        super.addComponent(slider, 0.099999994f, super.pixelsToRelativeY(58.0f), 0.8f, super.pixelsToRelativeY(15.0f));
        slider.addChangeListener(new ChangeListener(){

            @Override
            public void eventOccurred(float value) {
                FloatModifierUi.this.increase = Math.round(FloatModifierUi.this.sliderProgressToValue(value));
                String sign = FloatModifierUi.this.increase >= 0 ? "+" : "";
                FloatModifierUi.this.modifierText.setText(String.valueOf(sign) + FloatModifierUi.this.increase + "%");
                FloatTrait trait = FloatModifierUi.this.traitManager.getTrait();
                FloatModifierUi.super.updatePrice(FloatModifierUi.this.increase == 0 ? 0 : trait.getModifierCost(FloatModifierUi.this.increase));
            }
        });
    }

    private void addValueBoxes() {
        float yPos = 1.0f - super.pixelsToRelativeY(82.0f);
        this.addLabelText(String.valueOf(INCREASE) + ":", yPos);
        this.addBox(yPos);
        this.modifierText = this.addValueText("+0%", ColourPalette.BASE_BLUE, yPos);
    }

    private float sliderProgressToValue(float progress) {
        float prog = (progress - 0.5f) * 2.0f;
        float sign = prog < 0.0f ? -1 : 1;
        prog = Math.abs(prog);
        prog = this.smoothify(prog);
        return (prog *= sign) * 50.0f;
    }

    private float smoothify(float input) {
        float halfProg = input / 2.0f;
        return 4.0f * (halfProg * halfProg);
    }
}


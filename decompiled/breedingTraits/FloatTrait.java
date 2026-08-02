/*
 * Decompiled with CFR 0.152.
 */
package breedingTraits;

import breedingTraits.FloatTraitBlueprint;
import breedingTraits.Trait;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.TextInfo;
import gameManaging.GameManager;
import geneticModificationUi.FloatModifierManager;
import geneticModificationUi.TraitModificationManager;
import instances.Entity;
import java.io.IOException;
import session.GameMode;
import toolbox.Maths;
import utils.BinaryWriter;

public class FloatTrait
extends Trait {
    public final float value;
    public final float natural;
    public final float base;
    private final float std;
    private final FloatTraitBlueprint floatBlueprint;
    private int modifier = 0;

    public FloatTrait(float value, FloatTraitBlueprint blueprint) {
        super(blueprint);
        this.floatBlueprint = blueprint;
        this.value = value;
        this.base = this.natural = blueprint.naturalValue;
        this.std = blueprint.deviation * value;
    }

    public FloatTrait(float value, float base, FloatTraitBlueprint blueprint) {
        super(blueprint);
        this.floatBlueprint = blueprint;
        this.value = value;
        this.base = base;
        this.natural = blueprint.naturalValue;
        this.std = blueprint.deviation * value;
    }

    @Override
    public FloatTrait reproduce(boolean selectiveBreed, Entity entity) {
        float variation = (float)(Maths.RANDOM.nextGaussian() * (double)this.std);
        variation = this.applyBias(variation, selectiveBreed);
        float offspringValue = this.calculateOffspringTrait(variation);
        float childBase = selectiveBreed ? offspringValue : this.base;
        return new FloatTrait(offspringValue, childBase, (FloatTraitBlueprint)this.blueprint);
    }

    @Override
    public PopUpInfoGui getInfo() {
        return new TextInfo(this.blueprint.getName(), EntityInfoGui.FONT_SIZE){

            @Override
            public String getValue() {
                return ((FloatTraitBlueprint)FloatTrait.this.blueprint).formatTrait(FloatTrait.this.value);
            }
        };
    }

    public float getValue() {
        if (GameManager.getGameMode() == GameMode.BUILD) {
            float offspringValue = this.value + (float)this.modifier / 100.0f * this.value;
            return Math.max(0.0f, offspringValue);
        }
        return this.value;
    }

    public float getModdedValue() {
        float offspringValue = this.value + (float)this.modifier / 100.0f * this.value;
        return Math.max(0.0f, offspringValue);
    }

    public String getFormattedTrait() {
        return this.floatBlueprint.formatTrait(this.value);
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        GameMode mode = writer.getMode();
        if (mode != null && mode == GameMode.BUILD) {
            writer.writeFloat(this.getModdedValue());
            writer.writeFloat(this.base);
            writer.writeInt(0);
        } else {
            writer.writeFloat(this.value);
            writer.writeFloat(this.base);
            writer.writeInt(this.modifier);
        }
    }

    public void setModifier(int percent) {
        this.modifier = percent;
    }

    public void increaseModifier(int increase) {
        this.modifier += increase;
    }

    public int getModifier() {
        return this.modifier;
    }

    public int getModifierCost(int modifierIncrease) {
        int fullMod = this.modifier + modifierIncrease;
        int fullNaturalCost = this.getNaturalCost(fullMod);
        int fullChangeCost = this.getChangeCost(fullMod);
        int fullCost = fullNaturalCost + fullChangeCost;
        int alreadyPaidNatural = this.getNaturalCost(this.modifier);
        int alreadyPaidChange = this.getChangeCost(this.modifier);
        int alreadyPaid = alreadyPaidChange + alreadyPaidNatural;
        return Math.max(100, fullCost - alreadyPaid);
    }

    private float applyBias(float variation, boolean selectiveBreed) {
        float baseValue;
        float f = baseValue = selectiveBreed ? this.natural : this.base;
        if (Math.signum(this.value - baseValue) == Math.signum(variation)) {
            float multiplier = FloatTrait.getGaussianModifier(baseValue, this.floatBlueprint.naturalPull, this.value);
            variation *= multiplier;
        }
        return variation;
    }

    private float calculateOffspringTrait(float variation) {
        float offspringValue = this.value + variation;
        offspringValue += (float)this.modifier / 100.0f * offspringValue;
        return Math.max(0.0f, offspringValue);
    }

    private int getChangeCost(int modifier) {
        float mod = (float)Math.abs(modifier) / 100.0f;
        long cost = Math.max(0L, this.priceCalc(mod, this.floatBlueprint.steepness, this.floatBlueprint.spread, 0));
        if (cost > 2000000000L) {
            return 2000000000;
        }
        return (int)cost;
    }

    private int getNaturalCost(int modifier) {
        float modFactor = 1.0f + (float)modifier / 100.0f;
        float targetVal = this.value * modFactor;
        float increaseFactor = targetVal > this.natural ? targetVal / this.natural : this.natural / targetVal;
        long cost = Math.max(0L, this.priceCalc(increaseFactor -= 1.0f, this.floatBlueprint.steepness, this.floatBlueprint.spread, 0));
        if (cost > 2000000000L) {
            return 2000000000;
        }
        return (int)cost;
    }

    private long priceCalc(float modifier, float steepness, float spread, int recursions) {
        if (modifier <= 0.0f || recursions > 100) {
            return 0L;
        }
        float base = modifier * 100.0f;
        float val = (float)(150.0 * Math.pow(steepness, base / spread));
        return (long)(val + (float)this.priceCalc(modifier - 0.01f, steepness, spread, recursions + 1));
    }

    @Override
    public TraitModificationManager getModificationManager() {
        return new FloatModifierManager(this);
    }

    private static float getGaussianModifier(float base, float pull, float value) {
        float x = base > value ? base / value : value / base;
        x *= x;
        x = 1.0f + pull * (x - 1.0f);
        return 1.0f / x;
    }

    @Override
    public Trait duplicate() {
        float offspringValue = this.value + (float)this.modifier / 100.0f * this.value;
        offspringValue = Math.max(0.0f, offspringValue);
        return new FloatTrait(offspringValue, offspringValue, (FloatTraitBlueprint)this.blueprint);
    }
}


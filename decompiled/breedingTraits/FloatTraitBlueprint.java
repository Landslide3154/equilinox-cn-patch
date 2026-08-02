/*
 * Decompiled with CFR 0.152.
 */
package breedingTraits;

import breedingTraits.FloatTrait;
import breedingTraits.TraitBlueprint;
import toolbox.Maths;
import utils.BinaryReader;

public class FloatTraitBlueprint
extends TraitBlueprint {
    public final float deviation = 0.015f;
    public final float naturalPull = 15.0f;
    public final float steepness;
    public final float spread;
    public final float naturalValue;

    public FloatTraitBlueprint(String name, float naturalValue, float steepness, float spread) {
        super(name);
        this.steepness = steepness;
        this.spread = spread;
        this.naturalValue = naturalValue;
    }

    @Override
    public FloatTrait createRandomInstance() {
        float value = this.naturalValue;
        return new FloatTrait(value, this);
    }

    public String formatTrait(float value) {
        return Maths.toStringWithLimitedDigits(value, 4);
    }

    @Override
    public FloatTrait loadInstance(BinaryReader reader) throws Exception {
        float value = reader.readFloat();
        float base = reader.readFloat();
        int modifier = reader.readInt();
        FloatTrait trait = new FloatTrait(value, base, this);
        trait.setModifier(modifier);
        return trait;
    }
}


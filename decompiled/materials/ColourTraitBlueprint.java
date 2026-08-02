/*
 * Decompiled with CFR 0.152.
 */
package materials;

import blueprints.Blueprint;
import breedingTraits.TraitBlueprint;
import languages.GameText;
import materials.ColourTrait;
import materials.NaturalColour;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Colour;
import toolbox.Maths;
import utils.BinaryReader;

public class ColourTraitBlueprint
extends TraitBlueprint {
    private static final String NAME = GameText.getText(37);
    private final NaturalColour[] colours;
    private final boolean hasSecondNaturalColour;
    private final Blueprint entityBlueprint;

    public ColourTraitBlueprint(NaturalColour[] possibleColours, boolean secondNaturalColour, Blueprint blueprint) {
        super(NAME);
        this.colours = possibleColours;
        this.hasSecondNaturalColour = secondNaturalColour;
        this.entityBlueprint = blueprint;
    }

    public Colour getNaturalColour1() {
        return this.colours[0].colour;
    }

    public boolean hasSecondNaturalColour() {
        return this.hasSecondNaturalColour;
    }

    public Colour getNaturalColour2() {
        return this.hasSecondNaturalColour ? this.colours[1].colour : this.colours[0].colour;
    }

    public Blueprint getEntityBlueprint() {
        return this.entityBlueprint;
    }

    public NaturalColour[] getNaturalColours() {
        return this.colours;
    }

    @Override
    public ColourTrait createRandomInstance() {
        float blend = Maths.RANDOM.nextFloat();
        Colour result = Colour.interpolateColours(this.getNaturalColour1(), this.getNaturalColour2(), blend, null);
        return new ColourTrait(result, this);
    }

    @Override
    public ColourTrait loadInstance(BinaryReader reader) throws Exception {
        Colour value = new Colour(reader.readVector());
        Colour base = new Colour(reader.readVector());
        Vector3f modifier = reader.readVector();
        ColourTrait trait = new ColourTrait(value, base, this);
        trait.setModifier(new Colour(modifier));
        return trait;
    }
}


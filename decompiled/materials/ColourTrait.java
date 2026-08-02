/*
 * Decompiled with CFR 0.152.
 */
package materials;

import breedingTraits.Trait;
import components.Mutator;
import entityInfoGui.ColourInfoGui;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import geneticModificationUi.ColourModifierManager;
import geneticModificationUi.TraitModificationManager;
import instances.Entity;
import java.io.IOException;
import materials.ColourTraitBlueprint;
import org.lwjgl.util.vector.Vector3f;
import session.GameMode;
import toolbox.Colour;
import toolbox.Maths;
import utils.BinaryWriter;

public class ColourTrait
extends Trait {
    private Colour value;
    private Colour baseColour;
    public final ColourTraitBlueprint blueprint;
    private Colour modifier;
    private Vector3f difference = new Vector3f();

    protected ColourTrait(Colour value, ColourTraitBlueprint blueprint) {
        super(blueprint);
        this.value = value;
        this.blueprint = blueprint;
        this.baseColour = value.duplicate();
        this.modifier = value.duplicate();
    }

    protected ColourTrait(Colour value, Colour base, ColourTraitBlueprint blueprint) {
        super(blueprint);
        this.baseColour = base;
        this.value = value;
        this.blueprint = blueprint;
        this.modifier = value.duplicate();
    }

    @Override
    public ColourTrait reproduce(boolean selectiveBreed, Entity entity) {
        Vector3f newColour = Vector3f.add(this.difference, this.value.getVector(), null);
        if (Mutator.testForMutation(this.blueprint.getEntityBlueprint())) {
            Colour randomColour = new Colour();
            float hue = Maths.randomNumberBetween(0.0f, 1.0f);
            randomColour.setHsvColour(hue, Maths.randomNumberBetween(0.1f, 0.45f), Maths.randomNumberBetween(0.3f, 1.0f));
            entity.reproducer.notifyMutation();
            return new ColourTrait(randomColour, this.blueprint);
        }
        return new ColourTrait(new Colour(newColour), this.blueprint);
    }

    @Override
    public PopUpInfoGui getInfo() {
        return new ColourInfoGui(this.value, EntityInfoGui.FONT_SIZE);
    }

    public boolean hasModifier() {
        return !this.value.isEqualTo(this.modifier);
    }

    public Colour getValue() {
        return this.value;
    }

    public Colour getRealValue() {
        return GameManager.getGameMode() == GameMode.BUILD ? this.modifier : this.value;
    }

    @Override
    public Trait duplicate() {
        return new ColourTrait(this.modifier.duplicate(), this.blueprint);
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeVector(this.value.getVector());
        writer.writeVector(this.baseColour.getVector());
        writer.writeVector(this.modifier.getVector());
    }

    public void setModifier(Colour colour) {
        this.modifier.setColour(colour);
        Vector3f.sub(this.modifier.getVector(), this.value.getVector(), this.difference);
    }

    public Colour getModifier() {
        return this.modifier;
    }

    @Override
    public TraitModificationManager getModificationManager() {
        return new ColourModifierManager(this);
    }
}


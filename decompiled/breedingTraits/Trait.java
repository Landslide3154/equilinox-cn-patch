/*
 * Decompiled with CFR 0.152.
 */
package breedingTraits;

import breedingTraits.TraitBlueprint;
import entityInfoGui.PopUpInfoGui;
import geneticModificationUi.TraitModificationManager;
import instances.Entity;
import java.io.IOException;
import utils.BinaryWriter;

public abstract class Trait {
    public final TraitBlueprint blueprint;

    protected Trait(TraitBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    public abstract Trait reproduce(boolean var1, Entity var2);

    public abstract Trait duplicate();

    public abstract PopUpInfoGui getInfo();

    public abstract void export(BinaryWriter var1) throws IOException;

    public abstract TraitModificationManager getModificationManager();
}


/*
 * Decompiled with CFR 0.152.
 */
package breedingTraits;

import breedingTraits.Trait;
import componentArchitecture.ComponentType;
import utils.BinaryReader;

public abstract class TraitBlueprint {
    private String name;
    private ComponentType componmentType;
    private int index;

    public TraitBlueprint(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public ComponentType getComponmentType() {
        return this.componmentType;
    }

    public void setComponmentType(ComponentType componmentType) {
        this.componmentType = componmentType;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public abstract Trait createRandomInstance();

    public abstract Trait loadInstance(BinaryReader var1) throws Exception;
}


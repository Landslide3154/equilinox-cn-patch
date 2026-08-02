/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import breedingTraits.TraitBlueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public abstract class ComponentBlueprint {
    private ComponentType type;
    private boolean dynamic;
    private List<TraitBlueprint> traits = null;

    protected ComponentBlueprint(ComponentType type) {
        this.type = type;
        this.dynamic = type.isDynamic();
    }

    protected ComponentBlueprint(ComponentType type, boolean dynamic) {
        this.type = type;
        this.dynamic = dynamic;
    }

    public final ComponentType getComponentType() {
        return this.type;
    }

    public TraitBlueprint getTraitBlueprint(int index) {
        return this.traits.get(index);
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    protected void overrideDynamicSetting(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public int addTrait(TraitBlueprint traitBlueprint) {
        if (this.traits == null) {
            this.traits = new ArrayList<TraitBlueprint>();
        }
        int number = this.traits.size();
        this.traits.add(traitBlueprint);
        traitBlueprint.setComponmentType(this.type);
        traitBlueprint.setIndex(number);
        return number;
    }

    public List<TraitBlueprint> getTraits() {
        return this.traits;
    }

    public abstract Component createInstance();

    public abstract void delete();

    public abstract void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> var1);
}


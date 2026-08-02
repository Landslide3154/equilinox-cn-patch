/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import breedingTraits.Trait;
import componentArchitecture.Component;
import componentArchitecture.ComponentType;
import instances.Entity;
import java.util.ArrayList;
import java.util.List;

public class ComponentParams {
    private ComponentType type;
    private List<Trait> traits = null;

    public ComponentParams(Component parentComponent, boolean selectiveBreed, Entity entity) {
        this.type = parentComponent.getType();
        if (parentComponent.getTraits() != null) {
            this.traits = new ArrayList<Trait>();
            for (Trait trait : parentComponent.getTraits()) {
                this.traits.add(trait.reproduce(selectiveBreed, entity));
            }
        }
    }

    public ComponentParams(Component parentComponent) {
        this.type = parentComponent.getType();
        if (parentComponent.getTraits() != null) {
            this.traits = new ArrayList<Trait>();
            for (Trait trait : parentComponent.getTraits()) {
                this.traits.add(trait.duplicate());
            }
        }
    }

    public ComponentParams(ComponentType type) {
        this.type = type;
    }

    public ComponentParams(ComponentType type, Trait ... someTraits) {
        this.type = type;
        this.traits = new ArrayList<Trait>();
        Trait[] traitArray = someTraits;
        int n = someTraits.length;
        int n2 = 0;
        while (n2 < n) {
            Trait trait = traitArray[n2];
            this.traits.add(trait);
            ++n2;
        }
    }

    public final ComponentType getType() {
        return this.type;
    }

    public List<Trait> getTraits() {
        return this.traits;
    }
}


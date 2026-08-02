/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import breedingTraits.Trait;
import breedingTraits.TraitBlueprint;
import componentArchitecture.Action;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;
import componentArchitecture.ControlBehaviour;
import componentArchitecture.ParamsBundle;
import entityInfoGui.PopUpInfoGui;
import instances.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import userInterfaces.TextStatInfo;
import utils.BinaryReader;
import utils.BinaryWriter;

public abstract class Component {
    private List<Trait> traits;
    private ComponentBlueprint blueprint;

    protected Component(ComponentBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    public final ComponentType getType() {
        return this.blueprint.getComponentType();
    }

    public boolean reproduce(ParamsBundle params, boolean selected, Entity entity) {
        if (this.traits != null) {
            params.addParams(new ComponentParams(this, selected, entity));
        }
        return true;
    }

    public void duplicate(ParamsBundle params) {
        if (this.traits != null) {
            params.addParams(new ComponentParams(this));
        }
    }

    public ComponentBlueprint getBlueprint() {
        return this.blueprint;
    }

    public final void fullyExport(BinaryWriter writer) throws IOException {
        if (this.traits != null) {
            for (Trait trait : this.traits) {
                trait.export(writer);
            }
        }
        this.export(writer);
    }

    public List<Trait> getTraits() {
        return this.traits;
    }

    public Trait getTrait(int index) {
        return this.traits.get(index);
    }

    public void getPerformanceBuffsInfo(List<TextStatInfo> info) {
    }

    public void getControlableBehaviour(List<ControlBehaviour> behaviours) {
    }

    public abstract void getStatusInfo(List<PopUpInfoGui> var1);

    public abstract void getActions(List<Action> var1);

    public void update() {
    }

    public abstract void export(BinaryWriter var1) throws IOException;

    public abstract void create(ComponentBundle var1);

    public abstract void load(ComponentBundle var1, BinaryReader var2) throws Exception;

    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }

    public void loadComponentTraits(ComponentBlueprint blueprint, BinaryReader reader) throws Exception {
        List<TraitBlueprint> traitBlueprints = blueprint.getTraits();
        if (traitBlueprints == null) {
            return;
        }
        this.traits = new ArrayList<Trait>(traitBlueprints.size());
        for (TraitBlueprint traitBlueprint : traitBlueprints) {
            Trait trait = traitBlueprint.loadInstance(reader);
            if (trait == null) continue;
            this.traits.add(trait);
        }
    }

    public void createComponentTraits(ComponentBlueprint blueprint, ComponentBundle bundle) {
        List<TraitBlueprint> traitBlueprints = blueprint.getTraits();
        if (traitBlueprints == null) {
            return;
        }
        this.traits = new ArrayList<Trait>(traitBlueprints.size());
        ComponentParams params = bundle.getParameters(blueprint.getComponentType());
        if (params == null) {
            for (TraitBlueprint trait : traitBlueprints) {
                this.traits.add(trait.createRandomInstance());
            }
        } else {
            this.traits = params.getTraits();
        }
    }
}


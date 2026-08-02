/*
 * Decompiled with CFR 0.152.
 */
package healer;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import entityInfoGui.PopUpInfoGui;
import healer.HealerCompBlueprint;
import health.Disease;
import instances.Entity;
import java.util.List;
import utils.BinaryReader;
import utils.BinaryWriter;

public class HealerComponent
extends Component {
    private static final int HEAL_AMOUNT = 40;
    private final HealerCompBlueprint blueprint;
    private Entity entity;

    protected HealerComponent(HealerCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    public void heal(Disease diseaseComp) {
        diseaseComp.heal(40);
        this.entity.die(this.blueprint.getDeathAnim().createInstance(this.entity), false);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) {
    }
}


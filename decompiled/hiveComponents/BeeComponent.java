/*
 * Decompiled with CFR 0.152.
 */
package hiveComponents;

import breedingTraits.FloatTrait;
import building.BuildVisitComponent;
import building.BuilderComponent;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import hiveComponents.HiveComponent;
import instances.Entity;
import java.util.List;
import utils.BinaryReader;
import utils.BinaryWriter;

public class BeeComponent
extends Component
implements BuildVisitComponent {
    private static final int STANDARD_HONEY = 5;

    protected BeeComponent(ComponentBlueprint blueprint) {
        super(blueprint);
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

    @Override
    public void create(ComponentBundle bundle) {
        BuilderComponent builder = (BuilderComponent)bundle.getComponent(ComponentType.BUILDER);
        builder.registerVisitComponent(this);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public void visit(Entity structure, boolean fullyBuilt) {
        HiveComponent hive = (HiveComponent)structure.getComponent(ComponentType.HIVE);
        if (fullyBuilt) {
            hive.increaseHoney((int)(((FloatTrait)super.getTrait(0)).getValue() * 5.0f));
        } else {
            hive.indicateVisit();
        }
    }
}


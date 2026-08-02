/*
 * Decompiled with CFR 0.152.
 */
package perching;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import entityInfoGui.PopUpInfoGui;
import instances.Entity;
import instances.EntityListener;
import java.util.List;
import perching.PerchSlot;
import perching.PercherCompBlueprint;
import utils.BinaryReader;
import utils.BinaryWriter;

public class PercherComponent
extends Component {
    private final PercherCompBlueprint blueprint;
    private PerchSlot currentPerchSpot;
    private Entity entity;
    private boolean savePerch = false;

    protected PercherComponent(PercherCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    public void perchOnSpot(PerchSlot spot, boolean save) {
        this.currentPerchSpot = spot;
        this.savePerch = save;
        spot.fill(this);
    }

    public boolean shouldSavePerch() {
        return this.savePerch;
    }

    public int getEntityId() {
        return this.entity.getId();
    }

    public void leaveCurrentPerch() {
        if (this.currentPerchSpot != null) {
            this.currentPerchSpot.vacate(this);
            this.currentPerchSpot = null;
        }
    }

    public void notifyPerchRemoval() {
        if (this.blueprint.removeOnPerchRemoval) {
            this.entity.die(null, false);
        }
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
        this.entity = bundle.getEntity();
        this.addDeathListener();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    private void addDeathListener() {
        this.entity.getNotifier().addIncapacitatedListener(new EntityListener(){

            @Override
            public void execute() {
                PercherComponent.this.leaveCurrentPerch();
            }
        });
    }
}


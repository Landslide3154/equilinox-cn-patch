/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import death.FadeDeath;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import meerkats.TimeOutCompBlueprint;
import utils.BinaryReader;
import utils.BinaryWriter;

public class TimeOutComponent
extends Component {
    private final TimeOutCompBlueprint blueprint;
    private float timer = 0.0f;
    private Entity entity;

    protected TimeOutComponent(TimeOutCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void update() {
        this.timer += GameManager.getGameSeconds();
        if (this.timer > this.blueprint.getDecayTime()) {
            this.entity.die(new FadeDeath(1.0f, this.entity), false);
        }
    }

    public void reset() {
        this.timer = 0.0f;
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package treeCharging;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityInfoGui.PopUpInfoGui;
import growth.GrowthComponent;
import instances.Entity;
import interpolation.Timer;
import java.util.List;
import treeCharging.TreeChargeAi;
import utils.BinaryReader;
import utils.BinaryWriter;

public class TreeChargeComponent
extends Component
implements AiProvidingComponent {
    private final Timer TIMER = Timer.createLoopingTimer(60.0f, 200.0f, true);
    private Entity entity;
    private MovementComp mover;
    private GrowthComponent grower;
    private AiComponent aiComponent;
    private InformationComponent info;
    private boolean isCharging = false;

    protected TreeChargeComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        super.update();
        if (!this.grower.isFullyGrown()) {
            return;
        }
        if (!this.isCharging && this.TIMER.check()) {
            this.isCharging = true;
            this.aiComponent.queueAiProgram(new TreeChargeAi(this.entity, this, this.mover, this.info));
        }
    }

    @Override
    public void notifyAiFinished() {
        this.isCharging = false;
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
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.aiComponent = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.grower = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}


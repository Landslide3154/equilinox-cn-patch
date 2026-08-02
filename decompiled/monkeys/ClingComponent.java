/*
 * Decompiled with CFR 0.152.
 */
package monkeys;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityInfoGui.PopUpInfoGui;
import growth.GrowthComponent;
import instances.Entity;
import interpolation.Timer;
import java.util.List;
import monkeys.ClingAi;
import monkeys.ClingCompBlueprint;
import utils.BinaryReader;
import utils.BinaryWriter;

public class ClingComponent
extends Component
implements AiProvidingComponent {
    private static final Timer TIMER = Timer.createLoopingTimer(5.0f, 10.0f, true);
    private final ClingCompBlueprint blueprint;
    private InformationComponent info;
    private GrowthComponent growth;
    private Entity entity;
    private MovementComp mover;
    private AiComponent aiComp;
    private boolean notNeeded = false;

    protected ClingComponent(ClingCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void update() {
        if (this.notNeeded) {
            return;
        }
        if (this.growth.getGrowthFactor() < 0.5f && this.info.getParent() != null && !this.info.getParent().isDead()) {
            if (TIMER.check()) {
                this.aiComp.queueAiProgram(new ClingAi(this, this.entity, this.info.getParent(), this.growth, this.mover, this.blueprint));
                this.notNeeded = true;
            }
        } else {
            this.notNeeded = true;
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
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.aiComp = (AiComponent)bundle.getComponent(ComponentType.AI);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public void notifyAiFinished() {
        this.notNeeded = false;
    }
}


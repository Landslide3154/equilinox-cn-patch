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
import monkeys.TreeSwingAi;
import monkeys.TreeSwingCompBlueprint;
import utils.BinaryReader;
import utils.BinaryWriter;

public class TreeSwingComponent
extends Component
implements AiProvidingComponent {
    private static final Timer timer = Timer.createLoopingTimer(15.0f, 35.0f, true);
    private GrowthComponent growth;
    private MovementComp mover;
    private AiComponent aiComp;
    private InformationComponent info;
    private Entity entity;
    private boolean swinging = false;

    protected TreeSwingComponent(TreeSwingCompBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        if (this.swinging || this.growth.getGrowthFactor() < 0.5f) {
            return;
        }
        if (timer.check()) {
            this.swinging = true;
            this.aiComp.queueAiProgram(new TreeSwingAi(this.entity, this, this.mover, this.info));
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
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.aiComp = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public void notifyAiFinished() {
        this.swinging = false;
    }
}


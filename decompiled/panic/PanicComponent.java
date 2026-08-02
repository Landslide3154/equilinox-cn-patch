/*
 * Decompiled with CFR 0.152.
 */
package panic;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import interpolation.Timer;
import java.util.List;
import panic.PanicAi;
import utils.BinaryReader;
import utils.BinaryWriter;

public class PanicComponent
extends Component
implements AiProvidingComponent {
    private Timer timer = Timer.createLoopingTimer(20.0f, 45.0f, true).randomize();
    private boolean panicking = false;
    private MovementComp mover;
    private AiComponent aiComp;

    protected PanicComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        super.update();
        if (this.panicking) {
            return;
        }
        if (this.timer.check()) {
            this.aiComp.queueAiProgram(new PanicAi(this.mover, this));
            this.panicking = true;
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
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.aiComp = (AiComponent)bundle.getComponent(ComponentType.AI);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public void notifyAiFinished() {
        this.panicking = false;
    }
}


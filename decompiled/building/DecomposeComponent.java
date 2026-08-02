/*
 * Decompiled with CFR 0.152.
 */
package building;

import building.BuildComponent;
import building.DecomposeCompBlueprint;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import interpolation.Timer;
import java.io.IOException;
import java.util.List;
import session.GameMode;
import utils.BinaryReader;
import utils.BinaryWriter;

public class DecomposeComponent
extends Component {
    private BuildComponent buildComp;
    private Timer timer;
    private Timer initialTimer = Timer.createLoopingTimer(100.0f, true);
    private boolean decaying = false;

    protected DecomposeComponent(DecomposeCompBlueprint blueprint) {
        super(blueprint);
        this.timer = Timer.createLoopingTimer(blueprint.timePerLoss, true);
    }

    @Override
    public void update() {
        super.update();
        if (GameManager.getGameMode() == GameMode.BUILD) {
            return;
        }
        if (!this.decaying) {
            this.decaying = this.initialTimer.check();
            return;
        }
        if (this.timer.check()) {
            this.buildComp.build(-1, false);
        }
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeBoolean(this.decaying);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.buildComp = (BuildComponent)bundle.getComponent(ComponentType.BUILD);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        this.decaying = reader.readBoolean();
    }
}


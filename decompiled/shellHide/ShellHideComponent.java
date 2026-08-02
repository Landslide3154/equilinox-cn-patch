/*
 * Decompiled with CFR 0.152.
 */
package shellHide;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import entityInfoGui.PopUpInfoGui;
import growth.GrowthComponent;
import hunting.PreyComp;
import instances.Entity;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import shellHide.HidingAi;
import shellHide.ShellHideCompBlueprint;
import utils.BinaryReader;
import utils.BinaryWriter;

public class ShellHideComponent
extends Component
implements AiProvidingComponent,
PreyComp {
    private final ShellHideCompBlueprint blueprint;
    private MeshComponent mesh;
    private GrowthComponent growth;
    private AiComponent aiComponent;
    private MovementComp mover;
    private HidingAi hidingAi;
    private boolean hidden = false;
    private Set<Entity> predators = new HashSet<Entity>();

    protected ShellHideComponent(ShellHideCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    protected Set<Entity> getPredatorList() {
        return this.predators;
    }

    @Override
    public void alertToDanger(Entity predator) {
        this.predators.add(predator);
        if (!this.hidden) {
            this.switchState();
        }
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    public void hide() {
        if (this.hidingAi == null && this.growth.getStageNumber() == this.growth.getBlueprint().modelStages - 1) {
            this.hidden = true;
            this.mover.block(true);
            this.mesh.updateModelStage(this.growth.getBlueprint().modelStages);
            this.hidingAi = new HidingAi(this, this.mover.getTransform());
            this.aiComponent.queueAiProgram(this.hidingAi);
        }
    }

    public void unhide() {
        if (this.hidden) {
            this.mesh.updateModelStage(this.growth.getBlueprint().modelStages - 1);
            this.hidden = false;
            this.hidingAi.unhide();
        }
    }

    public void switchState() {
        if (this.hidden) {
            this.unhide();
        } else {
            this.hide();
        }
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeBoolean(this.hidden);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.aiComponent = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        this.hidden = reader.readBoolean();
        if (this.hidden) {
            this.mover.block(true);
            this.hidingAi = new HidingAi(this, this.mover.getTransform());
            this.aiComponent.queueAiProgram(this.hidingAi);
        }
    }

    @Override
    public void notifyAiFinished() {
        this.mover.block(false);
        this.hidingAi = null;
    }

    @Override
    public boolean isInvulnerable() {
        return this.hidden;
    }

    @Override
    public float getSafeRangeSquared() {
        return this.blueprint.safeRangeSquared;
    }
}


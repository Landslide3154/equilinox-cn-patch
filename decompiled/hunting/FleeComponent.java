/*
 * Decompiled with CFR 0.152.
 */
package hunting;

import aiComponent.Ai;
import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityInfoGui.PopUpInfoGui;
import hunting.FleeCompBlueprint;
import hunting.PreyComp;
import hunting.SimpleFleeAi;
import instances.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FleeComponent
extends Component
implements AiProvidingComponent,
PreyComp {
    public final FleeCompBlueprint fleeCompBlueprint;
    private Transformation transform;
    private AiComponent aiComponent;
    private Ai fleeAi;
    private boolean fleeing = false;
    private boolean hiding = false;
    private Set<Entity> predators = new HashSet<Entity>();

    protected FleeComponent(FleeCompBlueprint blueprint) {
        super(blueprint);
        this.fleeCompBlueprint = blueprint;
    }

    @Override
    public void alertToDanger(Entity predator) {
        this.predators.add(predator);
        if (!this.fleeing) {
            this.aiComponent.queueAiProgram(this.fleeAi);
            this.fleeing = true;
        }
    }

    protected Set<Entity> getPredatorList() {
        return this.predators;
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    public void setHiding(boolean hide) {
        this.hiding = hide;
    }

    @Override
    public boolean isInvulnerable() {
        return this.hiding;
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void notifyAiFinished() {
        this.fleeing = false;
    }

    @Override
    public void create(ComponentBundle bundle) {
        MovementComp mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.aiComponent = (AiComponent)bundle.getComponent(ComponentType.AI);
        InformationComponent info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.fleeAi = new SimpleFleeAi(this, mover, this.transform, info);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public float getSafeRangeSquared() {
        return this.fleeCompBlueprint.safeRangeSquared;
    }
}


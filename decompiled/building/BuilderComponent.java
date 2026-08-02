/*
 * Decompiled with CFR 0.152.
 */
package building;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import building.BuildAi;
import building.BuildVisitComponent;
import building.BuilderCompBlueprint;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import growth.GrowthComponent;
import interpolation.Timer;
import java.util.List;
import utils.BinaryReader;
import utils.BinaryWriter;

public class BuilderComponent
extends Component
implements AiProvidingComponent {
    private static final float MIN_TIME = 0.8f;
    private static final float MAX_TIME = 1.3f;
    private final Timer buildTimer;
    private final BuilderCompBlueprint blueprint;
    private BuildVisitComponent visitComp;
    private MovementComp mover;
    private InformationComponent info;
    private AiComponent aiComponent;
    private GrowthComponent grower;
    private boolean inProgress = false;

    protected BuilderComponent(BuilderCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
        this.buildTimer = Timer.createLoopingTimer(blueprint.averageBuildTime * 0.8f, blueprint.averageBuildTime * 1.3f, true).randomize();
    }

    public BuildVisitComponent getVisitComponent() {
        return this.visitComp;
    }

    public void registerVisitComponent(BuildVisitComponent visitComp) {
        this.visitComp = visitComp;
    }

    @Override
    public void update() {
        super.update();
        float time = GameManager.getSession().getStats().getCalendar().getRawTime();
        if (this.inProgress || this.grower.getGrowthFactor() < this.blueprint.buildAgeFactor || time < this.blueprint.startBuildingTime) {
            return;
        }
        if (this.buildTimer.check()) {
            this.aiComponent.queueAiProgram(new BuildAi(this, this.mover, this.info));
            this.inProgress = true;
        }
    }

    @Override
    public void notifyAiFinished() {
        this.inProgress = false;
    }

    @Override
    public BuilderCompBlueprint getBlueprint() {
        return this.blueprint;
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.aiComponent = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.grower = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
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
}


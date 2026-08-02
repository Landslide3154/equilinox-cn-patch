/*
 * Decompiled with CFR 0.152.
 */
package flinging;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import flinging.FlingingCompBlueprint;
import flinging.InsectCatchAi;
import frogMovement.FrogMovement;
import gameManaging.GameManager;
import growth.GrowthComponent;
import instances.Entity;
import interpolation.Timer;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FlingingComponent
extends Component
implements AiProvidingComponent {
    private static final Classification INSECTS = Classifier.getClassification("ai");
    private static final int SAMPLE_SIZE = 3;
    private static final int MIN_LOCAL_POP = 4;
    private FrogMovement mover;
    private InformationComponent info;
    private AiComponent aiComponent;
    private GrowthComponent growth;
    private boolean hunting = false;
    private final Timer timer;

    protected FlingingComponent(FlingingCompBlueprint blueprint) {
        super(blueprint);
        this.timer = Timer.createLoopingTimer(blueprint.minTime, blueprint.maxTime, true).randomize();
    }

    @Override
    public void update() {
        Entity target;
        super.update();
        if (this.growth.getGrowthFactor() > 0.5f && !this.hunting && this.timer.check() && (target = this.findTarget()) != null) {
            this.hunting = true;
            this.aiComponent.queueAiProgram(new InsectCatchAi(target, this.mover, this));
        }
    }

    @Override
    public void notifyAiFinished() {
        this.hunting = false;
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
        this.mover = (FrogMovement)bundle.getComponent(ComponentType.MOVEMENT);
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.aiComponent = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    private Entity findTarget() {
        Entity[] possibleTargets;
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(INSECTS, this.info.getRoamingRange(), basePos.x, basePos.z);
        Entity[] entityArray = possibleTargets = bundle.getRandomList(3);
        int n = possibleTargets.length;
        int n2 = 0;
        while (n2 < n) {
            Entity possibleTarget = entityArray[n2];
            GrowthComponent growth = (GrowthComponent)possibleTarget.getComponent(ComponentType.GROWTH);
            InformationComponent info = (InformationComponent)possibleTarget.getComponent(ComponentType.INFO);
            if (growth.getGrowthFactor() > 0.5f && info.getLocalPopulation() > 4) {
                return possibleTarget;
            }
            ++n2;
        }
        return null;
    }
}


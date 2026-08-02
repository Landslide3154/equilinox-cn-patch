/*
 * Decompiled with CFR 0.152.
 */
package spitting;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import breedingTraits.FloatTrait;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import growth.GrowthComponent;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import spitting.SpitAi;
import spitting.SpitCompBlueprint;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class SpitComponent
extends Component
implements AiProvidingComponent {
    private static final float MIN_TIME = 9.0f;
    private static final float MAX_TIME = 18.0f;
    private static final float RANGE = 2.0f;
    private static final float RANGE_SQRD = 4.0f;
    private MovementComp mover;
    private GrowthComponent growth;
    private Transformation transform;
    private Entity entity;
    private AiComponent aiComp;
    private final SpitCompBlueprint blueprint;
    private boolean spitting = false;
    private float timeTillSpit;

    protected SpitComponent(SpitCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void update() {
        super.update();
        if (this.spitting || this.growth.getGrowthFactor() < 0.5f) {
            return;
        }
        this.timeTillSpit -= GameManager.getGameSeconds();
        if (this.timeTillSpit <= 0.0f) {
            this.spit();
            this.generateSpitTime();
        }
    }

    private void spit() {
        Entity target = this.chooseEntityToSpitOn();
        if (target == null) {
            return;
        }
        this.aiComp.queueAiProgram(new SpitAi(this, target, this.mover, this.transform, this.blueprint.getSpitPosition(), this.entity));
        this.spitting = true;
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
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.entity = bundle.getEntity();
        this.aiComp = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.generateSpitTime();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    private void generateSpitTime() {
        this.timeTillSpit = Maths.randomNumberBetween(9.0f, 18.0f);
        this.timeTillSpit /= ((FloatTrait)super.getTrait(0)).getValue();
    }

    private Entity chooseEntityToSpitOn() {
        Vector3f pos = this.transform.getPosition();
        EntityBundle animals = GameManager.getWorld().getListOfSpecies(Classifier.getAnimalClassification(), 2, pos.x, pos.z);
        if (animals == null || animals.isEmpty()) {
            return null;
        }
        for (Entity animal : animals) {
            float val;
            if (animal == this.entity || animal == null || animal.isDead() || animal.isGrabbed() || !((val = Maths.getComparitableDistance(this.transform.getPosition(), animal.getTransform().getPosition())) > 4.0f)) continue;
            return animal;
        }
        return null;
    }

    @Override
    public void notifyAiFinished() {
        this.spitting = false;
    }
}


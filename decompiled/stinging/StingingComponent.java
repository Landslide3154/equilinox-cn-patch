/*
 * Decompiled with CFR 0.152.
 */
package stinging;

import animator.KeyFrame;
import animator.ValueAnimator;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import growth.GrowthComponent;
import health.LifeComponent;
import hunting.PreyComp;
import instances.Entity;
import interpolation.Timer;
import java.io.IOException;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class StingingComponent
extends Component {
    private static final Classification JELLY_FISH = Classifier.getClassification("afw172");
    private static final Classification FISH = Classifier.getClassification("af");
    private final Timer stingTimer = Timer.createLoopingTimer(0.2f, false).randomize();
    private static final float STING_RANGE_SIDE = 0.1f;
    private static final float STING_RANGE_VERT = 0.3f;
    private static final float STING_WOB = 10.0f;
    private static final float STING_WOB_TIME = 0.15f;
    private static final int STING_POWER = 10;
    private static final float COOLDOWN_TIME = 1.0f;
    private static final float STING_RANGE_SQUARED = 0.010000001f;
    private final ValueAnimator stingAnim = new ValueAnimator(new KeyFrame[]{new KeyFrame(0.0f, 0.0f), new KeyFrame(0.075f, 10.0f), new KeyFrame(0.22500001f, -10.0f), new KeyFrame(0.375f, 10.0f), new KeyFrame(0.45000002f, 0.0f)});
    private Transformation transform;
    private float cooldown = 0.0f;
    private Entity jellyFish;
    private GrowthComponent growth;

    protected StingingComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        super.update();
        if (this.growth.getGrowthFactor() < 0.5f) {
            return;
        }
        if (this.cooldown > 0.0f) {
            this.cooldown();
            return;
        }
        if (this.stingTimer.check()) {
            EntityBundle localAnimals = this.getLocalAnimals();
            this.stingInRangeAnimals(localAnimals);
        }
    }

    private void cooldown() {
        this.cooldown -= GameManager.getGameSeconds();
        this.stingAnim.updateAnimation(GameManager.getGameSeconds());
        this.transform.setXRotation(this.stingAnim.getValue());
        if (this.cooldown < 0.0f) {
            this.cooldown = 0.0f;
        }
    }

    private EntityBundle getLocalAnimals() {
        Vector3f jellyPosition = this.transform.getPosition();
        return GameManager.getWorld().getListOfSpecies(FISH, 2, jellyPosition.x, jellyPosition.z);
    }

    private void stingInRangeAnimals(EntityBundle localAnimals) {
        for (Entity animal : localAnimals) {
            if (animal.getBlueprint().getSpeciesClassification().isTypeOf(JELLY_FISH) || !this.isInRange(animal)) continue;
            this.sting(animal);
        }
    }

    private void sting(Entity animal) {
        this.stingAnim.reset();
        LifeComponent targetLife = (LifeComponent)animal.getComponent(ComponentType.LIFE);
        targetLife.health.takeDamage(10, null);
        this.cooldown = 1.0f;
        try {
            PreyComp fleeComp = (PreyComp)((Object)animal.getComponent(ComponentType.FLEE));
            if (fleeComp != null) {
                fleeComp.alertToDanger(this.jellyFish);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private boolean isInRange(Entity animal) {
        Vector3f targetPos = animal.getTransform().getPosition();
        Vector3f jellyPos = this.transform.getPosition();
        float disSquared = Maths.getComparitableDistance(targetPos.x, targetPos.z, jellyPos.x, jellyPos.z);
        if (disSquared < 0.010000001f) {
            return Math.abs(jellyPos.y - targetPos.y) < 0.3f;
        }
        return false;
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
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.jellyFish = bundle.getEntity();
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}


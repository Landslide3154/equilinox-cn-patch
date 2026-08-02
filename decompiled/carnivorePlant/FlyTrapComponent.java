/*
 * Decompiled with CFR 0.152.
 */
package carnivorePlant;

import blueprints.Blueprint;
import breedingTraits.FloatTrait;
import carnivorePlant.FlyTrapCompBlueprint;
import carnivorePlant.TongueShootComp;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import growth.GrowthComponent;
import instances.Entity;
import interpolation.Timer;
import java.io.IOException;
import java.util.List;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import resourceManagement.BlueprintRepository;
import toolbox.Maths;
import toolbox.TransformChangeListener;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FlyTrapComponent
extends Component {
    private final Classification INSECTS = Classifier.getClassification("ai");
    private static final int MIN_POPULATION = 3;
    private final float COOLDOWN_TIME = 1.5f;
    private final float RANGE_SQUARED = 0.040000003f;
    private final float HEIGHT_MAX = 0.6f;
    private final Timer checkTimer = Timer.createLoopingTimer(0.3f, 0.5f, true);
    private float cooldown = 0.0f;
    private Transformation transform;
    private GrowthComponent growth;
    private InformationComponent info;
    private Vector4f tonguePosition = new Vector4f();
    private final FlyTrapCompBlueprint blueprint;

    protected FlyTrapComponent(FlyTrapCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void update() {
        if (!this.readyToAttack()) {
            return;
        }
        Entity nearbyInsect = this.getNearbyInsect();
        if (nearbyInsect == null || !this.sufficientInsectsRemaining()) {
            return;
        }
        this.releaseTongue(new Vector3f(this.tonguePosition), nearbyInsect);
        this.cooldown = 1.5f;
    }

    private Entity getNearbyInsect() {
        Vector3f plantPos = this.transform.getPosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(this.INSECTS, 1, plantPos.x, plantPos.z);
        for (Entity insect : bundle) {
            if (!this.isInRange(insect)) continue;
            return insect;
        }
        return null;
    }

    private boolean isInRange(Entity insect) {
        Vector3f insectPos = insect.getTransform().getPosition();
        Vector3f plantPos = this.transform.getPosition();
        float disSquared = Maths.getComparitableDistance(insectPos.x, insectPos.z, plantPos.x, plantPos.z);
        float heightDiff = insectPos.y - this.tonguePosition.y;
        return disSquared < 0.040000003f && heightDiff > 0.0f && heightDiff < 0.6f;
    }

    private void releaseTongue(Vector3f pos, Entity insect) {
        Blueprint tongue = BlueprintRepository.getBlueprint(177);
        Transformation.TransformBlueprint transformBlueprint = this.transform.getBlueprint();
        Transformation.TransformParams param = new Transformation.TransformParams(pos, 0.0f, new FloatTrait(1.0E-4f, transformBlueprint.getSizeTraitBlueprint()));
        TongueShootComp.TongueShootParams tongueParam = new TongueShootComp.TongueShootParams(insect);
        Entity tongueInstance = tongue.createInstance(param, tongueParam);
        GameManager.getSession().getWorld().addInstance(tongueInstance, true);
    }

    private boolean sufficientInsectsRemaining() {
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(this.INSECTS, this.info.getRoamingRange(), basePos.x, basePos.z);
        return bundle.getSize() >= 3;
    }

    private boolean readyToAttack() {
        if (this.cooldown <= 0.0f && this.growth.isFullyGrown()) {
            return this.checkTimer.check();
        }
        this.cooldown -= GameManager.getGameSeconds();
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

    private void updateTonguePos() {
        Matrix4f.transform(this.transform.getModelMatrix(), this.blueprint.startPos, this.tonguePosition);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.updateTonguePos();
        this.transform.addChangeListener(new TransformChangeListener(){

            @Override
            public void transformChanged() {
                FlyTrapComponent.this.updateTonguePos();
            }
        });
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package baseMovement;

import baseMovement.MoveUtils;
import baseMovement.MovementComp;
import breedingTraits.FloatTrait;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import growth.GrowthComponent;
import health.LifeComponent;
import instances.Entity;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public abstract class BaseMovement
extends Component
implements MovementComp {
    public static final float ROT_SPEED = 180.0f;
    public static final float LOW_HEALTH = 0.2f;
    private final float runFactor;
    private static final float SMALL_SIZE_FACTOR = 0.4f;
    private static final float BIG_SIZE_FACTOR = 0.6f;
    private boolean moving = false;
    private boolean turning = false;
    private boolean running = false;
    private float rotSpeed;
    private float targetRotation;
    private float actualRotY;
    private float extraRotY;
    private Transformation transform;
    private GrowthComponent grower;
    private LifeComponent life;
    private Entity entity;
    private boolean blocked = false;
    private boolean eggStage = false;

    protected BaseMovement(ComponentBlueprint blueprint, float rotSpeed, float runFactor) {
        super(blueprint);
        this.rotSpeed = rotSpeed;
        this.runFactor = runFactor;
    }

    protected BaseMovement(ComponentBlueprint blueprint, float rotSpeed, boolean eggStage, float runFactor) {
        super(blueprint);
        this.eggStage = eggStage;
        this.rotSpeed = rotSpeed;
        this.runFactor = runFactor;
    }

    @Override
    public boolean isSwimming() {
        return false;
    }

    @Override
    public void update() {
        if (this.blocked || this.eggStage && this.grower.getStageNumber() == 0) {
            return;
        }
        boolean reached = MoveUtils.updateBaseRotation(this, this.targetRotation, this.rotSpeed);
        this.updateMovement(reached);
        this.moving = false;
        this.turning = false;
        GameManager.getWorld().getEntityGrid().updateInGrid(this.entity);
    }

    @Override
    public void walkForward() {
        this.moving = true;
        this.running = false;
    }

    @Override
    public void block(boolean blocked) {
        this.blocked = blocked;
    }

    public float getActualRotY() {
        return this.actualRotY;
    }

    public float getExtraRotY() {
        return this.extraRotY;
    }

    public void setActualRotY(float rotY) {
        this.actualRotY = rotY;
        this.getTransform().setYRotation(this.actualRotY + this.extraRotY);
    }

    @Override
    public float getHeadingRotation() {
        return this.actualRotY;
    }

    public void setExtraRotY(float rotY) {
        this.extraRotY = rotY;
        this.getTransform().setYRotation(this.actualRotY + this.extraRotY);
    }

    @Override
    public void run() {
        this.moving = true;
        this.running = true;
    }

    @Override
    public void turn(float rotY) {
        this.turning = true;
        this.targetRotation = rotY;
        this.targetRotation %= 360.0f;
    }

    @Override
    public boolean goToTargetAndFace(Vector3f target, boolean run, float radius) {
        return MoveUtils.goToTargetAndFace(this, target, run, radius);
    }

    @Override
    public void increaseTurn(float change) {
        this.turn(change + this.actualRotY);
    }

    @Override
    public boolean land(Vector3f target) {
        return true;
    }

    @Override
    public boolean goToTarget(Vector3f target, boolean run, float radius) {
        return MoveUtils.goToTarget(this, target, run, radius);
    }

    @Override
    public void goFromTarget(Vector3f target, boolean run) {
        MoveUtils.goFromTarget(this, target, run);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.life = (LifeComponent)bundle.getComponent(ComponentType.LIFE);
        this.grower = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.entity = bundle.getEntity();
        this.actualRotY = this.transform.getRotY();
    }

    public GrowthComponent getGrowthComp() {
        return this.grower;
    }

    public LifeComponent getLifeComp() {
        return this.life;
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public Transformation getTransform() {
        return this.transform;
    }

    public boolean isMoving() {
        return this.moving;
    }

    public boolean isTurning() {
        return this.turning;
    }

    public boolean isRunning() {
        return this.running;
    }

    public float getTargetRotation() {
        return this.targetRotation;
    }

    protected float getLifeFactor() {
        return Math.min(1.0f, this.life.getWellbeing() / 0.2f);
    }

    protected float getLifeFactor(float minEffect) {
        return minEffect + this.getLifeFactor() * (1.0f - minEffect);
    }

    protected float getRunFactor() {
        return this.running ? this.runFactor : 1.0f;
    }

    protected float getBaseSpeed() {
        return ((FloatTrait)super.getTrait(0)).getValue();
    }

    protected float getSizeFactor() {
        float standardSize = this.entity.getBlueprint().getMaxHeight() * 0.1f;
        float scaleablePart = 0.6f * (this.entity.getBoundingBox().getSizes().y / standardSize);
        return 0.4f + scaleablePart;
    }

    protected float getLifeSizeFactor() {
        float sizeFactor = this.getSizeFactor();
        float lifeFactor = this.getLifeFactor();
        return lifeFactor * sizeFactor;
    }

    protected float getTotalSpeedFactor() {
        return this.getLifeSizeFactor() * this.getRunFactor();
    }

    protected abstract void updateMovement(boolean var1);
}


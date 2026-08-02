/*
 * Decompiled with CFR 0.152.
 */
package birds;

import aiBasics.AiRoutine;
import baseMovement.MovementComp;
import birds.BirdAiBlueprint;
import birds.BirdSittingAnimation;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import gameManaging.GameManager;
import instances.Entity;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import perching.PerchComponent;
import perching.PerchSlot;
import perching.PercherComponent;
import toolbox.Maths;
import toolbox.Transformation;
import world.GridSection;
import world.PerSectionCode;

public class GrownBirdAi
implements AiRoutine {
    private static final float START_FLYING_TIME = 0.13f;
    private static final String DESC = GameText.getText(179);
    private static final String LAND_DESC = GameText.getText(198);
    private static final String SIT_DESC = GameText.getText(199);
    private final float TARGET_RADIUS = 0.2f;
    private final float SIT_TIME_MIN = 5.0f;
    private final float SIT_TIME_RAND = 8.0f;
    private final float WANDER_TIME_RAND = 5.0f;
    private final BirdAiBlueprint blueprint;
    private final MovementComp mover;
    private final Transformation transform;
    private final InformationComponent info;
    private final PercherComponent percherComp;
    private BirdSittingAnimation sittingAnim;
    private float rot = Maths.RANDOM.nextFloat() * 360.0f;
    private float timeTillWander;
    private boolean circling = true;
    private Vector3f currentWanderTarget;
    private boolean circleLeft = false;
    private PerchSlot targetPerch = null;
    private boolean landed = false;
    private float sittingTime = 0.0f;

    public GrownBirdAi(BirdAiBlueprint blueprint, MovementComp mover, Transformation transform, InformationComponent info, PercherComponent percherComp) {
        this.mover = mover;
        this.transform = transform;
        this.info = info;
        this.percherComp = percherComp;
        this.sittingAnim = new BirdSittingAnimation(transform);
        this.timeTillWander = Maths.RANDOM.nextFloat() * 5.0f + blueprint.getCircleMinTime();
        this.blueprint = blueprint;
    }

    @Override
    public boolean update() {
        if (this.landed) {
            this.sit();
        } else if (this.percherComp != null && this.targetPerch != null) {
            this.land();
        } else if (this.circling) {
            this.circle();
        } else {
            boolean reached = this.mover.goToTarget(this.currentWanderTarget, false, 0.2f);
            if (reached) {
                this.circling = true;
            }
        }
        if (this.transform.getPosition().y < GameManager.getWorld().getWaterHeight()) {
            this.switchToWander();
        }
        return false;
    }

    private void land() {
        if (this.targetPerch.isAvailable()) {
            this.landed = this.mover.land(this.targetPerch.getWorldPosition());
            if (this.landed) {
                this.switchToSit();
            }
        } else {
            this.targetPerch = null;
            this.switchToWander();
        }
    }

    private void switchToSit() {
        float time = GameManager.getSession().getStats().getCalendar().getRawTime();
        float diff = Math.max(0.13f - time, 0.0f);
        this.sittingTime = Maths.RANDOM.nextFloat() * 8.0f + 5.0f + (diff *= 720.0f);
        this.percherComp.perchOnSpot(this.targetPerch, false);
        this.sittingAnim.indicateStart();
    }

    private void sit() {
        this.sittingTime -= GameManager.getGameSeconds();
        this.sittingAnim.doAnimation();
        if (this.sittingTime <= 0.0f || !this.targetPerch.isInExistence()) {
            this.leavePerch();
        }
    }

    private void leavePerch() {
        this.percherComp.leaveCurrentPerch();
        this.targetPerch = null;
        this.landed = false;
        this.switchToWander();
    }

    private void switchToWander() {
        this.circling = false;
        this.timeTillWander = Maths.RANDOM.nextFloat() * 5.0f + this.blueprint.getCircleMinTime();
        this.currentWanderTarget = this.info.getRandomInRangePoint();
        this.rot = Maths.RANDOM.nextFloat() * 360.0f;
        this.circleLeft = Maths.RANDOM.nextBoolean();
    }

    private void switchToLanding() {
        Vector3f position = this.info.getBasePosition();
        final EntityBundle bigBundle = new EntityBundle();
        GameManager.getWorld().iterateGridSquaresNew(position.x, position.z, this.info.getRoamingRange(), new PerSectionCode(){

            @Override
            public void execute(GridSection gridSquare) {
                EntityBundle bundle = gridSquare.getEntitiesWithComponent(ComponentType.PERCH);
                if (bundle != null && !bundle.isEmpty()) {
                    bigBundle.merge(bundle);
                }
            }
        });
        if (!bigBundle.isEmpty()) {
            Entity tree = bigBundle.getRandomEntity();
            this.targetPerch = ((PerchComponent)tree.getComponent(ComponentType.PERCH)).getRandomAvailableSlot();
        }
    }

    private void circle() {
        this.mover.walkForward();
        this.mover.turn(this.rot);
        float change = GameManager.getGameSeconds() * this.blueprint.getCircleRot();
        this.rot += (change *= (float)(this.circleLeft ? 1 : -1));
        this.checkLanding();
    }

    private void checkLanding() {
        this.timeTillWander -= GameManager.getGameSeconds();
        if (this.timeTillWander < 0.0f) {
            if (this.percherComp != null) {
                this.switchToLanding();
            }
            if (this.targetPerch == null) {
                this.switchToWander();
            }
        }
    }

    @Override
    public void interrupt() {
        if (this.percherComp != null) {
            this.percherComp.leaveCurrentPerch();
        }
    }

    @Override
    public String getDescription() {
        if (this.landed) {
            return SIT_DESC;
        }
        if (this.targetPerch != null) {
            return LAND_DESC;
        }
        return DESC;
    }
}


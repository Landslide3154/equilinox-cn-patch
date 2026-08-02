/*
 * Decompiled with CFR 0.152.
 */
package ai;

import aiBasics.AiRoutine;
import baseMovement.MovementComp;
import components.InformationComponent;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;

public class GrownAi
implements AiRoutine {
    private final float TARGET_RADIUS = 0.2f;
    private final float ROT_SPEED = 25.0f;
    private final float WANDER_TIME_MIN = 10.0f;
    private final float WANDER_TIME_RAND = 15.0f;
    private final InformationComponent info;
    private final MovementComp mover;
    private final Transformation transform;
    private float rot = Maths.RANDOM.nextFloat() * 360.0f;
    private float timeTillWander = Maths.RANDOM.nextFloat() * 15.0f + 10.0f;
    private boolean circling = true;
    private Vector3f currentWanderTarget;
    private boolean circleLeft = false;

    public GrownAi(MovementComp mover, Transformation transform, InformationComponent info) {
        this.mover = mover;
        this.transform = transform;
        this.info = info;
    }

    @Override
    public boolean update() {
        if (this.circling) {
            this.circle();
        } else {
            boolean reached = this.mover.goToTarget(this.currentWanderTarget, false, 0.2f);
            if (reached) {
                this.mover.walkForward();
                this.circling = true;
            }
        }
        if (this.transform.getPosition().y < GameManager.getWorld().getWaterHeight()) {
            this.switchToWander();
        }
        return false;
    }

    private void switchToWander() {
        this.circling = false;
        this.timeTillWander = Maths.RANDOM.nextFloat() * 15.0f + 10.0f;
        this.currentWanderTarget = this.info.getRandomInRangePoint();
        this.rot = Maths.RANDOM.nextFloat() * 360.0f;
        this.circleLeft = Maths.RANDOM.nextBoolean();
    }

    private void circle() {
        this.timeTillWander -= GameManager.getGameSeconds();
        this.mover.walkForward();
        this.mover.turn(this.rot);
        float change = GameManager.getGameSeconds() * 25.0f;
        this.rot += (change *= (float)(this.circleLeft ? 1 : -1));
        if (this.timeTillWander < 0.0f || !this.info.isInBaseRange()) {
            this.switchToWander();
        }
    }

    @Override
    public void interrupt() {
    }

    @Override
    public String getDescription() {
        return "Wandering";
    }
}


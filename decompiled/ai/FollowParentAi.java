/*
 * Decompiled with CFR 0.152.
 */
package ai;

import ai.IdlePlay;
import aiBasics.AiRoutine;
import baseMovement.MovementComp;
import components.InformationComponent;
import instances.Entity;
import languages.GameText;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class FollowParentAi
implements AiRoutine {
    private static final String FOLLOW_DESC = GameText.getText(186);
    private static final String PLAYING_DESC = GameText.getText(979);
    private static final float MIN_STAND = 1.6f;
    private static final float MAX_STAND = 2.5f;
    private static final float PLAY_AROUND_AREA = 0.4f;
    private static final float TURN_TIME = 0.5f;
    private static final float TURN_AMOUNT = 40.0f;
    private static final float CLOSE_RADIUS = 0.3f;
    private static final float FAR_RADIUS = 0.9f;
    private static final float FAR_RADIUS_SQUARED = 0.80999994f;
    private final Transformation transform;
    private final MovementComp mover;
    private final InformationComponent info;
    private boolean pausing = false;
    private final IdlePlay idlePlay;

    public FollowParentAi(MovementComp mover, Transformation transform, InformationComponent info) {
        this.mover = mover;
        this.transform = transform;
        this.info = info;
        this.idlePlay = this.initIdlePlay();
    }

    public void setSwimmingCheck() {
        this.idlePlay.setSwimmingCheck(true);
    }

    @Override
    public boolean update() {
        this.checkPaused();
        if (this.pausing) {
            this.idlePlay.playAround();
        } else {
            this.moveToParent();
        }
        return false;
    }

    @Override
    public void interrupt() {
        this.idlePlay.reset();
    }

    @Override
    public String getDescription() {
        return this.hasParent() ? FOLLOW_DESC : PLAYING_DESC;
    }

    private void moveToParent() {
        Vector3f parentPosition = this.info.getParent().getTransform().getPosition();
        boolean reached = this.mover.goToTarget(parentPosition, true, 0.3f);
        if (reached && this.mover.normalize()) {
            this.pausing = true;
            this.idlePlay.reset();
        }
    }

    private void checkPaused() {
        Entity parent = this.info.getParent();
        if (parent == null || parent.isDead()) {
            this.pausing = true;
            return;
        }
        Vector2f toTarget = this.getToTargetVector();
        float distanceSquared = toTarget.lengthSquared();
        if (this.pausing && distanceSquared > 0.80999994f) {
            this.pausing = false;
        }
    }

    private Vector2f getToTargetVector() {
        Vector3f parentPos = this.info.getParent().getTransform().getPosition();
        Vector3f pos = this.transform.getPosition();
        return Vector2f.sub(new Vector2f(parentPos.x, parentPos.z), new Vector2f(pos.x, pos.z), null);
    }

    private IdlePlay initIdlePlay() {
        IdlePlay idlePlay = new IdlePlay(this.mover, this.transform, this.info);
        idlePlay.setPlayAroundArea(0.4f);
        idlePlay.setStandTime(1.6f, 2.5f);
        idlePlay.setTurnVariables(40.0f, 0.5f);
        return idlePlay;
    }

    private boolean hasParent() {
        return this.info.getParent() != null && !this.info.getParent().isDead();
    }
}


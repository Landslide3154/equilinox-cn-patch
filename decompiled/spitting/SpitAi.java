/*
 * Decompiled with CFR 0.152.
 */
package spitting;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import animator.KeyFrame;
import animator.ValueAnimator;
import baseMovement.MoveUtils;
import baseMovement.MovementComp;
import blueprints.Blueprint;
import gameManaging.GameManager;
import instances.Entity;
import languages.GameText;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import resourceManagement.BlueprintRepository;
import spitting.ProjectileComponent;
import spitting.SpitComponent;
import toolbox.Transformation;

public class SpitAi
implements Ai {
    private static final float SPIT_RELEASE = 0.7f;
    private static final KeyFrame[] ANIMATION = new KeyFrame[]{new KeyFrame(0.0f, 0.0f), new KeyFrame(0.5f, -20.0f), new KeyFrame(0.7f, 10.0f), new KeyFrame(1.3f, 0.0f)};
    private static final float DURATION = 0.8f;
    private static final String SPIT = GameText.getText(1104);
    private final SpitComponent component;
    private final Entity target;
    private final Transformation transform;
    private final Entity attacker;
    private final MovementComp mover;
    private final Vector4f spitPosition;
    private boolean readyToFire = false;
    private boolean doingFiring = false;
    private ValueAnimator animator = new ValueAnimator(ANIMATION);

    protected SpitAi(SpitComponent component, Entity target, MovementComp mover, Transformation transform, Vector4f spitPosition, Entity attacker) {
        this.target = target;
        this.component = component;
        this.attacker = attacker;
        this.transform = transform;
        this.mover = mover;
        this.spitPosition = spitPosition;
    }

    @Override
    public boolean carryOut() {
        if (this.target == null || this.target.isDead() || this.target.isGrabbed()) {
            return true;
        }
        if (!this.readyToFire) {
            this.moveToPosition();
        } else if (this.mover.normalize()) {
            this.doingFiring = true;
        }
        if (this.doingFiring) {
            return this.doFiringAction();
        }
        return false;
    }

    private void moveToPosition() {
        Vector3f targetPos = new Vector3f(this.target.getTransform().getPosition());
        Vector3f pos = this.transform.getPosition();
        Vector2f toTarget2d = new Vector2f(targetPos.x - pos.x, targetPos.z - pos.z);
        try {
            float angle = MoveUtils.goInDirection(this.mover, toTarget2d);
            if (angle < 5.0f) {
                this.readyToFire = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean doFiringAction() {
        float startTime = this.animator.getTime();
        boolean finished = this.animator.updateAnimation(GameManager.getGameSeconds());
        this.transform.setXRotation(this.animator.getValue());
        float endTime = this.animator.getTime();
        if (startTime <= 0.7f && endTime > 0.7f) {
            this.fire();
        }
        return finished;
    }

    private void fire() {
        Vector3f startPos = new Vector3f(this.getOffset());
        Vector3f startVelocity = this.calculateStartVelocity(this.target, startPos);
        this.releaseSpit(startVelocity, startPos);
    }

    private Vector3f calculateStartVelocity(Entity target, Vector3f startPos) {
        Vector3f targetPos = new Vector3f(target.getTransform().getPosition());
        targetPos.y += target.getBoundingBox().getHeight() * 0.5f;
        Vector2f toTarget2d = new Vector2f(targetPos.x - startPos.x, targetPos.z - startPos.z);
        float horizVelocity = toTarget2d.length() / 0.8f;
        float upVelocity = (targetPos.y + 1.6f - startPos.y) / 0.8f;
        toTarget2d.normalise();
        toTarget2d.scale(horizVelocity);
        return new Vector3f(toTarget2d.x, upVelocity, toTarget2d.y);
    }

    private void releaseSpit(Vector3f startVelocity, Vector3f startPos) {
        Blueprint spit = BlueprintRepository.getBlueprint(164);
        ProjectileComponent.ProjectileParams param = new ProjectileComponent.ProjectileParams(this.target, startVelocity, this.attacker);
        Transformation.TransformBlueprint transformBlueprint = this.transform.getBlueprint();
        Transformation.TransformParams param2 = new Transformation.TransformParams(startPos, 0.0f, transformBlueprint.generateRandomScale());
        Entity spitInstance = spit.createInstance(param, param2);
        GameManager.getSession().getWorld().addInstance(spitInstance, true);
    }

    private Vector4f getOffset() {
        return Matrix4f.transform(this.transform.getModelMatrix(), this.spitPosition, null);
    }

    @Override
    public float getPriority() {
        return 1.1f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.component;
    }

    @Override
    public void interrupt() {
        this.readyToFire = false;
        this.doingFiring = false;
        this.animator.reset();
    }

    @Override
    public String getDescription() {
        return SPIT;
    }
}


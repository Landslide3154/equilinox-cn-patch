/*
 * Decompiled with CFR 0.152.
 */
package fighting;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import breedingTraits.FloatTrait;
import classification.Classification;
import componentArchitecture.ComponentType;
import fighting.AttackAnimation;
import fighting.FightComponent;
import gameManaging.GameManager;
import health.LifeComponent;
import hunting.HuntComponent;
import hunting.PreyComp;
import instances.Entity;
import interpolation.Timer;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import session.GameMode;

public class FightAi
implements Ai {
    private static final float PRIORITY = 100.0f;
    private static final float CLOSE_RANGE = 5.5f;
    private static final float CLOSE_RANGE_SQR = 30.25f;
    private static final String DESC = GameText.getText(191);
    private final Entity entity;
    private final Entity target;
    private final MovementComp mover;
    private final FightComponent fightComp;
    private final HuntComponent huntComp;
    private final Classification preyClass;
    private final boolean oneOffAttack;
    private final Timer pauseTimer;
    private final AttackAnimation animation;
    private boolean attacking = false;
    private boolean runningAttack;
    private boolean wasInRange = false;
    private Timer chaseTimer = Timer.createOneOffTimer(9.0f, true);
    private Timer alertTimer = Timer.createLoopingTimer(0.2f, true).randomize();

    public FightAi(Entity entity, MovementComp mover, FightComponent fightComp, Entity target, boolean oneOffAttack, boolean running) {
        this.entity = entity;
        this.runningAttack = running;
        this.target = target;
        this.mover = mover;
        this.oneOffAttack = oneOffAttack;
        this.fightComp = fightComp;
        this.huntComp = null;
        this.preyClass = null;
        this.animation = fightComp.blueprint.getAnimation();
        this.pauseTimer = Timer.createLoopingTimer(this.animation.getDuration() + fightComp.blueprint.pauseTime, true);
    }

    public FightAi(Entity entity, MovementComp mover, FightComponent fightComp, Entity target, boolean oneOffAttack, boolean running, Classification prey) {
        this.entity = entity;
        this.runningAttack = running;
        this.target = target;
        this.mover = mover;
        this.oneOffAttack = oneOffAttack;
        this.fightComp = fightComp;
        this.huntComp = null;
        this.preyClass = prey;
        this.animation = fightComp.blueprint.getAnimation();
        this.pauseTimer = Timer.createLoopingTimer(this.animation.getDuration() + fightComp.blueprint.pauseTime, true);
    }

    public FightAi(Entity entity, MovementComp mover, FightComponent fightComp, Entity target, Classification prey, HuntComponent huntComp, boolean running) {
        this.entity = entity;
        this.target = target;
        this.mover = mover;
        this.runningAttack = running;
        this.fightComp = fightComp;
        this.oneOffAttack = false;
        this.huntComp = huntComp;
        this.preyClass = prey;
        this.animation = fightComp.blueprint.getAnimation();
        this.pauseTimer = Timer.createLoopingTimer(this.animation.getDuration() + fightComp.blueprint.pauseTime, true);
    }

    @Override
    public boolean carryOut() {
        if (!this.attacking && !this.isValidTarget()) {
            return true;
        }
        if (this.alertTimer.check()) {
            this.fightComp.alertPrey(this.preyClass);
        }
        if (this.attacking) {
            return this.doAttack();
        }
        if (!this.wasInRange) {
            this.checkTargetDistance();
        } else {
            boolean tooLong = this.chaseTimer.check();
            if (tooLong) {
                return true;
            }
        }
        this.chaseTarget();
        if (this.entity.getTransform().getPosition().y < GameManager.getWorld().getWaterHeight() - 0.2f) {
            return true;
        }
        return !this.attacking && this.target.isDead();
    }

    private boolean doAttack() {
        this.mover.block(true);
        boolean hit = this.animation.carryOut();
        if (hit) {
            this.chaseTimer.reset();
            this.dealDamage();
        }
        if (this.pauseTimer.check()) {
            this.mover.block(false);
            this.attacking = false;
            return this.oneOffAttack || GameManager.getGameMode() == GameMode.BUILD;
        }
        return false;
    }

    private void checkTargetDistance() {
        float disSquared = Vector3f.sub(this.target.getTransform().getPosition(), this.entity.getTransform().getPosition(), null).lengthSquared();
        boolean bl = this.wasInRange = disSquared < 30.25f;
        if (this.wasInRange) {
            this.chaseTimer.start();
        }
    }

    private void chaseTarget() {
        boolean reached = this.mover.goToTargetAndFace(this.target.getTransform().getPosition(), this.runningAttack, this.fightComp.blueprint.biteRange);
        if (reached && (!this.animation.needsNormalized() || this.mover.normalize())) {
            this.startAttack();
        }
    }

    private void startAttack() {
        this.animation.init(this.entity.getTransform(), this.target.getTransform());
        this.attacking = true;
    }

    private boolean dealDamage() {
        if (!this.isValidTarget()) {
            return true;
        }
        LifeComponent lifeComp = (LifeComponent)this.target.getComponent(ComponentType.LIFE);
        boolean dead = lifeComp.getHealth().takeDamage((int)((FloatTrait)this.fightComp.getTrait(0)).getValue(), this.entity);
        if (dead && this.huntComp != null) {
            this.huntComp.notifyKill(this.target);
        }
        return dead;
    }

    @Override
    public float getPriority() {
        return 100.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.fightComp;
    }

    @Override
    public void interrupt() {
        this.attacking = false;
        this.mover.block(false);
        this.wasInRange = false;
        this.chaseTimer.stop();
        this.pauseTimer.reset();
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    private boolean isValidTarget() {
        if (this.target.isDead() || this.target.isGrabbed()) {
            this.mover.block(false);
            return false;
        }
        PreyComp fleeComp = (PreyComp)((Object)this.target.getComponent(ComponentType.FLEE));
        return fleeComp == null || !fleeComp.isInvulnerable();
    }
}


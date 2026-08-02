/*
 * Decompiled with CFR 0.152.
 */
package flinging;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import breedingTraits.FloatTrait;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import death.ParticleDeath;
import events.EventData;
import events.EventManager;
import flinging.FlingingComponent;
import flying.FlyMovement;
import frogMovement.FrogMovement;
import gameManaging.GameManager;
import instances.Entity;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import session.GameMode;
import toolbox.Maths;
import toolbox.Transformation;

public class InsectCatchAi
implements Ai {
    private static final float DURATION = 0.4f;
    private static final ParticleSystem PARTICLES = new ParticleSystem(ColourPalette.BRIGHT_RED, false, new PointSpawn(), 10.0f, 1.0f, 0.3f, 3.0f, 0.05f);
    private static final float PRIORITY = 4.0f;
    private AiProvidingComponent component;
    private Entity target;
    private FrogMovement mover;
    private boolean launched = false;

    protected InsectCatchAi(Entity target, FrogMovement mover, FlingingComponent component) {
        this.component = component;
        this.mover = mover;
        this.target = target;
    }

    @Override
    public boolean carryOut() {
        if (this.target.isGrabbed() || this.target.isDead()) {
            return true;
        }
        if (this.launched) {
            return this.checkJumpStatus();
        }
        boolean reached = this.mover.goToTarget(this.target.getTransform().getPosition(), true, 1.0f);
        if (reached && this.mover.normalize()) {
            this.launchJump();
        }
        return false;
    }

    private void launchJump() {
        Vector3f targetPos = this.calcTargetPos();
        Vector3f frogPos = this.mover.getTransform().getPosition();
        Vector2f toTarget2d = new Vector2f(targetPos.x - frogPos.x, targetPos.z - frogPos.z);
        float horizVelocity = toTarget2d.length() / 0.4f;
        float upVelocity = (targetPos.y + 0.8f - frogPos.y) / 0.4f;
        toTarget2d.normalise();
        toTarget2d.scale(horizVelocity);
        this.mover.forceJump(new Vector3f(toTarget2d.x, upVelocity, toTarget2d.y));
        this.launched = true;
    }

    private Vector3f calcTargetPos() {
        MovementComp targetMovement = (MovementComp)((Object)this.target.getComponent(ComponentType.MOVEMENT));
        Vector3f aimPos = this.target.getTransform().getPosition();
        if (targetMovement instanceof FlyMovement) {
            Vector3f targetVel = new Vector3f(((FlyMovement)this.target.getComponent(ComponentType.MOVEMENT)).getVelocity());
            targetVel.scale(0.4f);
            return Vector3f.add(aimPos, targetVel, null);
        }
        return aimPos;
    }

    private boolean checkJumpStatus() {
        Vector3f frogPos;
        Vector3f targetPos = this.target.getTransform().getPosition();
        if (Vector3f.sub(targetPos, frogPos = this.mover.getTransform().getPosition(), null).lengthSquared() < 0.0225f) {
            this.killInsect();
            return true;
        }
        return this.mover.normalize();
    }

    private void killInsect() {
        if (GameManager.getGameMode() == GameMode.BUILD) {
            this.addNewFly(this.target);
        }
        EventManager.INSECT_CATCH.registerEvent(new EventData(), new String[0]);
        this.target.die(new ParticleDeath(this.target, PARTICLES), true);
    }

    private void addNewFly(Entity oldFly) {
        InformationComponent oldInfo = (InformationComponent)oldFly.getComponent(ComponentType.INFO);
        Vector3f newPos = oldInfo.getRandomInRangePoint();
        Transformation transform = oldFly.getTransform();
        newPos.y = GameManager.getWorld().getHeightOfTerrain(newPos.x, newPos.z);
        Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(newPos), Maths.RANDOM.nextFloat() * 360.0f, (FloatTrait)transform.getScaleTrait().duplicate());
        Entity newEntity = oldFly.duplicate(params);
        ((InformationComponent)newEntity.getComponent(ComponentType.INFO)).setBasePosition(oldInfo.getBasePosition());
        GameManager.getSession().getWorld().addInstance(newEntity, true);
    }

    @Override
    public float getPriority() {
        return 4.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.component;
    }

    @Override
    public void interrupt() {
    }

    @Override
    public String getDescription() {
        return "Chasing butterflies";
    }
}


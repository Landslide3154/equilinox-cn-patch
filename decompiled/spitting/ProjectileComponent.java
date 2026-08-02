/*
 * Decompiled with CFR 0.152.
 */
package spitting;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import events.EventData;
import events.EventManager;
import gameManaging.GameManager;
import health.LifeComponent;
import hunting.PreyComp;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import picking.EntityBox;
import resourceManagement.ParticleAtlasCache;
import spitting.ProjectileCompBlueprint;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class ProjectileComponent
extends Component {
    public static final float GRAVITY = -5.0f;
    private static final ParticleSystem particles = ProjectileComponent.createBigSplash();
    private static final ParticleSystem particlesSmall = ProjectileComponent.createSmallSplash();
    private Transformation transform;
    private Vector3f velocity = new Vector3f(0.0f, -1.0f, 0.0f);
    private Entity entity;
    private Entity target;
    private Entity attacker;
    private float speed = 0.001f;

    protected ProjectileComponent(ProjectileCompBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        super.update();
        this.updateVelocity();
        this.applyVelocity();
        this.updateRotation();
        if (this.checkHitAnimal()) {
            this.explodeOnAnimal();
        } else if (this.checkHitTerrain()) {
            this.explodeOnGround();
        } else {
            GameManager.getWorld().getEntityGrid().updateInGrid(this.entity);
        }
    }

    private void updateVelocity() {
        this.velocity.y += -5.0f * GameManager.getGameSeconds();
    }

    private void applyVelocity() {
        Vector3f scaledVelocity = new Vector3f(this.velocity);
        scaledVelocity.scale(GameManager.getGameSeconds());
        this.transform.increasePosition(scaledVelocity);
    }

    private void updateRotation() {
        Vector2f faceDir = new Vector2f(this.velocity.x, this.velocity.z);
        try {
            this.transform.setYRotation(Maths.calculateVectorRotationY(faceDir));
            this.transform.setXRotation((float)(-Math.toDegrees(Math.atan(this.velocity.y / this.speed))));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private boolean checkHitAnimal() {
        if (this.target == null) {
            return false;
        }
        EntityBox box = this.target.getBoundingBox();
        Transformation targetTrans = this.target.getTransform();
        Vector3f targetCenter = new Vector3f(targetTrans.getPosition());
        float halfHeight = box.getHeight() * 0.5f;
        targetCenter.y += halfHeight;
        float dis = Maths.getComparitableDistance(targetCenter, this.transform.getPosition());
        return dis < halfHeight * halfHeight;
    }

    private boolean checkHitTerrain() {
        Vector3f position = this.transform.getPosition();
        return position.y <= GameManager.getWorld().getHeightOfTerrain(position.x, position.z);
    }

    private void explodeOnAnimal() {
        particles.pulseParticles(this.transform.getPosition(), 1.0f);
        this.entity.die(null, true);
        LifeComponent life = (LifeComponent)this.target.getComponent(ComponentType.LIFE);
        if (life != null) {
            EventManager.SPIT_HIT.registerEvent(new EventData(), new String[0]);
            life.health.takeDamage(0, null);
        }
        if (this.attacker != null) {
            try {
                PreyComp fleeComp = (PreyComp)((Object)this.target.getComponent(ComponentType.FLEE));
                if (fleeComp != null) {
                    fleeComp.alertToDanger(this.attacker);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private void explodeOnGround() {
        particlesSmall.pulseParticles(this.transform.getPosition(), 1.0f);
        this.entity.die(null, true);
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
        ProjectileParams params = (ProjectileParams)bundle.getParameters(ComponentType.PROJECTILE);
        if (params != null) {
            this.velocity.set(params.velocity);
            this.speed = (float)Math.sqrt(this.velocity.x * this.velocity.x + this.velocity.z * this.velocity.z);
            this.target = params.target;
            this.attacker = params.attacker;
        }
        this.entity = bundle.getEntity();
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    private static ParticleSystem createBigSplash() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(16);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 12.0f, 1.2f, 0.5f, 0.44f, 0.028f);
        system.setDirection(Maths.UP, 0.2f);
        system.setScaleError(0.5f);
        system.setSpeedError(0.3f);
        system.setLifeError(0.3f);
        system.randomizeRotation();
        return system;
    }

    private static ParticleSystem createSmallSplash() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(16);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 5.0f, 0.5f, 0.35f, 0.4f, 0.02f);
        system.setDirection(Maths.UP, 0.2f);
        system.setScaleError(0.5f);
        system.setSpeedError(0.3f);
        system.setLifeError(0.3f);
        system.randomizeRotation();
        return system;
    }

    public static class ProjectileParams
    extends ComponentParams {
        private final Vector3f velocity;
        private final Entity target;
        private final Entity attacker;

        public ProjectileParams(Entity target, Vector3f velocity, Entity attacker) {
            super(ComponentType.PROJECTILE);
            this.velocity = velocity;
            this.target = target;
            this.attacker = attacker;
        }
    }
}


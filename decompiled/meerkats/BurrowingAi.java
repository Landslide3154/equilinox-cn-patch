/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.BaseMovement;
import baseMovement.MovementComp;
import blueprints.Blueprint;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import events.EventData;
import events.EventManager;
import gameManaging.GameManager;
import instances.Entity;
import meerkats.BurrowComponent;
import meerkats.BurrowStage;
import meerkats.TimeOutComponent;
import movementUtils.JumpToTarget;
import objectPools.Vec2Pool;
import objectPools.Vec3Pool;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import resourceManagement.BlueprintRepository;
import resourceManagement.ParticleAtlasCache;
import toolbox.Maths;
import toolbox.Transformation;
import world.GridSection;

public class BurrowingAi
implements Ai {
    private static final int BURROW_ID = 179;
    private static final float PRIORITY = 1.0f;
    private static final float HIGH_PRIORITY = 1.0E7f;
    private static final float DIG_DURATION = 2.0f;
    private static final float DIG_RADIUS = 0.15f;
    private static final float JUMP_RADIUS = 0.6f;
    private static final float JUMP_TIME = 0.5f;
    private static final float OUT_JUMP_TIME = 0.8f;
    private static final float OUT_JUMP_DIS = 0.3f;
    private static final float DIG_ROT = 80.0f;
    private static final float DIG_HEIGHT = -0.15f;
    private static final float BURROW_SPEED = 1.2f;
    private static final ParticleSystem DUST_PARTICLES = BurrowingAi.createDustParticleSystem();
    private static final ParticleSystem DUST_BURROW_PARTICLES = BurrowingAi.createDustBurrowParticleSystem();
    private static final ParticleSystem ROCK_PARTICLES = BurrowingAi.createRockParticleSystem();
    private static final ParticleSystem BURROW_PARTICLES = BurrowingAi.createRockBurrowParticleSystem();
    public static final ParticleSystem ROCK_JUMP_PARTICLES = BurrowingAi.createRockJumpParticleSystem();
    public static final ParticleSystem DUST_JUMP_PARTICLES = BurrowingAi.createDustJumpParticleSystem();
    public static final float DIG_PARTICLES_DIS = 55.0f;
    private static final float BURROW_PARTICLES_DIS = 35.0f;
    private static final float JUMP_PARTICLES_DIS = 40.0f;
    private final BurrowComponent burrowComp;
    private final MovementComp mover;
    private final Entity meerkat;
    private final Transformation transform;
    private Entity entranceBurrow;
    private final Vector3f entranceBurrowPos;
    private Entity exitBurrow;
    private Vector3f exitBurrowPos;
    private JumpToTarget jumper;
    private BurrowStage currentStage = BurrowStage.LOCATING;
    private float diggingTime = 0.0f;
    private boolean starting = true;
    private boolean jumpingDown = false;
    private Vector3f jumpAimPos = new Vector3f();
    private Vector3f jumpOutAimPos = new Vector3f();

    public BurrowingAi(Vector3f newBurrowPos, Entity meerkat, BurrowComponent burrowComp, MovementComp mover) {
        this.burrowComp = burrowComp;
        this.meerkat = meerkat;
        this.mover = mover;
        this.entranceBurrowPos = newBurrowPos;
        this.entranceBurrow = null;
        this.transform = meerkat.getTransform();
    }

    public BurrowingAi(Entity burrow, Entity meerkat, BurrowComponent burrowComp, MovementComp mover) {
        this.burrowComp = burrowComp;
        this.meerkat = meerkat;
        this.mover = mover;
        this.entranceBurrow = burrow;
        this.entranceBurrowPos = burrow.getTransform().getPosition();
        this.transform = meerkat.getTransform();
    }

    @Override
    public boolean carryOut() {
        if (this.starting) {
            this.checkIfStanding();
            this.starting = false;
        }
        if (this.jumpingDown) {
            this.updateJumpDown();
            return false;
        }
        if (this.currentStage == BurrowStage.LOCATING) {
            return this.moveToBurrowLocation();
        }
        if (this.currentStage == BurrowStage.ENTERING) {
            return this.enterBurrow();
        }
        if (this.currentStage == BurrowStage.BURROWING) {
            return this.burrowToExit();
        }
        return this.exitBurrow();
    }

    protected Entity getMeerkat() {
        return this.meerkat;
    }

    private void checkIfStanding() {
        if (this.transform.getRotX() < -70.0f) {
            this.jumpingDown = true;
            this.jumpAimPos.set(this.mover.getTransform().getPosition());
            this.jumpAimPos.y = this.mover.getTransform().getTerrainHeight();
            this.jumper = new JumpToTarget(this.mover.getTransform(), 0.3f, this.jumpAimPos, 0.0f);
        }
    }

    private void updateJumpDown() {
        this.mover.block(true);
        if (this.jumper.update(this.jumpAimPos)) {
            this.jumpingDown = false;
            this.mover.block(false);
        }
    }

    private boolean moveToBurrowLocation() {
        boolean reached = this.entranceBurrow == null ? this.mover.goToTarget(this.entranceBurrowPos, false, 0.15f) : this.mover.goToTargetAndFace(this.entranceBurrowPos, false, 0.6f);
        if (reached && this.mover.normalize()) {
            this.startEnteringStage();
        }
        return false;
    }

    private boolean enterBurrow() {
        if (this.entranceBurrow == null) {
            return this.digNewBurrow();
        }
        this.jumpIntoBurrow();
        return false;
    }

    private void startEnteringStage() {
        this.burrowComp.setUnderground(true);
        this.currentStage = BurrowStage.ENTERING;
        if (this.entranceBurrow == null) {
            this.entranceBurrowPos.set(this.meerkat.getTransform().getPosition());
        } else {
            this.jumpAimPos.set(this.entranceBurrowPos);
            this.jumpAimPos.y -= 0.1f;
            this.jumper = new JumpToTarget(this.transform, 0.5f, this.jumpAimPos, 90.0f);
        }
    }

    private boolean digNewBurrow() {
        if (BurrowComponent.findLocalBurrow(this.meerkat.getCurrentGridSection()) != null) {
            return true;
        }
        this.diggingTime += GameManager.getGameSeconds();
        if (this.diggingTime < 2.0f) {
            this.doDiggingIn();
            return false;
        }
        this.diggingTime = 0.0f;
        this.entranceBurrow = this.addBurrowObject(this.entranceBurrowPos);
        this.startBurrowingStage();
        return false;
    }

    private void doDiggingIn() {
        this.doDiggingParticles(this.entranceBurrowPos);
        float rotSpeed = 40.0f;
        this.transform.increaseRotation(rotSpeed * GameManager.getGameSeconds(), 0.0f, 0.0f);
        float halfDigDuration = 1.0f;
        if (this.diggingTime > halfDigDuration) {
            float sinkSpeed = -0.15f / halfDigDuration;
            this.transform.increasePosition(0.0f, sinkSpeed * GameManager.getGameSeconds(), 0.0f);
        }
    }

    private void jumpIntoBurrow() {
        this.mover.block(true);
        if (this.jumper.update(this.jumpAimPos)) {
            this.doJumpParticles(this.entranceBurrowPos);
            this.startBurrowingStage();
        }
    }

    private void startBurrowingStage() {
        GridSection section = this.getTargetSection();
        this.exitBurrow = BurrowComponent.findLocalBurrow(section);
        if (this.exitBurrow == null) {
            this.exitBurrowPos = BurrowComponent.getSuitableBurrowLocation(section, false);
        } else {
            ((TimeOutComponent)this.exitBurrow.getComponent(ComponentType.TIME_OUT)).reset();
            this.exitBurrowPos = this.exitBurrow.getTransform().getPosition();
        }
        this.currentStage = BurrowStage.BURROWING;
    }

    private boolean burrowToExit() {
        this.mover.block(true);
        Vector3f direction = Vec3Pool.get();
        Vector3f.sub(this.exitBurrowPos, this.transform.getPosition(), direction);
        Vector2f direction2d = Vec2Pool.get(direction.x, direction.z);
        Vec3Pool.release(direction);
        float distance = direction2d.length();
        if (distance != 0.0f) {
            direction2d.scale(1.0f / distance);
        } else {
            direction2d.set(1.0f, 0.0f);
        }
        float disToMove = 1.2f * GameManager.getGameSeconds();
        if (distance < disToMove) {
            this.transform.setXPosition(this.exitBurrowPos.x);
            this.transform.setZPosition(this.exitBurrowPos.z);
            this.startExitStage(direction2d);
            Vec2Pool.release(direction2d);
            GameManager.getWorld().getEntityGrid().updateInGrid(this.meerkat);
            return false;
        }
        direction2d.scale(disToMove);
        this.transform.increasePosition(direction2d.x, 0.0f, direction2d.y);
        Vector3f meerkatPos = this.transform.getPosition();
        float height = GameManager.getWorld().getHeightOfTerrain(meerkatPos.x, meerkatPos.z);
        this.transform.setYPosition(height - 0.2f);
        Vec2Pool.release(direction2d);
        Vector3f particlePos = Vec3Pool.get(meerkatPos);
        particlePos.y = height;
        this.doBurrowParticles(particlePos);
        Vec3Pool.release(particlePos);
        GameManager.getWorld().getEntityGrid().updateInGrid(this.meerkat);
        return false;
    }

    private void startExitStage(Vector2f dir) {
        if (!BurrowComponent.isSuitableLocation(this.exitBurrowPos)) {
            this.startBurrowingStage();
            return;
        }
        this.currentStage = BurrowStage.EXITING;
        if (this.exitBurrow != null) {
            this.startExitJump(dir);
        }
    }

    private void startExitJump(Vector2f dir) {
        this.doJumpParticles(this.exitBurrowPos);
        float rotY = Maths.calculateVectorRotationY(dir);
        this.transform.setXRotation(-90.0f);
        ((BaseMovement)this.mover).setActualRotY(rotY);
        this.jumpOutAimPos.set(this.exitBurrowPos);
        this.jumpOutAimPos.x += dir.x * 0.3f;
        this.jumpOutAimPos.z += dir.y * 0.3f;
        this.jumpOutAimPos.y = GameManager.getWorld().getHeightOfTerrain(this.jumpOutAimPos.x, this.jumpOutAimPos.z);
        this.jumper = new JumpToTarget(this.transform, 0.8f, this.jumpOutAimPos, 0.0f);
    }

    private boolean exitBurrow() {
        if (this.exitBurrow == null) {
            return this.digOutOfBurrow();
        }
        return this.jumpOutOfBurrow();
    }

    private boolean digOutOfBurrow() {
        this.diggingTime += GameManager.getGameSeconds();
        if (this.diggingTime < 2.0f) {
            this.doDiggingParticles(this.exitBurrowPos);
            return false;
        }
        this.diggingTime = 0.0f;
        if (!this.checkForOtherNearbyExit()) {
            this.exitBurrow = this.addBurrowObject(this.exitBurrowPos);
            this.startExitJump(new Vector2f(0.0f, 1.0f));
        }
        return false;
    }

    private boolean checkForOtherNearbyExit() {
        this.exitBurrow = BurrowComponent.findLocalBurrow(this.meerkat.getCurrentGridSection());
        if (this.exitBurrow == null) {
            return false;
        }
        this.exitBurrowPos = this.exitBurrow.getTransform().getPosition();
        this.currentStage = BurrowStage.BURROWING;
        return true;
    }

    private boolean jumpOutOfBurrow() {
        this.mover.block(true);
        if (this.jumper.update(this.jumpOutAimPos)) {
            this.mover.block(false);
            return true;
        }
        return false;
    }

    private GridSection getTargetSection() {
        InformationComponent info = (InformationComponent)this.meerkat.getComponent(ComponentType.INFO);
        GridSection currentSection = this.meerkat.getCurrentGridSection();
        GridSection targetSection = null;
        while (targetSection == null || targetSection == currentSection) {
            Vector3f inRangePoint = info.getRandomInRangePoint();
            targetSection = GameManager.getWorld().getEntityGrid().getSectionAtPosition(inRangePoint.x, inRangePoint.z);
        }
        return targetSection;
    }

    private Entity addBurrowObject(Vector3f pos) {
        EventManager.HOLE_DIG.registerEvent(new EventData(), new String[0]);
        Blueprint burrowModel = BlueprintRepository.getBlueprint(179);
        Transformation.TransformBlueprint transform = (Transformation.TransformBlueprint)burrowModel.getComponent(ComponentType.TRANSFORM);
        Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(pos), Maths.RANDOM.nextFloat() * 360.0f, transform.generateRandomScale());
        Entity burrow = burrowModel.createInstance(params);
        GameManager.getSession().getWorld().addInstance(burrow, true);
        return burrow;
    }

    @Override
    public float getPriority() {
        return this.currentStage == BurrowStage.LOCATING ? 1.0f : 1.0E7f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.burrowComp;
    }

    @Override
    public void interrupt() {
        this.burrowComp.setUnderground(false);
        this.diggingTime = 0.0f;
        this.mover.block(false);
        this.currentStage = BurrowStage.LOCATING;
        this.jumpingDown = false;
    }

    @Override
    public String getDescription() {
        return this.currentStage.toString();
    }

    private static ParticleSystem createRockParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(9);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 15.0f, 1.8f, 0.35f, 0.5f, 0.04f);
        system.setDirection(Maths.UP, 0.2f);
        system.setScaleError(0.5f);
        system.setSpeedError(0.5f);
        system.setLifeError(0.3f);
        system.randomizeRotation();
        return system;
    }

    private static ParticleSystem createRockBurrowParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(9);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 40.0f, 1.3f, 0.35f, 0.43f, 0.075f);
        system.setDirection(Maths.UP, 0.2f);
        system.setScaleError(0.7f);
        system.setSpeedError(0.7f);
        system.setLifeError(0.6f);
        system.randomizeRotation();
        return system;
    }

    private static ParticleSystem createRockJumpParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(9);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 25.0f, 1.85f, 0.35f, 1.3f, 0.06f);
        system.setDirection(Maths.UP, 0.1f);
        system.setScaleError(0.5f);
        system.setSpeedError(0.7f);
        system.setLifeError(0.6f);
        system.randomizeRotation();
        return system;
    }

    private static ParticleSystem createDustJumpParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(14);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 3.0f, 0.65f, 0.01f, 1.2f, 0.9f);
        system.setDirection(Maths.UP, 0.1f);
        system.setLifeError(0.2f);
        system.setSpeedError(0.2f);
        system.randomizeRotation();
        return system;
    }

    private static ParticleSystem createDustBurrowParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(14);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 6.5f, 0.28f, 0.01f, 1.5f, 0.7f);
        system.setDirection(Maths.UP, 0.25f);
        system.setLifeError(0.3f);
        system.randomizeRotation();
        return system;
    }

    private static ParticleSystem createDustParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(14);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 4.5f, 0.28f, 0.01f, 2.4f, 1.6f);
        system.setDirection(Maths.UP, 0.25f);
        system.setLifeError(0.3f);
        system.randomizeRotation();
        return system;
    }

    private void doDiggingParticles(Vector3f pos) {
        if (this.meerkat.getCurrentGridSection().getDistanceFromCam() > 55.0f) {
            return;
        }
        DUST_PARTICLES.generateParticles(pos, 1.0f);
        ROCK_PARTICLES.generateParticles(pos, 1.0f);
    }

    private void doBurrowParticles(Vector3f pos) {
        if (this.meerkat.getCurrentGridSection().getDistanceFromCam() > 35.0f) {
            return;
        }
        BURROW_PARTICLES.generateParticles(pos, 0.6f);
        DUST_BURROW_PARTICLES.generateParticles(pos, 0.6f);
    }

    private void doJumpParticles(Vector3f pos) {
        if (this.meerkat.getCurrentGridSection().getDistanceFromCam() > 40.0f) {
            return;
        }
        ROCK_JUMP_PARTICLES.pulseParticles(pos, 1.0f);
        DUST_JUMP_PARTICLES.pulseParticles(pos, 1.0f);
    }
}


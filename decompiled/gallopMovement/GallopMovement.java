/*
 * Decompiled with CFR 0.152.
 */
package gallopMovement;

import baseMovement.BaseMovement;
import baseMovement.MoveUtils;
import breedingTraits.FloatTrait;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import gallopMovement.GallopMovementBlueprint;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

public class GallopMovement
extends BaseMovement {
    private static final float DECELERATION = 0.95f;
    private static final float MIN_BOUNCE_FACTOR = 0.4f;
    private static final float VEL_MATCH_FACTOR = 2.5f;
    private static final float BACK_SINK_CUT_OFF = -0.008f;
    private final GallopMovementBlueprint blueprint;
    private boolean bouncing = false;
    private boolean inAirStage = false;
    private boolean rotationLocked = false;
    private Vector3f velocity = new Vector3f();
    private MeshComponent mesh;
    private final Vector4f frontPoint;
    private final Vector4f backPoint;
    private float currentSpeed = 0.0f;
    private boolean movedLastFrame = false;

    protected GallopMovement(GallopMovementBlueprint blueprint) {
        super(blueprint, 180.0f, blueprint.getRunFactor());
        this.blueprint = blueprint;
        this.frontPoint = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
        this.backPoint = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public boolean normalize() {
        return !this.inAirStage && !this.bouncing;
    }

    @Override
    protected void updateMovement(boolean targetRotReached) {
        if (super.isMoving()) {
            this.movedLastFrame = true;
        } else if (this.movedLastFrame && !super.isMoving()) {
            this.movedLastFrame = false;
            if (!this.bouncing) {
                this.fixLandedBodyPosition();
            }
        }
        if (super.isMoving() && !this.bouncing) {
            this.startBounce();
        }
        if (this.bouncing) {
            this.updateContactPoints();
            this.updateBounce();
        }
    }

    @Override
    public void create(ComponentBundle bundle) {
        super.create(bundle);
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
    }

    private void updateContactPoints() {
        this.frontPoint.z = this.blueprint.getFrontZ(this.mesh.getCurrentStageNumber());
        this.backPoint.z = this.blueprint.getBackZ(this.mesh.getCurrentStageNumber());
    }

    private void startBounce() {
        double rot = Math.toRadians(this.getTransform().getRotY());
        float scaleFactor = super.getSizeFactor();
        scaleFactor *= scaleFactor;
        float lifeFactor = super.getLifeFactor(0.4f);
        this.currentSpeed = super.getBaseSpeed() * super.getRunFactor() * lifeFactor * scaleFactor;
        float dx = this.currentSpeed * (float)Math.sin(rot);
        float dz = this.currentSpeed * (float)Math.cos(rot);
        float power = this.getBouncePower() * super.getSizeFactor() * lifeFactor;
        this.velocity.set(dx, power, dz);
        this.bouncing = true;
        this.inAirStage = true;
    }

    private void updateBounce() {
        if (this.inAirStage) {
            this.updateFlight();
        } else {
            this.updateLanding();
        }
        if (this.bouncing) {
            this.updateBodyRotation();
        }
    }

    private void updateBodyRotation() {
        float targetBodyRot = 0.0f;
        if (this.inAirStage) {
            targetBodyRot = (float)Math.toDegrees(-Math.atan2(this.velocity.y, this.currentSpeed * 2.5f / super.getLifeFactor(0.4f)));
            targetBodyRot = Maths.clamp(targetBodyRot, -40.0f, 40.0f);
        }
        if (!this.rotationLocked) {
            float rotX = this.getTransform().getRotX();
            rotX -= GameManager.getGameSeconds() * this.blueprint.upRotSpeed;
            if (this.inAirStage && rotX <= targetBodyRot) {
                rotX = targetBodyRot;
                this.rotationLocked = true;
            }
            this.getTransform().setXRotation(rotX);
        }
        if (this.rotationLocked) {
            this.getTransform().setXRotation(targetBodyRot);
        }
    }

    private void updateFlight() {
        MoveUtils.applyVelocityWithGravity(this.velocity, this.getTransform(), this.blueprint.gravityFactor, GameManager.getGameSeconds());
        float altitude = this.calculateHeightAboveContact(this.frontPoint);
        if (altitude <= 0.0f) {
            this.getTransform().increasePosition(0.0f, -altitude, 0.0f);
            if (this.velocity.y < 0.0f) {
                this.inAirStage = false;
                this.rotationLocked = false;
            }
        }
    }

    private void updateLanding() {
        this.velocity.scale(0.95f);
        MoveUtils.applyVelocity(this.velocity, this.getTransform(), GameManager.getGameSeconds());
        Vector4f frontWorld = this.convertToWorldPos(this.frontPoint);
        float frontHeight = this.getHeightAboveTerrain(frontWorld.x, frontWorld.y, frontWorld.z);
        this.getTransform().increasePosition(0.0f, -frontHeight, 0.0f);
        Vector4f backWorld = this.convertToWorldPos(this.backPoint);
        float backHeight = this.getHeightAboveTerrain(backWorld.x, backWorld.y, backWorld.z);
        if (backHeight <= 0.0f) {
            this.bouncing = false;
            if (!super.isMoving() && backHeight < -0.008f) {
                this.fixLandedBodyPosition();
            }
        }
    }

    private void fixLandedBodyPosition() {
        Vector3f centerPos = this.getTransform().getPosition();
        float backHeight = this.getHeightAboveTerrain(centerPos.x, centerPos.y, centerPos.z);
        this.getTransform().increasePosition(0.0f, -backHeight, 0.0f);
        Vector4f frontWorld = this.convertToWorldPos(this.frontPoint);
        float frontHeight = GameManager.getWorld().getHeightOfTerrain(frontWorld.x, frontWorld.z);
        Vector3f frontGrounded = new Vector3f(frontWorld.x, frontHeight, frontWorld.z);
        Vector3f v1 = Vector3f.sub(new Vector3f(frontWorld), centerPos, null);
        Vector3f v2 = Vector3f.sub(frontGrounded, centerPos, null);
        try {
            v1.normalise();
            v2.normalise();
            float dotProcuct = Vector3f.dot(v1, v2);
            float angle = (float)Math.toDegrees(Math.acos(dotProcuct));
            this.getTransform().increaseRotation(angle, 0.0f, 0.0f);
        }
        catch (Exception e) {
            System.err.println("GALLOPING ISSUE - ERROR*********************************");
            this.getTransform().increaseRotation(30.0f, 0.0f, 0.0f);
        }
    }

    private float calculateHeightAboveContact(Vector4f contactPoint) {
        Vector4f worldPoint = this.convertToWorldPos(contactPoint);
        return this.getHeightAboveTerrain(worldPoint.x, worldPoint.y, worldPoint.z);
    }

    private Vector4f convertToWorldPos(Vector4f localPoint) {
        Matrix4f modelMatrix = this.getTransform().getModelMatrix();
        return Matrix4f.transform(modelMatrix, localPoint, null);
    }

    private float getHeightAboveTerrain(float posX, float posY, float posZ) {
        float terrainHeight = GameManager.getWorld().getHeightOfTerrain(posX, posZ);
        return posY - terrainHeight;
    }

    private float getBouncePower() {
        FloatTrait bounceTrait = (FloatTrait)super.getTrait(1);
        return bounceTrait.getValue();
    }
}


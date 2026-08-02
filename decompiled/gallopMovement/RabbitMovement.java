/*
 * Decompiled with CFR 0.152.
 */
package gallopMovement;

import baseMovement.BaseMovement;
import baseMovement.MoveUtils;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import gallopMovement.RabbitMovementBlueprint;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

public class RabbitMovement
extends BaseMovement {
    private static final float DECELERATION = 0.95f;
    private static final float MIN_BOUNCE_FACTOR = 0.4f;
    private static final float BACK_SINK_CUT_OFF = -0.006f;
    private final RabbitMovementBlueprint blueprint;
    private MeshComponent mesh;
    private boolean bouncing = false;
    private boolean inAirStage = false;
    private Vector3f velocity = new Vector3f();
    private final Vector4f frontPoint;
    private final Vector4f backPoint;
    private boolean movedLastFrame = false;

    protected RabbitMovement(RabbitMovementBlueprint blueprint) {
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
        this.checkIfJustStopped();
        if (super.isMoving() && !this.bouncing) {
            this.startBounce();
        }
        if (this.bouncing) {
            this.updateContactPoints();
            this.updateBounce();
        }
    }

    private void checkIfJustStopped() {
        if (super.isMoving()) {
            this.movedLastFrame = true;
        } else if (this.movedLastFrame && !super.isMoving()) {
            this.movedLastFrame = false;
            if (!this.bouncing) {
                this.fixLandedBodyPosition();
            }
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
        float speed = super.getBaseSpeed() * super.getTotalSpeedFactor();
        float dx = speed * (float)Math.sin(rot);
        float dz = speed * (float)Math.cos(rot);
        float power = this.blueprint.bouncePower * super.getSizeFactor() * super.getLifeFactor(0.4f);
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
    }

    private void updateFlight() {
        MoveUtils.applyVelocityWithGravity(this.velocity, this.getTransform(), GameManager.getGameSeconds());
        this.getTransform().increaseRotation(GameManager.getGameSeconds() * this.blueprint.upRotSpeed, 0.0f, 0.0f);
        float altitude = this.calculateHeightAboveContact(this.frontPoint);
        if (altitude <= 0.0f) {
            this.getTransform().increasePosition(0.0f, -altitude, 0.0f);
            if (this.velocity.y < 0.0f) {
                this.inAirStage = false;
            }
        }
    }

    private void updateLanding() {
        this.velocity.scale(0.95f);
        MoveUtils.applyVelocity(this.velocity, this.getTransform(), GameManager.getGameSeconds());
        this.getTransform().increaseRotation(-GameManager.getGameSeconds() * this.blueprint.downRotSpeed, 0.0f, 0.0f);
        float frontHeight = this.calculateHeightAboveContact(this.frontPoint);
        this.getTransform().increasePosition(0.0f, -frontHeight, 0.0f);
        float backHeight = this.calculateHeightAboveContact(this.backPoint);
        if (backHeight <= 0.0f) {
            this.bouncing = false;
            if (!super.isMoving() && backHeight < -0.006f) {
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

    private Vector4f convertToWorldPos(Vector4f localPoint) {
        Matrix4f modelMatrix = this.getTransform().getModelMatrix();
        return Matrix4f.transform(modelMatrix, localPoint, null);
    }

    private float getHeightAboveTerrain(float posX, float posY, float posZ) {
        float terrainHeight = GameManager.getWorld().getHeightOfTerrain(posX, posZ);
        return posY - terrainHeight;
    }

    private float calculateHeightAboveContact(Vector4f contactPoint) {
        Vector4f worldPoint = Maths.VEC4;
        Matrix4f modelMatrix = this.getTransform().getModelMatrix();
        Matrix4f.transform(modelMatrix, contactPoint, worldPoint);
        float terrainHeight = GameManager.getWorld().getHeightOfTerrain(worldPoint.x, worldPoint.z);
        return worldPoint.y - terrainHeight;
    }
}


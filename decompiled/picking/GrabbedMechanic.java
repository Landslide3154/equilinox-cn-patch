/*
 * Decompiled with CFR 0.152.
 */
package picking;

import basics.DisplayManager;
import classification.Classifier;
import gameManaging.GameManager;
import instances.Entity;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import picking.EntityBox;
import toolbox.MousePicker;
import toolbox.Transformation;

public class GrabbedMechanic {
    private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
    private static final Vector4f FORWARDS = new Vector4f(0.0f, 0.0f, -1.0f, 1.0f);
    private static final int BOUNCES = 2;
    private static final float BOUNCE_DAMPER = 0.5f;
    private static final float FALL_GRAVITY = 40.0f;
    private static final float HANG_HEIGHT_FACTOR = 1.2f;
    private static final float MIN_HANG_HEIGHT = 1.0f;
    private static final float DROP_INIT_SPEED = 0.35f;
    private static final float MAX_POS_CHANGE = 0.1f;
    private static final float MIN_POS_CHANGE = 0.05f;
    private static final float ROPE_EXTENTION = 1.1f;
    private final float ropeLength;
    private float swingGravity;
    private Transformation transform;
    private EntityBox bounds;
    private MousePicker picker;
    private boolean onTheFloor = false;
    private boolean inWater = false;
    private boolean dropped = false;
    private float hangHeight;
    private int bounces = 2;
    private Vector3f hangPoint = new Vector3f();
    private Vector3f returnPosition = new Vector3f();
    private Vector3f velocity = new Vector3f(0.0f, 0.0f, 0.0f);
    private Vector3f previousPos = new Vector3f(0.0f, 0.0f, 0.0f);
    private Vector3f hangPointChange = new Vector3f();
    private boolean isBouncer;

    public GrabbedMechanic(Entity entity) {
        this.isBouncer = entity.getBlueprint().getClassification().isTypeOf(Classifier.getAnimalClassification());
        this.bounds = entity.getBoundingBox();
        this.ropeLength = this.bounds.getHeight() * 1.1f;
        this.transform = entity.getTransform();
        this.swingGravity = 2.0f + this.bounds.getSizes().y * 1.8f;
        this.picker = GameManager.getTerrainPicker();
        this.hangHeight = Math.max(1.0f, 0.4f + this.bounds.getSizes().y * 1.2f);
        this.picker.setOffsetPointHeight(this.hangHeight);
        this.returnPosition.set(this.transform.getPosition());
        this.transform.setPosition(this.hangPoint.x, this.hangPoint.y - this.ropeLength, this.hangPoint.z);
    }

    public void returnToStartPosition() {
        this.transform.setPosition(this.returnPosition.x, this.returnPosition.y, this.returnPosition.z);
        this.onTheFloor = true;
    }

    public void drop() {
        this.dropped = true;
        this.bounces = this.isBouncer ? 2 : 0;
        this.velocity.y = 5.0f + this.hangHeight * 0.35f;
        this.velocity.x = 0.0f;
        this.velocity.z = 0.0f;
    }

    public boolean isOnFloor() {
        return this.onTheFloor;
    }

    public boolean isInWater() {
        return this.inWater;
    }

    public void update() {
        if (!this.dropped) {
            this.swing();
        } else {
            this.fall();
            this.checkLanded();
        }
    }

    private void checkLanded() {
        float height;
        Vector3f pos = this.transform.getPosition();
        if (this.isBouncer && pos.y < GameManager.getWorld().getWaterHeight()) {
            this.onTheFloor = true;
            this.inWater = true;
        }
        if (pos.y <= (height = GameManager.getWorld().getHeightOfTerrain(pos.x, pos.z))) {
            this.velocity.y = -this.velocity.y * 0.5f;
            this.transform.setPosition(pos.x, height, pos.z);
            if (this.bounces == 0) {
                this.onTheFloor = true;
            }
            --this.bounces;
        }
    }

    private void fall() {
        float delta = DisplayManager.getDeltaSeconds();
        this.velocity.y -= delta * 40.0f;
        this.transform.increasePosition(this.velocity.x * delta, this.velocity.y * delta, this.velocity.z * delta);
    }

    private void swing() {
        Vector3f offsetPoint = this.picker.getCurrentOffsetPoint();
        if (offsetPoint != null) {
            Vector3f start = new Vector3f(this.hangPoint);
            this.hangPoint.set(offsetPoint);
            Vector3f.sub(offsetPoint, start, this.hangPointChange);
            this.updatePosition();
        }
    }

    private void updatePosition() {
        this.velocity.y -= DisplayManager.getDeltaSeconds() * this.swingGravity;
        this.cancelOutUnnaturalMovement();
        this.extrapolateMovement();
        Vector3f toCenter = Vector3f.sub(this.hangPoint, this.transform.getPosition(), null);
        this.limitDistanceFromPivot(toCenter);
        this.updateModelRotation(toCenter);
    }

    private void cancelOutUnnaturalMovement() {
        if (this.hangPointChange.length() > 0.1f) {
            float moveDis = this.hangPointChange.length() - 0.1f;
            this.hangPointChange.normalise();
            this.hangPointChange.scale(moveDis);
            Vector3f.add(this.hangPointChange, this.transform.getPosition(), this.transform.getPosition());
        } else if (this.hangPointChange.length() < 0.05f) {
            Vector3f.add(this.hangPointChange, this.transform.getPosition(), this.transform.getPosition());
        }
    }

    private void extrapolateMovement() {
        this.previousPos.set(this.transform.getPosition());
        this.transform.increasePosition(this.velocity.x, this.velocity.y, this.velocity.z);
    }

    private void limitDistanceFromPivot(Vector3f toCenter) {
        float length = toCenter.length();
        if (length > this.ropeLength) {
            float dis = length - this.ropeLength;
            toCenter.normalise();
            toCenter.scale(dis);
            Vector3f.add(toCenter, this.transform.getPosition(), this.transform.getPosition());
        }
        this.velocity.set(Vector3f.sub(this.transform.getPosition(), this.previousPos, null));
    }

    private void updateModelRotation(Vector3f toCenter) {
        toCenter.normalise();
        Matrix4f mat = new Matrix4f();
        mat.rotate((float)Math.toRadians(this.transform.getRotY()), Y_AXIS);
        Vector4f forwards = Matrix4f.transform(mat, FORWARDS, null);
        this.transform.updateModelMatrix(toCenter, new Vector3f(forwards));
    }
}


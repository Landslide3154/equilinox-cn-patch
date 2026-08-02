/*
 * Decompiled with CFR 0.152.
 */
package bounceMovement;

import bounceMovement.BounceBaseMovement;
import bounceMovement.FlouncerBlueprint;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class FlouncerMovement
extends BounceBaseMovement {
    private final FlouncerBlueprint blueprint;
    private Vector3f targetRotation = new Vector3f();

    protected FlouncerMovement(FlouncerBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    protected void updateInFlight(float height) {
        this.updateRotation(height);
    }

    @Override
    protected void startBounce() {
        this.randomizeTargetRotation();
    }

    @Override
    protected void endBounce() {
    }

    private void updateRotation(float height) {
        float progress = height / this.blueprint.standardHeight;
        float xRot = Maths.interpolate(0.0f, this.targetRotation.x, progress);
        float zRot = Maths.interpolate(0.0f, this.targetRotation.z, progress);
        super.getTransform().setXRotation(xRot);
        super.getTransform().setZRotation(zRot);
    }

    private void randomizeTargetRotation() {
        float doubleBounceRot = this.blueprint.bounceRotation * 2.0f;
        this.targetRotation.x = Maths.RANDOM.nextFloat() * doubleBounceRot - this.blueprint.bounceRotation;
        this.targetRotation.z = Maths.RANDOM.nextFloat() * doubleBounceRot - this.blueprint.bounceRotation;
    }
}


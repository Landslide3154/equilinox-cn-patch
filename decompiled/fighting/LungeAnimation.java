/*
 * Decompiled with CFR 0.152.
 */
package fighting;

import fighting.AttackAnimation;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;

public class LungeAnimation
implements AttackAnimation {
    private static final float DAMAGE_TIME = 0.5f;
    private static final float TARGET_ROT = -30.0f;
    private static final float DURATION = 0.35f;
    private static final float LUNGE_DIS = 0.3f;
    private float originalRot;
    private Vector3f originalPos;
    private Vector3f targetPos;
    private Transformation transform;
    private float time = 0.0f;
    private boolean damaged = false;

    protected LungeAnimation() {
    }

    @Override
    public void init(Transformation attackerTransform, Transformation targetTransform) {
        this.time = 0.0f;
        this.damaged = false;
        this.transform = attackerTransform;
        this.originalPos = new Vector3f(attackerTransform.getPosition());
        this.originalRot = attackerTransform.getRotX();
        this.calcTargetPos(targetTransform.getPosition());
    }

    @Override
    public boolean carryOut() {
        if (this.time >= 1.0f) {
            return false;
        }
        this.time += GameManager.getGameSeconds() / 0.35f;
        float blend = 1.0f - Math.abs(Maths.fakeSin(-1.0f, 1.0f, 0.25f + this.time * 0.5f));
        this.transform.setXRotation(Maths.interpolate(this.originalRot, -30.0f, blend));
        Vector3f newPos = Maths.interpolate(this.originalPos, this.targetPos, blend);
        this.transform.setPosition(newPos);
        return this.checkDamage();
    }

    private boolean checkDamage() {
        if (!this.damaged && this.time >= 0.5f) {
            this.damaged = true;
            return true;
        }
        return false;
    }

    private void calcTargetPos(Vector3f targetPos) {
        Vector3f difference = Vector3f.sub(targetPos, this.originalPos, null);
        difference.normalise();
        difference.scale(0.3f);
        this.targetPos = Vector3f.add(this.originalPos, difference, null);
    }

    @Override
    public boolean needsNormalized() {
        return true;
    }

    @Override
    public float getDuration() {
        return 0.35f;
    }
}


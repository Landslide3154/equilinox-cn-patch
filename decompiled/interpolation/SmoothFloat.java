/*
 * Decompiled with CFR 0.152.
 */
package interpolation;

import toolbox.Maths;

public class SmoothFloat {
    private float agility;
    private float target;
    private float actual;

    public SmoothFloat(float initialValue, float agility) {
        this.target = initialValue;
        this.actual = initialValue;
        this.agility = agility;
    }

    public void update(float delta) {
        float offset = this.target - this.actual;
        float factor = delta * this.agility;
        this.actual = factor > 1.0f ? this.target : (this.actual += offset * factor);
    }

    public void setAgility(float agil) {
        this.agility = agil;
    }

    public void clampTarget(float min, float max) {
        this.target = Maths.clamp(this.target, min, max);
    }

    public void cancelTarget() {
        this.target = this.actual;
    }

    public void invertActual() {
        this.actual = -this.actual;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public void increaseTarget(float increase) {
        this.target += increase;
    }

    public void force(float newValue) {
        this.actual = newValue;
        this.target = newValue;
    }

    public void forceOnlyActualValue(float newValue) {
        float difference = this.target - this.actual;
        this.actual = newValue;
        this.target = this.actual + difference;
    }

    public void instantIncrease(float increase) {
        this.actual += increase;
    }

    public boolean reached() {
        return Math.abs(this.actual - this.target) < 0.001f;
    }

    public void increaseAll(float increase) {
        this.actual += increase;
        this.target += increase;
    }

    public float get() {
        return this.actual;
    }
}


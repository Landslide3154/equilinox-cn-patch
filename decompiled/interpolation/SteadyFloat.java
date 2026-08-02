/*
 * Decompiled with CFR 0.152.
 */
package interpolation;

public class SteadyFloat {
    private float maxChangePerSec;
    private float target;
    private float current;
    private boolean reached = true;

    public SteadyFloat(float initialValue, float maxChangePerSec) {
        this.current = initialValue;
        this.target = initialValue;
        this.maxChangePerSec = maxChangePerSec;
    }

    public void setTarget(float target) {
        this.target = target;
        this.reached = false;
    }

    public boolean isReached() {
        return this.reached;
    }

    public void setMaxChangePerSec(float maxChange) {
        this.maxChangePerSec = maxChange;
    }

    public float update(float delta) {
        if (this.reached) {
            return this.current;
        }
        float difference = this.target - this.current;
        float maxAllowedChange = this.maxChangePerSec * delta;
        if (Math.abs(difference) <= maxAllowedChange) {
            this.reached = true;
            this.current = this.target;
        } else {
            this.current += maxAllowedChange * Math.signum(difference);
        }
        return this.current;
    }

    public float get() {
        return this.current;
    }
}


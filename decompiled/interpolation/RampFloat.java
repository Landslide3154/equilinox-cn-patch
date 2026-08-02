/*
 * Decompiled with CFR 0.152.
 */
package interpolation;

public class RampFloat {
    private final float max;
    private final float min;
    private final float changePerSec;
    private float value = 0.0f;

    public RampFloat(float min, float max, float changePerSec) {
        this.min = min;
        this.max = max;
        this.changePerSec = changePerSec;
    }

    public float get() {
        return this.value;
    }

    public void ramp(float delta) {
        this.value += this.changePerSec * delta;
        this.value = Math.min(this.value, this.max);
    }

    public void rampDown(float delta) {
        this.value -= this.changePerSec * delta;
        this.value = Math.max(this.value, this.min);
    }
}


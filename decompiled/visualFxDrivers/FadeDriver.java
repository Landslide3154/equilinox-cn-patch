/*
 * Decompiled with CFR 0.152.
 */
package visualFxDrivers;

import visualFxDrivers.ValueDriver;

public class FadeDriver
extends ValueDriver {
    private float start;
    private float end;
    private float peak;

    public FadeDriver(float peak, float start, float end, float duration) {
        super(duration);
        this.peak = peak;
        this.start = start;
        this.end = end;
    }

    public float getStart() {
        return this.start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public float getEnd() {
        return this.end;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public float getPeak() {
        return this.peak;
    }

    public void setPeak(float peak) {
        this.peak = peak;
    }

    @Override
    protected float calculateValue(float time) {
        if (time < this.start) {
            return time / this.start * this.peak;
        }
        if (time > this.end) {
            return (1.0f - (time - this.end) / (1.0f - this.end)) * this.peak;
        }
        return this.peak;
    }
}


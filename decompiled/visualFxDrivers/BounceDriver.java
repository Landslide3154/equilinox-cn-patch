/*
 * Decompiled with CFR 0.152.
 */
package visualFxDrivers;

import toolbox.Maths;
import visualFxDrivers.ValueDriver;

public class BounceDriver
extends ValueDriver {
    private float startValue;
    private float endValue;
    private float peakValue;
    private float max = 0.0f;
    private boolean reachedTarget = false;

    public BounceDriver(float start, float peak, float length) {
        super(length);
        this.endValue = this.startValue = start;
        this.peakValue = peak;
    }

    public BounceDriver(float start, float peak, float end, float length) {
        super(length);
        this.startValue = start;
        this.endValue = end;
        this.peakValue = peak;
    }

    @Override
    protected float calculateValue(float time) {
        if (!this.reachedTarget && time >= this.max) {
            this.max = time;
            if (time < 0.5f) {
                return Maths.cosInterpolate(this.startValue, this.peakValue, time * 2.0f);
            }
            return Maths.cosInterpolate(this.peakValue, this.endValue, (time - 0.5f) * 2.0f);
        }
        this.reachedTarget = true;
        return this.endValue;
    }
}


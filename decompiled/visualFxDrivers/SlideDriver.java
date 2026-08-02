/*
 * Decompiled with CFR 0.152.
 */
package visualFxDrivers;

import toolbox.Maths;
import visualFxDrivers.ValueDriver;

public class SlideDriver
extends ValueDriver {
    private float startValue;
    private float endValue;
    private float max = 0.0f;
    private boolean reachedTarget = false;

    public SlideDriver(float start, float end, float length) {
        super(length);
        this.startValue = start;
        this.endValue = end;
    }

    @Override
    protected float calculateValue(float time) {
        if (!this.reachedTarget && time >= this.max) {
            this.max = time;
            return Maths.cosInterpolate(this.startValue, this.endValue, time);
        }
        this.reachedTarget = true;
        return this.endValue;
    }
}


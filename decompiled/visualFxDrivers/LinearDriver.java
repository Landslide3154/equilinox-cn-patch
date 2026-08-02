/*
 * Decompiled with CFR 0.152.
 */
package visualFxDrivers;

import visualFxDrivers.ValueDriver;

public class LinearDriver
extends ValueDriver {
    private float startValue;
    private float difference;

    public LinearDriver(float startValue, float endValue, float length) {
        super(length);
        this.startValue = startValue;
        this.difference = endValue - startValue;
    }

    @Override
    protected float calculateValue(float time) {
        return this.startValue + time * this.difference;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package visualFxDrivers;

import visualFxDrivers.ValueDriver;

public class SinWaveDriver
extends ValueDriver {
    private float min;
    private float amplitude;

    public SinWaveDriver(float min, float max, float length) {
        super(length);
        this.min = min;
        this.amplitude = max - min;
    }

    @Override
    protected float calculateValue(float time) {
        float value = 0.5f + (float)Math.sin((double)time * Math.PI * 2.0) * 0.5f;
        return this.min + value * this.amplitude;
    }
}


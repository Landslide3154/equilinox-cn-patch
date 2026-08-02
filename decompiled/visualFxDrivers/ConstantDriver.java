/*
 * Decompiled with CFR 0.152.
 */
package visualFxDrivers;

import visualFxDrivers.ValueDriver;

public class ConstantDriver
extends ValueDriver {
    private float value;

    public ConstantDriver(float constant) {
        super(1.0f);
        this.value = constant;
    }

    @Override
    protected float calculateValue(float time) {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}


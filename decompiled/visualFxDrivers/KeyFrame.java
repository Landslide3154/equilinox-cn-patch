/*
 * Decompiled with CFR 0.152.
 */
package visualFxDrivers;

public class KeyFrame {
    private float value;
    private float time;

    public KeyFrame(float time, float value) {
        this.time = time;
        this.value = value;
    }

    protected float getValue() {
        return this.value;
    }

    protected void setValue(float value) {
        this.value = value;
    }

    protected float getTime() {
        return this.time;
    }

    protected void setTime(float time) {
        this.time = time;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package visualFxDrivers;

import toolbox.Colour;

public class ColourBounceDriver {
    private Colour current;
    private Colour startValue;
    private Colour endValue;
    private Colour peakValue;
    private float max = 0.0f;
    private boolean reachedTarget = false;
    private float currentTime = 0.0f;
    private float length;

    public ColourBounceDriver(Colour current, Colour peak, float length) {
        this.length = length;
        this.current = current;
        this.startValue = current.duplicate();
        this.endValue = this.startValue.duplicate();
        this.peakValue = peak.duplicate();
    }

    public ColourBounceDriver(Colour current, Colour peak, Colour end, float length) {
        this.length = length;
        this.current = current;
        this.startValue = current.duplicate();
        this.endValue = end.duplicate();
        this.peakValue = peak.duplicate();
    }

    public void update(float delta) {
        this.currentTime += delta;
        this.currentTime %= this.length;
        float time = this.currentTime / this.length;
        this.calculateValue(time);
    }

    protected void calculateValue(float time) {
        if (!this.reachedTarget && time >= this.max) {
            this.max = time;
            if (time < 0.5f) {
                Colour.interpolateColours(this.startValue, this.peakValue, time * 2.0f, this.current);
            } else {
                Colour.interpolateColours(this.peakValue, this.endValue, (time - 0.5f) * 2.0f, this.current);
            }
        } else {
            this.reachedTarget = true;
        }
    }
}


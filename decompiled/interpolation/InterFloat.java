/*
 * Decompiled with CFR 0.152.
 */
package interpolation;

import toolbox.Maths;

public class InterFloat {
    private float changePerSec;
    private float start;
    private float finish;
    private float totalRequiredTime;
    private float time = 0.0f;

    public InterFloat(float changePerSec) {
        this.changePerSec = changePerSec;
    }

    public InterFloat() {
    }

    public void setSlide(float start, float finish) {
        this.start = start;
        this.finish = finish;
        this.totalRequiredTime = Math.abs(finish - start) / this.changePerSec;
        this.time = 0.0f;
    }

    public void setSlideWithChange(float start, float finish, float changePerSec) {
        this.changePerSec = changePerSec;
        this.start = start;
        this.finish = finish;
        this.totalRequiredTime = Math.abs(finish - start) / changePerSec;
        this.time = 0.0f;
    }

    public void setSlideWithSetTime(float start, float finish, float timeTake) {
        this.totalRequiredTime = timeTake;
        this.start = start;
        this.finish = finish;
        this.changePerSec = Math.abs(finish - start) / this.totalRequiredTime;
        this.time = 0.0f;
    }

    public boolean isReached() {
        return this.time >= this.totalRequiredTime;
    }

    public float update(float delta) {
        this.time += delta;
        if (this.isReached()) {
            return this.finish;
        }
        float blend = this.time / this.totalRequiredTime;
        blend = Math.min(1.0f, blend);
        return Maths.interpolate(this.start, this.finish, blend);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package interpolation;

import basics.DisplayManager;
import gameManaging.GameManager;
import toolbox.Maths;

public class Timer {
    private final float minTime;
    private final float maxTime;
    private final boolean randomTime;
    private float totalTime;
    private final boolean looping;
    private final boolean gameTime;
    private boolean started = false;
    private float time = 0.0f;

    private Timer(float totalTime, boolean looping, boolean gameTime) {
        this.totalTime = totalTime;
        this.looping = looping;
        this.gameTime = gameTime;
        this.randomTime = false;
        this.minTime = 0.0f;
        this.maxTime = 0.0f;
    }

    private Timer(float minTime, float maxTime, boolean gameTime) {
        this.totalTime = Maths.randomNumberBetween(minTime, maxTime);
        this.looping = true;
        this.gameTime = gameTime;
        this.randomTime = true;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public Timer start() {
        this.time = 0.0f;
        this.started = true;
        return this;
    }

    public Timer reset() {
        this.time = 0.0f;
        return this;
    }

    public void resetTo(float maxTime) {
        this.time = 0.0f;
        this.totalTime = maxTime;
    }

    public void stop() {
        this.time = 0.0f;
        this.started = false;
    }

    public boolean check() {
        if (!this.looping && !this.started) {
            return false;
        }
        float timePassed = this.gameTime ? GameManager.getGameSeconds() : DisplayManager.getDeltaSeconds();
        this.time += timePassed;
        if (this.time >= this.totalTime) {
            float f = this.time = this.looping ? (this.time = this.time % this.totalTime) : 0.0f;
            if (this.randomTime) {
                this.totalTime = Maths.randomNumberBetween(this.minTime, this.maxTime);
            }
            this.started = false;
            return true;
        }
        return false;
    }

    public Timer randomize() {
        this.time = Maths.RANDOM.nextFloat() * this.totalTime;
        return this;
    }

    public static Timer createLoopingTimer(float minTime, float maxTime, boolean gameTime) {
        return new Timer(minTime, maxTime, gameTime);
    }

    public static Timer createLoopingTimer(float time, boolean gameTime) {
        return new Timer(time, true, gameTime);
    }

    public static Timer createOneOffTimer(float time, boolean gameTime) {
        return new Timer(time, false, gameTime);
    }
}


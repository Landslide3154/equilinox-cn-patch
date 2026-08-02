/*
 * Decompiled with CFR 0.152.
 */
package instances;

import gameManaging.GameManager;
import toolbox.Colour;
import visualFxDrivers.BounceDriver;
import visualFxDrivers.SinWaveDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class Tinter {
    private ValueDriver driver;
    private boolean active = false;
    private Colour tintColour;
    private float tintAlpha = 0.0f;
    private boolean turnOff = false;

    public boolean hasTint() {
        return this.active;
    }

    public Colour getColour() {
        return this.tintColour;
    }

    public float getAlpha() {
        return this.tintAlpha;
    }

    public void pulse(Colour tintColour, float maxAlpha, float period) {
        this.tintColour = tintColour;
        this.driver = new BounceDriver(this.tintAlpha, maxAlpha, period);
        this.active = true;
        this.turnOff = true;
    }

    public void slideOn(Colour colour, float maxAlpha, float slideTime) {
        this.tintColour = colour;
        this.driver = new SlideDriver(this.tintAlpha, maxAlpha, slideTime);
        this.active = true;
        this.turnOff = false;
    }

    public void slideOff(float slideTime) {
        this.driver = new SlideDriver(this.tintAlpha, 0.0f, slideTime);
        this.active = true;
        this.turnOff = true;
    }

    public void flash(Colour colour, float maxAlpha, float period) {
        this.tintColour = colour;
        this.driver = new SinWaveDriver(0.0f, maxAlpha, period);
        this.active = true;
        this.turnOff = false;
    }

    public void update() {
        if (this.active) {
            this.tintAlpha = this.driver.update(GameManager.getGameSeconds());
            if (this.turnOff && this.driver.hasCompletedOnePeriod()) {
                this.turnOff();
            }
        }
    }

    private void turnOff() {
        this.active = false;
        this.tintAlpha = 0.0f;
    }
}


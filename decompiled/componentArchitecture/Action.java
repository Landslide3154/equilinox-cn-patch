/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import audio.SoundMaestro;
import gameManaging.GameManager;
import mainGuis.GuiSounds;
import toolbox.Maths;

public abstract class Action {
    private String name;
    private final int cooldownTime;
    private boolean hasCost = false;
    private int cost;
    private float lastExcecutionTime = Float.MIN_VALUE;

    public Action(String name, int cooldownTime) {
        this.name = name;
        this.cooldownTime = cooldownTime;
    }

    public Action(String name, int cooldownTime, int cost) {
        this.name = name;
        this.cost = cost;
        boolean bl = this.hasCost = cost > 0;
        if (this.hasCost) {
            this.name = String.valueOf(this.name) + " (" + Maths.formatNumber(cost) + " dp)";
        }
        this.cooldownTime = cooldownTime;
    }

    public String getName() {
        return this.name;
    }

    public boolean isReady() {
        return GameManager.getGameTime() - this.lastExcecutionTime > (float)this.cooldownTime && this.isAffordable();
    }

    public float getProgression() {
        return (GameManager.getGameTime() - this.lastExcecutionTime) / (float)this.cooldownTime;
    }

    public final void excecute() {
        this.lastExcecutionTime = GameManager.getGameTime();
        if (this.hasCost) {
            GameManager.getSession().getStats().increaseDp(-this.cost);
            SoundMaestro.playSystemSound(GuiSounds.CASH);
        }
        this.carryOut();
    }

    public boolean isAffordable() {
        if (!this.hasCost) {
            return true;
        }
        int dp = GameManager.getSession().getStats().getDpCount();
        return dp >= this.cost;
    }

    public abstract void carryOut();
}


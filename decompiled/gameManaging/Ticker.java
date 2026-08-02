/*
 * Decompiled with CFR 0.152.
 */
package gameManaging;

import gameManaging.GameManager;

public class Ticker {
    private static int nextId = 1;
    private final int id;
    private final int periodInFrames;

    public Ticker(float period) {
        this.periodInFrames = (int)(period * 100.0f);
        this.id = nextId++ % this.periodInFrames;
    }

    public boolean isActive() {
        return GameManager.getTicker() % this.periodInFrames == this.id;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package clouds;

import gameManaging.GameManager;
import instances.Entity;
import toolbox.Transformation;

public class CloudManager {
    private final Entity clouds;
    private final float height;
    private final float center;

    protected CloudManager(Entity clouds, float height, float worldSize) {
        this.clouds = clouds;
        this.height = height;
        this.center = worldSize / 2.0f;
        Transformation transform = clouds.getTransform();
        transform.setPosition(this.center, height, this.center);
    }

    public void update() {
        Transformation transform = this.clouds.getTransform();
        transform.setPosition(this.center, this.height, this.center);
        transform.increaseRotation(0.0f, GameManager.getGameSeconds(), 0.0f);
    }
}


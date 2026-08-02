/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAi;
import gameManaging.GameManager;
import instances.Entity;
import toolbox.Maths;
import toolbox.Transformation;

public class FloaterDeath
implements DeathAi {
    private static final float FLOAT_ACCEL = 0.15f;
    private static final float DEATH_DEPTH = 0.15f;
    private static final float DEATH_TRANS = 0.1f;
    private static final float MAX_FLOAT_SPEED = 0.5f;
    private static final float TURN_SPEED = 70.0f;
    private final Entity entity;
    private final Transformation transform;
    private final float deathRotation;
    private float upSpeed = 0.0f;
    private boolean turning = true;
    private boolean floating = true;

    public FloaterDeath(Entity entity, float deathRotation) {
        this.entity = entity;
        this.transform = entity.getTransform();
        this.deathRotation = deathRotation;
    }

    @Override
    public boolean update() {
        if (this.turning) {
            this.updateTurn();
        }
        if (this.floating) {
            return this.updateFloat();
        }
        return false;
    }

    @Override
    public boolean isEssential() {
        return false;
    }

    private boolean updateFloat() {
        this.upSpeed += 0.15f * GameManager.getGameSeconds();
        this.upSpeed = Math.min(0.5f, this.upSpeed);
        this.transform.increasePosition(0.0f, this.upSpeed * GameManager.getGameSeconds(), 0.0f);
        float depth = GameManager.getWorld().getWaterHeight() - 0.15f - this.transform.getPosition().y;
        this.entity.setAlpha(Maths.clamp(depth / 0.1f, 0.0f, 1.0f));
        return depth <= 0.0f;
    }

    private void updateTurn() {
        this.transform.increaseRotation(0.0f, 0.0f, GameManager.getGameSeconds() * 70.0f);
        if (this.transform.getRotZ() >= this.deathRotation) {
            this.transform.setZRotation(this.deathRotation);
            this.turning = false;
        }
    }

    @Override
    public void init() {
        if (this.entity.isCurrentlyInStaticBatch()) {
            GameManager.getSession().getSceneData().makeDynamic(this.entity);
        }
    }
}


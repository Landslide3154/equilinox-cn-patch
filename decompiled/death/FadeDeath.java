/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAi;
import gameManaging.GameManager;
import instances.Entity;

public class FadeDeath
implements DeathAi {
    private float fadeTime;
    private float time;
    private Entity entity;

    public FadeDeath(float fadeTime, Entity entity) {
        this.entity = entity;
        this.fadeTime = fadeTime;
        this.time = fadeTime;
    }

    @Override
    public boolean update() {
        this.time -= GameManager.getGameSeconds();
        this.entity.setAlpha(Math.max(0.0f, this.time / this.fadeTime));
        return this.time <= 0.0f;
    }

    @Override
    public void init() {
        if (this.entity.isCurrentlyInStaticBatch()) {
            GameManager.getSession().getSceneData().makeDynamic(this.entity);
        }
    }

    @Override
    public boolean isEssential() {
        return false;
    }
}


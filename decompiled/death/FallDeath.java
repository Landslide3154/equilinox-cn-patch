/*
 * Decompiled with CFR 0.152.
 */
package death;

import baseMovement.MovementComp;
import componentArchitecture.Component;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import death.DeathAi;
import death.FallDeathBlueprint;
import gameManaging.GameManager;
import instances.Entity;
import materials.MaterialComponent;

public class FallDeath
implements DeathAi {
    private float time = 0.0f;
    private boolean exploded = false;
    private Entity entity;
    private FallDeathBlueprint blueprint;
    private MaterialComponent material;
    private MeshComponent mesh;
    private MovementComp mover;
    private boolean normalized = false;

    public FallDeath(FallDeathBlueprint blueprint, Entity entity) {
        this.entity = entity;
        this.blueprint = blueprint;
        this.material = (MaterialComponent)entity.getComponent(ComponentType.MATERIAL);
        this.mesh = (MeshComponent)entity.getComponent(ComponentType.MESH);
        this.mover = (MovementComp)((Object)entity.getComponent(ComponentType.MOVEMENT));
    }

    @Override
    public boolean update() {
        if (this.mover != null) {
            ((Component)((Object)this.mover)).update();
            if (!this.normalized && !this.mover.normalize()) {
                return false;
            }
            this.normalized = true;
        }
        this.time += GameManager.getGameSeconds();
        float prog = Math.min(1.0f, this.time / this.blueprint.getFallTime());
        if (!this.exploded && prog >= this.blueprint.getExplodeTime() && this.blueprint.isParticleModelStage(this.mesh.getCurrentStageNumber())) {
            this.explode();
        }
        float rotProgress = (float)((-2.0 * Math.exp(8.4 * (double)prog - 7.0) + 8.0) / 8.0);
        this.entity.getTransform().setZRotation((1.0f - rotProgress) * this.blueprint.getFallenAngle());
        this.entity.setAlpha(Math.max(0.0f, Math.min(1.0f, this.blueprint.getTotalTime() - this.time)));
        boolean finished = this.time >= this.blueprint.getTotalTime();
        return finished;
    }

    private void explode() {
        if (this.blueprint.hasParticleEffect()) {
            if (this.blueprint.useEntityColour() && this.material != null) {
                this.blueprint.getSystem().pulseParticles(this.entity.getTransform().getModelMatrix(), this.material.getMaterial(), 1.0f);
            } else {
                this.blueprint.getSystem().pulseParticles(this.entity.getTransform().getModelMatrix(), 1.0f);
            }
        }
        this.exploded = true;
    }

    @Override
    public boolean isEssential() {
        return false;
    }

    @Override
    public void init() {
        if (this.entity.isCurrentlyInStaticBatch()) {
            GameManager.getSession().getSceneData().makeDynamic(this.entity);
        }
    }
}


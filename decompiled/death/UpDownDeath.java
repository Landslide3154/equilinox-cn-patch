/*
 * Decompiled with CFR 0.152.
 */
package death;

import baseMovement.MoveUtils;
import componentArchitecture.ComponentType;
import death.DeathAi;
import death.UpDownDeathBlueprint;
import gameManaging.GameManager;
import instances.Entity;
import materials.MaterialComponent;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class UpDownDeath
implements DeathAi {
    private final Entity entity;
    private final Transformation transform;
    private final MaterialComponent material;
    private final UpDownDeathBlueprint blueprint;
    private Vector3f velocity;

    public UpDownDeath(UpDownDeathBlueprint blueprint, Entity entity, Transformation transform) {
        this.blueprint = blueprint;
        this.entity = entity;
        this.transform = transform;
        this.material = (MaterialComponent)entity.getComponent(ComponentType.MATERIAL);
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
        this.velocity = new Vector3f(0.0f, this.blueprint.getSpeed(), 0.0f);
    }

    @Override
    public boolean update() {
        MoveUtils.applyVelocityWithGravity(this.velocity, this.transform, GameManager.getGameSeconds());
        float terrainHeight = GameManager.getWorld().getHeightOfTerrain(this.transform.getPosition().x, this.transform.getPosition().z);
        if (this.velocity.y < 0.0f && this.transform.getPosition().y < terrainHeight + 0.1f) {
            this.emitParticles();
            return true;
        }
        return false;
    }

    private void emitParticles() {
        Vector3f position = new Vector3f(this.entity.getTransform().getPosition());
        position.y += this.entity.getBoundingBox().getHeight() / 2.0f;
        if (this.material != null) {
            this.blueprint.getParticleSystem().pulseParticles(position, this.material.getMaterial(), 1.0f);
        } else {
            this.blueprint.getParticleSystem().pulseParticles(position, 1.0f);
        }
    }
}


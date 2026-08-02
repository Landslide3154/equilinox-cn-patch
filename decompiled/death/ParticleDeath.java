/*
 * Decompiled with CFR 0.152.
 */
package death;

import componentArchitecture.ComponentType;
import death.DeathAi;
import instances.Entity;
import materials.MaterialComponent;
import org.lwjgl.util.vector.Vector3f;
import particles.ParticleSystem;

public class ParticleDeath
implements DeathAi {
    private Entity entity;
    private ParticleSystem system;
    private MaterialComponent material;

    public ParticleDeath(Entity entity, ParticleSystem system) {
        this.entity = entity;
        this.system = system;
        this.material = (MaterialComponent)entity.getComponent(ComponentType.MATERIAL);
    }

    @Override
    public boolean isEssential() {
        return false;
    }

    @Override
    public boolean update() {
        Vector3f position = new Vector3f(this.entity.getTransform().getPosition());
        position.y += this.entity.getBoundingBox().getHeight() / 2.0f;
        if (this.material != null) {
            this.system.pulseParticles(position, this.material.getMaterial(), 1.0f);
        } else {
            this.system.pulseParticles(position, 1.0f);
        }
        return true;
    }

    @Override
    public void init() {
    }
}


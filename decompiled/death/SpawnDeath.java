/*
 * Decompiled with CFR 0.152.
 */
package death;

import blueprints.Blueprint;
import componentArchitecture.ComponentType;
import death.DeathAi;
import death.SpawnDeathBlueprint;
import fruit.DecayParams;
import gameManaging.GameManager;
import growth.GrowthComponent;
import instances.Entity;
import org.lwjgl.util.vector.Vector3f;
import resourceManagement.BlueprintRepository;
import toolbox.Maths;
import toolbox.Transformation;

public class SpawnDeath
implements DeathAi {
    private static final float SPAWN_SPEED = 4.0f;
    private final Entity entity;
    private final SpawnDeathBlueprint blueprint;
    private final int count;

    public SpawnDeath(Entity entity, SpawnDeathBlueprint blueprint, int count) {
        this.entity = entity;
        this.blueprint = blueprint;
        this.count = count;
    }

    @Override
    public void init() {
    }

    @Override
    public boolean isEssential() {
        return true;
    }

    @Override
    public boolean update() {
        if (this.blueprint.mustBeFullGrown() && !((GrowthComponent)this.entity.getComponent(ComponentType.GROWTH)).isFullyGrown()) {
            return true;
        }
        int i = 0;
        while (i < this.count) {
            this.spawnFruit();
            ++i;
        }
        return true;
    }

    private void spawnFruit() {
        Transformation transform = this.entity.getTransform();
        Transformation.TransformBlueprint transformBlueprint = transform.getBlueprint();
        Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(transform.getPosition()), Maths.RANDOM.nextFloat() * 360.0f, transformBlueprint.generateRandomScale());
        DecayParams params2 = new DecayParams(Maths.generateVectorWithinCone(Maths.UP, 0.5f, 4.0f));
        Blueprint fruitBlueprint = BlueprintRepository.getBlueprint(this.blueprint.getEntityId());
        Entity entity = fruitBlueprint.createInstance(params, params2);
        GameManager.getSession().getWorld().addInstance(entity, false);
    }
}


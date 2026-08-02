/*
 * Decompiled with CFR 0.152.
 */
package instances;

import blueprints.Blueprint;
import componentArchitecture.ComponentParams;
import instances.Entity;
import world.AddableEntity;

public class UninstantiatedEntity
implements AddableEntity {
    private Blueprint blueprint;
    private ComponentParams[] params;

    public UninstantiatedEntity(Blueprint blueprint, ComponentParams[] params) {
        this.blueprint = blueprint;
        this.params = params;
    }

    @Override
    public Entity getEntity() {
        if (this.blueprint.isLoaded()) {
            return this.blueprint.createInstance(this.params);
        }
        return null;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package instances;

import instances.Entity;

public abstract class EntityGetRequest {
    private final int entityId;

    public EntityGetRequest(int entityId) {
        this.entityId = entityId;
    }

    public int getId() {
        return this.entityId;
    }

    public abstract void provideEntity(Entity var1);
}


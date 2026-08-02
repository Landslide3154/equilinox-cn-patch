/*
 * Decompiled with CFR 0.152.
 */
package resourceManagement;

import blueprints.Blueprint;

public class CachedBlueprint {
    protected final Blueprint blueprint;
    protected final boolean locked;
    private int uses;

    protected CachedBlueprint(Blueprint blueprint, boolean locked) {
        this.blueprint = blueprint;
        this.locked = locked;
        this.uses = 1;
    }

    protected boolean isLocked() {
        return this.locked;
    }

    protected void incrementUses() {
        ++this.uses;
    }

    protected void decrementUses() {
        --this.uses;
    }

    protected boolean isBeingUsed() {
        return this.uses > 0;
    }
}


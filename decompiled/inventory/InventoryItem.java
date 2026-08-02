/*
 * Decompiled with CFR 0.152.
 */
package inventory;

import blueprints.Blueprint;

public class InventoryItem {
    protected final Blueprint blueprint;
    private int count;

    public InventoryItem(Blueprint blueprint, int count) {
        this.blueprint = blueprint;
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public void increaseCount() {
        ++this.count;
    }

    public void decreaseCount() {
        --this.count;
    }
}


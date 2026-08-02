/*
 * Decompiled with CFR 0.152.
 */
package batches;

import instances.Entity;
import java.util.ArrayList;
import java.util.List;

public class SubBlueprintBundle {
    private int start;
    private int length;
    private List<Entity> entities = new ArrayList<Entity>();

    protected SubBlueprintBundle(int start, int length) {
        this.start = start;
        this.length = length;
    }

    public int getStartVertex() {
        return this.start / 10;
    }

    public int getVertexCount() {
        return this.length / 10;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    protected void shiftLeft(int amount) {
        this.start -= amount;
    }

    protected void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    protected boolean removeEntity(Entity entity) {
        return this.entities.remove(entity);
    }
}


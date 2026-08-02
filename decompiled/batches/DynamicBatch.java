/*
 * Decompiled with CFR 0.152.
 */
package batches;

import batches.BlueprintBundle;
import batches.DynamicMemoryManager;
import blueprints.Blueprint;
import instances.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicBatch {
    private static final int MAX_VERTEX_COUNT = 70000;
    private DynamicMemoryManager memoryManager;
    private Map<Blueprint, BlueprintBundle> blueprintBundles = new HashMap<Blueprint, BlueprintBundle>();

    public DynamicBatch() {
        this.memoryManager = new DynamicMemoryManager(70000);
    }

    public DynamicBatch(List<Entity> dynamicEntities) {
        this.memoryManager = new DynamicMemoryManager(70000);
        for (Entity entity : dynamicEntities) {
            this.addEntity(entity);
        }
    }

    public int getVao() {
        return this.memoryManager.getVao();
    }

    public void delete() {
        this.memoryManager.delete();
        this.blueprintBundles.clear();
    }

    public Map<Blueprint, BlueprintBundle> getData() {
        return this.blueprintBundles;
    }

    public synchronized boolean attemptToAddWithoutUpdate(Entity entity) {
        if (this.blueprintBundles.containsKey(entity.getBlueprint())) {
            this.addEntity(entity);
            return true;
        }
        return false;
    }

    public boolean defrag() {
        return this.memoryManager.refactor();
    }

    public boolean addEntity(Entity entity) {
        BlueprintBundle bundle = this.blueprintBundles.get(entity.getBlueprint());
        boolean memoryChange = false;
        if (bundle == null) {
            bundle = this.memoryManager.allocateMemory(entity.getBlueprint());
            this.blueprintBundles.put(entity.getBlueprint(), bundle);
            memoryChange = true;
        }
        bundle.addEntity(entity);
        return memoryChange;
    }

    public void removeNow(Entity entity) {
        BlueprintBundle bundle = this.blueprintBundles.get(entity.getBlueprint());
        if (bundle != null) {
            bundle.removeEntity(entity);
        }
    }

    public void delete(Blueprint blueprint) {
        BlueprintBundle bundle = this.blueprintBundles.remove(blueprint);
        if (bundle != null) {
            this.memoryManager.unmapMemory(bundle);
        }
    }
}


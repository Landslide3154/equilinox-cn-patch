/*
 * Decompiled with CFR 0.152.
 */
package batches;

import batches.MemorySlot;
import batches.SubBlueprintBundle;
import blueprints.AdditionSubBlueprint;
import blueprints.Blueprint;
import blueprints.SubBlueprint;
import instances.Entity;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlueprintBundle
extends MemorySlot {
    private Blueprint blueprint;
    private Map<SubBlueprint, SubBlueprintBundle> subBundles = new ConcurrentHashMap<SubBlueprint, SubBlueprintBundle>();

    protected BlueprintBundle(Blueprint blueprint, int startIndex, int length) {
        super(startIndex, length, false);
        this.blueprint = blueprint;
        int pointer = startIndex;
        int previousDataLength = 0;
        for (SubBlueprint subBlueprint : blueprint.getSubBlueprints()) {
            if (!(subBlueprint instanceof AdditionSubBlueprint)) {
                pointer += previousDataLength;
            }
            this.subBundles.put(subBlueprint, new SubBlueprintBundle(pointer, subBlueprint.getDataLength()));
            previousDataLength = subBlueprint.getDataLength();
        }
    }

    public Map<SubBlueprint, SubBlueprintBundle> getSubBundles() {
        return this.subBundles;
    }

    protected void addEntity(Entity entity) {
        SubBlueprintBundle subBundle = this.subBundles.get(entity.getSubBlueprint());
        subBundle.addEntity(entity);
    }

    protected void removeEntity(Entity entity) {
        SubBlueprintBundle subBundle = this.subBundles.get(entity.getSubBlueprint());
        subBundle.removeEntity(entity);
    }

    @Override
    protected float[] getData() {
        return this.blueprint.getData();
    }

    @Override
    protected void shiftLeft(int amount) {
        super.increaseStartIndex(-amount);
        super.increaseEndIndex(-amount);
        for (SubBlueprintBundle subBundle : this.subBundles.values()) {
            subBundle.shiftLeft(amount);
        }
    }
}


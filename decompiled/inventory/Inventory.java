/*
 * Decompiled with CFR 0.152.
 */
package inventory;

import blueprints.Blueprint;
import inventory.InventoryItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import resourceManagement.BlueprintRepository;
import utils.BinaryReader;
import utils.BinaryWriter;

public class Inventory {
    private boolean loaded = false;
    private List<InventoryItem> items = new ArrayList<InventoryItem>();

    private Inventory() {
    }

    public static Inventory loadInventory(BinaryReader reader) throws Exception {
        Inventory inventory = new Inventory();
        int itemCount = reader.readInt();
        int i = 0;
        while (i < itemCount) {
            int blueprintId = reader.readInt();
            int count = reader.readInt();
            Blueprint blueprint = BlueprintRepository.getBlueprint(blueprintId);
            inventory.items.add(new InventoryItem(blueprint, count));
            ++i;
        }
        inventory.loaded = true;
        return inventory;
    }

    public static Inventory newInventory() {
        Inventory inventory = new Inventory();
        Inventory.addDefeultItems(inventory);
        inventory.loaded = true;
        return inventory;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public void addItem(Blueprint blueprint) {
        int index = this.getIndex(blueprint);
        if (index >= 0) {
            this.items.get(index).increaseCount();
        } else {
            this.items.add(new InventoryItem(blueprint, 1));
        }
    }

    public int removeItem(int index) {
        InventoryItem item = this.items.get(index);
        item.decreaseCount();
        if (item.getCount() == 0) {
            this.items.remove(index);
        }
        return item.getCount();
    }

    public int getCountOfItem(int index) {
        return this.items.get(index).getCount();
    }

    public List<InventoryItem> getItems() {
        return this.items;
    }

    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.items.size());
        for (InventoryItem item : this.items) {
            writer.writeInt(item.blueprint.getId());
            writer.writeInt(item.getCount());
        }
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    private int getIndex(Blueprint blueprint) {
        int i = 0;
        while (i < this.items.size()) {
            if (this.items.get((int)i).blueprint == blueprint) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    private static void addDefeultItems(Inventory inventory) {
    }
}


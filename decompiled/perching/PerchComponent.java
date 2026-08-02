/*
 * Decompiled with CFR 0.152.
 */
package perching;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import growth.GrowthComponent;
import instances.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import perching.PerchCompBlueprint;
import perching.PerchSlot;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class PerchComponent
extends Component {
    private final PerchCompBlueprint blueprint;
    private PerchSlot[] slots;

    protected PerchComponent(PerchCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    public PerchSlot getNextAvailableSlot() {
        PerchSlot[] perchSlotArray = this.slots;
        int n = this.slots.length;
        int n2 = 0;
        while (n2 < n) {
            PerchSlot slot = perchSlotArray[n2];
            if (slot.isAvailable()) {
                return slot;
            }
            ++n2;
        }
        return null;
    }

    public boolean hasAvailableSlots() {
        PerchSlot[] perchSlotArray = this.slots;
        int n = this.slots.length;
        int n2 = 0;
        while (n2 < n) {
            PerchSlot slot = perchSlotArray[n2];
            if (slot.isAvailable()) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public PerchSlot getRandomAvailableSlot() {
        ArrayList<PerchSlot> availableSlots = new ArrayList<PerchSlot>();
        PerchSlot[] perchSlotArray = this.slots;
        int n = this.slots.length;
        int n2 = 0;
        while (n2 < n) {
            PerchSlot slot = perchSlotArray[n2];
            if (slot.isAvailable()) {
                availableSlots.add(slot);
            }
            ++n2;
        }
        if (availableSlots.isEmpty()) {
            return null;
        }
        return (PerchSlot)availableSlots.get(Maths.RANDOM.nextInt(availableSlots.size()));
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        int count = 0;
        PerchSlot[] perchSlotArray = this.slots;
        int n = this.slots.length;
        int n2 = 0;
        while (n2 < n) {
            PerchSlot slot = perchSlotArray[n2];
            if (slot.needsExporting()) {
                ++count;
            }
            ++n2;
        }
        if (count == 0) {
            writer.writeBoolean(false);
        } else {
            writer.writeBoolean(true);
            writer.writeInt(count);
            int i = 0;
            while (i < this.slots.length) {
                if (this.slots[i].needsExporting()) {
                    writer.writeInt(i);
                    this.slots[i].export(writer);
                }
                ++i;
            }
        }
    }

    @Override
    public void create(ComponentBundle bundle) {
        GrowthComponent grower = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.initSlots(bundle.getEntity(), transform, grower);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        boolean needsLoading = reader.readBoolean();
        if (needsLoading) {
            int count = reader.readInt();
            int i = 0;
            while (i < count) {
                int slotId = reader.readInt();
                this.slots[slotId].load(reader, bundle);
                ++i;
            }
        }
    }

    private void initSlots(Entity entity, Transformation entityTransform, GrowthComponent grower) {
        this.slots = new PerchSlot[this.blueprint.perchPositions.length];
        int i = 0;
        while (i < this.slots.length) {
            this.slots[i] = new PerchSlot(this.blueprint.perchPositions[i], entity, entityTransform, grower);
            ++i;
        }
    }
}


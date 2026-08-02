/*
 * Decompiled with CFR 0.152.
 */
package batchingSystem;

import batchingSystem.MemorySlot;
import java.util.ArrayList;
import java.util.List;

public class MemoryChain {
    private MemorySlot endSlot = null;
    private MemorySlot headSlot = null;
    private List<MemorySlot> gaps = new ArrayList<MemorySlot>();

    public MemorySlot append(byte[] data) {
        MemorySlot slot = MemorySlot.createDataSlot(this, this.getEndPointer(), data);
        slot.connectToPrevious(this.endSlot);
        slot.connectToNext(null);
        return slot;
    }

    public MemorySlot getEndSlot() {
        return this.endSlot;
    }

    public MemorySlot getHeadSlot() {
        return this.headSlot;
    }

    public void display() {
        System.out.print("DATA: ");
        MemorySlot current = this.headSlot;
        while (current != null) {
            System.out.print(current);
            current = current.getNextSlot();
        }
        System.out.println();
    }

    public void refactor() {
        while (!this.gaps.isEmpty()) {
            this.gaps.get(0).remove();
        }
    }

    public int getEndPointer() {
        if (this.endSlot == null) {
            return 0;
        }
        return this.endSlot.getEndIndex();
    }

    public void clear() {
        this.endSlot = null;
        this.gaps.clear();
    }

    public List<MemorySlot> getGaps() {
        return this.gaps;
    }

    protected void setEndSlot(MemorySlot endSlot) {
        this.endSlot = endSlot;
    }

    protected void setHeadSlot(MemorySlot headSlot) {
        this.headSlot = headSlot;
    }

    protected void addGapToList(MemorySlot endSlot) {
        this.gaps.add(endSlot);
    }

    protected void removeGapFromList(MemorySlot next) {
        this.gaps.remove(next);
    }
}


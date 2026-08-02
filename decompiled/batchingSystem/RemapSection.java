/*
 * Decompiled with CFR 0.152.
 */
package batchingSystem;

import batchingSystem.BatchMemory;
import batchingSystem.MemoryChain;
import batchingSystem.MemorySlot;
import toolbox.Maths;

public class RemapSection {
    private final MemoryChain chain;
    private final BatchMemory memory;
    private final MemorySlot gap;
    private final MemorySlot previousNode;
    private final int remapStart;
    private int remapEnd;
    private int remainingSpace;

    protected RemapSection(BatchMemory memory, MemorySlot previousNode, int remapStart, int remapEnd, int remainingSpace) {
        this.memory = memory;
        this.chain = memory.getMemoryChain();
        this.gap = null;
        this.remapEnd = remapEnd;
        this.remainingSpace = remainingSpace;
        this.remapStart = remapStart;
        this.previousNode = previousNode;
    }

    protected RemapSection(BatchMemory memory, MemorySlot gap, MemorySlot previousNode, int remapStart, int remapEnd, int remainingSpace) {
        this.memory = memory;
        this.chain = memory.getMemoryChain();
        this.gap = gap;
        this.remapEnd = remapEnd;
        this.remainingSpace = remainingSpace;
        this.remapStart = remapStart;
        this.previousNode = previousNode;
    }

    public int getRemaining() {
        return this.remainingSpace;
    }

    public MemorySlot storeData(byte[] data) {
        if (data.length > this.remainingSpace) {
            System.err.println("Trying to store more than remaining space in remap section!");
            return null;
        }
        MemorySlot newDataSlot = this.gap != null ? this.gap.fillWith(data) : this.chain.append(data);
        this.remainingSpace -= data.length;
        this.remapEnd = Math.max(this.remapEnd, newDataSlot.getEndIndex());
        return newDataSlot;
    }

    public void remap() {
        if (this.remapEnd == this.remapStart) {
            return;
        }
        byte[] remapData = this.getAllRemappingData();
        this.memory.remap(remapData, this.remapStart);
    }

    private byte[] getAllRemappingData() {
        MemorySlot currentNode = this.previousNode == null ? this.chain.getHeadSlot() : this.previousNode.getNextSlot();
        byte[] totalData = new byte[this.remapEnd - this.remapStart];
        int pointer = 0;
        System.out.println(String.valueOf(this.remapStart) + ", " + this.remapEnd);
        while (currentNode != null && currentNode.getStartIndex() < this.remapEnd) {
            byte[] newData = currentNode.getData();
            int length = Math.min(currentNode.getEndIndex(), this.remapEnd) - currentNode.getStartIndex();
            Maths.storeInArray(totalData, newData, pointer, length);
            pointer += length;
            currentNode = currentNode.getNextSlot();
        }
        return totalData;
    }
}


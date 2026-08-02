/*
 * Decompiled with CFR 0.152.
 */
package batchingSystem;

import batchingSystem.MemoryChain;
import batchingSystem.MemorySlotGap;

public class MemorySlot {
    private final MemoryChain chain;
    private final byte[] data;
    private int startIndex;
    private int endIndex;
    private MemorySlot previousSlot = null;
    private MemorySlot nextSlot = null;

    protected MemorySlot(MemoryChain chain, byte[] data, int start, int length) {
        this.chain = chain;
        this.data = data;
        this.startIndex = start;
        this.endIndex = start + length;
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getLength() {
        return this.endIndex - this.startIndex;
    }

    public MemorySlot getNextSlot() {
        return this.nextSlot;
    }

    public MemorySlot getPreviousSlot() {
        return this.previousSlot;
    }

    public MemorySlot fillWith(byte[] data) {
        System.err.println("Can't fill a data slot!");
        return null;
    }

    public MemorySlot remove() {
        MemorySlot gap = MemorySlot.createGap(this.chain, this.startIndex, this.getLength());
        gap.connectToPrevious(this.previousSlot);
        gap.connectToNext(this.nextSlot);
        return gap;
    }

    public boolean isGap() {
        return false;
    }

    public String toString() {
        String all = "";
        byte[] byArray = this.data;
        int n = this.data.length;
        int n2 = 0;
        while (n2 < n) {
            byte b = byArray[n2];
            all = String.valueOf(all) + b + ", ";
            ++n2;
        }
        return all;
    }

    protected void connectToNext(MemorySlot next) {
        this.nextSlot = next;
        if (next != null) {
            next.previousSlot = this;
        } else {
            this.chain.setEndSlot(this);
        }
    }

    protected void connectToPrevious(MemorySlot previous) {
        this.previousSlot = previous;
        if (previous != null) {
            previous.nextSlot = this;
        } else {
            this.chain.setHeadSlot(this);
        }
    }

    protected void increaseStartIndex(int increase) {
        this.startIndex += increase;
    }

    protected void increaseEndIndex(int increase) {
        this.endIndex += increase;
    }

    protected void shiftLeft(int amount) {
        this.startIndex -= amount;
        this.endIndex -= amount;
        if (this.nextSlot != null) {
            this.nextSlot.shiftLeft(amount);
        }
    }

    protected static MemorySlot createGap(MemoryChain chain, int start, int length) {
        MemorySlotGap gap = new MemorySlotGap(chain, start, length);
        chain.addGapToList(gap);
        return gap;
    }

    protected static MemorySlot createDataSlot(MemoryChain chain, int start, byte[] data) {
        return new MemorySlot(chain, data, start, data.length);
    }
}


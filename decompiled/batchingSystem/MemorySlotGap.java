/*
 * Decompiled with CFR 0.152.
 */
package batchingSystem;

import batchingSystem.MemoryChain;
import batchingSystem.MemorySlot;

public class MemorySlotGap
extends MemorySlot {
    private final MemoryChain chain;

    protected MemorySlotGap(MemoryChain chain, int start, int length) {
        super(chain, null, start, length);
        this.chain = chain;
    }

    @Override
    public MemorySlot remove() {
        this.chain.removeGapFromList(this);
        this.getNextSlot().connectToPrevious(this.getPreviousSlot());
        this.getNextSlot().shiftLeft(this.getLength());
        return null;
    }

    @Override
    public MemorySlot fillWith(byte[] data) {
        MemorySlot dataSlot = MemorySlot.createDataSlot(this.chain, this.getStartIndex(), data);
        dataSlot.connectToPrevious(this.getPreviousSlot());
        if (dataSlot.getLength() == this.getLength()) {
            dataSlot.connectToNext(this.getNextSlot());
            this.chain.removeGapFromList(this);
        } else {
            dataSlot.connectToNext(this);
            this.increaseStartIndex(dataSlot.getLength());
        }
        return dataSlot;
    }

    @Override
    public boolean isGap() {
        return true;
    }

    @Override
    public String toString() {
        String text = "";
        int i = 0;
        while (i < this.getLength()) {
            text = String.valueOf(text) + "_, ";
            ++i;
        }
        return text;
    }

    @Override
    protected void shiftLeft(int amount) {
        this.increaseStartIndex(-amount);
    }

    @Override
    protected void connectToNext(MemorySlot next) {
        if (next == null) {
            this.connectToEnd();
        } else if (next.isGap()) {
            this.mergeWithNextGap(next);
        } else {
            super.connectToNext(next);
        }
    }

    @Override
    protected void connectToPrevious(MemorySlot previous) {
        if (previous != null && previous.isGap()) {
            this.mergeWithPreviousGap(previous);
        } else {
            super.connectToPrevious(previous);
        }
    }

    private void mergeWithPreviousGap(MemorySlot previous) {
        this.chain.removeGapFromList(previous);
        super.increaseStartIndex(-previous.getLength());
        this.connectToPrevious(previous.getPreviousSlot());
    }

    private void mergeWithNextGap(MemorySlot nextGap) {
        this.chain.removeGapFromList(nextGap);
        this.increaseEndIndex(nextGap.getLength());
        this.connectToNext(nextGap.getNextSlot());
    }

    private void connectToEnd() {
        this.chain.removeGapFromList(this);
        if (this.getPreviousSlot() != null) {
            this.getPreviousSlot().connectToNext(null);
        } else {
            this.chain.setEndSlot(null);
            this.chain.setHeadSlot(null);
        }
    }
}


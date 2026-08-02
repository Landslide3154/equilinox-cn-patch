/*
 * Decompiled with CFR 0.152.
 */
package batches;

import java.util.List;

public class MemorySlot {
    public static final int VERTEX_FLOAT_COUNT = 10;
    private int startIndex;
    private int endIndex;
    private boolean isGap;
    private MemorySlot previousSlot = null;
    private MemorySlot nextSlot = null;
    private float[] data;

    protected MemorySlot(int start, int length, boolean isGap) {
        this.startIndex = start;
        this.endIndex = start + length;
        this.isGap = isGap;
    }

    protected void clear() {
        this.data = null;
    }

    protected int getStartIndex() {
        return this.startIndex;
    }

    protected int getEndIndex() {
        return this.endIndex;
    }

    protected float[] getData() {
        return this.data;
    }

    protected MemorySlot getNextSlot() {
        return this.nextSlot;
    }

    protected int getLength() {
        return this.endIndex - this.startIndex;
    }

    protected MemorySlot getPreviousSlot() {
        return this.previousSlot;
    }

    protected boolean isGap() {
        return this.isGap;
    }

    protected void connectToPrevious(MemorySlot previous) {
        this.previousSlot = previous;
        if (previous != null) {
            previous.nextSlot = this;
        }
    }

    protected void connectToNext(MemorySlot next) {
        this.nextSlot = next;
        if (next != null) {
            next.previousSlot = this;
        }
    }

    protected void shiftLeft(int amount) {
        this.startIndex -= amount;
        this.endIndex -= amount;
    }

    protected void increaseStartIndex(int filledLength) {
        this.startIndex += filledLength;
    }

    protected void increaseEndIndex(int length) {
        this.endIndex += length;
    }

    protected static void append(MemorySlot newSlot, MemorySlot currentEnd) {
        if (currentEnd != null) {
            currentEnd.connectToNext(newSlot);
        }
    }

    protected static void insertInGap(MemorySlot newSlot, MemorySlot gap, List<MemorySlot> gaps) {
        if (gap.previousSlot != null) {
            gap.previousSlot.connectToNext(newSlot);
        }
        if (gap.getLength() == newSlot.getLength()) {
            newSlot.connectToNext(gap.nextSlot);
            gaps.remove(gap);
        } else {
            newSlot.connectToNext(gap);
            gap.increaseStartIndex(newSlot.getLength());
        }
    }

    protected static MemorySlot createGap(int start, int length) {
        return new MemorySlot(start, length, true);
    }

    protected static MemorySlot createDataSlot(int start, float[] data) {
        MemorySlot slot = new MemorySlot(start, data.length, false);
        slot.data = data;
        return slot;
    }
}


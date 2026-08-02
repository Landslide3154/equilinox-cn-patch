/*
 * Decompiled with CFR 0.152.
 */
package batches;

import basics.Loader;
import batches.MemorySlot;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import toolbox.Maths;

public class StaticMemoryManager {
    private static final FloatBuffer BUFFER = BufferUtils.createFloatBuffer(700000);
    private static final int MAX_VERTEX_REFACTOR = 300;
    private final int MAX_VERTEX_COUNT;
    private int endPointer = 0;
    private MemorySlot endSlot = null;
    private List<MemorySlot> gaps = new ArrayList<MemorySlot>();
    private int vao;
    private int vbo;
    private FloatBuffer floatBuffer;

    protected StaticMemoryManager(int maxVertexCount) {
        if (maxVertexCount > 70000) {
            this.floatBuffer = BufferUtils.createFloatBuffer(10 * maxVertexCount);
            System.out.println("INCREASED SIZE OF STATIC BATCH: " + maxVertexCount + " vertices");
        } else {
            this.floatBuffer = BUFFER;
        }
        this.vao = Loader.createVAO();
        this.vbo = Loader.createEmptyInterleavedVBO(this.vao, maxVertexCount, 0, 4, 3, 3);
        this.MAX_VERTEX_COUNT = maxVertexCount;
    }

    protected void delete() {
        Loader.deleteVaoFromCache(this.vao);
        this.gaps.clear();
    }

    protected MemorySlot allocateMemory(float[] data) {
        if ((data.length + this.endPointer) / 10 > this.MAX_VERTEX_COUNT) {
            System.err.println("Static Batch Full!!!");
            return null;
        }
        for (MemorySlot gap : this.gaps) {
            if (gap.getLength() < data.length) continue;
            MemorySlot bundle = this.storeEntityData(data, gap.getStartIndex());
            MemorySlot.insertInGap(bundle, gap, this.gaps);
            return bundle;
        }
        MemorySlot bundle = this.storeEntityData(data, this.endPointer);
        MemorySlot.append(bundle, this.endSlot);
        this.endPointer += data.length;
        this.endSlot = bundle;
        return bundle;
    }

    protected MemorySlot[] massAllocateMemory(float[][] data) {
        MemorySlot[] slots = new MemorySlot[data.length];
        float[] allData = Maths.concatenateArrays(data);
        if (allData.length + this.endPointer > this.MAX_VERTEX_COUNT * 10) {
            System.err.println("Static Batch Full!!!");
            return null;
        }
        this.store(allData, this.endPointer);
        int i = 0;
        while (i < data.length) {
            MemorySlot bundle = MemorySlot.createDataSlot(this.endPointer, data[i]);
            MemorySlot.append(bundle, this.endSlot);
            this.endPointer += data[i].length;
            this.endSlot = bundle;
            slots[i] = bundle;
            ++i;
        }
        return slots;
    }

    protected int getVao() {
        return this.vao;
    }

    protected int getVertexCount() {
        return this.endPointer / 10;
    }

    protected void unmapMemory(MemorySlot slot) {
        MemorySlot nextSlot = slot.getNextSlot();
        MemorySlot previousSlot = slot.getPreviousSlot();
        if (nextSlot == null) {
            this.removeNextToEnd(slot, nextSlot, previousSlot);
        } else if (nextSlot.isGap()) {
            this.removeNextToGap(slot, nextSlot, previousSlot);
        } else {
            this.removeNextToData(slot, nextSlot, previousSlot);
        }
    }

    protected boolean refactor() {
        if (this.gaps.isEmpty()) {
            return false;
        }
        MemorySlot gap = this.gaps.remove(0);
        gap.getNextSlot().connectToPrevious(gap.getPreviousSlot());
        MemorySlot currentSlot = gap.getNextSlot();
        ArrayList<float[]> accumData = new ArrayList<float[]>();
        int length = 0;
        while (currentSlot != null && !currentSlot.isGap() && length < 300) {
            currentSlot.shiftLeft(gap.getLength());
            float[] data = currentSlot.getData();
            accumData.add(data);
            length += data.length / 10;
            currentSlot = currentSlot.getNextSlot();
        }
        if (currentSlot == null) {
            this.store(Maths.concatenateArrays(accumData), gap.getStartIndex());
            this.endPointer -= gap.getLength();
        } else {
            accumData.add(new float[gap.getLength()]);
            this.store(Maths.concatenateArrays(accumData), gap.getStartIndex());
            if (currentSlot.isGap()) {
                currentSlot.increaseStartIndex(-gap.getLength());
            } else {
                MemorySlot newGap = MemorySlot.createGap(currentSlot.getPreviousSlot().getEndIndex(), gap.getLength());
                this.gaps.add(newGap);
                newGap.connectToPrevious(currentSlot.getPreviousSlot());
                newGap.connectToNext(currentSlot);
            }
        }
        return true;
    }

    private MemorySlot storeEntityData(float[] data, int startIndex) {
        MemorySlot bundle = MemorySlot.createDataSlot(startIndex, data);
        this.store(data, bundle.getStartIndex());
        return bundle;
    }

    private void store(float[] data, int start) {
        Loader.storeDataInVbo(this.vbo, this.floatBuffer, data, start);
    }

    private void storeZeros(int start, int length) {
        float[] gap = new float[length];
        Loader.storeDataInVbo(this.vbo, this.floatBuffer, gap, start);
    }

    private void removeNextToGap(MemorySlot slot, MemorySlot nextSlot, MemorySlot previousSlot) {
        if (previousSlot != null && previousSlot.isGap()) {
            this.gaps.remove(nextSlot);
            previousSlot.connectToNext(nextSlot.getNextSlot());
            previousSlot.increaseEndIndex(slot.getLength() + nextSlot.getLength());
        } else {
            nextSlot.increaseStartIndex(-slot.getLength());
            nextSlot.connectToPrevious(previousSlot);
        }
        this.storeZeros(slot.getStartIndex(), slot.getLength());
        slot.clear();
    }

    private void removeNextToData(MemorySlot slot, MemorySlot nextSlot, MemorySlot previousSlot) {
        if (previousSlot != null && previousSlot.isGap()) {
            previousSlot.increaseEndIndex(slot.getLength());
            previousSlot.connectToNext(nextSlot);
        } else {
            MemorySlot gap = MemorySlot.createGap(slot.getStartIndex(), slot.getLength());
            this.gaps.add(gap);
            gap.connectToPrevious(previousSlot);
            gap.connectToNext(nextSlot);
        }
        this.storeZeros(slot.getStartIndex(), slot.getLength());
        slot.clear();
    }

    private void removeNextToEnd(MemorySlot slot, MemorySlot nextSlot, MemorySlot previousSlot) {
        if (previousSlot != null && previousSlot.isGap()) {
            this.gaps.remove(previousSlot);
            if (previousSlot.getPreviousSlot() != null) {
                previousSlot.getPreviousSlot().connectToNext(null);
            }
            this.endSlot = previousSlot.getPreviousSlot();
            this.endPointer -= slot.getLength() + previousSlot.getLength();
        } else {
            if (previousSlot != null) {
                previousSlot.connectToNext(null);
            }
            this.endSlot = previousSlot;
            this.endPointer -= slot.getLength();
        }
        slot.clear();
    }
}


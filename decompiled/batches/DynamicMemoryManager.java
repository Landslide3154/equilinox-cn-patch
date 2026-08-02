/*
 * Decompiled with CFR 0.152.
 */
package batches;

import basics.Loader;
import batches.BlueprintBundle;
import batches.MemorySlot;
import blueprints.Blueprint;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import toolbox.Maths;

public class DynamicMemoryManager {
    private int endPointer = 0;
    private MemorySlot endSlot = null;
    private List<MemorySlot> gaps = new ArrayList<MemorySlot>();
    private int vao = Loader.createVAO();
    private int vbo;
    private FloatBuffer buffer;

    protected DynamicMemoryManager(int maxVertexCount) {
        this.vbo = Loader.createEmptyInterleavedVBO(this.vao, maxVertexCount, 0, 4, 3, 3);
        this.buffer = BufferUtils.createFloatBuffer(maxVertexCount * 10);
    }

    protected void delete() {
        Loader.deleteVaoFromCache(this.vao);
    }

    protected BlueprintBundle allocateMemory(Blueprint blueprint) {
        float[] data = blueprint.getData();
        for (MemorySlot gap : this.gaps) {
            if (gap.getLength() < data.length) continue;
            BlueprintBundle bundle = this.storeBlueprintData(blueprint, data, gap.getStartIndex());
            MemorySlot.insertInGap(bundle, gap, this.gaps);
            return bundle;
        }
        BlueprintBundle bundle = this.storeBlueprintData(blueprint, data, this.endPointer);
        MemorySlot.append(bundle, this.endSlot);
        this.endPointer += data.length;
        System.out.println("Dynamic Batch filled " + (float)this.endPointer / (float)this.buffer.capacity() * 100.0f + "%");
        this.endSlot = bundle;
        return bundle;
    }

    protected int getVao() {
        return this.vao;
    }

    protected void unmapMemory(BlueprintBundle bundle) {
        MemorySlot nextSlot = bundle.getNextSlot();
        MemorySlot previousSlot = bundle.getPreviousSlot();
        if (nextSlot == null) {
            this.removeNextToEnd(bundle, nextSlot, previousSlot);
        } else if (nextSlot.isGap()) {
            this.removeNextToGap(bundle, nextSlot, previousSlot);
        } else {
            this.removeNextToData(bundle, nextSlot, previousSlot);
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
        while (currentSlot != null && !currentSlot.isGap()) {
            currentSlot.shiftLeft(gap.getLength());
            accumData.add(currentSlot.getData());
            currentSlot = currentSlot.getNextSlot();
        }
        this.store(Maths.concatenateArrays(accumData), gap.getStartIndex());
        if (currentSlot == null) {
            this.endPointer -= gap.getLength();
        } else {
            currentSlot.shiftLeft(gap.getLength());
        }
        return true;
    }

    private BlueprintBundle storeBlueprintData(Blueprint blueprint, float[] data, int startIndex) {
        BlueprintBundle bundle = new BlueprintBundle(blueprint, startIndex, data.length);
        this.store(data, bundle.getStartIndex());
        return bundle;
    }

    private void store(float[] data, int start) {
        Loader.storeDataInVbo(this.vbo, this.buffer, data, start);
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
    }
}


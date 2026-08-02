/*
 * Decompiled with CFR 0.152.
 */
package batchingSystem;

import batchingSystem.BiggestGapsFinder;
import batchingSystem.MemoryChain;
import batchingSystem.MemorySlot;
import batchingSystem.RemapSection;
import java.util.ArrayList;
import java.util.List;
import openglObjects.Vao;
import openglObjects.Vbo;

public class BatchMemory {
    private static final int MAX_REMAP = 15;
    private Vao vao;
    private Vbo vbo;
    private final MemoryChain memoryChain = new MemoryChain();
    private final BiggestGapsFinder gapFinder = new BiggestGapsFinder(this.memoryChain, 15);

    private BatchMemory() {
    }

    public RemapSection startRemapAdd(int requiredSize) {
        List<MemorySlot> gaps = this.gapFinder.findGaps();
        if (this.gapFinder.getBestScore() >= requiredSize) {
            return this.remapGaps(gaps, 0);
        }
        return this.startAppending();
    }

    public RemapSection startRemapRemove(MemorySlot slot) {
        if (slot.getNextSlot() == null) {
            slot.remove();
            return this.startAppending();
        }
        MemorySlot gap = slot.remove();
        if (gap.getLength() > 15) {
            return new RemapSection(this, gap, gap.getPreviousSlot(), gap.getStartIndex(), gap.getEndIndex(), gap.getLength());
        }
        List<MemorySlot> gaps = this.getFollowingGaps(gap.getStartIndex());
        return this.remapGaps(gaps, gap.getEndIndex());
    }

    public RemapSection startAppending() {
        MemorySlot previousSlot = this.memoryChain.getEndSlot();
        int remapEnd = this.memoryChain.getEndPointer();
        return new RemapSection(this, previousSlot, remapEnd, remapEnd, 15);
    }

    public MemoryChain getMemoryChain() {
        return this.memoryChain;
    }

    public void clear() {
    }

    public void delete() {
    }

    public static BatchMemory initEmptyBatch() {
        return new BatchMemory();
    }

    public static BatchMemory initBatch() {
        return new BatchMemory();
    }

    protected void remap(byte[] data, int start) {
        System.out.println("REMAP: " + start + " - " + (start + data.length));
    }

    private RemapSection remapGaps(List<MemorySlot> gaps, int minEnd) {
        int startRemap = gaps.get(0).getStartIndex();
        MemorySlot lastGap = gaps.get(gaps.size() - 1);
        int endRemap = Math.max(lastGap.getStartIndex(), minEnd);
        MemorySlot previousSlot = gaps.get(0).getPreviousSlot();
        this.mergeGaps(gaps);
        int maxEnd = startRemap + 15;
        if (maxEnd >= this.memoryChain.getEndPointer()) {
            lastGap.remove();
            int remaining = maxEnd - this.memoryChain.getEndPointer();
            return new RemapSection(this, previousSlot, startRemap, this.memoryChain.getEndPointer(), remaining);
        }
        int remaining = lastGap.getEndIndex() < maxEnd ? lastGap.getLength() : maxEnd - lastGap.getStartIndex();
        return new RemapSection(this, lastGap, previousSlot, startRemap, endRemap, remaining);
    }

    private void mergeGaps(List<MemorySlot> gaps) {
        int i = 0;
        while (i < gaps.size() - 1) {
            gaps.get(i).remove();
            ++i;
        }
    }

    private List<MemorySlot> getFollowingGaps(int gapStart) {
        ArrayList<MemorySlot> gaps = new ArrayList<MemorySlot>();
        int maxStart = gapStart + 15;
        for (MemorySlot gap : this.memoryChain.getGaps()) {
            if (gap.getEndIndex() <= gapStart) continue;
            if (gap.getStartIndex() > maxStart) {
                return gaps;
            }
            gaps.add(gap);
        }
        return gaps;
    }
}


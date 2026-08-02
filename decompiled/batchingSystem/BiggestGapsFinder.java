/*
 * Decompiled with CFR 0.152.
 */
package batchingSystem;

import batchingSystem.MemoryChain;
import batchingSystem.MemorySlot;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BiggestGapsFinder {
    private final int maxRemapSize;
    private final MemoryChain chain;
    private List<MemorySlot> currentlyChecking = new ArrayList<MemorySlot>();
    private List<MemorySlot> currentBestGaps = new ArrayList<MemorySlot>();
    private int currentBestLength = 0;

    public BiggestGapsFinder(MemoryChain chain, int maxRemapSize) {
        this.chain = chain;
        this.maxRemapSize = maxRemapSize;
    }

    public List<MemorySlot> findGaps() {
        this.resetResult();
        for (MemorySlot slot : this.chain.getGaps()) {
            this.currentlyChecking.add(slot);
            this.removeOutOfRangeGaps(slot.getStartIndex());
            int score = this.calculateCurrentScore(slot.getEndIndex());
            this.testScoreWithCurrentBest(score);
        }
        return this.currentBestGaps;
    }

    public int getBestScore() {
        return this.currentBestLength;
    }

    private void resetResult() {
        this.currentBestGaps.clear();
        this.currentlyChecking.clear();
        this.currentBestLength = 0;
    }

    private void removeOutOfRangeGaps(int lastGapStart) {
        Iterator<MemorySlot> iterator = this.currentlyChecking.iterator();
        while (iterator.hasNext()) {
            MemorySlot slot = iterator.next();
            if (lastGapStart - slot.getStartIndex() > this.maxRemapSize) {
                iterator.remove();
                continue;
            }
            return;
        }
    }

    private int calculateCurrentScore(int lastGapEnd) {
        int score = 0;
        for (MemorySlot gap : this.currentlyChecking) {
            score += gap.getLength();
        }
        int decrease = Math.max(0, lastGapEnd - this.currentlyChecking.get(0).getStartIndex() - this.maxRemapSize);
        return score -= decrease;
    }

    private void testScoreWithCurrentBest(int currentScore) {
        if (currentScore > this.currentBestLength) {
            this.currentBestGaps.clear();
            this.currentBestGaps.addAll(this.currentlyChecking);
            this.currentBestLength = currentScore;
        }
    }
}


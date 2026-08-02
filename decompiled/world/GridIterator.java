/*
 * Decompiled with CFR 0.152.
 */
package world;

import world.EntitiesGrid;
import world.GridSection;

public class GridIterator {
    private EntitiesGrid grid;
    private int startX;
    private int startZ;
    private int currentX = 0;
    private int currentZ = 0;
    private int range;
    private boolean skipHalf = false;
    private boolean skipEven = false;
    private GridSection nextSection = null;

    public GridIterator(EntitiesGrid grid, int startX, int startZ, int range) {
        this.grid = grid;
        this.range = range;
        this.startX = startX;
        this.startZ = startZ;
        this.findNext();
    }

    public GridIterator(EntitiesGrid grid, int startX, int startZ, int range, boolean skipHalf, boolean skipEven) {
        this.grid = grid;
        this.range = range;
        this.startX = startX;
        this.startZ = startZ;
        this.skipHalf = skipHalf;
        this.skipEven = skipEven;
        this.findNext();
    }

    public boolean hasNext() {
        return this.nextSection != null;
    }

    public GridSection next() {
        GridSection section = this.nextSection;
        this.increase();
        this.findNext();
        return section;
    }

    private void findNext() {
        this.nextSection = null;
        while (!(this.currentX >= this.range || this.getNextSection() && this.isValidSquare())) {
            this.increase();
        }
    }

    private boolean getNextSection() {
        this.nextSection = this.grid.getSection(this.currentX + this.startX, this.currentZ + this.startZ);
        return this.nextSection != null;
    }

    private boolean isValidSquare() {
        if (!this.skipHalf) {
            return true;
        }
        boolean even = (this.currentZ + this.currentX) % 2 == 0;
        return even ^ this.skipEven;
    }

    private void increase() {
        ++this.currentZ;
        if (this.currentZ >= this.range) {
            this.currentZ = 0;
            ++this.currentX;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import toolbox.Highlight;

public class WorldHighlights {
    private static final WorldHighlights highlights = new WorldHighlights();
    private Highlight highlight1 = new Highlight();
    private Highlight highlight2 = new Highlight();

    private WorldHighlights() {
    }

    public static WorldHighlights getHighlights() {
        return highlights;
    }

    public int getHighlightCount() {
        int count = 0;
        if (this.highlight1.isShown()) {
            ++count;
        }
        if (this.highlight2.isShown()) {
            ++count;
        }
        return count;
    }

    public void updateHighlights() {
        this.highlight1.update();
        this.highlight2.update();
    }

    public Highlight getHighlight1() {
        return this.highlight1;
    }

    public Highlight getHighlight2() {
        return this.highlight2;
    }

    public Highlight getActiveHighlight() {
        if (this.highlight1.isShown()) {
            return this.highlight1;
        }
        return this.highlight2;
    }
}


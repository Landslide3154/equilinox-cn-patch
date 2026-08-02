/*
 * Decompiled with CFR 0.152.
 */
package batches;

public enum BatchType {
    NORMAL(false, true, true, true),
    CLUTTER(true, false, false, false),
    UNDER_WATER(false, false, true, true);

    private boolean fadeOutDistant;
    private boolean hasShadows;
    private boolean hasReflection;
    private boolean seenUnderWater;

    private BatchType(boolean fadeOut, boolean shadows, boolean reflection, boolean underWater) {
        this.fadeOutDistant = fadeOut;
        this.hasShadows = shadows;
        this.hasReflection = reflection;
        this.seenUnderWater = underWater;
    }

    public boolean isFadeOutDistant() {
        return this.fadeOutDistant;
    }

    public boolean hasShadows() {
        return this.hasShadows;
    }

    public boolean hasReflection() {
        return this.hasReflection;
    }

    public boolean isSeenUnderWater() {
        return this.seenUnderWater;
    }
}


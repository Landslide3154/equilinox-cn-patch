/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

public enum BurrowStage {
    LOCATING("Going to burrow"),
    ENTERING("Entering Burrow"),
    BURROWING("Burrowing"),
    EXITING("Exiting Burrow");

    private String name;

    private BurrowStage(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}


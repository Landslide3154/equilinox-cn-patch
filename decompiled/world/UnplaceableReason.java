/*
 * Decompiled with CFR 0.152.
 */
package world;

import languages.GameText;

public enum UnplaceableReason {
    NO_PROBLEM(GameText.getText(1086)),
    NEEDS_DRY_LAND(GameText.getText(1087)),
    NEEDS_WATER(GameText.getText(1088)),
    ENTITY_TOO_CLOSE(GameText.getText(1089)),
    OFF_WORLD(GameText.getText(1098));

    private String name;

    private UnplaceableReason(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}


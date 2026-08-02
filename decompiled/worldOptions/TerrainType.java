/*
 * Decompiled with CFR 0.152.
 */
package worldOptions;

import languages.GameText;

public enum TerrainType {
    FLAT(GameText.getText(703), 0.66f),
    NORMAL(GameText.getText(704), 0.85f),
    HILLY(GameText.getText(705), 1.1f);

    protected final String name;
    protected final float smoothnessValue;

    private TerrainType(String name, float smoothnessValue) {
        this.name = name;
        this.smoothnessValue = smoothnessValue;
    }

    public String toString() {
        return this.name;
    }
}


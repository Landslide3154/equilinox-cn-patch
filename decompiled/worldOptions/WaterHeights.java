/*
 * Decompiled with CFR 0.152.
 */
package worldOptions;

import languages.GameText;

public enum WaterHeights {
    DRY(GameText.getText(699), -4.0f),
    NORMAL(GameText.getText(700), -2.0f),
    WET(GameText.getText(701), 0.0f);

    protected final String name;
    protected final float waterHeight;

    private WaterHeights(String name, float waterHeight) {
        this.name = name;
        this.waterHeight = waterHeight;
    }

    public String toString() {
        return this.name;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package food;

import languages.GameText;

public enum FoodSectionType {
    WHOLE,
    FRUIT,
    UPROOT,
    SAMPLE,
    TO_SHARE,
    HONEY(GameText.getText(987)),
    ROOT_VEG;

    private final boolean overwritesName;
    private String name;

    private FoodSectionType() {
        this.overwritesName = false;
    }

    private FoodSectionType(String name) {
        this.overwritesName = true;
        this.name = name;
    }

    public String getOverwriteName() {
        return this.name;
    }

    public boolean overwritesName() {
        return this.overwritesName;
    }
}


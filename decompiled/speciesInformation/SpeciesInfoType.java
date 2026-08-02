/*
 * Decompiled with CFR 0.152.
 */
package speciesInformation;

import languages.GameText;

public enum SpeciesInfoType {
    GENERAL(GameText.getText(442)),
    PREFERENCES(GameText.getText(443)),
    ABILITIES(GameText.getText(444));

    private String name;

    private SpeciesInfoType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}


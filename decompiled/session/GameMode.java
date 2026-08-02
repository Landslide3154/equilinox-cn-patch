/*
 * Decompiled with CFR 0.152.
 */
package session;

import languages.GameText;

public enum GameMode {
    NORMAL(1149),
    SIM(1144),
    BUILD(1145);

    private final int textId;

    private GameMode(int id) {
        this.textId = id;
    }

    public String toString() {
        return GameText.getText(this.textId);
    }
}


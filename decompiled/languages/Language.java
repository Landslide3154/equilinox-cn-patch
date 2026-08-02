/*
 * Decompiled with CFR 0.152.
 */
package languages;

public enum Language {
    ENGLISH("English");

    private final String name;

    private Language(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}


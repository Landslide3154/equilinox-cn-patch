/*
 * Decompiled with CFR 0.152.
 */
package languages;

public class ComplexString {
    private final String original;

    public ComplexString(String original) {
        this.original = original;
    }

    public String getString(String ... inputs) {
        String newString = this.original;
        int i = 0;
        while (i < inputs.length) {
            newString = newString.replaceFirst("\\$" + i + "\\$", inputs[i]);
            ++i;
        }
        return newString;
    }
}


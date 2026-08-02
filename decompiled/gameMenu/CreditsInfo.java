/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import java.util.LinkedHashMap;
import java.util.Map;

public class CreditsInfo {
    private int lineCount = 0;
    private Map<String, String[]> credits = new LinkedHashMap<String, String[]>();

    public void addInfo(String title, String ... names) {
        this.credits.put(title, names);
        this.lineCount += 1 + (names.length - 1) / 2;
    }

    public Map<String, String[]> getCredits() {
        return this.credits;
    }

    public int getLineCount() {
        return this.lineCount;
    }
}


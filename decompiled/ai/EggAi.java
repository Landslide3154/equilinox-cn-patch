/*
 * Decompiled with CFR 0.152.
 */
package ai;

import aiBasics.AiRoutine;
import languages.GameText;

public class EggAi
implements AiRoutine {
    private static final String DESC = GameText.getText(190);

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public void interrupt() {
    }

    @Override
    public String getDescription() {
        return DESC;
    }
}


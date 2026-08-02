/*
 * Decompiled with CFR 0.152.
 */
package effects;

import effects.Effect;
import effects.PacifyEffect;
import utils.CSVReader;

public class EffectLoader {
    private static final int PACIFY = 0;

    public static Effect loadEffect(CSVReader reader) {
        int type = reader.getNextLabelInt();
        if (type == 0) {
            float time = reader.getNextLabelFloat();
            return new PacifyEffect(time);
        }
        return null;
    }
}


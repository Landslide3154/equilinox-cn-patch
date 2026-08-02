/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAiBlueprint;
import death.DeathAiType;
import utils.CSVReader;

public class DeathAICreator {
    public static DeathAiBlueprint loadDeathAi(CSVReader reader) {
        DeathAiType type = DeathAiType.valueOf(reader.getNextString());
        return type.load(reader);
    }
}


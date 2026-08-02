/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAi;
import instances.Entity;
import utils.CSVReader;

public interface DeathAiBlueprint {
    public DeathAiBlueprint loadInfo(CSVReader var1);

    public DeathAi createInstance(Entity var1);

    public DeathAi createInstance(Entity var1, Object var2);
}


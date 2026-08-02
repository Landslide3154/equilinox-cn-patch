/*
 * Decompiled with CFR 0.152.
 */
package healer;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import death.DeathAICreator;
import death.DeathAiBlueprint;
import healer.HealerCompBlueprint;
import utils.CSVReader;

public class HealerCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        DeathAiBlueprint death = DeathAICreator.loadDeathAi(reader);
        return new HealerCompBlueprint(death);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


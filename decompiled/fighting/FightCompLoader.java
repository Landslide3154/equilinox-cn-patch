/*
 * Decompiled with CFR 0.152.
 */
package fighting;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import fighting.FightCompBlueprint;
import utils.CSVReader;

public class FightCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int attackDamage = reader.getNextLabelInt();
        boolean revenge = reader.getNextLabelBool();
        int animId = reader.getNextLabelInt();
        float biteRange = reader.getNextLabelFloat();
        float pauseTime = reader.getNextLabelFloat();
        return new FightCompBlueprint(attackDamage, revenge, animId, biteRange, pauseTime);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


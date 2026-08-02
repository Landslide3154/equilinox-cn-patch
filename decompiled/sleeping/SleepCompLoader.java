/*
 * Decompiled with CFR 0.152.
 */
package sleeping;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import sleeping.SleepCompBlueprint;
import utils.CSVReader;

public class SleepCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float sleepStartMin = reader.getNextLabelFloat() / 24.0f;
        float sleepStartMax = reader.getNextLabelFloat() / 24.0f;
        float sleepEndMin = reader.getNextLabelFloat() / 24.0f;
        float sleepEndMax = reader.getNextLabelFloat() / 24.0f;
        return new SleepCompBlueprint(sleepStartMin, sleepStartMax, sleepEndMin, sleepEndMax);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


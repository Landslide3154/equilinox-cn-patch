/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import fruit.FruiterCompBlueprint;
import utils.CSVReader;

public class FruiterCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int index = reader.getNextLabelInt();
        int count = reader.getNextLabelInt();
        float fruitTime = 5.0f;
        if (!reader.isEndOfLine()) {
            fruitTime = reader.getNextLabelFloat();
        }
        return new FruiterCompBlueprint(index, count, fruitTime);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


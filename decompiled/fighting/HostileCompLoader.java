/*
 * Decompiled with CFR 0.152.
 */
package fighting;

import blueprints.Blueprint;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import fighting.HostileCompBlueprint;
import utils.CSVReader;

public class HostileCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float averageAttackTime = reader.getNextLabelFloat();
        Classification enemyClass = Classifier.getClassification(reader.getNextLabelString());
        boolean notify = reader.getNextLabelBool();
        return new HostileCompBlueprint(enemyClass, averageAttackTime, notify);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


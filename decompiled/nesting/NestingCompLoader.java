/*
 * Decompiled with CFR 0.152.
 */
package nesting;

import blueprints.Blueprint;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import nesting.NestingCompBlueprint;
import utils.CSVReader;

public class NestingCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        Classification classification = Classifier.getClassification(reader.getNextLabelString());
        int hatchingStage = reader.getNextLabelInt();
        boolean decreasesModel = reader.getNextLabelBool();
        return new NestingCompBlueprint(classification, hatchingStage, decreasesModel);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


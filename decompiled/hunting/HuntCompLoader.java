/*
 * Decompiled with CFR 0.152.
 */
package hunting;

import blueprints.Blueprint;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import hunting.HuntCompBlueprint;
import utils.CSVReader;

public class HuntCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int range = reader.getNextLabelInt();
        Classification[] preyClasses = new Classification[reader.getNextLabelInt()];
        int i = 0;
        while (i < preyClasses.length) {
            preyClasses[i] = Classifier.getClassification(reader.getNextLabelString());
            ++i;
        }
        boolean huntsYoung = reader.getNextLabelBool();
        boolean huntsOld = reader.getNextLabelBool();
        return new HuntCompBlueprint(range, preyClasses, huntsYoung, huntsOld);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


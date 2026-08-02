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
import hunting.FleeCompBlueprint;
import meerkats.HoleHideCompBlueprint;
import shellHide.ShellHideCompBlueprint;
import utils.CSVReader;

public class FleeCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        String first = reader.getNextString();
        float safeRange = reader.getNextFloat();
        if (first.equals("TURTLE")) {
            return new ShellHideCompBlueprint(safeRange);
        }
        if (first.equals("MEERKAT")) {
            return new HoleHideCompBlueprint(safeRange);
        }
        boolean canBeOnLand = reader.getNextLabelBool();
        boolean canSwim = reader.getNextLabelBool();
        Classification hidingSpot = null;
        if (!reader.isEndOfLine()) {
            hidingSpot = Classifier.getClassification(reader.getNextLabelString());
        }
        return new FleeCompBlueprint(safeRange, hidingSpot, canSwim, canBeOnLand);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


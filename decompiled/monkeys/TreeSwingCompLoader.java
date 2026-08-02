/*
 * Decompiled with CFR 0.152.
 */
package monkeys;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import monkeys.TreeSwingCompBlueprint;
import org.lwjgl.util.vector.Vector3f;
import utils.CSVReader;

public class TreeSwingCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        Vector3f handPos = reader.getNextLabelVector();
        return new TreeSwingCompBlueprint(handPos);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


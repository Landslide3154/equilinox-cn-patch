/*
 * Decompiled with CFR 0.152.
 */
package monkeys;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import monkeys.ClingCompBlueprint;
import org.lwjgl.util.vector.Vector3f;
import utils.CSVReader;

public class ClingCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        Vector3f offset = reader.getNextLabelVector();
        float xRot = reader.getNextLabelFloat();
        return new ClingCompBlueprint(offset, xRot);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


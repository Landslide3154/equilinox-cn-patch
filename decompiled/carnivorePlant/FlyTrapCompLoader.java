/*
 * Decompiled with CFR 0.152.
 */
package carnivorePlant;

import blueprints.Blueprint;
import carnivorePlant.FlyTrapCompBlueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utils.CSVReader;

public class FlyTrapCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        Vector3f startPos = reader.getNextLabelVector();
        return new FlyTrapCompBlueprint(new Vector4f(startPos.x, startPos.y, startPos.z, 1.0f));
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


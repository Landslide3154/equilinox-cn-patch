/*
 * Decompiled with CFR 0.152.
 */
package spitting;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import spitting.SpitCompBlueprint;
import utils.CSVReader;

public class SpitCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        Vector3f spitPos = reader.getNextLabelVector();
        Vector4f spitPosition = new Vector4f(spitPos.x, spitPos.y, spitPos.z, 1.0f);
        return new SpitCompBlueprint(spitPosition);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package perching;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import org.lwjgl.util.vector.Vector4f;
import perching.PerchCompBlueprint;
import utils.CSVReader;

public class PerchCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int slotCount = reader.getNextInt();
        Vector4f[] slots = new Vector4f[slotCount];
        int i = 0;
        while (i < slotCount) {
            slots[i] = new Vector4f(reader.getNextFloat(), reader.getNextFloat(), reader.getNextFloat(), 1.0f);
            ++i;
        }
        return new PerchCompBlueprint(slots);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


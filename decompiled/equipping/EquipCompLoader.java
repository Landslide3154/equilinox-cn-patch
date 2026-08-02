/*
 * Decompiled with CFR 0.152.
 */
package equipping;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import equipping.EquipCompBlueprint;
import org.lwjgl.util.vector.Vector3f;
import utils.CSVReader;

public class EquipCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int count = reader.getNextLabelInt();
        Vector3f[] positions = new Vector3f[count];
        int i = 0;
        while (i < count) {
            positions[i] = reader.getNextLabelVector();
            ++i;
        }
        return new EquipCompBlueprint(positions);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


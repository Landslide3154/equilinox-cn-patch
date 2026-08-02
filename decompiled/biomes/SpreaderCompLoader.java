/*
 * Decompiled with CFR 0.152.
 */
package biomes;

import biomes.Biome;
import biomes.SpreaderCompBlueprint;
import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import org.lwjgl.util.vector.Vector3f;
import utils.CSVReader;

public class SpreaderCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        Biome biome = Biome.values()[reader.getNextInt()];
        Vector3f colourOffsets = reader.getNextVector();
        float strength = reader.getNextFloat();
        int distance = reader.getNextInt();
        return new SpreaderCompBlueprint(biome, strength, distance, colourOffsets);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


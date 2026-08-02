/*
 * Decompiled with CFR 0.152.
 */
package particleComponent;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import particleComponent.ParticleCompBlueprint;
import particleComponent.ParticleSystemLoader;
import particles.ParticleSystem;
import utils.CSVReader;

public class ParticleCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        ParticleSystem system = ParticleSystemLoader.loadParticleSystem(reader);
        float range = reader.getNextLabelFloat();
        int[] stages = reader.getNextLabelIntArray();
        boolean takesMaterial = reader.getNextLabelBool();
        return new ParticleCompBlueprint(system, range, takesMaterial, stages);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }

    private int[] loadModelStages(CSVReader reader) {
        int count = reader.getNextInt();
        int[] stages = new int[count];
        int i = 0;
        while (i < count) {
            stages[i] = reader.getNextInt();
            ++i;
        }
        return stages;
    }
}


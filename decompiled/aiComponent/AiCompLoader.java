/*
 * Decompiled with CFR 0.152.
 */
package aiComponent;

import aiComponent.AiCompBlueprint;
import aiComponent.AiProgramType;
import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import utils.CSVReader;

public class AiCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        AiProgramType programType = AiProgramType.valueOf(reader.getNextString());
        return new AiCompBlueprint(programType.loadProgramBlueprint(reader));
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


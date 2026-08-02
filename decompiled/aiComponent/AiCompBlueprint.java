/*
 * Decompiled with CFR 0.152.
 */
package aiComponent;

import aiComponent.AiComponent;
import aiComponent.AiProgramBlueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class AiCompBlueprint
extends ComponentBlueprint {
    private AiProgramBlueprint idleProgramBlueprint;

    protected AiCompBlueprint(AiProgramBlueprint idleProgramBlueprint) {
        super(ComponentType.AI);
        this.idleProgramBlueprint = idleProgramBlueprint;
    }

    protected AiProgramBlueprint getAiProgramBlueprint() {
        return this.idleProgramBlueprint;
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }

    @Override
    public Component createInstance() {
        return new AiComponent(this);
    }
}


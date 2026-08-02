/*
 * Decompiled with CFR 0.152.
 */
package healer;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import death.DeathAiBlueprint;
import healer.HealerComponent;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class HealerCompBlueprint
extends ComponentBlueprint {
    private final DeathAiBlueprint death;

    protected HealerCompBlueprint(DeathAiBlueprint death) {
        super(ComponentType.HEALER);
        this.death = death;
    }

    protected DeathAiBlueprint getDeathAnim() {
        return this.death;
    }

    @Override
    public Component createInstance() {
        return new HealerComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


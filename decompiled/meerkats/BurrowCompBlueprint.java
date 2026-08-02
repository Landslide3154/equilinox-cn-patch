/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import meerkats.BurrowComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BurrowCompBlueprint
extends ComponentBlueprint {
    protected BurrowCompBlueprint() {
        super(ComponentType.BURROW);
    }

    @Override
    public Component createInstance() {
        return new BurrowComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", "Creates a network of burrows and tunnels in its territory."));
    }
}


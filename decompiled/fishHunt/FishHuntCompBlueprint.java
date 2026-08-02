/*
 * Decompiled with CFR 0.152.
 */
package fishHunt;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import fishHunt.FishHuntComponent;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FishHuntCompBlueprint
extends ComponentBlueprint {
    protected FishHuntCompBlueprint() {
        super(ComponentType.FISH_HUNT);
    }

    @Override
    public Component createInstance() {
        return new FishHuntComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import fruit.DecayComponent;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class DecayCompBlueprint
extends ComponentBlueprint {
    protected final float lifeInHours;

    protected DecayCompBlueprint(float lifeInDays) {
        super(ComponentType.DECAY);
        this.lifeInHours = lifeInDays;
    }

    @Override
    public Component createInstance() {
        return new DecayComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


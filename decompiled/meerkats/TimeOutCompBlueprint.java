/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import meerkats.TimeOutComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class TimeOutCompBlueprint
extends ComponentBlueprint {
    private final float decayTime;

    protected TimeOutCompBlueprint(float decayTime) {
        super(ComponentType.TIME_OUT);
        this.decayTime = decayTime;
    }

    public float getDecayTime() {
        return this.decayTime;
    }

    @Override
    public Component createInstance() {
        return new TimeOutComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


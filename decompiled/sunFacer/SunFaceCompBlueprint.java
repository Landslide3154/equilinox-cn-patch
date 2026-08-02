/*
 * Decompiled with CFR 0.152.
 */
package sunFacer;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import sunFacer.SunFaceComponent;

public class SunFaceCompBlueprint
extends ComponentBlueprint {
    protected SunFaceCompBlueprint() {
        super(ComponentType.SUN_FACER);
    }

    @Override
    public Component createInstance() {
        return new SunFaceComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", "Turns to face the sun."));
    }
}


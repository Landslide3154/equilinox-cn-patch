/*
 * Decompiled with CFR 0.152.
 */
package flying;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import flying.FlyMovement;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FlyBlueprint
extends ComponentBlueprint {
    public static final int ID = 10;

    public FlyBlueprint() {
        super(ComponentType.MOVEMENT);
    }

    @Override
    public Component createInstance() {
        return new FlyMovement(this, 100.0f);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


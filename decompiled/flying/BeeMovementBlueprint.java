/*
 * Decompiled with CFR 0.152.
 */
package flying;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import flying.BeeMovement;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BeeMovementBlueprint
extends ComponentBlueprint {
    public static final int ID = 11;
    public final float cruiseHeight;

    public BeeMovementBlueprint(float height) {
        super(ComponentType.MOVEMENT);
        this.cruiseHeight = height;
    }

    @Override
    public Component createInstance() {
        return new BeeMovement(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


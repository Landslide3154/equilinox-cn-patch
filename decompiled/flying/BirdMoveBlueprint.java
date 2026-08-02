/*
 * Decompiled with CFR 0.152.
 */
package flying;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import flying.BirdMovement;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BirdMoveBlueprint
extends ComponentBlueprint {
    public static final int ID = 12;
    protected final float glideDown;

    public BirdMoveBlueprint(float glideDown) {
        super(ComponentType.MOVEMENT);
        this.glideDown = glideDown;
    }

    @Override
    public Component createInstance() {
        return new BirdMovement(this, 150.0f);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


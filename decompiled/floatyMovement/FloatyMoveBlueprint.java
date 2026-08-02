/*
 * Decompiled with CFR 0.152.
 */
package floatyMovement;

import baseMovement.BaseMovementBlueprint;
import componentArchitecture.Component;
import floatyMovement.FloatyMovement;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FloatyMoveBlueprint
extends BaseMovementBlueprint {
    public static final int ID = 21;
    private static final float BASE_SPEED = 0.08f;
    private static final float ROT_SPEED = 24.0f;

    public FloatyMoveBlueprint() {
        super(0.08f, 24.0f, false);
    }

    @Override
    public Component createInstance() {
        return new FloatyMovement(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package dolphinMovement;

import baseMovement.BaseMovementBlueprint;
import componentArchitecture.Component;
import dolphinMovement.DolphinMovement;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class DolphinMoveBlueprint
extends BaseMovementBlueprint {
    public static final int ID = 45;
    private static final float SWIM_HEIGHT = 0.4f;
    protected final int rotType;
    protected final float minRot;
    protected final float maxRot;
    protected final float rockSpeed;
    protected final float swimHeight;
    protected final float swimFactor;
    protected float swimInertia = 0.25f;

    public DolphinMoveBlueprint(float speed, int rotType, float minRot, float maxRot, float rockSpeed) {
        super(speed, 180.0f);
        this.rotType = rotType;
        this.minRot = minRot;
        this.maxRot = maxRot;
        this.rockSpeed = rockSpeed;
        this.swimHeight = 0.4f;
        this.swimFactor = 1.0f;
    }

    @Override
    public Component createInstance() {
        return new DolphinMovement(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


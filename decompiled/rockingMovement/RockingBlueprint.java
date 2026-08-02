/*
 * Decompiled with CFR 0.152.
 */
package rockingMovement;

import baseMovement.BaseMovementBlueprint;
import componentArchitecture.Component;
import java.util.List;
import java.util.Map;
import rockingMovement.RockingMovement;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class RockingBlueprint
extends BaseMovementBlueprint {
    public static final int ID = 9;
    private static final float SWIM_HEIGHT = 4.0f;
    protected final int rotType;
    protected final float minRot;
    protected final float maxRot;
    protected final float rockSpeed;
    protected final float swimHeight;
    protected final float swimFactor;
    protected float swimInertia = 1.0f;

    public RockingBlueprint(float speed, int rotType, float minRot, float maxRot, float rockSpeed) {
        super(speed, 180.0f);
        this.rotType = rotType;
        this.minRot = minRot;
        this.maxRot = maxRot;
        this.rockSpeed = rockSpeed;
        this.swimHeight = 4.0f;
        this.swimFactor = 1.0f;
    }

    public RockingBlueprint(float speed, int rotType, float minRot, float maxRot, float rockSpeed, float swimHeight) {
        super(speed, 180.0f);
        this.rotType = rotType;
        this.minRot = minRot;
        this.maxRot = maxRot;
        this.rockSpeed = rockSpeed;
        this.swimHeight = swimHeight;
        this.swimFactor = 1.0f;
    }

    public RockingBlueprint(float speed, int rotType, float minRot, float maxRot, float rockSpeed, float swimHeight, boolean eggStage, float swimFactor) {
        super(speed, 180.0f, eggStage);
        this.rotType = rotType;
        this.minRot = minRot;
        this.maxRot = maxRot;
        this.rockSpeed = rockSpeed;
        this.swimHeight = swimHeight;
        this.swimFactor = swimFactor;
    }

    public RockingBlueprint(float speed, int rotType, float minRot, float maxRot, float rockSpeed, float swimHeight, boolean eggStage, float swimFactor, float swimInertia) {
        super(speed, 180.0f, eggStage);
        this.rotType = rotType;
        this.minRot = minRot;
        this.maxRot = maxRot;
        this.rockSpeed = rockSpeed;
        this.swimHeight = swimHeight;
        this.swimFactor = swimFactor;
        this.swimInertia = swimInertia;
    }

    @Override
    public Component createInstance() {
        return new RockingMovement(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


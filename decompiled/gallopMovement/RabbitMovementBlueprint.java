/*
 * Decompiled with CFR 0.152.
 */
package gallopMovement;

import baseMovement.BaseMovementBlueprint;
import componentArchitecture.Component;
import gallopMovement.RabbitMovement;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import utils.CSVReader;

public class RabbitMovementBlueprint
extends BaseMovementBlueprint {
    public static final int ID = 7;
    protected final float bouncePower;
    protected final float upRotSpeed;
    protected final float downRotSpeed;
    protected final float[] frontZ;
    protected final float[] backZ;

    public RabbitMovementBlueprint(CSVReader reader) {
        super(reader.getNextLabelFloat(), 180.0f);
        this.bouncePower = reader.getNextLabelFloat();
        this.upRotSpeed = reader.getNextLabelFloat();
        this.downRotSpeed = reader.getNextLabelFloat();
        this.frontZ = reader.getNextLabelFloatArray();
        this.backZ = reader.getNextLabelFloatArray();
    }

    protected float getFrontZ(int modelStage) {
        return this.frontZ[modelStage];
    }

    protected float getBackZ(int modelStage) {
        return this.backZ[modelStage];
    }

    @Override
    public Component createInstance() {
        return new RabbitMovement(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


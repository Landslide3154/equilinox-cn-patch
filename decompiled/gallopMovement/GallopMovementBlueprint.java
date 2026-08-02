/*
 * Decompiled with CFR 0.152.
 */
package gallopMovement;

import baseMovement.BaseMovementBlueprint;
import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import gallopMovement.GallopMovement;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import utils.CSVReader;

public class GallopMovementBlueprint
extends BaseMovementBlueprint {
    private static final String BOUNCE_POW = GameText.getText(933);
    public static final int ID = 13;
    private final float[] frontZ;
    private final float[] backZ;
    protected final float upRotSpeed;
    protected final float gravityFactor;

    public GallopMovementBlueprint(CSVReader reader) {
        super(reader.getNextLabelFloat());
        super.addTrait(new FloatTraitBlueprint(BOUNCE_POW, reader.getNextLabelFloat(), 3.0f, 9.0f));
        this.upRotSpeed = reader.getNextLabelFloat();
        this.gravityFactor = reader.getNextLabelFloat();
        this.frontZ = reader.getNextLabelFloatArray();
        this.backZ = reader.getNextLabelFloatArray();
        if (!reader.isEndOfLine()) {
            super.setRunFactor(reader.getNextLabelFloat());
        }
    }

    @Override
    public Component createInstance() {
        return new GallopMovement(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }

    protected float getFrontZ(int modelStage) {
        return this.frontZ[modelStage];
    }

    protected float getBackZ(int modelStage) {
        return this.backZ[modelStage];
    }
}


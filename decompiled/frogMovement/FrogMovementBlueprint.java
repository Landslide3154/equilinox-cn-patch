/*
 * Decompiled with CFR 0.152.
 */
package frogMovement;

import baseMovement.BaseMovementBlueprint;
import blueprints.Blueprint;
import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import frogMovement.FrogMovement;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FrogMovementBlueprint
extends BaseMovementBlueprint {
    private static final String BOUNCE_POW = GameText.getText(933);
    public static final int ID = 6;
    protected final float waitTime;
    protected final float bounciness;

    public FrogMovementBlueprint(Blueprint species, float speed, float bouncePower, float waitTime, float bounciness) {
        super(speed);
        this.waitTime = waitTime;
        this.bounciness = bounciness;
        super.addTrait(new FloatTraitBlueprint(BOUNCE_POW, bouncePower, 2.0f, 7.0f));
    }

    @Override
    public Component createInstance() {
        return new FrogMovement(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


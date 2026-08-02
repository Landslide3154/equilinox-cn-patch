/*
 * Decompiled with CFR 0.152.
 */
package bounceMovement;

import baseMovement.BaseMovementBlueprint;
import bounceMovement.BounceBaseMovement;
import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BounceBaseBlueprint
extends BaseMovementBlueprint {
    private static final String BOUNCE_POW = GameText.getText(933);
    private static final String BOINGS = GameText.getText(934);
    public static final int ID = 14;

    public BounceBaseBlueprint(float speed, float rotSpeed, float bouncePower) {
        super(speed, rotSpeed);
        this.addTraits(bouncePower);
    }

    private void addTraits(float bouncePower) {
        super.addTrait(new FloatTraitBlueprint(BOUNCE_POW, bouncePower, 2.0f, 11.5f){

            @Override
            public String formatTrait(float value) {
                return String.valueOf(Math.round(value * 10.0f)) + " " + BOINGS;
            }
        });
    }

    @Override
    public Component createInstance() {
        return new BounceBaseMovement(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


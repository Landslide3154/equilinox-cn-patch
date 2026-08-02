/*
 * Decompiled with CFR 0.152.
 */
package stinging;

import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import stinging.StingingComponent;

public class StingingCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(1152);
    private static final String POWER = GameText.getText(1154);

    protected StingingCompBlueprint() {
        super(ComponentType.STINGING);
        super.addTrait(new FloatTraitBlueprint(POWER, 1.0f, 1.8f, 15.5f){

            @Override
            public String formatTrait(float value) {
                return String.valueOf(Math.round(value * 100.0f)) + "%";
            }
        });
    }

    @Override
    public Component createInstance() {
        return new StingingComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}


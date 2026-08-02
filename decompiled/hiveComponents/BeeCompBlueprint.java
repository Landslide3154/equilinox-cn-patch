/*
 * Decompiled with CFR 0.152.
 */
package hiveComponents;

import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import hiveComponents.BeeComponent;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BeeCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(456);
    private static final String HONEY_TRAIT = GameText.getText(457);

    protected BeeCompBlueprint() {
        super(ComponentType.BEE);
        super.addTrait(new FloatTraitBlueprint(HONEY_TRAIT, 1.0f, 2.0f, 8.0f){

            @Override
            public String formatTrait(float value) {
                return String.valueOf(Math.round(value * 100.0f)) + "%";
            }
        });
    }

    @Override
    public Component createInstance() {
        return new BeeComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}


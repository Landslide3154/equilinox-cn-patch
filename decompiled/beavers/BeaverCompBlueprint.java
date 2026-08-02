/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import beavers.BeaverComponent;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BeaverCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(1060);

    protected BeaverCompBlueprint() {
        super(ComponentType.BEAVER);
    }

    @Override
    public Component createInstance() {
        return new BeaverComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}


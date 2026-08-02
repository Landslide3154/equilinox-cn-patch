/*
 * Decompiled with CFR 0.152.
 */
package treeCharging;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import treeCharging.TreeChargeComponent;

public class TreeChargeCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(402);

    protected TreeChargeCompBlueprint() {
        super(ComponentType.CHARGE);
    }

    @Override
    public Component createInstance() {
        return new TreeChargeComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}


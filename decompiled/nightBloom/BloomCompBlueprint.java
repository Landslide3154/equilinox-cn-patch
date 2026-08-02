/*
 * Decompiled with CFR 0.152.
 */
package nightBloom;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import nightBloom.BloomComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BloomCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(1157);

    protected BloomCompBlueprint() {
        super(ComponentType.BLOOM);
    }

    @Override
    public Component createInstance() {
        return new BloomComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}


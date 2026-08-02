/*
 * Decompiled with CFR 0.152.
 */
package panic;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import panic.PanicComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class PanicCompBlueprint
extends ComponentBlueprint {
    private static final String DESC = GameText.getText(407);

    protected PanicCompBlueprint() {
        super(ComponentType.PANIC);
    }

    @Override
    public Component createInstance() {
        return new PanicComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", DESC));
    }
}


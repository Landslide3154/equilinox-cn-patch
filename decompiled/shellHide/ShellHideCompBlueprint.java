/*
 * Decompiled with CFR 0.152.
 */
package shellHide;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import shellHide.ShellHideComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class ShellHideCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(642);
    protected final float safeRangeSquared;

    public ShellHideCompBlueprint(float safeRange) {
        super(ComponentType.FLEE);
        this.safeRangeSquared = safeRange * safeRange;
    }

    @Override
    public Component createInstance() {
        return new ShellHideComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}


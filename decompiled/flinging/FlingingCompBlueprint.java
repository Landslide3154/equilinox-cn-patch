/*
 * Decompiled with CFR 0.152.
 */
package flinging;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import flinging.FlingingComponent;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FlingingCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(416);
    protected final float minTime;
    protected final float maxTime;

    protected FlingingCompBlueprint(float minTime, float maxTime) {
        super(ComponentType.FLINGING);
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    @Override
    public Component createInstance() {
        return new FlingingComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}


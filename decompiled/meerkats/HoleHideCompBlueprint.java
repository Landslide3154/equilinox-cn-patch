/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import meerkats.HoleHideComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class HoleHideCompBlueprint
extends ComponentBlueprint {
    private final float safeRangeSquared;

    public HoleHideCompBlueprint(float safeRange) {
        super(ComponentType.FLEE);
        this.safeRangeSquared = safeRange * safeRange;
    }

    public float getSafeRangeSquared() {
        return this.safeRangeSquared;
    }

    @Override
    public Component createInstance() {
        return new HoleHideComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", "Can hide in burrows to escape from predators."));
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package perching;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import perching.PercherComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class PercherCompBlueprint
extends ComponentBlueprint {
    protected final boolean removeOnPerchRemoval;

    protected PercherCompBlueprint(boolean removeOnRemoval) {
        super(ComponentType.PERCHER);
        this.removeOnPerchRemoval = removeOnRemoval;
    }

    @Override
    public Component createInstance() {
        return new PercherComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package peacock;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import peacock.PeacockComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class PeacockCompBlueprint
extends ComponentBlueprint {
    protected PeacockCompBlueprint() {
        super(ComponentType.PEACOCK);
    }

    @Override
    public Component createInstance() {
        return new PeacockComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


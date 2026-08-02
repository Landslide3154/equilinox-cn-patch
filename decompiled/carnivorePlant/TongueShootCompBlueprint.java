/*
 * Decompiled with CFR 0.152.
 */
package carnivorePlant;

import carnivorePlant.TongueShootComp;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class TongueShootCompBlueprint
extends ComponentBlueprint {
    protected TongueShootCompBlueprint() {
        super(ComponentType.TONGUE_SHOOT);
    }

    @Override
    public Component createInstance() {
        return new TongueShootComp(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


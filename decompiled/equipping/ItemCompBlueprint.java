/*
 * Decompiled with CFR 0.152.
 */
package equipping;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import equipping.ItemComponent;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class ItemCompBlueprint
extends ComponentBlueprint {
    protected final float averageDecayTime;

    protected ItemCompBlueprint(float decayTime) {
        super(ComponentType.ITEM);
        this.averageDecayTime = decayTime;
    }

    @Override
    public Component createInstance() {
        return new ItemComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


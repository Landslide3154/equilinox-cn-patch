/*
 * Decompiled with CFR 0.152.
 */
package sound;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import sound.SoundComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class SoundCompBlueprint
extends ComponentBlueprint {
    protected SoundCompBlueprint() {
        super(ComponentType.SOUND);
    }

    @Override
    public Component createInstance() {
        return new SoundComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


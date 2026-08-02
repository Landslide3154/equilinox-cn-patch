/*
 * Decompiled with CFR 0.152.
 */
package carnivorePlant;

import carnivorePlant.FlyTrapComponent;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.vector.Vector4f;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FlyTrapCompBlueprint
extends ComponentBlueprint {
    protected final Vector4f startPos;

    protected FlyTrapCompBlueprint(Vector4f startPos) {
        super(ComponentType.FLY_TRAP);
        this.startPos = startPos;
    }

    @Override
    public Component createInstance() {
        return new FlyTrapComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Abiltity", "Catches and consumes nearby insects."));
    }
}


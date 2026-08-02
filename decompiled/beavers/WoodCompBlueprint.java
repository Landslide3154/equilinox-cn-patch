/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import beavers.WoodComponent;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import toolbox.Colour;

public class WoodCompBlueprint
extends ComponentBlueprint {
    protected final float cuttingTime;
    protected final float barkFactor;
    protected final Colour barkColour;

    protected WoodCompBlueprint(float cuttingTime, float barkFactor, Colour colour) {
        super(ComponentType.WOOD);
        this.cuttingTime = cuttingTime;
        this.barkFactor = barkFactor;
        this.barkColour = colour;
    }

    @Override
    public Component createInstance() {
        return new WoodComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine("Cutting Time", String.valueOf(this.cuttingTime) + " seconds"));
        info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine("Bark Percentage", String.valueOf(Math.round(this.barkFactor * 100.0f)) + "%"));
    }
}


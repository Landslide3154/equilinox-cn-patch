/*
 * Decompiled with CFR 0.152.
 */
package birdHunt;

import birdHunt.BirdHuntComponent;
import classification.Classifier;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BirdHuntCompBlueprint
extends ComponentBlueprint {
    protected BirdHuntCompBlueprint() {
        super(ComponentType.BIRD_HUNT);
    }

    @Override
    public Component createInstance() {
        return new BirdHuntComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.PREFERENCES).add(new SpeciesInfoLine("Hunts", Classifier.getClassification("ahs").getName()));
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("ability", "Hunts small animals and carries them back to the nest area."));
    }
}


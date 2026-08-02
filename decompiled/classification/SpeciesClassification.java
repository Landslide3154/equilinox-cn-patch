/*
 * Decompiled with CFR 0.152.
 */
package classification;

import blueprints.Blueprint;
import classification.Classification;
import resourceManagement.BlueprintRepository;

public class SpeciesClassification
extends Classification {
    private final int speciesId;

    protected SpeciesClassification(String speciesId, Classification parent) {
        super(speciesId, parent);
        this.speciesId = Integer.parseInt(speciesId);
    }

    @Override
    protected String getIdentificationString() {
        return Integer.toString(this.speciesId);
    }

    @Override
    public String getName() {
        Blueprint species = BlueprintRepository.getBlueprint(this.speciesId);
        return species.getName();
    }

    @Override
    public boolean isSpecies() {
        return true;
    }
}


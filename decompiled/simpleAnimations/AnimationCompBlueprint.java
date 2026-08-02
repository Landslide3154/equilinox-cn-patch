/*
 * Decompiled with CFR 0.152.
 */
package simpleAnimations;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import simpleAnimations.AnimationBlueprint;
import simpleAnimations.AnimationComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class AnimationCompBlueprint
extends ComponentBlueprint {
    private final AnimationBlueprint animBlueprint;

    protected AnimationCompBlueprint(AnimationBlueprint animBlueprint) {
        super(ComponentType.ANIMATION);
        this.animBlueprint = animBlueprint;
    }

    @Override
    public Component createInstance() {
        return new AnimationComponent(this);
    }

    public AnimationBlueprint getAnimBlueprint() {
        return this.animBlueprint;
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}


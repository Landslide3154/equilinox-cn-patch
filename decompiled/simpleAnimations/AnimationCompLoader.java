/*
 * Decompiled with CFR 0.152.
 */
package simpleAnimations;

import blueprints.Blueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import simpleAnimations.AnimationBlueprint;
import simpleAnimations.AnimationCompBlueprint;
import simpleAnimations.AnimationFactory;
import utils.CSVReader;

public class AnimationCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        AnimationBlueprint animBlueprint = AnimationFactory.createAnimation(reader);
        return new AnimationCompBlueprint(animBlueprint);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        return null;
    }
}


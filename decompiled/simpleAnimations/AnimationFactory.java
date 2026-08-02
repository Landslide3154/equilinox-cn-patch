/*
 * Decompiled with CFR 0.152.
 */
package simpleAnimations;

import simpleAnimations.AnimationBlueprint;
import simpleAnimations.BounceAnimation;
import utils.CSVReader;

public class AnimationFactory {
    private static final int BOUNCE = 1;

    public static AnimationBlueprint createAnimation(CSVReader reader) {
        int id = reader.getNextInt();
        if (id == 1) {
            return new BounceAnimation.BounceAnimationBlueprint(reader);
        }
        return new BounceAnimation.BounceAnimationBlueprint(reader);
    }
}


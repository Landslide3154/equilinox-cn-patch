/*
 * Decompiled with CFR 0.152.
 */
package simpleAnimations;

import simpleAnimations.Animation;
import toolbox.Transformation;

public interface AnimationBlueprint {
    public Animation createInstance(Transformation var1);
}


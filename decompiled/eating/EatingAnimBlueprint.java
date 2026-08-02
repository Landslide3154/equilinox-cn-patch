/*
 * Decompiled with CFR 0.152.
 */
package eating;

import baseMovement.MovementComp;
import eating.EatingAnimation;
import eating.StandardEatingAi;
import toolbox.Transformation;

public interface EatingAnimBlueprint {
    public EatingAnimation createInstance(Transformation var1, MovementComp var2, StandardEatingAi var3);
}


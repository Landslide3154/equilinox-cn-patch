/*
 * Decompiled with CFR 0.152.
 */
package eating;

import baseMovement.MovementComp;
import eating.DiggingEater;
import eating.DivingEater;
import eating.EatingAnimBlueprint;
import eating.EatingAnimation;
import eating.InstantEatAnim;
import eating.SimpleEatAnimation;
import eating.StandardEatingAi;
import eating.ThrowingEater;
import toolbox.Transformation;

public class TempEatAnimBlueprint
implements EatingAnimBlueprint {
    private final int id;

    public TempEatAnimBlueprint(int id) {
        this.id = id;
    }

    @Override
    public EatingAnimation createInstance(Transformation transform, MovementComp mover, StandardEatingAi eater) {
        if (this.id == 1) {
            return new ThrowingEater(transform, eater, mover);
        }
        if (this.id == 2) {
            return new DiggingEater(transform, mover, eater);
        }
        if (this.id == 3) {
            return new DivingEater(mover, transform, eater);
        }
        if (this.id == 4) {
            return new InstantEatAnim(eater);
        }
        return new SimpleEatAnimation(transform, mover, eater);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package fishHunt;

import baseMovement.MovementComp;
import componentArchitecture.ComponentType;
import eating.EatingComponent;
import eating.FoodFinder;
import eating.StandardEatingAi;
import hunting.PreyComp;
import instances.Entity;
import interpolation.Timer;
import toolbox.Transformation;

public class FishEatingAi
extends StandardEatingAi {
    private Timer timer = Timer.createOneOffTimer(6.0f, true).start();

    public FishEatingAi(FoodFinder foodTarget, Transformation transform, MovementComp mover, EatingComponent eatingComp, boolean urgent) {
        super(foodTarget, transform, mover, eatingComp, urgent);
    }

    @Override
    public boolean carryOut() {
        Entity target = this.getTarget();
        if (target != null) {
            PreyComp fleeComp = (PreyComp)((Object)target.getComponent(ComponentType.FLEE));
            if (this.timer.check() || fleeComp != null && fleeComp.isInvulnerable()) {
                return true;
            }
        }
        return super.carryOut();
    }

    @Override
    public void interrupt() {
        this.timer.reset();
        super.interrupt();
    }
}


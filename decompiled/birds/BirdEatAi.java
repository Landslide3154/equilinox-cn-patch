/*
 * Decompiled with CFR 0.152.
 */
package birds;

import baseMovement.MovementComp;
import eating.EatingComponent;
import eating.FoodFinder;
import eating.StandardEatingAi;
import growth.GrowthComponent;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class BirdEatAi
extends StandardEatingAi {
    private MovementComp mover;
    private GrowthComponent growth;

    public BirdEatAi(FoodFinder foodTarget, Transformation transform, MovementComp mover, EatingComponent eatingComp, boolean urgent, GrowthComponent growth) {
        super(foodTarget, transform, mover, eatingComp, urgent);
        this.mover = mover;
        this.growth = growth;
    }

    @Override
    protected boolean goToFood(Vector3f targetPos, boolean runs, float radius) {
        if (this.growth.getGrowthFactor() > 0.5f) {
            Vector3f aim = new Vector3f(targetPos);
            aim.y += 0.01f;
            return this.mover.land(aim);
        }
        return this.mover.goToTarget(targetPos, runs, radius);
    }
}


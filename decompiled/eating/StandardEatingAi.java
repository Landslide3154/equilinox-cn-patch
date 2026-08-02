/*
 * Decompiled with CFR 0.152.
 */
package eating;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import eating.EatingAnimation;
import eating.EatingCompBlueprint;
import eating.EatingComponent;
import eating.FoodFinder;
import instances.Entity;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class StandardEatingAi
implements Ai {
    private static final String DESC = GameText.getText(180);
    private static final float PRIORITY = 7.0f;
    private static final float HIGH_PRIORITY = 9.5f;
    private final MovementComp mover;
    private final EatingComponent eatingComp;
    private final float priority;
    private final FoodFinder foodFinder;
    private boolean nomming = false;
    private EatingAnimation eatingAnimation;

    public StandardEatingAi(FoodFinder foodTarget, Transformation transform, MovementComp mover, EatingComponent eatingComp, boolean urgent) {
        this.mover = mover;
        this.eatingComp = eatingComp;
        this.foodFinder = foodTarget;
        this.eatingAnimation = this.foodFinder.getChosenDietOption().getEatingAnimation().createInstance(transform, mover, this);
        this.priority = urgent ? 9.5f : 7.0f;
    }

    @Override
    public boolean carryOut() {
        boolean targetAvailable = this.foodFinder.checkTarget();
        if (!targetAvailable && !this.nomming) {
            return true;
        }
        if (this.nomming) {
            return this.eatingAnimation.doNomming(targetAvailable);
        }
        Vector3f targetPos = this.foodFinder.getTarget().getTransform().getPosition();
        this.nomming = this.goToFood(targetPos, this.eatingComp.runsToFood(), this.getEatingRadius());
        return false;
    }

    protected boolean goToFood(Vector3f targetPos, boolean runs, float radius) {
        return this.mover.goToTarget(targetPos, runs, radius);
    }

    @Override
    public float getPriority() {
        return this.priority;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.eatingComp;
    }

    protected Entity getTarget() {
        return this.foodFinder.getTarget();
    }

    protected void eat() {
        this.eatingComp.eat(this.foodFinder.getTargetFoodComp(), this.foodFinder.getFoodType());
    }

    private float getEatingRadius() {
        return ((EatingCompBlueprint)this.eatingComp.getBlueprint()).getEatingRadius();
    }

    @Override
    public void interrupt() {
        if (this.nomming) {
            this.nomming = false;
            this.eatingAnimation.interrupt();
        }
    }

    @Override
    public String getDescription() {
        return DESC;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package eating;

import baseMovement.MovementComp;
import eating.EatingAnimation;
import eating.StandardEatingAi;
import gameManaging.GameManager;
import toolbox.Transformation;

public class SimpleEatAnimation
implements EatingAnimation {
    private static final float EAT_TIME = 2.0f;
    private final Transformation transform;
    private final MovementComp mover;
    private final StandardEatingAi eater;
    private boolean ready = false;
    private float nomTime = 2.0f;

    public SimpleEatAnimation(Transformation transform, MovementComp mover, StandardEatingAi eater) {
        this.transform = transform;
        this.mover = mover;
        this.eater = eater;
    }

    @Override
    public boolean doNomming(boolean targetAvailable) {
        if (!targetAvailable) {
            this.transform.setXRotation(0.0f);
            return true;
        }
        if (!this.checkReady()) {
            return false;
        }
        this.nomTime -= GameManager.getGameSeconds();
        float bob = (float)Math.sin(Math.PI * 2 * (double)this.nomTime);
        this.transform.setXRotation(bob * 10.0f);
        if (this.nomTime < 0.0f) {
            this.transform.setXRotation(0.0f);
            this.eater.eat();
            return true;
        }
        return false;
    }

    private boolean checkReady() {
        if (!this.ready) {
            this.ready = this.mover.normalize();
        }
        return this.ready;
    }

    @Override
    public void interrupt() {
        this.transform.setXRotation(0.0f);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package ai;

import ai.StopStartAi;
import baseMovement.MovementComp;
import components.InformationComponent;
import toolbox.Transformation;

public class StopStartWithSwimAi
extends StopStartAi {
    private final MovementComp mover;

    public StopStartWithSwimAi(MovementComp mover, Transformation transform, InformationComponent info) {
        super(mover, transform, info, false);
        this.mover = mover;
        super.switchToWander();
    }

    @Override
    protected void updateWander() {
        boolean reached = this.mover.goToTarget(this.getTarget(), false, 0.2f);
        if (reached) {
            if (this.mover.isSwimming()) {
                this.switchToWander();
            } else {
                this.switchToIdle();
            }
        }
    }
}


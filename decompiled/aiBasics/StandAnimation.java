/*
 * Decompiled with CFR 0.152.
 */
package aiBasics;

import baseMovement.MovementComp;

public class StandAnimation {
    private final MovementComp mover;
    private boolean standing = false;

    public StandAnimation(MovementComp mover) {
        this.mover = mover;
    }

    public void update() {
        if (!this.standing && this.mover.normalize()) {
            this.standing = true;
        }
    }

    public void init() {
        this.standing = false;
    }
}


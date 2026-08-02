/*
 * Decompiled with CFR 0.152.
 */
package peacock;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import instances.Entity;
import peacock.PeacockComponent;

public class PeacockAi
implements Ai {
    private Entity target = null;
    private PeacockComponent peacockComp;
    private MovementComp mover;

    public PeacockAi(Entity target, PeacockComponent peacockComp, MovementComp mover) {
        this.peacockComp = peacockComp;
        this.mover = mover;
        this.target = target;
    }

    @Override
    public boolean carryOut() {
        if (this.target != null && !this.target.isDead() && !this.target.isGrabbed()) {
            boolean reached = this.mover.goToTargetAndFace(this.target.getTransform().getPosition(), true, 0.5f);
            return reached;
        }
        return true;
    }

    @Override
    public float getPriority() {
        return 10.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.peacockComp;
    }

    @Override
    public void interrupt() {
        this.target = null;
    }

    @Override
    public String getDescription() {
        return "Flaring";
    }
}


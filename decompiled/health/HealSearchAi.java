/*
 * Decompiled with CFR 0.152.
 */
package health;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.ComponentType;
import healer.HealerComponent;
import health.Disease;
import instances.Entity;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;

public class HealSearchAi
implements Ai {
    private final float PRIORITY = 8.1f;
    private final float HEAL_RADIUS = 0.2f;
    private static final String DESC = GameText.getText(1011);
    private final MovementComp mover;
    private final Entity healingObject;
    private final Disease diseaseComp;

    public HealSearchAi(Entity healingObject, Disease diseaseComp, MovementComp mover) {
        this.healingObject = healingObject;
        this.diseaseComp = diseaseComp;
        this.mover = mover;
    }

    @Override
    public boolean carryOut() {
        if (this.objectUnavailable()) {
            return true;
        }
        Vector3f aimPosition = this.healingObject.getTransform().getPosition();
        boolean reached = this.mover.goToTarget(aimPosition, true, 0.2f);
        if (reached) {
            HealerComponent healer = (HealerComponent)this.healingObject.getComponent(ComponentType.HEALER);
            healer.heal(this.diseaseComp);
            return true;
        }
        return false;
    }

    @Override
    public float getPriority() {
        return 8.1f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.diseaseComp;
    }

    @Override
    public void interrupt() {
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    private boolean objectUnavailable() {
        return this.healingObject.isDead() || this.healingObject.isGrabbed();
    }
}


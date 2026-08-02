/*
 * Decompiled with CFR 0.152.
 */
package panic;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import gameManaging.GameManager;
import interpolation.Timer;
import org.lwjgl.util.vector.Vector3f;
import panic.PanicComponent;

public class PanicAi
implements Ai {
    private static final float TURN_SPEED = 80.0f;
    private static final float PRIORITY = 9.0f;
    private MovementComp mover;
    private PanicComponent panicComp;
    private Timer timer = Timer.createLoopingTimer(5.0f, 10.0f, true);
    private static final float PAD = 0.5f;

    protected PanicAi(MovementComp mover, PanicComponent panicComp) {
        this.mover = mover;
        this.panicComp = panicComp;
    }

    @Override
    public boolean carryOut() {
        if (this.isTooNearEdge()) {
            return true;
        }
        this.mover.run();
        this.mover.increaseTurn(GameManager.getGameSeconds() * 80.0f);
        return this.timer.check();
    }

    @Override
    public float getPriority() {
        return 9.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.panicComp;
    }

    @Override
    public void interrupt() {
    }

    @Override
    public String getDescription() {
        return "Panicking";
    }

    private boolean isTooNearEdge() {
        Vector3f pos = this.mover.getTransform().getPosition();
        return pos.x > 99.5f || pos.x < 0.5f || pos.z > 99.5f || pos.z < 0.5f;
    }
}


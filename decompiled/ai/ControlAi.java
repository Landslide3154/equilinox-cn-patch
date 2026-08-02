/*
 * Decompiled with CFR 0.152.
 */
package ai;

import aiComponent.Ai;
import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.ControlBehaviour;
import gameManaging.GameManager;
import java.util.List;
import languages.GameText;
import toolbox.MyKeyboard;
import toolbox.Transformation;

public class ControlAi
implements Ai {
    private static final String DESC = GameText.getText(184);
    private static final float TURN_SPEED = 180.0f;
    private final MovementComp movement;
    private final Transformation transform;
    private final AiComponent aiComp;
    private final List<ControlBehaviour> behaviours;

    public ControlAi(Transformation transform, MovementComp movement, AiComponent aiComp, List<ControlBehaviour> behaviours) {
        this.movement = movement;
        this.transform = transform;
        this.aiComp = aiComp;
        this.behaviours = behaviours;
    }

    @Override
    public boolean carryOut() {
        MyKeyboard keyboard = MyKeyboard.getKeyboard();
        if (keyboard.isKeyDown(17)) {
            if (keyboard.isKeyDown(29)) {
                this.movement.run();
            } else {
                this.movement.walkForward();
            }
        }
        if (keyboard.isKeyDown(30)) {
            this.movement.increaseTurn(GameManager.getGameSeconds() * 180.0f);
        } else if (keyboard.isKeyDown(32)) {
            this.movement.increaseTurn(GameManager.getGameSeconds() * -180.0f);
        }
        for (ControlBehaviour behaviour : this.behaviours) {
            if (behaviour.isContinuous()) {
                if (!keyboard.isKeyDown(behaviour.getKey())) continue;
                behaviour.doAction();
                continue;
            }
            if (!keyboard.keyDownEventOccurred(behaviour.getKey())) continue;
            behaviour.doAction();
        }
        return false;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public float getPriority() {
        return 100000.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.aiComp;
    }

    @Override
    public void interrupt() {
    }
}


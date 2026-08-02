/*
 * Decompiled with CFR 0.152.
 */
package aiBasics;

import aiBasics.AiRoutine;
import aiComponent.Ai;
import aiComponent.AiProvidingComponent;

public class BasicAi
implements Ai {
    private final AiRoutine aiRoutine;
    private final AiProvidingComponent component;
    private final int priority;

    public BasicAi(AiRoutine aiRoutine, int priority, AiProvidingComponent component) {
        this.aiRoutine = aiRoutine;
        this.priority = priority;
        this.component = component;
    }

    @Override
    public void interrupt() {
        this.aiRoutine.interrupt();
    }

    @Override
    public String getDescription() {
        return this.aiRoutine.getDescription();
    }

    @Override
    public boolean carryOut() {
        return this.aiRoutine.update();
    }

    @Override
    public float getPriority() {
        return this.priority;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.component;
    }
}


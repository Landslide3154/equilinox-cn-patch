/*
 * Decompiled with CFR 0.152.
 */
package aiBasics;

import aiBasics.AiRoutine;
import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import growth.GrowthComponent;

public class AgeDependentAI
implements Ai {
    private final GrowthComponent growth;
    private final AiRoutine[] routines;

    public AgeDependentAI(GrowthComponent growth, AiRoutine ... routines) {
        this.routines = routines;
        this.growth = growth;
    }

    @Override
    public boolean carryOut() {
        this.routines[this.growth.getStageNumber()].update();
        return false;
    }

    @Override
    public String getDescription() {
        return this.routines[this.growth.getStageNumber()].getDescription();
    }

    @Override
    public void interrupt() {
        this.routines[this.growth.getStageNumber()].interrupt();
    }

    @Override
    public float getPriority() {
        return 0.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return null;
    }
}


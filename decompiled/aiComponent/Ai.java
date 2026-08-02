/*
 * Decompiled with CFR 0.152.
 */
package aiComponent;

import aiComponent.AiProvidingComponent;

public interface Ai {
    public boolean carryOut();

    public float getPriority();

    public AiProvidingComponent getComponent();

    public void interrupt();

    public String getDescription();
}


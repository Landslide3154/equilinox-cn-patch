/*
 * Decompiled with CFR 0.152.
 */
package fighting;

import toolbox.Transformation;

public interface AttackAnimation {
    public boolean carryOut();

    public void init(Transformation var1, Transformation var2);

    public boolean needsNormalized();

    public float getDuration();
}


/*
 * Decompiled with CFR 0.152.
 */
package hunting;

import instances.Entity;

public interface PreyComp {
    public boolean isInvulnerable();

    public void alertToDanger(Entity var1);

    public float getSafeRangeSquared();
}


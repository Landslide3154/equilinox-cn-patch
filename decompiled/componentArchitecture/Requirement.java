/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import breedingTrees.ReqInfo;
import instances.Entity;
import java.util.List;

public interface Requirement {
    public boolean check(Entity var1);

    public void getGuiInfo(List<ReqInfo> var1);

    public boolean isSecret();
}


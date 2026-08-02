/*
 * Decompiled with CFR 0.152.
 */
package effects;

import instances.Entity;
import java.util.List;
import speciesInformation.SpeciesInfoLine;

public interface Effect {
    public void apply(Entity var1);

    public void getInfo(List<SpeciesInfoLine> var1);
}


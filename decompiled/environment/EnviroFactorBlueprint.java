/*
 * Decompiled with CFR 0.152.
 */
package environment;

import environment.EnviroFactor;
import gridLayout.FilterId;
import speciesInformation.SpeciesInfoLine;

public interface EnviroFactorBlueprint
extends Comparable<EnviroFactorBlueprint> {
    public EnviroFactor createInstance();

    public SpeciesInfoLine getInfo();

    public void addFilterValues(FilterId var1);

    public int getPriority();
}


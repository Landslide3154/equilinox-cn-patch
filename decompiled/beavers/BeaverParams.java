/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;

public class BeaverParams
extends ComponentParams {
    protected final boolean denBuildingImpossible;

    public BeaverParams(boolean denBuildingImpossible) {
        super(ComponentType.BEAVER);
        this.denBuildingImpossible = denBuildingImpossible;
    }
}


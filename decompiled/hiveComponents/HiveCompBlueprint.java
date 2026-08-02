/*
 * Decompiled with CFR 0.152.
 */
package hiveComponents;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import hiveComponents.HiveComponent;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class HiveCompBlueprint
extends ComponentBlueprint {
    protected final int maxHoney;
    protected final int startStage;
    protected final int stageCount;

    protected HiveCompBlueprint(int maxHoney, int startStage, int stageCount) {
        super(ComponentType.HIVE);
        this.maxHoney = maxHoney;
        this.startStage = startStage;
        this.stageCount = stageCount;
    }

    @Override
    public Component createInstance() {
        return new HiveComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> infos) {
    }
}


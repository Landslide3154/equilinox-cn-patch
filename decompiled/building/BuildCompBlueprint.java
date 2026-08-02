/*
 * Decompiled with CFR 0.152.
 */
package building;

import building.BuildComponent;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BuildCompBlueprint
extends ComponentBlueprint {
    protected final int stageCount;
    protected final int totalBuildPoints;
    protected final int fullyBuiltStage;

    protected BuildCompBlueprint(int stageCount, int totalBuildPoints) {
        super(ComponentType.BUILD);
        this.stageCount = stageCount;
        this.totalBuildPoints = totalBuildPoints;
        this.fullyBuiltStage = totalBuildPoints;
    }

    protected BuildCompBlueprint(int stageCount, int totalBuildPoints, int fullyBuildStage) {
        super(ComponentType.BUILD);
        this.stageCount = stageCount;
        this.totalBuildPoints = totalBuildPoints;
        this.fullyBuiltStage = fullyBuildStage;
    }

    @Override
    public Component createInstance() {
        return new BuildComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine("Total Build Points", String.valueOf(this.totalBuildPoints) + " points"));
    }
}


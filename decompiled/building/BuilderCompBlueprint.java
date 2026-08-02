/*
 * Decompiled with CFR 0.152.
 */
package building;

import blueprints.Blueprint;
import building.BuilderComponent;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import java.util.List;
import java.util.Map;
import languages.ComplexString;
import languages.GameText;
import resourceManagement.BlueprintRepository;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class BuilderCompBlueprint
extends ComponentBlueprint {
    private static final ComplexString ABILITY = GameText.getComplexText(431);
    protected final int buildModelId;
    protected final int buildPoints;
    protected final boolean needsPerch;
    protected final float buildAgeFactor;
    protected final float averageBuildTime;
    protected final float startBuildingTime;

    protected BuilderCompBlueprint(int buildModelId, int buildPoints, boolean needsPerch, float averageBuildTime, float buildAge, float startBuildingTime) {
        super(ComponentType.BUILDER);
        this.buildModelId = buildModelId;
        this.buildPoints = buildPoints;
        this.averageBuildTime = averageBuildTime;
        this.buildAgeFactor = buildAge;
        this.needsPerch = needsPerch;
        this.startBuildingTime = startBuildingTime;
    }

    @Override
    public Component createInstance() {
        return new BuilderComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> infos) {
        Blueprint blueprint = BlueprintRepository.getBlueprint(this.buildModelId);
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)blueprint.getComponent(ComponentType.INFO);
        if (info != null) {
            infos.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY.getString(info.getName())));
        }
        infos.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine("Building Speed", String.valueOf(this.buildPoints) + " points per visit"));
    }
}


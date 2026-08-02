/*
 * Decompiled with CFR 0.152.
 */
package health;

import blueprints.Blueprint;
import breeding.BreedingCompBlueprint;
import breedingTrees.ReqInfo;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import death.DeathAICreator;
import death.DeathAiBlueprint;
import environment.EnviroCompBlueprint;
import environment.EnvironmentComponentLoader;
import health.Disease;
import health.LifeCompBlueprint;
import health.LifeComponent;
import instances.Entity;
import java.util.List;
import utils.CSVReader;

public class LifeCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        float averagePop = reader.getNextLabelFloat();
        float averageLife = reader.getNextLabelFloat();
        float[] popFactors = reader.getNextLabelFloatArray();
        BreedingCompBlueprint breedingBlueprint = BreedingCompBlueprint.load(reader, false);
        DeathAiBlueprint deathAi = DeathAICreator.loadDeathAi(reader);
        EnviroCompBlueprint enviro = EnvironmentComponentLoader.loadEnviroBlueprint(reader);
        if (!reader.isEndOfLine()) {
            int defPoints = reader.getNextLabelInt();
            return new LifeCompBlueprint(averagePop, averageLife, defPoints, popFactors, deathAi, breedingBlueprint, enviro, blueprint.isAnimal());
        }
        return new LifeCompBlueprint(averagePop, averageLife, popFactors, deathAi, breedingBlueprint, enviro, blueprint.isAnimal());
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        int type = reader.getNextLabelInt();
        if (type == 1) {
            return this.createDiseaseRequirement();
        }
        if (type == 2) {
            return EnvironmentComponentLoader.loadRequirement(reader);
        }
        return null;
    }

    private Requirement createDiseaseRequirement() {
        return new Requirement(){

            @Override
            public boolean check(Entity entity) {
                LifeComponent wellbeing = (LifeComponent)entity.getComponent(ComponentType.LIFE);
                Disease disease = wellbeing.getDiseaseComponent();
                if (disease != null) {
                    return disease.isDiseased();
                }
                return false;
            }

            @Override
            public void getGuiInfo(List<ReqInfo> components) {
                components.add(new ReqInfo("Status", "Diseased"));
            }

            @Override
            public boolean isSecret() {
                return true;
            }
        };
    }
}


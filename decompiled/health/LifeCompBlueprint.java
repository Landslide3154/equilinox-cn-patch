/*
 * Decompiled with CFR 0.152.
 */
package health;

import breeding.BreedingCompBlueprint;
import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import death.DeathAi;
import death.DeathAiBlueprint;
import environment.EnviroCompBlueprint;
import health.LifeComponent;
import instances.Entity;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import time.Calendar;

public class LifeCompBlueprint
extends ComponentBlueprint {
    private static final int STANDARD_DEF_POINTS = 50;
    private static final String LIFE_STAT = GameText.getText(194);
    private static final String DEF_STAT = GameText.getText(193);
    private static final String DISEASE_RES = GameText.getText(871);
    public final BreedingCompBlueprint breedInfo;
    public final EnviroCompBlueprint enviroBlueprint;
    public final float averagePopulation;
    protected final float averageLifeLength;
    protected final int defencePoints;
    private float[] populationFactors;
    private DeathAiBlueprint deathAi;
    private boolean showDefPoints = false;

    protected LifeCompBlueprint(float averagePopulation, float averageLifeLength, int defencePoints, float[] popFactors, DeathAiBlueprint deathAi, BreedingCompBlueprint breedBlueprint, EnviroCompBlueprint enviroBlueprint, boolean isAnimal) {
        super(ComponentType.LIFE);
        this.averagePopulation = averagePopulation;
        this.averageLifeLength = averageLifeLength;
        this.defencePoints = defencePoints;
        this.showDefPoints = true;
        this.breedInfo = breedBlueprint;
        this.deathAi = deathAi;
        this.enviroBlueprint = enviroBlueprint;
        this.populationFactors = popFactors;
        if (isAnimal) {
            super.addTrait(new FloatTraitBlueprint(DISEASE_RES, 1.0f, 7.0f, 11.0f));
        }
    }

    protected LifeCompBlueprint(float averagePopulation, float averageLifeLength, float[] popFactors, DeathAiBlueprint deathAi, BreedingCompBlueprint breedBlueprint, EnviroCompBlueprint enviroBlueprint, boolean isAnimal) {
        super(ComponentType.LIFE);
        this.averagePopulation = averagePopulation;
        this.averageLifeLength = averageLifeLength;
        this.defencePoints = 50;
        this.breedInfo = breedBlueprint;
        this.deathAi = deathAi;
        this.enviroBlueprint = enviroBlueprint;
        this.populationFactors = popFactors;
        if (isAnimal) {
            super.addTrait(new FloatTraitBlueprint(DISEASE_RES, 1.0f, 7.0f, 11.0f));
        }
    }

    @Override
    public Component createInstance() {
        return new LifeComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        if (this.showDefPoints) {
            info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine(DEF_STAT, Integer.toString(this.defencePoints)));
        }
        info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine(LIFE_STAT, Calendar.formatTimeHours(this.averageLifeLength)));
        this.enviroBlueprint.getInfo(info);
    }

    protected BreedingCompBlueprint getBreedInfo() {
        return this.breedInfo;
    }

    protected float[] getPopulationFactors() {
        return this.populationFactors;
    }

    protected EnviroCompBlueprint getEnviroBlueprint() {
        return this.enviroBlueprint;
    }

    protected float getAveragePopulation() {
        return this.averagePopulation;
    }

    protected float getAverageLifeLength() {
        return this.averageLifeLength;
    }

    protected DeathAi getDeathAiInstance(Entity entity) {
        return this.deathAi.createInstance(entity);
    }
}


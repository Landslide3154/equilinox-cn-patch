/*
 * Decompiled with CFR 0.152.
 */
package breeding;

import blueprints.Blueprint;
import breeding.BreedingComponent;
import breedingTrees.ReqInfo;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import health.LifeComponent;
import instances.Entity;
import java.util.ArrayList;
import java.util.List;
import resourceManagement.BlueprintRepository;
import utils.BinaryReader;
import utils.CSVReader;

public class BreedingCompBlueprint {
    private static final float STD_FACTOR = 0.5f;
    private final int parentId;
    private final int requiredPoints;
    private final boolean secretSpecies;
    private final List<Requirement> requirements;
    protected final float standardDeviation;
    protected final float breedMaturityAge;
    protected final float averageBreedTime;

    protected BreedingCompBlueprint(int parentId, int breedingCount, List<Requirement> requirements, float breedMaturity, float breedTimeAverage, boolean secret) {
        this.parentId = parentId;
        this.requiredPoints = breedingCount;
        this.requirements = requirements;
        this.secretSpecies = secret;
        this.breedMaturityAge = breedMaturity;
        this.averageBreedTime = breedTimeAverage;
        this.standardDeviation = this.averageBreedTime * 0.5f;
    }

    public BreedingComponent createInstance(Entity entity, LifeComponent life) {
        return new BreedingComponent(entity, this, life);
    }

    public BreedingComponent loadInstance(BinaryReader reader, Entity entity, LifeComponent life) throws Exception {
        boolean boost = reader.readBoolean();
        float nextBreed = reader.readFloat();
        int generation = reader.readInt();
        BreedingComponent component = new BreedingComponent(entity, this, boost, nextBreed, generation, life);
        component.loadEvolveData(reader);
        return component;
    }

    public boolean checkRequirements(Entity offspring) {
        boolean passed = true;
        for (Requirement requirement : this.requirements) {
            passed &= requirement.check(offspring);
        }
        return passed;
    }

    public int getRequiredEvolvePoints() {
        return this.requiredPoints;
    }

    public boolean isSecret() {
        return this.secretSpecies;
    }

    public List<Requirement> getRequirements() {
        return this.requirements;
    }

    public List<ReqInfo> getRequirementGuis() {
        ArrayList<ReqInfo> guiComponents = new ArrayList<ReqInfo>();
        for (Requirement req : this.requirements) {
            req.getGuiInfo(guiComponents);
        }
        return guiComponents;
    }

    public Blueprint getParent() {
        if (this.parentId > 0) {
            return BlueprintRepository.getBlueprint(this.parentId);
        }
        return null;
    }

    public static BreedingCompBlueprint load(CSVReader reader, boolean secret) {
        float breedMaturity = reader.getNextLabelFloat();
        float averageBreedTime = reader.getNextLabelFloat();
        int parentId = reader.getNextLabelInt();
        int breedCount = 0;
        List<Requirement> reqs = new ArrayList<Requirement>();
        if (parentId >= 0) {
            breedCount = reader.getNextLabelInt();
            reqs = BreedingCompBlueprint.loadRequirements(reader);
        }
        return new BreedingCompBlueprint(parentId, breedCount, reqs, breedMaturity, averageBreedTime, secret);
    }

    private static List<Requirement> loadRequirements(CSVReader reader) {
        int count = reader.getNextLabelInt();
        ArrayList<Requirement> reqs = new ArrayList<Requirement>(count);
        int i = 0;
        while (i < count) {
            Requirement req = ComponentType.valueOf(reader.getNextString()).loadRequirement(reader);
            reqs.add(req);
            ++i;
        }
        return reqs;
    }
}


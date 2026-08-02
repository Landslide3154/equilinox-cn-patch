/*
 * Decompiled with CFR 0.152.
 */
package food;

import breedingTraits.FloatTrait;
import breedingTraits.Trait;
import classification.Classification;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityInfoGui.PopUpInfoGui;
import food.FoodComponent;
import food.FoodSection;
import food.RootVegSectionBlueprint;
import gameManaging.GameManager;
import growth.GrowthComponent;
import instances.Entity;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;
import world.World;

public class RootVegSection
implements FoodSection {
    private static final float DIFF = 0.3f;
    private static final int MIN_COUNT = 3;
    private final RootVegSectionBlueprint blueprint;
    private GrowthComponent growth;
    private InformationComponent info;
    private Entity entity;
    private final FoodComponent foodComp;

    public RootVegSection(RootVegSectionBlueprint blueprint, FoodComponent foodComp) {
        this.blueprint = blueprint;
        this.foodComp = foodComp;
    }

    @Override
    public int eat() {
        FloatTrait trait = (FloatTrait)this.foodComp.getTrait(this.blueprint.getTraitIndex());
        int potatoCount = this.getCount(trait.getValue());
        this.entity.die(this.blueprint.deathAnimation.createInstance(this.entity, potatoCount), false);
        return this.blueprint.foodPoints;
    }

    @Override
    public boolean canBeEaten() {
        Classification classification;
        Vector3f pos = this.info.getBasePosition();
        World world = GameManager.getWorld();
        int totalPopulation = world.getPopulation(classification = this.entity.getBlueprint().getSpeciesClassification(), this.info.getRoamingRange(), pos.x, pos.z);
        return totalPopulation > 3 && this.growth.getGrowthFactor() > 0.75f;
    }

    @Override
    public void create(ComponentBundle bundle, Trait edibility) {
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.entity = bundle.getEntity();
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void load(BinaryReader reader) throws Exception {
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    private int getCount(float average) {
        float difference = average * 0.3f;
        float min = average - difference;
        float max = average + difference;
        return Math.round(Maths.randomNumberBetween(min, max));
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package food;

import breedingTraits.FloatTrait;
import breedingTraits.Trait;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import food.FoodSection;
import food.WholeFoodSectionBlueprint;
import growth.GrowthComponent;
import instances.Entity;
import java.util.List;
import utils.BinaryReader;
import utils.BinaryWriter;

public class WholeFoodSection
implements FoodSection {
    private Entity entity;
    private WholeFoodSectionBlueprint blueprint;
    private GrowthComponent growth;
    private FloatTrait edibility;

    protected WholeFoodSection(WholeFoodSectionBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public boolean canBeEaten() {
        return this.growth == null || this.growth.getGrowthFactor() > 0.5f;
    }

    @Override
    public int eat() {
        this.entity.die(this.blueprint.deathAnimation.createInstance(this.entity), false);
        return Math.round((float)this.blueprint.foodPoints * this.edibility.value);
    }

    @Override
    public void create(ComponentBundle bundle, Trait edibility) {
        this.entity = bundle.getEntity();
        this.edibility = (FloatTrait)edibility;
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
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
}


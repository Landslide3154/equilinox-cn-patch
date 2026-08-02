/*
 * Decompiled with CFR 0.152.
 */
package food;

import breedingTraits.FloatTrait;
import breedingTraits.Trait;
import componentArchitecture.ComponentBundle;
import entityInfoGui.PopUpInfoGui;
import food.FoodSection;
import food.FoodSectionBlueprint;
import java.util.List;
import utils.BinaryReader;
import utils.BinaryWriter;

public class SampleSection
implements FoodSection {
    private final FoodSectionBlueprint blueprint;
    private FloatTrait edibility;

    protected SampleSection(FoodSectionBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public int eat() {
        return Math.round((float)this.blueprint.foodPoints * this.edibility.value);
    }

    @Override
    public boolean canBeEaten() {
        return true;
    }

    @Override
    public void create(ComponentBundle bundle, Trait edibility) {
        this.edibility = (FloatTrait)edibility;
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


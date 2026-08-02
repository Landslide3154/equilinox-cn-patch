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
import food.FoodSectionBlueprint;
import fruit.FruiterComponent;
import java.util.List;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FruitSection
implements FoodSection {
    private final FoodSectionBlueprint blueprint;
    private FruiterComponent fruiter;
    private FloatTrait edibility;

    protected FruitSection(FoodSectionBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public boolean canBeEaten() {
        return this.fruiter.hasFruit();
    }

    @Override
    public int eat() {
        this.fruiter.decreaseFruit();
        return Math.round((float)this.blueprint.foodPoints * this.edibility.value);
    }

    @Override
    public void create(ComponentBundle bundle, Trait edibility) {
        this.fruiter = (FruiterComponent)bundle.getComponent(ComponentType.FRUITER);
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


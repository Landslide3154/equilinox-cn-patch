/*
 * Decompiled with CFR 0.152.
 */
package food;

import breedingTraits.Trait;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import events.EventData;
import events.EventManager;
import food.FoodSection;
import food.FoodSectionBlueprint;
import hiveComponents.HiveComponent;
import java.util.List;
import utils.BinaryReader;
import utils.BinaryWriter;

public class HoneySection
implements FoodSection {
    private final FoodSectionBlueprint blueprint;
    private HiveComponent hive;

    public HoneySection(FoodSectionBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public int eat() {
        int points = this.hive.getHoneyCount();
        int eatingAmount = Math.min(points, this.blueprint.foodPoints);
        int change = Math.abs(this.hive.increaseHoney(-eatingAmount));
        EventManager.HONEY_TAKEN.registerEvent(new EventData(null, change), new String[0]);
        return change;
    }

    @Override
    public boolean canBeEaten() {
        return this.hive.hasHoney();
    }

    @Override
    public void create(ComponentBundle bundle, Trait edibility) {
        this.hive = (HiveComponent)bundle.getComponent(ComponentType.HIVE);
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


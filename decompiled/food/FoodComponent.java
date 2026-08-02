/*
 * Decompiled with CFR 0.152.
 */
package food;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import effects.Effect;
import entityInfoGui.PopUpInfoGui;
import food.FoodCompBlueprint;
import food.FoodSection;
import food.FoodSectionType;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FoodComponent
extends Component {
    private Map<FoodSectionType, FoodSection> sections;
    private final FoodCompBlueprint blueprint;
    private Entity entity;

    protected FoodComponent(FoodCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    protected void setSections(Map<FoodSectionType, FoodSection> sections) {
        this.sections = sections;
    }

    public boolean canBeEaten(FoodSectionType type) {
        FoodSection section = this.sections.get((Object)type);
        if (section == null) {
            return false;
        }
        return section.canBeEaten();
    }

    public boolean hasEffect() {
        return this.blueprint.effect != null;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Effect getEffect() {
        return this.blueprint.effect;
    }

    public int eat(FoodSectionType type) {
        return this.sections.get((Object)type).eat();
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        for (FoodSection section : this.sections.values()) {
            section.getStatusInfo(info);
        }
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        for (FoodSection section : this.sections.values()) {
            section.export(writer);
        }
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.entity = bundle.getEntity();
        for (FoodSection section : this.sections.values()) {
            section.create(bundle, super.getTrait(0));
        }
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        for (FoodSection section : this.sections.values()) {
            section.load(reader);
        }
    }
}


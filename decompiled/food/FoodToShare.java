/*
 * Decompiled with CFR 0.152.
 */
package food;

import breedingTraits.Trait;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.TextInfo;
import food.FoodSection;
import food.FoodToShareBlueprint;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FoodToShare
implements FoodSection {
    private static final String PORTIONS = GameText.getText(449);
    private final FoodToShareBlueprint blueprint;
    private MeshComponent mesh;
    private Entity entity;
    private int remainingPortions;

    public FoodToShare(FoodToShareBlueprint blueprint) {
        this.blueprint = blueprint;
        this.remainingPortions = blueprint.portionCount;
    }

    @Override
    public int eat() {
        --this.remainingPortions;
        if (this.remainingPortions == 0) {
            this.entity.die(null, false);
        } else {
            this.chooseMeshStage();
        }
        return this.blueprint.foodPoints;
    }

    @Override
    public boolean canBeEaten() {
        return true;
    }

    @Override
    public void create(ComponentBundle bundle, Trait edibility) {
        this.entity = bundle.getEntity();
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
    }

    private void chooseMeshStage() {
        float portionsPerStage = (float)this.blueprint.portionCount / (float)this.mesh.getStageCount();
        int portionsGone = this.blueprint.portionCount - this.remainingPortions;
        int stage = (int)Math.floor((float)portionsGone / portionsPerStage);
        if (this.mesh.getCurrentStageNumber() != stage) {
            this.mesh.updateModelStage(stage);
        }
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        info.add(new TextInfo(PORTIONS, EntityInfoGui.FONT_SIZE){

            @Override
            public String getValue() {
                return Integer.toString(FoodToShare.this.remainingPortions);
            }
        });
    }

    @Override
    public void load(BinaryReader reader) throws Exception {
        this.remainingPortions = reader.readInt();
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.remainingPortions);
    }
}


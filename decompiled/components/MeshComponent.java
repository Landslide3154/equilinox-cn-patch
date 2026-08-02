/*
 * Decompiled with CFR 0.152.
 */
package components;

import blueprints.Blueprint;
import blueprints.SubBlueprint;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import session.GameMode;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;
import world.World;

public class MeshComponent
extends Component {
    private Blueprint blueprint;
    private int currentStage;
    private Entity entity;
    private boolean randomize;

    private MeshComponent(MeshCompBlueprint meshComp, Blueprint blueprint, boolean randomize) {
        super(meshComp);
        this.randomize = randomize;
        this.blueprint = blueprint;
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.entity = bundle.getEntity();
        this.currentStage = this.randomize ? Maths.RANDOM.nextInt(this.blueprint.getSubBlueprints().size()) : (GameManager.sessionManager.hasWorldReady() && GameManager.getGameMode() == GameMode.BUILD ? this.blueprint.getMainSubBlueprintId() : 0);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.entity = bundle.getEntity();
        this.currentStage = reader.readInt();
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    public void updateModelStage(int stage) {
        if (!this.entity.isStatic()) {
            GameManager.getSession().getSceneData().removeDynamicEntity(this.entity);
        }
        this.currentStage = stage;
        if (this.entity.isStatic()) {
            int newBatchId = World.calculateBatchId(this.entity);
            GameManager.getSession().getSceneData().updateStaticEntity(this.entity, newBatchId);
        } else {
            GameManager.getSession().getSceneData().addDynamicEntity(this.entity);
        }
    }

    public int getStageCount() {
        return this.blueprint.getSubBlueprints().size();
    }

    public int getCurrentStageNumber() {
        return this.currentStage;
    }

    public SubBlueprint getCurrentModelStage() {
        return this.blueprint.getSubBlueprints().get(this.currentStage);
    }

    public SubBlueprint getModelStage(int stage) {
        return this.blueprint.getSubBlueprints().get(stage);
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.currentStage);
    }

    /* synthetic */ MeshComponent(MeshCompBlueprint meshCompBlueprint, Blueprint blueprint, boolean bl, MeshComponent meshComponent) {
        this(meshCompBlueprint, blueprint, bl);
    }

    public static class MeshCompBlueprint
    extends ComponentBlueprint {
        private Blueprint blueprint;

        public MeshCompBlueprint(Blueprint blueprint) {
            super(ComponentType.MESH);
            this.blueprint = blueprint;
        }

        @Override
        public Component createInstance() {
            return new MeshComponent(this, this.blueprint, this.blueprint.isRandomModelStages(), null);
        }

        @Override
        public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        }

        @Override
        public void delete() {
        }
    }
}


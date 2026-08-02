/*
 * Decompiled with CFR 0.152.
 */
package building;

import building.BuildCompBlueprint;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.TextInfo;
import events.EventData;
import events.EventManager;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import utils.BinaryReader;
import utils.BinaryWriter;

public class BuildComponent
extends Component {
    private static final String BUILD_PROG = GameText.getText(197);
    private final BuildCompBlueprint blueprint;
    private Entity entity;
    private MeshComponent mesh;
    private int buildPoints = 0;
    private int currentStage = 0;

    protected BuildComponent(BuildCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    public int getPointsPerStage() {
        return this.blueprint.fullyBuiltStage / (this.blueprint.stageCount - 1);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        info.add(new TextInfo(BUILD_PROG, EntityInfoGui.FONT_SIZE){

            @Override
            public String getValue() {
                return String.valueOf(BuildComponent.this.buildPoints) + "/" + ((BuildComponent)BuildComponent.this).blueprint.totalBuildPoints;
            }
        });
    }

    public int getBuildPoints() {
        return this.buildPoints;
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    public void build(int points, boolean forceUpdate) {
        this.buildPoints += points;
        this.buildPoints = Math.min(this.buildPoints, this.blueprint.totalBuildPoints);
        int newStage = this.calculateCurrentStage();
        EventManager.BUILD.registerEvent(new EventData(this), this.entity.getBlueprint().getSpeciesClassification().getKey());
        if (this.buildPoints < 0) {
            this.entity.die(null, false);
        } else if (newStage != this.currentStage || forceUpdate) {
            this.mesh.updateModelStage(newStage);
            this.currentStage = newStage;
        }
    }

    public void updateColour() {
        this.mesh.updateModelStage(this.currentStage);
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.buildPoints);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        this.buildPoints = reader.readInt();
        this.currentStage = this.calculateCurrentStage();
    }

    public boolean isFullyBuilt() {
        return this.buildPoints == this.blueprint.totalBuildPoints;
    }

    private int calculateCurrentStage() {
        float factor = (float)this.buildPoints / (float)this.blueprint.fullyBuiltStage;
        factor = Math.min(1.0f, factor);
        return (int)((float)(this.blueprint.stageCount - 1) * factor);
    }
}


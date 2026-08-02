/*
 * Decompiled with CFR 0.152.
 */
package hiveComponents;

import building.BuildComponent;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import death.FadeDeath;
import entityInfoGui.BarMouseOverGui;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.ProgressInfo;
import events.EventData;
import events.EventManager;
import gameManaging.GameManager;
import guis.GuiComponent;
import hiveComponents.HiveCompBlueprint;
import instances.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import utils.BinaryReader;
import utils.BinaryWriter;

public class HiveComponent
extends Component {
    private static final String HONEY = GameText.getText(1084);
    private static final String HONEY_CONTENT = GameText.getText(1085);
    private static final float MAX_IDLE = 50.0f;
    private final HiveCompBlueprint blueprint;
    private MeshComponent mesh;
    private BuildComponent buildComp;
    private Entity entity;
    private int honeyCount = 0;
    private int currentStage = 0;
    private float idleTime = 0.0f;

    protected HiveComponent(HiveCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
        this.currentStage = blueprint.startStage - 1;
    }

    @Override
    public void update() {
        this.idleTime += GameManager.getGameSeconds();
        if (this.idleTime > 50.0f) {
            this.entity.die(new FadeDeath(1.0f, this.entity), false);
        }
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        if (this.buildComp.isFullyBuilt()) {
            info.add(new ProgressInfo(HONEY_CONTENT, EntityInfoGui.FONT_SIZE, false){

                @Override
                protected GuiComponent addMouseOver() {
                    String[] headers = new String[]{HONEY};
                    return new BarMouseOverGui(headers){

                        @Override
                        public List<BarMouseOverGui.StatData> getData() {
                            ArrayList<BarMouseOverGui.StatData> data = new ArrayList<BarMouseOverGui.StatData>();
                            data.add(new BarMouseOverGui.StatData(String.valueOf(HiveComponent.this.honeyCount) + "/" + ((HiveComponent)(this).HiveComponent.this).blueprint.maxHoney, BarMouseOverGui.HEADING_COLOUR));
                            return data;
                        }
                    };
                }

                @Override
                protected float getValue() {
                    return (float)HiveComponent.this.honeyCount / (float)((HiveComponent)HiveComponent.this).blueprint.maxHoney;
                }
            });
        }
    }

    public void indicateVisit() {
        this.idleTime = 0.0f;
    }

    public int increaseHoney(int honey) {
        int newStage;
        int beforeCount = this.honeyCount;
        this.indicateVisit();
        if (!this.buildComp.isFullyBuilt()) {
            return 0;
        }
        this.honeyCount += honey;
        this.honeyCount = Math.min(Math.max(this.honeyCount, 0), this.blueprint.maxHoney);
        if (this.honeyCount == this.blueprint.maxHoney) {
            EventManager.HONEY_FULL.registerEvent(new EventData(), "full");
        }
        if ((newStage = this.calculateCurrentStage()) != this.currentStage) {
            this.mesh.updateModelStage(newStage);
            this.currentStage = newStage;
        }
        return this.honeyCount - beforeCount;
    }

    public boolean hasHoney() {
        return this.honeyCount > 0;
    }

    public int getHoneyCount() {
        return this.honeyCount;
    }

    @Override
    public void getActions(List<Action> actions) {
        actions.add(new Action("Harvest", 1){

            @Override
            public void carryOut() {
                int change = HiveComponent.this.increaseHoney(-((HiveComponent)HiveComponent.this).blueprint.maxHoney);
                if ((change = Math.abs(change)) > 0) {
                    EventManager.HONEY_TAKEN.registerEvent(new EventData(null, change), new String[0]);
                }
            }
        });
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.honeyCount);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.buildComp = (BuildComponent)bundle.getComponent(ComponentType.BUILD);
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        this.honeyCount = reader.readInt();
        this.currentStage = this.calculateCurrentStage();
    }

    private int calculateCurrentStage() {
        float factor = (float)this.honeyCount / (float)this.blueprint.maxHoney;
        return this.blueprint.startStage - 1 + (int)((float)this.blueprint.stageCount * factor);
    }
}


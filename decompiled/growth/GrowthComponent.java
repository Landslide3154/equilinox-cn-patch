/*
 * Decompiled with CFR 0.152.
 */
package growth;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.TextInfo;
import environment.EnviroComponent;
import gameManaging.GameManager;
import growth.GrowthCompBlueprint;
import health.LifeComponent;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import session.GameMode;
import toolbox.Maths;
import userInterfaces.TextStatInfo;
import utils.BinaryReader;
import utils.BinaryWriter;

public abstract class GrowthComponent
extends Component {
    private static final String NAME = GameText.getText(882);
    private static final String GROW_SPEED = GameText.getText(988);
    private static final String GROW_DESC = GameText.getText(989);
    private static final float GROWTH_STD = 0.3f;
    private EnviroComponent enviro;
    private GrowthCompBlueprint blueprint;
    private int stageNumber = 0;
    private float currentStageTime = 0.0f;
    private float totalStageTime;
    private boolean fullyGrown = false;

    protected GrowthComponent(GrowthCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void update() {
        if (this.fullyGrown) {
            return;
        }
        this.currentStageTime += GameManager.getDeltaHours() * this.enviro.getBoost();
        if (!this.checkFinished() && this.currentStageTime >= this.totalStageTime) {
            this.currentStageTime = 0.0f;
            ++this.stageNumber;
            this.switchToStage(this.stageNumber);
        }
    }

    public float getGrowthFactor() {
        if (this.fullyGrown) {
            return 1.0f;
        }
        float stage = this.stageNumber;
        if (this.blueprint.startsHalf()) {
            stage -= 0.5f;
        }
        return (stage += this.currentStageTime / this.totalStageTime) / (float)this.blueprint.getFullStageCount();
    }

    public int getGrowthPercent() {
        return (int)(this.getGrowthFactor() * 100.0f);
    }

    public boolean isFullyGrown() {
        return this.fullyGrown;
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        info.add(new TextInfo(NAME, EntityInfoGui.FONT_SIZE){

            @Override
            public String getValue() {
                return String.valueOf(GrowthComponent.this.getGrowthPercent()) + "%";
            }
        });
    }

    @Override
    public void getPerformanceBuffsInfo(List<TextStatInfo> info) {
        super.getPerformanceBuffsInfo(info);
        if (!this.fullyGrown) {
            info.add(new TextStatInfo(GROW_SPEED, String.valueOf(Math.round(this.enviro.getBoost() * 100.0f)) + "%", GROW_DESC));
        }
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeBoolean(this.fullyGrown);
        if (!this.fullyGrown) {
            writer.writeInt(this.stageNumber);
            writer.writeFloat(this.totalStageTime);
            writer.writeFloat(this.currentStageTime);
        }
    }

    @Override
    public GrowthCompBlueprint getBlueprint() {
        return this.blueprint;
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.setComponents(bundle);
        this.fullyGrown = reader.readBoolean();
        if (!this.fullyGrown) {
            this.stageNumber = reader.readInt();
            this.totalStageTime = reader.readFloat();
            this.currentStageTime = reader.readFloat();
        } else {
            this.stageNumber = this.blueprint.getTotalStageCount() - 1;
        }
    }

    public int getStageNumber() {
        return this.stageNumber;
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.setComponents(bundle);
        this.generateGrowTime();
        if (this.blueprint.startsHalf()) {
            this.currentStageTime = this.totalStageTime / 2.0f;
        }
        if (GameManager.getGameMode() == GameMode.BUILD) {
            this.fullyGrown = true;
            this.stageNumber = this.blueprint.getTotalStageCount() - 1;
        }
    }

    protected float getStageProgress() {
        return this.currentStageTime / this.totalStageTime;
    }

    protected abstract void switchToStage(int var1);

    public abstract void finishGrowing();

    public abstract void forceUpdate();

    private boolean checkFinished() {
        if (this.stageNumber == this.blueprint.getTotalStageCount() - 1) {
            boolean finished = false;
            if (this.blueprint.startsHalf()) {
                finished = this.currentStageTime >= this.totalStageTime / 2.0f;
            } else {
                boolean bl = finished = this.currentStageTime >= this.totalStageTime;
            }
            if (finished) {
                this.fullyGrown = true;
                this.finishGrowing();
            }
            return finished;
        }
        return false;
    }

    private void generateGrowTime() {
        float gauss = (float)Maths.RANDOM.nextGaussian();
        float standardDeviation = this.blueprint.averageGrowthTime * 0.3f;
        float totalTime = gauss * standardDeviation + this.blueprint.averageGrowthTime;
        totalTime = Math.max(totalTime, standardDeviation);
        this.totalStageTime = totalTime / (float)this.blueprint.getFullStageCount();
    }

    private void setComponents(ComponentBundle bundle) {
        this.enviro = ((LifeComponent)bundle.getComponent(ComponentType.LIFE)).getEnviroComponent();
    }
}


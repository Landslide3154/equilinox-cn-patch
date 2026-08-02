/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import entityInfoGui.PopUpInfoGui;
import fruit.FruiterCompBlueprint;
import gameManaging.GameManager;
import growth.GrowthComponent;
import health.LifeComponent;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import session.GameMode;
import toolbox.Maths;
import userInterfaces.TextStatInfo;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FruiterComponent
extends Component {
    private static final String BUFF_NAME = GameText.getText(403);
    private static final String BUFF_DESC = GameText.getText(404);
    private static final float VARIANCE = 0.2f;
    private static final float ENVIRO_INFLUENCE = 0.65f;
    private MeshComponent mesh;
    private GrowthComponent growth;
    private LifeComponent lifeComp;
    private FruiterCompBlueprint blueprint;
    private int currentFruitStage;
    private boolean producing = false;
    private float timeTillFruit;

    protected FruiterComponent(FruiterCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
        this.timeTillFruit = Maths.getRandomVariance(blueprint.getFruitTime(), 0.2f);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    public void removeAllFruit() {
        if (this.currentFruitStage > 0) {
            this.currentFruitStage = 0;
            this.mesh.updateModelStage(this.currentFruitStage + this.blueprint.getFruitModelIndex() - 1);
        }
    }

    public void decreaseFruit() {
        if (this.currentFruitStage > 0) {
            --this.currentFruitStage;
            this.mesh.updateModelStage(this.currentFruitStage + this.blueprint.getFruitModelIndex() - 1);
        }
    }

    public void increaseFruit() {
        if (this.currentFruitStage < this.blueprint.getFruitStageCount()) {
            ++this.currentFruitStage;
            this.mesh.updateModelStage(this.currentFruitStage + this.blueprint.getFruitModelIndex() - 1);
        }
    }

    public boolean hasFruit() {
        return this.currentFruitStage > 0;
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void getPerformanceBuffsInfo(List<TextStatInfo> info) {
        float factor = Maths.getFactor(this.lifeComp.getEnvironmentalSatisfaction(), 0.65f);
        int percent = (int)(factor * 100.0f);
        info.add(new TextStatInfo(BUFF_NAME, String.valueOf(percent) + "%", BUFF_DESC));
    }

    @Override
    public void update() {
        if (this.checkProducing() && this.currentFruitStage < this.blueprint.getFruitStageCount()) {
            this.timeTillFruit -= GameManager.getGameSeconds() * Maths.getFactor(this.lifeComp.getEnvironmentalSatisfaction(), 0.65f);
            if (this.timeTillFruit <= 0.0f) {
                ++this.currentFruitStage;
                this.mesh.updateModelStage(this.currentFruitStage + this.blueprint.getFruitModelIndex() - 1);
                this.timeTillFruit = Maths.getRandomVariance(this.blueprint.getFruitTime(), 0.2f);
            }
        }
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.currentFruitStage);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.lifeComp = (LifeComponent)bundle.getComponent(ComponentType.LIFE);
        if (GameManager.getGameMode() == GameMode.BUILD) {
            this.currentFruitStage = this.blueprint.getFruitStageCount();
        }
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.lifeComp = (LifeComponent)bundle.getComponent(ComponentType.LIFE);
        this.currentFruitStage = reader.readInt();
    }

    private boolean checkProducing() {
        if (!this.producing && (this.growth == null || this.growth.isFullyGrown())) {
            this.producing = true;
            this.timeTillFruit = Maths.getRandomVariance(this.blueprint.getFruitTime(), 0.2f);
        }
        return this.producing;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package growth;

import blueprints.SubBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import growth.GrowthCompBlueprint;
import growth.GrowthComponent;
import session.GameMode;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;

public class StaticGrowthComponent
extends GrowthComponent {
    private GrowthCompBlueprint blueprint;
    private MeshComponent mesh;
    private Transformation transform;

    protected StaticGrowthComponent(GrowthCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    protected void switchToStage(int stage) {
        int actualStage = this.convert(stage);
        if (actualStage % this.blueprint.subStages == 0) {
            this.mesh.updateModelStage(actualStage / this.blueprint.subStages);
        } else {
            this.mesh.updateModelStage(this.mesh.getCurrentStageNumber());
        }
        this.updateScale(this.transform.getScaleTrait().getValue());
    }

    @Override
    public void forceUpdate() {
        this.updateScale(this.transform.getScaleTrait().getValue());
    }

    @Override
    public void finishGrowing() {
        this.switchToStage(this.blueprint.getTotalStageCount());
    }

    @Override
    public void create(ComponentBundle bundle) {
        super.create(bundle);
        this.setComponents(bundle);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        super.load(bundle, reader);
        this.setComponents(bundle);
        if (reader.getSession().getMode() == GameMode.BUILD) {
            this.updateScale(this.transform.getScaleTrait().getValue());
        } else {
            this.updateScale(this.transform.getScaleTrait().value);
        }
    }

    private void updateScale(float value) {
        SubBlueprint currentModel = this.mesh.getCurrentModelStage();
        float scaleProgression = this.getScaleProgression();
        float scale = Maths.interpolate(currentModel.getMinGrowth(), currentModel.getMaxGrowth(), scaleProgression);
        this.transform.setScale(value * scale);
    }

    private void setComponents(ComponentBundle bundle) {
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
    }

    private int convert(int stage) {
        return stage + (this.blueprint.subStages - 1) / 2;
    }

    private float getScaleProgression() {
        float growth = super.getGrowthFactor();
        float growthTime = 1.0f / (float)(this.blueprint.modelStages - 1);
        float currentStage = (growth + growthTime / 2.0f) / growthTime;
        int stage = this.mesh.getCurrentStageNumber();
        return Math.min(1.0f, currentStage - (float)stage);
    }
}


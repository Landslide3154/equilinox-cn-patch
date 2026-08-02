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
import instances.DpPerMinCounter;
import interpolation.Timer;
import session.GameMode;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;

public class DynamicGrowthComponent
extends GrowthComponent {
    private static final float DPPM_START = 0.3f;
    private DpPerMinCounter dppm;
    private MeshComponent mesh;
    private Transformation transform;
    private final Timer timer = Timer.createLoopingTimer(5.0f, false).randomize();

    protected DynamicGrowthComponent(GrowthCompBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        if (!super.isFullyGrown()) {
            super.update();
            SubBlueprint currentStage = this.mesh.getCurrentModelStage();
            float scale = Maths.interpolate(currentStage.getMinGrowth(), currentStage.getMaxGrowth(), this.getStageProgress());
            this.transform.setScale(this.transform.getScaleTrait().getValue() * scale);
            if (this.timer.check()) {
                this.recalculateDpMultiplier();
            }
        }
    }

    @Override
    public void create(ComponentBundle bundle) {
        super.create(bundle);
        this.setComponents(bundle);
        this.recalculateDpMultiplier();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        super.load(bundle, reader);
        this.setComponents(bundle);
        this.recalculateDpMultiplier();
        if (reader.getSession().getMode() == GameMode.BUILD) {
            this.transform.setScale(this.transform.getScaleTrait().getValue());
        }
    }

    private void recalculateDpMultiplier() {
        float factor = this.getGrowthFactor();
        this.dppm.registerModifier(this.getType(), Maths.interpolate(0.3f, 1.0f, factor));
    }

    @Override
    protected void switchToStage(int stage) {
        this.mesh.updateModelStage(stage);
    }

    @Override
    public void finishGrowing() {
        this.transform.setScale(this.transform.getScaleTrait().getValue());
    }

    @Override
    public void forceUpdate() {
        this.transform.setScale(this.transform.getScaleTrait().getValue());
    }

    private void setComponents(ComponentBundle bundle) {
        this.dppm = bundle.getDpCounter();
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
    }
}


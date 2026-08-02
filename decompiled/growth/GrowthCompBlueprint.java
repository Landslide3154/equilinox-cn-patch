/*
 * Decompiled with CFR 0.152.
 */
package growth;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import growth.DynamicGrowthComponent;
import growth.StaticGrowthComponent;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import time.Calendar;

public abstract class GrowthCompBlueprint
extends ComponentBlueprint {
    private static final String GROWTH = GameText.getText(883);
    protected final float averageGrowthTime;
    public final int modelStages;
    protected final int subStages;
    private final boolean dynamic;

    protected GrowthCompBlueprint(float growthTime, int modelStages) {
        super(ComponentType.GROWTH, true);
        this.averageGrowthTime = growthTime;
        this.modelStages = modelStages;
        this.subStages = 1;
        this.dynamic = true;
    }

    protected GrowthCompBlueprint(float growthTime, int modelStages, int subStages) {
        super(ComponentType.GROWTH, false);
        this.averageGrowthTime = growthTime;
        this.modelStages = modelStages;
        this.subStages = subStages;
        this.dynamic = false;
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        SpeciesInfoLine line = new SpeciesInfoLine(GROWTH, Calendar.formatTimeHours(this.averageGrowthTime));
        info.get((Object)SpeciesInfoType.GENERAL).add(line);
    }

    protected int getFullStageCount() {
        return (this.modelStages - 1) * this.subStages;
    }

    protected int getTotalStageCount() {
        int extra = this.startsHalf() ? 1 : 0;
        return this.getFullStageCount() + extra;
    }

    protected boolean startsHalf() {
        return this.dynamic || this.subStages % 2 == 0;
    }

    public static class DynamicGrowthCompBlueprint
    extends GrowthCompBlueprint {
        protected DynamicGrowthCompBlueprint(float growthTime, int modelStages) {
            super(growthTime, modelStages);
        }

        @Override
        public Component createInstance() {
            return new DynamicGrowthComponent(this);
        }
    }

    public static class StaticGrowthCompBlueprint
    extends GrowthCompBlueprint {
        protected StaticGrowthCompBlueprint(float growthTime, int modelStages, int subStages) {
            super(growthTime, modelStages, subStages);
        }

        @Override
        public Component createInstance() {
            return new StaticGrowthComponent(this);
        }
    }
}


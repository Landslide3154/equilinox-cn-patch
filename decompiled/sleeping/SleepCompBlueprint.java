/*
 * Decompiled with CFR 0.152.
 */
package sleeping;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import sleeping.SleepComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import toolbox.Maths;

public class SleepCompBlueprint
extends ComponentBlueprint {
    private static final String SLEEP_ABILITY = GameText.getText(996);
    private final float startMin;
    private final float startMax;
    private final float endMin;
    private final float endMax;

    protected SleepCompBlueprint(float startMin, float startMax, float endMin, float endMax) {
        super(ComponentType.SLEEP);
        this.startMin = startMin;
        this.startMax = startMax < startMin ? startMax + 1.0f : startMax;
        this.endMin = endMin;
        this.endMax = endMax < endMin ? endMax + 1.0f : endMax;
    }

    protected float generateStartSleepTime() {
        return Maths.randomNumberBetween(this.startMin, this.startMax) % 1.0f;
    }

    protected float generateEndSleepTime() {
        return Maths.randomNumberBetween(this.endMin, this.endMax) % 1.0f;
    }

    @Override
    public Component createInstance() {
        return new SleepComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", SLEEP_ABILITY));
    }
}


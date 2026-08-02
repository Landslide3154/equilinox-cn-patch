/*
 * Decompiled with CFR 0.152.
 */
package building;

import building.DecomposeComponent;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.ComplexString;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class DecomposeCompBlueprint
extends ComponentBlueprint {
    private static final String DECAY = GameText.getText(202);
    private static final ComplexString RATE = GameText.getComplexText(203);
    protected final float timePerLoss;

    protected DecomposeCompBlueprint(float timePerLoss) {
        super(ComponentType.DECOMPOSE);
        this.timePerLoss = timePerLoss;
    }

    @Override
    public Component createInstance() {
        return new DecomposeComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        float buildPoints = 1.0f / this.timePerLoss;
        String value = String.format("%.1f", Float.valueOf(buildPoints));
        info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine(DECAY, RATE.getString(value)));
    }
}


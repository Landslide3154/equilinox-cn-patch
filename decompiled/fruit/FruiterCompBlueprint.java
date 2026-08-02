/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import fruit.FruiterComponent;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FruiterCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(399);
    private int fruitModelIndex;
    private int fruitStageCount;
    private float fruitTime;

    protected FruiterCompBlueprint(int fruitModelIndex, int stageCount, float fruitTime) {
        super(ComponentType.FRUITER);
        this.fruitModelIndex = fruitModelIndex;
        this.fruitStageCount = stageCount;
        this.fruitTime = fruitTime;
    }

    @Override
    public Component createInstance() {
        return new FruiterComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }

    protected float getFruitTime() {
        return this.fruitTime;
    }

    protected int getFruitModelIndex() {
        return this.fruitModelIndex;
    }

    protected int getFruitStageCount() {
        return this.fruitStageCount;
    }
}


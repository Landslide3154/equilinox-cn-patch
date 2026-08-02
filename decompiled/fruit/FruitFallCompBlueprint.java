/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import blueprints.Blueprint;
import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import fruit.FruitFallComponent;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FruitFallCompBlueprint
extends ComponentBlueprint {
    private static final String PRODUCTIVITY = GameText.getText(1062);
    protected final int fruitModelId;
    protected final float averageFruitTime;
    protected final float spawnHeight;
    protected final float spawnRadius;

    protected FruitFallCompBlueprint(int fruitModelId, float averageFruitTime, float spawnHeight, float spawnRadius, Blueprint blueprint) {
        super(ComponentType.FRUIT_FALL);
        this.fruitModelId = fruitModelId;
        this.averageFruitTime = averageFruitTime;
        this.spawnHeight = spawnHeight;
        this.spawnRadius = spawnRadius;
        this.addTrait();
    }

    @Override
    public Component createInstance() {
        return new FruitFallComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }

    private void addTrait() {
        super.addTrait(new FloatTraitBlueprint(PRODUCTIVITY, 1.0f, 2.1f, 7.0f){

            @Override
            public String formatTrait(float value) {
                return "x" + String.format("%.2f", Float.valueOf(value));
            }
        });
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import blueprints.Blueprint;
import breedingTraits.FloatTrait;
import breedingTrees.ReqInfo;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import fruit.FruitFallCompBlueprint;
import fruit.FruitFallComponent;
import instances.Entity;
import java.util.List;
import utils.CSVReader;

public class FruitFallLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int fruitId = reader.getNextLabelInt();
        float averageFruitTime = reader.getNextLabelFloat();
        float spawnHeight = reader.getNextLabelFloat();
        float spawnRadius = reader.getNextLabelFloat();
        return new FruitFallCompBlueprint(fruitId, averageFruitTime, spawnHeight, spawnRadius, blueprint);
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        final float target = reader.getNextLabelFloat();
        return new Requirement(){

            @Override
            public boolean check(Entity entity) {
                FruitFallComponent fruitFall = (FruitFallComponent)entity.getComponent(ComponentType.FRUIT_FALL);
                FloatTrait productivity = (FloatTrait)fruitFall.getTrait(0);
                float difference = target - productivity.value;
                return difference <= 0.005f;
            }

            @Override
            public void getGuiInfo(List<ReqInfo> components) {
                components.add(new ReqInfo("Productivity", String.valueOf(String.format("%.2f", Float.valueOf(target))) + "x"));
            }

            @Override
            public boolean isSecret() {
                return false;
            }
        };
    }
}


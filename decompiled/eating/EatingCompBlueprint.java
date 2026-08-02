/*
 * Decompiled with CFR 0.152.
 */
package eating;

import aiComponent.Ai;
import baseMovement.MovementComp;
import birds.BirdEatAi;
import blueprints.Blueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import eating.DietOption;
import eating.EatingComponent;
import eating.FoodFinder;
import eating.StandardEatingAi;
import fishHunt.FishEatingAi;
import growth.GrowthComponent;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import toolbox.Transformation;

public class EatingCompBlueprint
extends ComponentBlueprint {
    private static final String EATS = GameText.getText(538);
    private static final String HUNGER_INFO = GameText.getText(539);
    private static final String FOOD_POINTS = GameText.getText(540);
    private static final int STANDARD_AI = 0;
    private static final int FISH_AI = 1;
    private final int maxHungerPoints;
    private final float hungerPerHour;
    private final DietOption[] diet;
    private final boolean runToFood;
    private final int eatingAiId;
    private final float eatingRadius;
    private final boolean hasEggStage;

    public EatingCompBlueprint(int maxHunger, float hungerPerHour, float eatingRadius, DietOption[] diet, Blueprint blueprint, boolean runs, int ai, boolean eggStage) {
        super(ComponentType.EATING);
        this.maxHungerPoints = maxHunger;
        this.hungerPerHour = hungerPerHour;
        this.eatingRadius = eatingRadius;
        this.diet = diet;
        this.eatingAiId = ai;
        this.runToFood = runs;
        this.hasEggStage = eggStage;
    }

    public boolean runsToFood() {
        return this.runToFood;
    }

    public boolean hasEggStage() {
        return this.hasEggStage;
    }

    public float getEatingRadius() {
        return this.eatingRadius;
    }

    @Override
    public Component createInstance() {
        return new EatingComponent(this);
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        String names = "";
        int i = 0;
        while (i < this.diet.length) {
            names = String.valueOf(names) + this.diet[i].getName();
            if (i != this.diet.length - 1) {
                names = String.valueOf(names) + ", ";
            }
            ++i;
        }
        info.get((Object)SpeciesInfoType.PREFERENCES).add(new SpeciesInfoLine(EATS, names));
        info.get((Object)SpeciesInfoType.PREFERENCES).add(new SpeciesInfoLine(HUNGER_INFO, String.valueOf(this.hungerPerHour) + " " + FOOD_POINTS));
    }

    @Override
    public void delete() {
    }

    public Ai getEatingAi(FoodFinder foodTarget, Transformation transform, MovementComp mover, GrowthComponent growth, EatingComponent eatingComp, boolean urgent) {
        if (this.eatingAiId == 0) {
            return new StandardEatingAi(foodTarget, transform, mover, eatingComp, urgent);
        }
        if (this.eatingAiId == 1) {
            return new FishEatingAi(foodTarget, transform, mover, eatingComp, urgent);
        }
        return new BirdEatAi(foodTarget, transform, mover, eatingComp, urgent, growth);
    }

    protected int getMaxHungerPoints() {
        return this.maxHungerPoints;
    }

    protected float getHungerPerHour() {
        return this.hungerPerHour;
    }

    protected DietOption[] getDiet() {
        return this.diet;
    }
}


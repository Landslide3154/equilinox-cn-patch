/*
 * Decompiled with CFR 0.152.
 */
package eating;

import componentArchitecture.ComponentType;
import eating.DietOption;
import eating.EatingComponent;
import entityBundle.EntityBundle;
import food.FoodComponent;
import food.FoodSectionType;
import gameManaging.GameManager;
import instances.Entity;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class FoodFinder {
    private static final int MAX_CHECK = 5;
    private final Transformation transform;
    private final EatingComponent eatingComp;
    private Entity target;
    private DietOption dietOption;
    private FoodComponent foodTarget;

    public FoodFinder(Transformation transform, EatingComponent eatingComp) {
        this.transform = transform;
        this.eatingComp = eatingComp;
    }

    public Entity getTarget() {
        return this.target;
    }

    public FoodComponent getTargetFoodComp() {
        return this.foodTarget;
    }

    public FoodSectionType getFoodType() {
        return this.dietOption.getFoodType();
    }

    public DietOption getChosenDietOption() {
        return this.dietOption;
    }

    public boolean checkTarget() {
        return this.target != null && !this.target.isDead() && !this.target.isGrabbed() && this.foodTarget.canBeEaten(this.dietOption.getFoodType());
    }

    public boolean chooseTarget() {
        Vector3f pos = this.transform.getPosition();
        DietOption[] diet = this.eatingComp.getDiet();
        this.target = null;
        int i = 0;
        while (i < diet.length) {
            this.findFood(diet[i], pos);
            if (this.target != null) {
                return true;
            }
            ++i;
        }
        return false;
    }

    private void findFood(DietOption foodType, Vector3f pos) {
        EntityBundle entities = GameManager.getWorld().getListOfSpecies(foodType.getClassification(), this.eatingComp.getSearchRange(), pos.x, pos.z);
        if (!entities.isEmpty()) {
            this.chooseRandomEdibleEntity(entities, foodType);
        }
    }

    private void chooseRandomEdibleEntity(EntityBundle entities, DietOption dietOption) {
        Entity[] randomList;
        Entity[] entityArray = randomList = entities.getRandomList(5);
        int n = randomList.length;
        int n2 = 0;
        while (n2 < n) {
            Entity entity = entityArray[n2];
            FoodComponent food = (FoodComponent)entity.getComponent(ComponentType.FOOD);
            if (food != null && food.canBeEaten(dietOption.getFoodType())) {
                this.setTarget(entity, food, dietOption);
                return;
            }
            ++n2;
        }
    }

    private void setTarget(Entity entity, FoodComponent food, DietOption dietOption) {
        this.foodTarget = food;
        this.target = entity;
        this.dietOption = dietOption;
    }
}


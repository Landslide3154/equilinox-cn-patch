/*
 * Decompiled with CFR 0.152.
 */
package eating;

import classification.Classification;
import classification.Classifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import utils.BinaryReader;
import utils.BinaryWriter;

public class DietTracker {
    private final int mealCount;
    private LinkedList<Classification> dietList;
    private boolean dirty = false;
    private Classification currentDiet = null;

    public DietTracker(int mealCount) {
        this.mealCount = mealCount;
        this.dietList = new LinkedList();
    }

    public DietTracker(int mealCount, LinkedList<Classification> dietList) {
        this.mealCount = mealCount;
        this.dietList = dietList;
        this.dirty = true;
    }

    public void registerMeal(Classification speciesClassification) {
        this.dietList.addFirst(speciesClassification);
        this.dirty = true;
        if (this.dietList.size() > this.mealCount) {
            this.dietList.removeLast();
        }
    }

    public Collection<Classification> getFullDiet() {
        return this.dietList;
    }

    public Classification getCurrentMainDiet() {
        if (this.dirty) {
            this.currentDiet = this.recalculateCurrentDiet();
            this.dirty = false;
        }
        return this.currentDiet;
    }

    public boolean isDietPredominantly(Classification checkDiet) {
        int passMark = (this.dietList.size() + 1) / 2;
        int currentScore = 0;
        for (Classification food : this.dietList) {
            if (!food.isTypeOf(checkDiet) || ++currentScore != passMark) continue;
            return true;
        }
        return false;
    }

    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.dietList.size());
        for (Classification food : this.dietList) {
            writer.writeString(food.getKey());
        }
    }

    public static DietTracker load(int mealCount, BinaryReader reader) throws Exception {
        LinkedList<Classification> dietList = new LinkedList<Classification>();
        int size = reader.readInt();
        int i = 0;
        while (i < size) {
            dietList.add(Classifier.getClassification(reader.readString()));
            ++i;
        }
        return new DietTracker(mealCount, dietList);
    }

    private Classification recalculateCurrentDiet() {
        int passMark = (this.dietList.size() + 1) / 2;
        ArrayList<Classification> foodCats = new ArrayList<Classification>();
        int tier = this.fillInitialList(foodCats);
        return this.findCurrentDiet(tier, foodCats, passMark);
    }

    private int fillInitialList(List<Classification> foodCats) {
        int tier = 0;
        for (Classification food : this.dietList) {
            foodCats.add(food);
            tier = Math.max(food.getCategoryTier(), tier);
        }
        return tier;
    }

    private Classification findCurrentDiet(int tier, List<Classification> foodCats, int passMark) {
        int i = tier - 1;
        while (i >= 0) {
            Classification result = this.getMajorityClassifications(foodCats, passMark);
            if (result != null) {
                return result;
            }
            foodCats.clear();
            for (Classification food : this.dietList) {
                foodCats.add(food.getTier(i));
            }
            --i;
        }
        return null;
    }

    private Classification getMajorityClassifications(List<Classification> classifications, int passMark) {
        HashMap<Classification, Integer> counts = new HashMap<Classification, Integer>();
        int highestCount = 0;
        for (Classification foodClass : classifications) {
            Integer count = (Integer)counts.get(foodClass);
            if (count == null) {
                count = 0;
            }
            int newCount = count + 1;
            highestCount = Math.max(newCount, highestCount);
            counts.put(foodClass, newCount);
        }
        if (highestCount < passMark) {
            return null;
        }
        Classification result = null;
        for (Map.Entry entry : counts.entrySet()) {
            if ((Integer)entry.getValue() != highestCount) continue;
            if (result == null) {
                result = (Classification)entry.getKey();
                continue;
            }
            return null;
        }
        return result;
    }
}


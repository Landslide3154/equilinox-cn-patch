/*
 * Decompiled with CFR 0.152.
 */
package shopping;

import blueprints.Blueprint;
import classification.Classification;
import classification.Classifier;
import gameManaging.GameManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mainGuis.GuiRepository;
import resourceManagement.BlueprintRepository;
import sessionStats.LockStatus;
import shopping.Shop;
import shops.PlacementManager;
import shops.ShopItem;

public class ShopManager {
    private final Shop plantShop;
    private final Shop animalShop;
    private final PlacementManager placementManager;

    public ShopManager() {
        List<Blueprint> allSpecies = GameManager.BREED_TREES.getAllSpecies();
        this.plantShop = this.createPlantShop(allSpecies);
        this.animalShop = this.createAnimalShop(allSpecies);
        this.placementManager = new PlacementManager();
    }

    public void reset() {
        this.placementManager.clear();
        this.plantShop.reset();
        this.animalShop.reset();
    }

    public PlacementManager getPlacementManager() {
        return this.placementManager;
    }

    public ShopItem getItem(int id) {
        Blueprint blueprint = BlueprintRepository.getBlueprint(id);
        if (blueprint.getClassification().isTypeOf(Classifier.getAnimalClassification())) {
            return this.animalShop.getItem(id);
        }
        return this.plantShop.getItem(id);
    }

    public void unlockNecessaryItems(Collection<Integer> unlockedItems) {
        this.animalShop.addUnlockedSpecies(unlockedItems);
        this.plantShop.addUnlockedSpecies(unlockedItems);
    }

    public void updateLockStatus(LockStatus lockStatus) {
        this.animalShop.updateLockedStatus(lockStatus);
        this.plantShop.updateLockedStatus(lockStatus);
    }

    public void unlockAll() {
        this.animalShop.unlockAll();
        this.plantShop.unlockAll();
    }

    public void unlockItems(Collection<Integer> unlockedItems) {
        for (int id : unlockedItems) {
            this.unlockItem(id);
        }
    }

    public void unlockItem(int id) {
        this.animalShop.unlockSpecies(id);
        this.plantShop.unlockSpecies(id);
    }

    public Shop getPlantShop() {
        return this.plantShop;
    }

    public Shop getAnimalShop() {
        return this.animalShop;
    }

    private Shop createPlantShop(List<Blueprint> allSpecies) {
        ArrayList<Classification> categories = new ArrayList<Classification>();
        categories.addAll(Classifier.getPlantClassification().getChildren());
        categories.add(Classifier.getClassification("er"));
        ArrayList<Blueprint> items = new ArrayList<Blueprint>();
        Classification plantCategory = Classifier.getPlantClassification();
        for (Blueprint species : allSpecies) {
            if (!species.getClassification().isTypeOf(plantCategory)) continue;
            items.add(species);
        }
        items.add(BlueprintRepository.getBlueprint(6));
        items.add(BlueprintRepository.getBlueprint(9));
        items.add(BlueprintRepository.getBlueprint(35));
        items.add(BlueprintRepository.getBlueprint(134));
        items.add(BlueprintRepository.getBlueprint(162));
        items.add(BlueprintRepository.getBlueprint(163));
        items.add(BlueprintRepository.getBlueprint(166));
        items.add(BlueprintRepository.getBlueprint(181));
        return new Shop(GuiRepository.SPECIES_256, categories, items, true);
    }

    private Shop createAnimalShop(List<Blueprint> allSpecies) {
        ArrayList<Classification> categories = new ArrayList<Classification>();
        categories.addAll(Classifier.getAnimalClassification().getChildren());
        ArrayList<Blueprint> items = new ArrayList<Blueprint>();
        Classification animalCategory = Classifier.getAnimalClassification();
        for (Blueprint species : allSpecies) {
            if (!species.getClassification().isTypeOf(animalCategory)) continue;
            items.add(species);
        }
        return new Shop(GuiRepository.ANIMAL_128, categories, items, false);
    }
}


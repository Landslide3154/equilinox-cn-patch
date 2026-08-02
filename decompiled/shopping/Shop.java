/*
 * Decompiled with CFR 0.152.
 */
package shopping;

import biomes.Biome;
import biomes.SpreaderCompBlueprint;
import blueprints.Blueprint;
import classification.Classification;
import componentArchitecture.ComponentType;
import environment.EnviroFactorBlueprint;
import gameManaging.GameManager;
import gridLayout.CategoryNames;
import gridLayout.FilterId;
import gridLayout.FilterOptions;
import gridLayout.PageTracker;
import health.LifeCompBlueprint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import languages.GameText;
import resourceManagement.BlueprintRepository;
import sessionStats.LockStatus;
import shopping.BlueprintShopItem;
import shops.ShopItem;
import textures.Texture;
import userInterfaces.TabButtonUi;

public class Shop {
    private static final String ALL = GameText.getText(1050);
    private static final String NO_REQ = GameText.getText(1051);
    private static final String LIVES_IN = GameText.getText(1052);
    private static final String SPREADS = GameText.getText(1053);
    private static final String LIVE_PREFIX = GameText.getText(1054);
    private static final String SPREAD_PREFIX = GameText.getText(1055);
    private final Collection<Classification> categories;
    private TabButtonUi shopTab;
    private final Texture shopLargeIcon;
    private final boolean showSpreadFilter;
    private Map<Integer, ShopItem> items = new LinkedHashMap<Integer, ShopItem>();
    private PageTracker tracker = new PageTracker(2);

    public Shop(Texture shopLargeIcon, Collection<Classification> mainCategories, Collection<Blueprint> allItems, boolean showSpreadFilter) {
        this.categories = mainCategories;
        this.shopLargeIcon = shopLargeIcon;
        this.showSpreadFilter = showSpreadFilter;
        for (Blueprint item : allItems) {
            this.addItem(item);
        }
    }

    public Texture getLargeIcon() {
        return this.shopLargeIcon;
    }

    public void setTab(TabButtonUi tab) {
        this.shopTab = tab;
    }

    public PageTracker getTracker() {
        return this.tracker;
    }

    public TabButtonUi getShopButton() {
        return this.shopTab;
    }

    private void addItem(Blueprint blueprint) {
        LifeCompBlueprint lifeComp;
        int[] categories = this.getCategoryNumber(blueprint.getSpeciesClassification());
        FilterId filterValues = new FilterId(0);
        if (categories[1] == -1) {
            filterValues.add(0, categories[0]);
        } else {
            filterValues.add(0, categories[0], categories[1]);
        }
        SpreaderCompBlueprint spreader = (SpreaderCompBlueprint)blueprint.getComponent(ComponentType.SPREADER);
        if (this.showSpreadFilter && spreader != null) {
            filterValues.add(1, 1, spreader.biome.ordinal());
        }
        if ((lifeComp = (LifeCompBlueprint)blueprint.getComponent(ComponentType.LIFE)) != null) {
            List<EnviroFactorBlueprint> factors = lifeComp.enviroBlueprint.getFactorBlueprints();
            for (EnviroFactorBlueprint factor : factors) {
                factor.addFilterValues(filterValues);
            }
        }
        this.items.put(blueprint.getId(), new BlueprintShopItem(blueprint, filterValues, this));
    }

    public List<ShopItem> getShopStock() {
        ArrayList<ShopItem> itemList = new ArrayList<ShopItem>();
        itemList.addAll(this.items.values());
        return itemList;
    }

    public void unlockSpecies(int id) {
        ShopItem item = this.items.get(id);
        if (item != null && item.isLocked()) {
            item.setLocked(false);
        }
    }

    public FilterOptions getCategoryNames() {
        LinkedHashMap<String, String[]> allCats = new LinkedHashMap<String, String[]>();
        for (Classification category : this.categories) {
            this.addCategoryName(allCats, category);
        }
        LinkedHashMap<String, String[]> biomeCats = new LinkedHashMap<String, String[]>();
        String[] biomeNames = new String[Biome.values().length];
        int i = 0;
        while (i < biomeNames.length) {
            biomeNames[i] = Biome.values()[i].toString();
            ++i;
        }
        biomeCats.put(LIVES_IN, biomeNames);
        if (this.showSpreadFilter) {
            biomeCats.put(SPREADS, biomeNames);
        }
        CategoryNames main = new CategoryNames(ALL, allCats, true);
        CategoryNames reqs = new CategoryNames(NO_REQ, biomeCats, false);
        reqs.setPrefixes(new String[]{String.valueOf(LIVE_PREFIX) + ": ", String.valueOf(SPREAD_PREFIX) + ": "});
        return new FilterOptions(main, reqs);
    }

    public void addUnlockedSpecies(Collection<Integer> unlockedSpecies) {
        for (Integer speciesId : unlockedSpecies) {
            Blueprint species = BlueprintRepository.getBlueprint(speciesId);
            if (species == null || GameManager.BREED_TREES.isBaseSpecies(species)) continue;
            this.unlockSpecies(species.getId());
        }
    }

    public int getNumberUnlocked() {
        int count = 0;
        for (ShopItem item : this.items.values()) {
            if (item.isLocked()) continue;
            ++count;
        }
        return count;
    }

    public ShopItem getItem(int id) {
        return this.items.get(id);
    }

    public void updateLockedStatus(LockStatus lockedStatus) {
        for (ShopItem item : this.items.values()) {
            if (item.isLocked()) continue;
            lockedStatus.unlockBlueprint(((BlueprintShopItem)item).getBlueprint());
        }
    }

    public void unlockAll() {
        for (ShopItem item : this.items.values()) {
            item.setLocked(false);
            item.setNotNew();
        }
        this.tracker.reset();
    }

    public void reset() {
        for (ShopItem item : this.items.values()) {
            Blueprint species = ((BlueprintShopItem)item).getBlueprint();
            if (!item.hasLinkedTask() && (!this.isAlive(species) || GameManager.BREED_TREES.isBaseSpecies(species))) continue;
            item.setLocked(true);
            item.setNotNew();
        }
        this.tracker.reset();
    }

    private int[] getCategoryNumber(Classification speciesCat) {
        int[] categoryIds = new int[2];
        for (Classification category : this.categories) {
            boolean done = Shop.checkCategory(speciesCat, category, categoryIds);
            if (!done) continue;
            return categoryIds;
        }
        return null;
    }

    private static boolean checkCategory(Classification speciesCat, Classification cat, int[] categoryIds) {
        if (speciesCat.isTypeOf(cat)) {
            if (cat.getChildren().isEmpty()) {
                categoryIds[1] = -1;
            } else {
                for (Classification subCat : cat.getChildren()) {
                    if (speciesCat.isTypeOf(subCat)) break;
                    categoryIds[1] = categoryIds[1] + 1;
                }
            }
            return true;
        }
        categoryIds[0] = categoryIds[0] + 1;
        return false;
    }

    private void addCategoryName(Map<String, String[]> allCats, Classification classification) {
        Collection<Classification> subs = classification.getChildren();
        String[] subCategories = new String[subs.size()];
        int i = 0;
        for (Classification sub : subs) {
            subCategories[i++] = sub.getName();
        }
        allCats.put(classification.getName(), subCategories);
    }

    private boolean isAlive(Blueprint species) {
        return species.getComponent(ComponentType.LIFE) != null;
    }
}


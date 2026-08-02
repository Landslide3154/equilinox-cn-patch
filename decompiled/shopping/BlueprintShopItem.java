/*
 * Decompiled with CFR 0.152.
 */
package shopping;

import audio.SoundMaestro;
import blueprints.Blueprint;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import gameManaging.GameManager;
import gridLayout.CurrentFilterSettings;
import gridLayout.FilterId;
import gridLayout.GridComponent;
import gridLayout.ItemPageGui;
import guis.GuiComponent;
import health.LifeCompBlueprint;
import languages.ComplexString;
import languages.GameText;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import session.GameMode;
import shopping.Shop;
import shops.LockedRequirementUi;
import shops.ShopItem;
import shops.ShopItemGui;
import speciesInformation.SpeciesInfoGui;
import tasks.Task;
import textures.Texture;
import unlockGuide.UnlockGuideUi;
import userInterfaces.Listener;

public class BlueprintShopItem
implements ShopItem {
    private static final String REQ_TASK = GameText.getText(92);
    private static final String EVOLVE_FROM = GameText.getText(690);
    private static final String SPEC_UNLOCK_TITLE = GameText.getText(86);
    private static final String ITEM_UNLOCK_TITLE = GameText.getText(87);
    private static final ComplexString UNLOCK_DESC = GameText.getComplexText(88);
    private static final ComplexString ITEM_UNLOCK = GameText.getComplexText(90);
    protected final Blueprint blueprint;
    private InformationComponent.InformationCompBlueprint info;
    private boolean locked = false;
    private boolean linkedTask = false;
    private boolean isNew = false;
    private Task requiredTask;
    private final Shop shop;
    private final FilterId filterValues;

    public BlueprintShopItem(Blueprint blueprint, FilterId filterValues, Shop shop) {
        this.blueprint = blueprint;
        this.filterValues = filterValues;
        this.shop = shop;
        this.info = (InformationComponent.InformationCompBlueprint)blueprint.getComponent(ComponentType.INFO);
    }

    public Blueprint getBlueprint() {
        return this.blueprint;
    }

    @Override
    public int getPrice() {
        return this.info.getPrice();
    }

    @Override
    public String getName() {
        return this.info.getName();
    }

    @Override
    public Texture getIcon() {
        return this.info.getIcon();
    }

    @Override
    public boolean isLocked() {
        return this.locked;
    }

    @Override
    public void buy() {
        GameManager.getShops().getPlacementManager().selectItem(this.blueprint, this.shop.getShopButton(), false);
    }

    @Override
    public void unlock() {
        if (this.blueprint.getComponent(ComponentType.LIFE) != null) {
            this.notifySpeciesUnlocked(this.shop.getLargeIcon());
            GameManager.getSession().getStats().getLockStatus().unlockBlueprint(this.blueprint);
        } else {
            this.notifyBonusItemUnlocked();
        }
        this.isNew = true;
        this.locked = false;
    }

    @Override
    public void displayInfo() {
        SpeciesInfoGui.createSpeciesInfoGui(this.blueprint);
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public void linkTask(Task task) {
        this.linkedTask = true;
        this.requiredTask = task;
    }

    @Override
    public GuiComponent getComponentGui(ItemPageGui itemPage) {
        return new ShopItemGui(this, itemPage);
    }

    @Override
    public boolean hasLinkedTask() {
        return this.linkedTask;
    }

    @Override
    public int compareTo(GridComponent other) {
        return this.info.getPrice() > ((BlueprintShopItem)other).info.getPrice() ? 1 : -1;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @Override
    public void setNotNew() {
        this.isNew = false;
    }

    @Override
    public int getTier() {
        return GameManager.BREED_TREES.getTier(this.blueprint);
    }

    @Override
    public boolean isSpecial() {
        return this.blueprint.isSecret();
    }

    @Override
    public Task getRequiredTask() {
        return this.requiredTask;
    }

    @Override
    public Shop getShop() {
        return this.shop;
    }

    @Override
    public boolean flipIcon() {
        return this.info.isFlipTexture();
    }

    @Override
    public boolean isInFilterGroup(CurrentFilterSettings currentFilter) {
        return currentFilter.check(this.filterValues);
    }

    private void notifySpeciesUnlocked(Texture icon) {
        EquilinoxGuis.notify(SPEC_UNLOCK_TITLE, UNLOCK_DESC.getString(this.info.getName()), icon, null, new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                SpeciesInfoGui.createSpeciesInfoGui(BlueprintShopItem.this.blueprint);
            }
        });
    }

    private void notifyBonusItemUnlocked() {
        EquilinoxGuis.notify(ITEM_UNLOCK_TITLE, ITEM_UNLOCK.getString(this.info.getName()), GuiRepository.ITEMS_256, null, new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                SpeciesInfoGui.createSpeciesInfoGui(BlueprintShopItem.this.blueprint);
            }
        });
    }

    @Override
    public GuiComponent getLockedMouseover() {
        if (this.linkedTask) {
            return new LockedRequirementUi(REQ_TASK, this.requiredTask.name);
        }
        LifeCompBlueprint lifeComp = (LifeCompBlueprint)this.blueprint.getComponent(ComponentType.LIFE);
        return new LockedRequirementUi(EVOLVE_FROM, lifeComp.breedInfo.getParent().getName());
    }

    @Override
    public void reactToLockedClick() {
        if (GameManager.getGameMode() == GameMode.NORMAL) {
            SoundMaestro.playSystemSound(GuiSounds.SELECT);
            UnlockGuideUi.openUnlockGuidePanel(this);
        } else {
            SoundMaestro.playSystemSound(GuiSounds.NEGATIVE);
        }
    }

    @Override
    public boolean matchesSearch(String searchString) {
        return this.getName().toLowerCase().contains(searchString.toLowerCase());
    }
}


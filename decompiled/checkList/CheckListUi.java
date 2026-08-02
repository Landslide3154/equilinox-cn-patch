/*
 * Decompiled with CFR 0.152.
 */
package checkList;

import basics.DisplayManager;
import checkList.DisplayContents;
import checkList.ListDisplayContents;
import checkList.ListElement;
import checkList.MultiDataDisplayUi;
import entityBundle.EntityBundle;
import evolveStatusOverview.EvolveDisplayContents;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import instances.Entity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import languages.GameText;
import main.Camera;
import main.EquilinoxMusic;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import music.MusicTrack;
import notificationPopUp.NotificationDisplayContents;
import org.lwjgl.util.vector.Vector2f;
import shopping.BlueprintShopItem;
import shops.ShopItem;
import tasks.Task;
import tasks.TaskManager;
import tasks.TaskState;
import userInterfaces.Listener;
import userInterfaces.ProgressBarUi;

public class CheckListUi
extends GuiComponent {
    private static final int NORMAL_COMPLETE = 14;
    private static final int TOP_PAD_PIXELS = 35;
    private static final int SIDE_PIXELS = 40;
    private static final int BAR_HEIGHT_PIXELS = 30;
    private static final String PLANTS = GameText.getText(859);
    private static final String ANIMALS = GameText.getText(860);
    private static final String MUSIC = GameText.getText(861);
    private static final String TASKS = GameText.getText(862);
    private static final String EVOLVE = GameText.getText(1063);
    private static final String NOTIFICATIONS = GameText.getText(1116);
    private final ProgressBarUi progressBar;
    private final MultiDataDisplayUi displayUi;
    int count = 0;
    int complete = 0;
    private int nextAnimalIndex = 0;

    public CheckListUi() {
        Map<String, DisplayContents> data = this.initData();
        this.progressBar = new ProgressBarUi((float)(this.complete - 14) / (float)(this.count - 14));
        this.displayUi = new MultiDataDisplayUi(data);
    }

    @Override
    protected void init() {
        super.init();
        float sidePad = 40.0f / (this.getScale().x * (float)DisplayManager.getUiWidth());
        this.addProgressBar(sidePad);
        this.addDisplayUi(sidePad);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addDisplayUi(float sidePad) {
        float pixelHeight = super.getPixelHeight();
        float yStart = 100.0f / pixelHeight;
        float gapHeight = 35.0f / pixelHeight;
        float height = 1.0f - (yStart + gapHeight);
        float width = 1.0f - 2.0f * sidePad;
        super.addComponent(this.displayUi, sidePad, yStart, width, height);
    }

    private void addProgressBar(float sidePad) {
        this.progressBar.setBarColour(ColourPalette.LIGHT_GREEN);
        this.progressBar.showText(ColourPalette.WHITE, UiSettings.TITLE_FONT, 0.0f);
        float pixelHeight = super.getPixelHeight();
        float yStart = 35.0f / pixelHeight;
        float height = 30.0f / pixelHeight;
        super.addComponent(this.progressBar, sidePad, yStart, 1.0f - 2.0f * sidePad, height);
    }

    private Map<String, DisplayContents> initData() {
        int count;
        LinkedHashMap<String, DisplayContents> data = new LinkedHashMap<String, DisplayContents>();
        ArrayList<ListElement> list = new ArrayList<ListElement>();
        TaskManager taskManager = GameManager.getTaskManager();
        data.put(EVOLVE, new EvolveDisplayContents());
        data.put(NOTIFICATIONS, new NotificationDisplayContents());
        for (ShopItem item : GameManager.getShops().getAnimalShop().getShopStock()) {
            EntityBundle bundle;
            ++this.count;
            count = 0;
            if (!item.isLocked()) {
                count = GameManager.getWorld().getEntityGrid().getSortedEntities().getEntityCount(((BlueprintShopItem)item).getBlueprint().getSpeciesClassification());
                bundle = GameManager.getWorld().getEntityGrid().getSortedEntities().getEntities(((BlueprintShopItem)item).getBlueprint());
                count = bundle == null ? 0 : bundle.getSize();
                ++this.complete;
            } else {
                bundle = null;
            }
            list.add(new ListElement(item.getName(), !item.isLocked(), ((BlueprintShopItem)item).getBlueprint(), count, new Listener(){

                @Override
                public void eventOccurred(boolean on) {
                    if (bundle != null) {
                        CheckListUi checkListUi = CheckListUi.this;
                        checkListUi.nextAnimalIndex = checkListUi.nextAnimalIndex % bundle.getSize();
                        Entity nextEntity = bundle.get(CheckListUi.this.nextAnimalIndex);
                        CheckListUi checkListUi2 = CheckListUi.this;
                        checkListUi2.nextAnimalIndex = checkListUi2.nextAnimalIndex + 1;
                        Camera.getCamera().focusOn(nextEntity.getTransform().getPosition());
                    }
                }
            }));
        }
        data.put(ANIMALS, new ListDisplayContents(ANIMALS, list));
        list = new ArrayList();
        for (ShopItem item : GameManager.getShops().getPlantShop().getShopStock()) {
            EntityBundle bundle2;
            ++this.count;
            count = 0;
            if (!item.isLocked()) {
                bundle2 = GameManager.getWorld().getEntityGrid().getSortedEntities().getEntities(((BlueprintShopItem)item).getBlueprint());
                count = bundle2 == null ? 0 : bundle2.getSize();
                ++this.complete;
            } else {
                bundle2 = null;
            }
            list.add(new ListElement(item.getName(), !item.isLocked(), ((BlueprintShopItem)item).getBlueprint(), count, new Listener(){

                @Override
                public void eventOccurred(boolean on) {
                    if (bundle2 != null) {
                        CheckListUi checkListUi = CheckListUi.this;
                        checkListUi.nextAnimalIndex = checkListUi.nextAnimalIndex % bundle2.getSize();
                        Entity nextEntity = bundle2.get(CheckListUi.this.nextAnimalIndex);
                        CheckListUi checkListUi2 = CheckListUi.this;
                        checkListUi2.nextAnimalIndex = checkListUi2.nextAnimalIndex + 1;
                        Camera.getCamera().focusOn(nextEntity.getTransform().getPosition());
                    }
                }
            }));
        }
        data.put(PLANTS, new ListDisplayContents(PLANTS, list));
        list = new ArrayList();
        for (Task task : taskManager.getTasks()) {
            boolean completed = task.getState() == TaskState.COMPLETE || task.alreadyCompleted();
            ++this.count;
            if (completed) {
                ++this.complete;
            }
            list.add(new ListElement(task.name, completed));
        }
        data.put(TASKS, new ListDisplayContents(TASKS, list));
        list = new ArrayList();
        for (MusicTrack track : EquilinoxMusic.getPlaylist().getOrderedTracks()) {
            ++this.count;
            if (!track.isLocked()) {
                ++this.complete;
            }
            list.add(new ListElement(track.getName(), !track.isLocked()));
        }
        data.put(MUSIC, new ListDisplayContents(MUSIC, list));
        System.out.println(String.valueOf(this.complete) + " completed.");
        return data;
    }
}


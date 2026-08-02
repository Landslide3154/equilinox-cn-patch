/*
 * Decompiled with CFR 0.152.
 */
package toolbar;

import basics.DisplayManager;
import checkList.CheckListUi;
import gameManaging.GameManager;
import gridLayout.GridGui;
import gridLayout.StatusGetter;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import helpUi.HelpPanelUi;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.DiseaseCounterGui;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.HungerCounterUi;
import mainGuis.UiSettings;
import musicTab.MusicUi;
import org.lwjgl.util.vector.Vector2f;
import shopping.Shop;
import shops.ShopItem;
import tasks.TaskState;
import textures.Texture;
import toolTips.ToolTipInfo;
import toolbar.BiomePicker;
import toolbar.DpCounter;
import toolbar.DppmCounter;
import toolbar.SlideInPanel;
import toolbar.SlideInPanelSync;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickableGroup;
import userInterfaces.TabButtonUi;
import utils.MyFile;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class Toolbar
extends GuiComponent {
    private static final String HELP_TIP = GameText.getText(157);
    private static final String HELP_DESC = GameText.getText(158);
    private static final String SPECIES_TITLE = GameText.getText(159);
    private static final String SPECIES_TIP = GameText.getText(160);
    private static final String SPECIES_DESC = GameText.getText(161);
    private static final String BONUS_TITLE = GameText.getText(162);
    private static final String BONUS_TIP = GameText.getText(163);
    private static final String BONUS_DESC = GameText.getText(164);
    private static final String TASK_NOTIFY = GameText.getText(165);
    private static final String TASK_TIP = GameText.getText(166);
    private static final String TASK_DESC = GameText.getText(167);
    private static final String BIOME_TIP = GameText.getText(168);
    private static final String BIOME_DESC = GameText.getText(169);
    private static final String ERASE_TIP = GameText.getText(1091);
    private static final String ERASE_DESC = GameText.getText(1092);
    private static final String MUSIC_TIP = GameText.getText(170);
    private static final String MUSIC_DESC = GameText.getText(171);
    private static final String CHECK_TIP = GameText.getText(209);
    private static final String CHECK_DESC = GameText.getText(210);
    public static final Texture BIOME_ICON = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "biomePick.png")).noFiltering().clampEdges().create();
    public static final Texture BASES_ICON = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "bases.png")).noFiltering().clampEdges().create();
    public static final Texture TASKS_ICON = GuiRepository.TASKS;
    public static final Texture MUSIC_ICON = GuiRepository.MUSIC;
    public static final int PIXEL_WIDTH = 645;
    public static final int PIXEL_HEIGHT = 36;
    public static final float HEIGHT = 36.0f / (float)DisplayManager.getUiHeight();
    public static final int FONT_SIZE_FACTOR = 576;
    public static final float FONT_SIZE = UiSettings.LARGE_FONT;
    public static final float TEXT_Y = 0.19f;
    public static final int BAR_WIDTH_PIXELS = 15;
    private static final float BAR_WIDTH = 0.023255814f;
    private static final float COUNTER_WIDTH = 0.26f;
    private static final float PER_MINUTE_WIDTH = 0.26f;
    public static final float BUTTON_HEIGHT = 0.6f;
    private static final float SEPARATOR_WIDTH = 0.0015503876f;
    public static final float SLIDE_TIME = 0.2f;
    private static final float TEXT_Y_OFFSET = -0.015f;
    private static final float ONE_PIX = 0.0015503876f;
    private static final float COUNTER_X = 0.023255814f;
    private static final float PER_MIN_X = 0.28325582f;
    private static final float BUTTON1_X = 0.5432558f;
    private static final float BUTTON_AREA_WIDTH = 0.4567442f;
    private static final float BUTTON_COUNT = 8.0f;
    private static final float BUTTON_WIDTH = 0.056899227f;
    private static final float BUTTON2_X = 0.60015506f;
    private static final float BUTTON3_X = 0.6570543f;
    private static final float BUTTON4_X = 0.71395355f;
    private static final float BUTTON5_X = 0.7708528f;
    private static final float BUTTON6_X = 0.82775205f;
    private static final float SEPARATOR3_X = 0.8846513f;
    private static final float BUTTON7_X = 0.8862017f;
    private static final float SEPARATOR2_X = 0.5417054f;
    private static final float BUTTON8_X = 0.9431009f;
    private static final float HUNGER_X = 1.0000001f;
    private static final float DISEASE_X = 1.0568993f;
    private static final float BUTTON_Y = 0.19999999f;
    private static final float BUTTON_OFFSET = 0.028546512f;
    public static final float Y_GAP = 0.01f;
    private GuiTexture background;
    private GuiTexture bar;
    private GuiTexture separator1;
    private GuiTexture separator2;
    private GuiTexture separator4;
    private DpCounter dpCounter;
    private DppmCounter dppmCounter;
    private GuiClickableGroup group;
    private TabButtonUi eraserButton;
    private TabButtonUi biomeButton;
    private TabButtonUi plantShopButton;
    private TabButtonUi animalShopButton;
    private TabButtonUi taskButton;
    private TabButtonUi helpButton;
    private DiseaseCounterGui diseaseCounter;
    private HungerCounterUi hungerCounter;
    private ValueDriver yDriver = new ConstantDriver(0.0f);
    private boolean displayed = false;
    private SlideInPanel largePanel;
    private SlideInPanelSync panelSync = new SlideInPanelSync();
    private BiomePicker biomePicker;
    private boolean reduce = false;
    private TabButtonUi musicButton;
    private TabButtonUi statButton;

    public Toolbar() {
        this.diseaseCounter = new DiseaseCounterGui();
        this.hungerCounter = new HungerCounterUi();
        this.initToolbarTextures();
        this.addCounters();
        float yScale = HEIGHT;
        float xScale = 645.0f / (float)DisplayManager.getUiWidth();
        GuiMaster.addComponent(this, 0.0f, -yScale, xScale, yScale);
        this.largePanel = new SlideInPanel(this, yScale + 0.01f, xScale, true, false, false);
        super.addCenteredComponentX(this.hungerCounter, 1.0285466f, 0.19999999f, 0.6f);
        super.addCenteredComponentX(this.diseaseCounter, 1.0854459f, 0.19999999f, 0.6f);
        this.show(false);
    }

    public void reduce(boolean reduce) {
        this.reduce = reduce;
        this.taskButton.show(!reduce);
        this.statButton.show(!reduce);
        this.helpButton.show(!reduce);
        if (reduce) {
            this.plantShopButton.setRelativeX(0.71395355f);
            this.animalShopButton.setRelativeX(0.7708528f);
            this.musicButton.setRelativeX(0.82775205f);
        } else {
            this.plantShopButton.setRelativeX(0.5432558f);
            this.animalShopButton.setRelativeX(0.60015506f);
            this.musicButton.setRelativeX(0.7708528f);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.addButtons();
        this.biomePicker = new BiomePicker(this.biomeButton);
    }

    public void stopButtonWobbles() {
        if (this.taskButton != null) {
            this.taskButton.flash(false);
        }
        if (this.plantShopButton != null) {
            this.plantShopButton.flash(false);
        }
        if (this.animalShopButton != null) {
            this.animalShopButton.flash(false);
        }
    }

    public DiseaseCounterGui getDiseaseCounter() {
        return this.diseaseCounter;
    }

    public HungerCounterUi getHungerCounter() {
        return this.hungerCounter;
    }

    public DpCounter getDpCounter() {
        return this.dpCounter;
    }

    public DppmCounter getDppmCounter() {
        return this.dppmCounter;
    }

    public void display(boolean display) {
        if (display) {
            this.displayToolbar();
        } else {
            this.undisplayToolbar();
        }
    }

    public boolean isDisplayed() {
        return this.displayed;
    }

    public void turnOffButtonOptions() {
        this.group.turnOffCurrentlyActive();
    }

    public void wobbleTaskButton() {
        if (!this.taskButton.isToggledOn()) {
            this.taskButton.flash(true);
        }
    }

    public void stopTaskButtonWobble() {
        if (!this.taskButton.isToggledOn()) {
            this.taskButton.flash(false);
        }
    }

    public void wobbleSpeciesButton() {
        if (!this.plantShopButton.isToggledOn()) {
            this.plantShopButton.flash(true);
        }
    }

    public TabButtonUi getSpeciesButton() {
        return this.plantShopButton;
    }

    public TabButtonUi getHelpButton() {
        return this.helpButton;
    }

    public TabButtonUi getBonusButton() {
        return this.animalShopButton;
    }

    public TabButtonUi getEraserButton() {
        return this.eraserButton;
    }

    public void wobbleBonusButton() {
        if (!this.animalShopButton.isToggledOn()) {
            this.animalShopButton.flash(true);
        }
    }

    public SlideInPanel displayGuestPanel(GuiComponent content, float xScale, float yScale, boolean horizontal) {
        SlideInPanel panel = new SlideInPanel(this, HEIGHT + 0.01f, xScale, yScale, false, false, horizontal);
        this.panelSync.display(panel, content);
        this.turnOffButtonOptions();
        return panel;
    }

    public boolean closeGuestPanel() {
        boolean active = this.group.turnOffCurrentlyActive();
        return this.checkCloseSlidePanel() || active;
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        return super.isMouseOverFocusIrrelevant() || this.diseaseCounter.isShown() && this.diseaseCounter.isMouseOverFocusIrrelevant() || this.hungerCounter.isShown() && this.hungerCounter.isMouseOverFocusIrrelevant();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        this.bar.setPosition(position.x, position.y, scale.x * 0.023255814f, scale.y);
        this.separator1.setPosition(position.x + scale.x * 0.28325582f, position.y, scale.x * 0.0015503876f, scale.y);
        if (this.reduce) {
            float pos = 0.7124032f;
            this.separator2.setPosition(position.x + scale.x * pos, position.y, scale.x * 0.0015503876f, scale.y);
        } else {
            this.separator2.setPosition(position.x + scale.x * 0.5417054f, position.y, scale.x * 0.0015503876f, scale.y);
        }
        this.separator4.setPosition(position.x + scale.x * 0.8846513f, position.y, scale.x * 0.0015503876f, scale.y);
    }

    @Override
    protected void updateSelf() {
        this.background.update();
        float yPos = this.yDriver.update(DisplayManager.getDeltaSeconds());
        super.setRelativeY(yPos);
        if (yPos <= -super.getRelativeScaleY()) {
            this.dpCounter.removeIndicatorTexts();
            this.show(false);
        }
        this.biomePicker.update();
        this.panelSync.update();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.bar);
        if (!this.reduce) {
            data.addTexture(this.getLevel(), this.separator1);
        }
        data.addTexture(this.getLevel(), this.separator2);
        data.addTexture(this.getLevel(), this.separator4);
    }

    private void initToolbarTextures() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.DARK_GREY);
        this.background.setAlphaDriver(new ConstantDriver(0.75f));
        this.background.setBlurry(true);
        this.bar = new GuiTexture(GuiRepository.BLOCK);
        this.bar.setOverrideColour(ColourPalette.GREEN);
        this.separator1 = new GuiTexture(GuiRepository.BLOCK);
        this.separator1.setOverrideColour(ColourPalette.MIDDLE_GREY);
        this.separator2 = new GuiTexture(GuiRepository.BLOCK);
        this.separator2.setOverrideColour(ColourPalette.MIDDLE_GREY);
        this.separator4 = new GuiTexture(GuiRepository.BLOCK);
        this.separator4.setOverrideColour(ColourPalette.MIDDLE_GREY);
    }

    private void addCounters() {
        this.dpCounter = new DpCounter(this, 0);
        this.dppmCounter = new DppmCounter(0);
        super.addComponent(this.dpCounter, 0.023255814f, -0.015f, 0.26f, 1.0f);
        super.addComponent(this.dppmCounter, 0.28325582f, -0.015f, 0.26f, 1.0f);
    }

    private void addButtons() {
        this.group = new GuiClickableGroup();
        this.addBiomeButton();
        this.addEraserButton();
        this.addPlantShopButton();
        this.addAnimalShopButton();
        this.addCheckListButton();
        this.addHelpButton();
        this.addTasksButton();
        this.addMusicButton();
    }

    private void displayToolbar() {
        if (!this.displayed) {
            this.displayed = true;
            this.show(true);
            this.yDriver = new SlideDriver(this.getRelativeY(), 0.0f, 0.2f);
        }
    }

    private void undisplayToolbar() {
        if (this.displayed) {
            this.displayed = false;
            this.yDriver = new SlideDriver(this.getRelativeY(), -super.getRelativeScaleY(), 0.2f);
            this.group.turnOffCurrentlyActive();
            this.checkCloseSlidePanel();
        }
    }

    private void addPlantShopButton() {
        this.plantShopButton = this.addButton(GuiRepository.SPECIES_ICON, 0.5432558f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    EquilinoxGuis.getExtraInfoGui().close();
                    Shop speciesShop = GameManager.getShops().getPlantShop();
                    List<ShopItem> bases = speciesShop.getShopStock();
                    int unlockedCount = speciesShop.getNumberUnlocked();
                    int totalCount = bases.size();
                    final String textString = String.valueOf(SPECIES_TITLE) + ": " + unlockedCount + "/" + totalCount;
                    Toolbar.this.panelSync.display(Toolbar.this.largePanel, new GridGui(bases, 0.71428573f, 100, new StatusGetter(){

                        @Override
                        public String getStatus() {
                            return textString;
                        }
                    }, speciesShop.getCategoryNames(), speciesShop.getTracker()));
                } else if (event.isToggleOff() && Toolbar.this.checkCloseSlidePanel()) {
                    EquilinoxGuis.getExtraInfoGui().close();
                }
            }
        });
        GameManager.getShops().getPlantShop().setTab(this.plantShopButton);
        this.plantShopButton.setToolTip(ToolTipInfo.newInfo(SPECIES_TIP, SPECIES_DESC));
    }

    private void addAnimalShopButton() {
        this.animalShopButton = this.addButton(GuiRepository.ANIMAL_ICON, 0.60015506f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    EquilinoxGuis.getExtraInfoGui().close();
                    Shop shop = GameManager.getShops().getAnimalShop();
                    List<ShopItem> items = shop.getShopStock();
                    int unlockedCount = shop.getNumberUnlocked();
                    int totalCount = items.size();
                    final String textString = String.valueOf(BONUS_TITLE) + ": " + unlockedCount + "/" + totalCount;
                    Toolbar.this.panelSync.display(Toolbar.this.largePanel, new GridGui(items, 0.71428573f, 100, new StatusGetter(){

                        @Override
                        public String getStatus() {
                            return textString;
                        }
                    }, shop.getCategoryNames(), shop.getTracker()));
                } else if (event.isToggleOff() && Toolbar.this.checkCloseSlidePanel()) {
                    EquilinoxGuis.getExtraInfoGui().close();
                }
            }
        });
        GameManager.getShops().getAnimalShop().setTab(this.animalShopButton);
        this.animalShopButton.setToolTip(ToolTipInfo.newInfo(BONUS_TIP, BONUS_DESC));
    }

    private void addTasksButton() {
        this.taskButton = this.addButton(GuiRepository.TASK_ICON, 0.6570543f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    EquilinoxGuis.getExtraInfoGui().close();
                    Toolbar.this.panelSync.display(Toolbar.this.largePanel, new GridGui(GameManager.getTaskManager().getTasks(), 1.2422361f, 170, new StatusGetter(){

                        @Override
                        public String getStatus() {
                            int unlockedCount = GameManager.getTaskManager().getCompletedTaskCount();
                            int totalCount = GameManager.getTaskManager().getTaskCount();
                            String textString = String.valueOf(TASK_NOTIFY) + ": " + unlockedCount + "/" + totalCount;
                            return textString;
                        }
                    }, TaskState.getCategories(), GameManager.getTaskManager().getTracker()));
                } else if (event.isToggleOff() && Toolbar.this.checkCloseSlidePanel()) {
                    EquilinoxGuis.getExtraInfoGui().close();
                }
            }
        });
        this.taskButton.setToolTip(ToolTipInfo.newInfo(TASK_TIP, TASK_DESC));
    }

    private void addBiomeButton() {
        this.biomeButton = this.addButton(GuiRepository.EYEDROP, 0.8862017f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    Toolbar.this.panelSync.close();
                    EquilinoxGuis.getExtraInfoGui().close();
                }
                if (event.toggleChange) {
                    Toolbar.this.biomePicker.activate(event.eventState);
                }
            }
        });
        this.biomeButton.setHotkey(48);
        this.biomeButton.setToolTip(ToolTipInfo.newInfo(BIOME_TIP, BIOME_DESC));
    }

    private void addEraserButton() {
        this.eraserButton = this.addButton(GuiRepository.ERASER, 0.9431009f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    Toolbar.this.panelSync.close();
                    EquilinoxGuis.getExtraInfoGui().close();
                }
                if (event.isToggleOn()) {
                    GameManager.getEntityPicker().setDeleteMode(2.0f);
                } else if (event.isToggleOff()) {
                    GameManager.getEntityPicker().turnOffDeleteMode();
                }
            }
        });
        this.eraserButton.setToolTip(ToolTipInfo.newInfo(ERASE_TIP, ERASE_DESC));
    }

    private void addMusicButton() {
        this.musicButton = this.addButton(GuiRepository.MUSIC_ICON, 0.7708528f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    Toolbar.this.panelSync.display(Toolbar.this.largePanel, new MusicUi());
                    EquilinoxGuis.getExtraInfoGui().close();
                } else if (event.isToggleOff() && Toolbar.this.checkCloseSlidePanel()) {
                    EquilinoxGuis.getExtraInfoGui().close();
                }
            }
        });
        this.musicButton.setToolTip(ToolTipInfo.newInfo(MUSIC_TIP, MUSIC_DESC));
    }

    public void openHelpUi(int tab) {
        HelpPanelUi.START_PAGE = tab;
        if (!this.helpButton.isToggledOn()) {
            this.helpButton.toggle();
        } else {
            this.panelSync.display(this.largePanel, new HelpPanelUi());
        }
    }

    private void addHelpButton() {
        this.helpButton = this.addButton(GuiRepository.HELP_ICON2, 0.82775205f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    Toolbar.this.panelSync.display(Toolbar.this.largePanel, new HelpPanelUi());
                } else if (event.isToggleOff()) {
                    HelpPanelUi.START_PAGE = 0;
                    if (Toolbar.this.checkCloseSlidePanel()) {
                        EquilinoxGuis.getExtraInfoGui().close();
                    }
                }
            }
        });
        this.helpButton.setToolTip(ToolTipInfo.newInfo(HELP_TIP, HELP_DESC));
    }

    private void addCheckListButton() {
        this.statButton = this.addButton(GuiRepository.LIST_ICON, 0.71395355f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                boolean close;
                if (event.isToggleOn()) {
                    EquilinoxGuis.getExtraInfoGui().close();
                    Toolbar.this.panelSync.display(Toolbar.this.largePanel, new CheckListUi());
                } else if (event.isToggleOff() && (close = Toolbar.this.checkCloseSlidePanel())) {
                    EquilinoxGuis.getExtraInfoGui().close();
                }
            }
        });
        this.statButton.setToolTip(ToolTipInfo.newInfo(CHECK_TIP, CHECK_DESC));
    }

    private TabButtonUi addButton(Texture icon, float xPos, ClickListener listener) {
        TabButtonUi button = new TabButtonUi(icon, 20);
        super.addComponent(button, xPos, 0.0f, 0.056899227f, 1.0f);
        button.addListener(listener);
        this.group.addButton(button);
        return button;
    }

    private boolean checkCloseSlidePanel() {
        if (this.group.areAllOff() && !this.panelSync.hasWaiting()) {
            return this.panelSync.close();
        }
        return false;
    }
}


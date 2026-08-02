/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import audio.SoundMaestro;
import basics.DisplayManager;
import dropDownBoxUi.ComboBoxUi;
import fontRendering.Text;
import gridLayout.CategoryNames;
import gridLayout.CurrentFilterSettings;
import gridLayout.FilterOptions;
import gridLayout.GridComponent;
import gridLayout.ItemsPanelGui;
import gridLayout.PageSelectionGui;
import gridLayout.PageTracker;
import gridLayout.SingleFilterSetting;
import gridLayout.StatusGetter;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import toolbox.MyMouse;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.Listener;
import userInterfaces.SearchButtonUi;
import userInterfaces.Tab2ButtonUi;
import userInterfaces.TextFieldGui;

public class GridGui
extends GuiComponent {
    private static final int HEADER_PIXEL_HEIGHT = 40;
    private static final int NUMBERS_PIXEL_HEIGHT = 40;
    public static final int TEXT_START_PIXELS = 9;
    public static final int TEXT_PAD_X = 3;
    public static final int FILTER_START_PIXELS = 9;
    public static final int BUTTON_START_PIXELS = 11;
    public static final int FILTER_HEIGHT_PIXELS = 25;
    private static final int FILTER_WIDTH_PIXELS = 140;
    private static final int FILTER_PAD_PIXELS = 10;
    private static final int BUTTON_PIXELS = 20;
    public static final float FONT_SIZE = UiSettings.LARGE_FONT;
    private final FilterOptions filters;
    private ItemsPanelGui itemsPanel;
    private PageSelectionGui pageSelectionGui;
    private StatusGetter status;
    private List<? extends GridComponent> items;
    private float itemAspectRatio;
    private int itemPixelWidth;
    private Tab2ButtonUi filterOffButton;
    private TextFieldGui textInput;
    private PageTracker pageTracker;
    private Text statusText;
    private List<ComboBoxUi> comboBoxes = new ArrayList<ComboBoxUi>();

    public GridGui(List<? extends GridComponent> items, float itemAspectRatio, int itemPixelWidth, StatusGetter status, FilterOptions filters, PageTracker tracker) {
        this.status = status;
        this.pageTracker = tracker;
        this.items = items;
        Collections.sort(this.items);
        this.itemPixelWidth = itemPixelWidth;
        this.itemAspectRatio = itemAspectRatio;
        this.filters = filters;
    }

    @Override
    protected void init() {
        float xPos = 1.0f;
        boolean showSearch = this.pageTracker.searchTerm != null && !this.pageTracker.searchTerm.isEmpty();
        this.addSearchButton(xPos -= super.pixelsToRelativeX(40.0f), showSearch);
        if (showSearch) {
            this.createItemsDisplay(this.getSearchedItems(this.pageTracker.searchTerm));
            this.addTextInput(this.pageTracker.searchTerm);
            this.textInput.show(true);
        } else {
            this.createItemsDisplay(this.getFilteredItems(this.pageTracker.filterSettings));
            this.addTextInput("");
            this.textInput.show(false);
        }
        this.addStatus();
        float filterLength = super.pixelsToRelativeX(150.0f);
        int i = 0;
        while (i < this.filters.getFilterOptions().length) {
            this.addFilterBox(this.filters.getFilterOptions()[i], xPos -= filterLength, this.pageTracker.filterSettings.getFilter(i), !this.textInput.isShown());
            ++i;
        }
        this.addFilterButton(xPos - super.pixelsToRelativeX(30.0f));
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        this.statusText.setText(this.status.getStatus());
        this.checkScrolling();
    }

    private void addSearchButton(float xPos, boolean show) {
        SearchButtonUi searchButton = new SearchButtonUi(GuiRepository.SEARCH, ColourPalette.LIGHT_GREY, ColourPalette.BASE_BLUE, show);
        searchButton.setPreferredPixelSize(25);
        super.addPixelComp(searchButton, xPos, super.pixelsToRelativeY(9.0f));
        searchButton.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    GridGui.this.switchDisplayedItems(GridGui.this.items);
                    for (ComboBoxUi comboBox : GridGui.this.comboBoxes) {
                        comboBox.select(0, null);
                        comboBox.show(false);
                    }
                    GridGui.this.textInput.clearText(false);
                    GridGui.this.textInput.show(true);
                } else if (event.isToggleOff()) {
                    GridGui.this.textInput.show(false);
                    ((GridGui)GridGui.this).pageTracker.searchTerm = null;
                    for (ComboBoxUi comboBox : GridGui.this.comboBoxes) {
                        comboBox.show(true);
                    }
                    GridGui.this.switchDisplayedItems(GridGui.this.items);
                }
            }
        });
    }

    private void addTextInput(String startString) {
        this.textInput = new TextFieldGui(startString, UiSettings.NORM_FONT, 30, true, ColourPalette.LIGHT_GREY, 6, 1);
        float offset = super.pixelsToRelativeX(40.0f);
        float width = super.pixelsToRelativeX(300.0f);
        super.addComponent(this.textInput, 1.0f - (width + offset), this.pixelsToRelativeY(9.0f), width, this.pixelsToRelativeY(25.0f));
        this.textInput.addChangeListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                String term;
                ((GridGui)GridGui.this).pageTracker.searchTerm = term = GridGui.this.textInput.getCurrentText();
                GridGui.this.switchDisplayedItems(GridGui.this.getSearchedItems(term));
            }
        });
    }

    private void checkScrolling() {
        int increase;
        if (super.isMouseOver() && (increase = MyMouse.getActiveMouse().getDWheel()) != 0) {
            this.pageSelectionGui.notifyNext(increase < 0);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private List<? extends GridComponent> getFilteredItems(CurrentFilterSettings currentFilter) {
        ArrayList<GridComponent> filteredItems = new ArrayList<GridComponent>();
        for (GridComponent gridComponent : this.items) {
            if (!gridComponent.isInFilterGroup(currentFilter)) continue;
            filteredItems.add(gridComponent);
        }
        return filteredItems;
    }

    private List<? extends GridComponent> getSearchedItems(String searchString) {
        ArrayList<GridComponent> foundItems = new ArrayList<GridComponent>();
        for (GridComponent gridComponent : this.items) {
            if (!gridComponent.matchesSearch(searchString)) continue;
            foundItems.add(gridComponent);
        }
        return foundItems;
    }

    private void switchDisplayedItems(List<? extends GridComponent> newItems) {
        this.pageSelectionGui.remove();
        this.itemsPanel.remove();
        this.pageTracker.page = 0;
        this.filterOffButton.show(this.pageTracker.filterSettings.isFiltering());
        this.createItemsDisplay(newItems);
    }

    private void createItemsDisplay(List<? extends GridComponent> items) {
        this.createItemsPanel(items, this.itemAspectRatio, this.itemPixelWidth);
        this.createPageSelectionPanel();
    }

    private void createItemsPanel(List<? extends GridComponent> items, float itemAspectRatio, int itemPixelWidth) {
        this.itemsPanel = new ItemsPanelGui(items, itemAspectRatio, itemPixelWidth, this.pageTracker);
        float headerHeight = super.pixelsToRelativeY(40.0f);
        float footerHeight = super.pixelsToRelativeY(40.0f);
        super.addComponent(this.itemsPanel, 0.0f, headerHeight, 1.0f, 1.0f - (headerHeight + footerHeight));
    }

    private void createPageSelectionPanel() {
        this.pageSelectionGui = new PageSelectionGui(this.itemsPanel, this.pageTracker.page);
        float footerHeight = super.pixelsToRelativeY(40.0f);
        super.addComponent(this.pageSelectionGui, 0.0f, 1.0f - footerHeight, 1.0f, footerHeight);
    }

    private void addStatus() {
        this.statusText = Text.newText(this.status.getStatus()).setFontSize(FONT_SIZE).create();
        this.statusText.setColour(ColourPalette.WHITE);
        float startY = super.pixelsToRelativeY(9.0f);
        float startX = super.pixelsToRelativeX(18.0f);
        super.addText(this.statusText, startX, startY, 1.0f);
    }

    private void addFilterBox(CategoryNames catNames, float xPos, SingleFilterSetting currentFilter, boolean show) {
        int currentMainCat = currentFilter.isNoFilter() ? 0 : currentFilter.getMainCategory() + 1;
        ComboBoxUi comboBox = new ComboBoxUi(catNames.getCategoryObjects(), currentMainCat, currentFilter.getSubCategory());
        this.comboBoxes.add(comboBox);
        float pixelHeight = (float)DisplayManager.getUiHeight() * super.getScale().y;
        float startY = 9.0f / pixelHeight;
        float height = 25.0f / pixelHeight;
        float width = super.pixelsToRelativeX(140.0f);
        super.addComponent(comboBox, xPos, startY, width, height);
        this.addFilterListener(comboBox, currentFilter);
        comboBox.show(show);
    }

    private void addFilterButton(float xPos) {
        this.filterOffButton = new Tab2ButtonUi(GuiRepository.FILTER_OFF, GuiRepository.FILTER_OFF, ColourPalette.LIGHT_GREY, false);
        this.filterOffButton.setMouseOverColour(ColourPalette.WHITE);
        this.filterOffButton.setPreferredPixelSize(20);
        float pixelHeight = (float)DisplayManager.getUiHeight() * super.getScale().y;
        float startY = 11.0f / pixelHeight;
        super.addPixelComp(this.filterOffButton, xPos, startY);
        this.filterOffButton.show(this.pageTracker.filterSettings.isFiltering());
        this.filterOffButton.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    SoundMaestro.playSystemSound(GuiSounds.getClickSound());
                    for (ComboBoxUi comboBox : GridGui.this.comboBoxes) {
                        comboBox.select(0, null);
                    }
                }
            }
        });
    }

    private void addFilterListener(final ComboBoxUi comboBox, final SingleFilterSetting currentFilter) {
        comboBox.addSelectionListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (comboBox.getSelectedIndex() == 0) {
                    currentFilter.reset();
                } else {
                    currentFilter.set(comboBox.getSelectedIndex() - 1, comboBox.getSelectedSubIndex());
                }
                GridGui.this.switchDisplayedItems(GridGui.this.getFilteredItems(((GridGui)GridGui.this).pageTracker.filterSettings));
            }
        });
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package controllerUi;

import aiComponent.AiComponent;
import basics.DisplayManager;
import componentArchitecture.ComponentType;
import componentArchitecture.ControlBehaviour;
import components.InformationComponent;
import controllerUi.ControlsTabUi;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.StatusPanelGui;
import fontRendering.Text;
import gameManaging.GameManager;
import gameManaging.GameState;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import instances.Entity;
import java.util.List;
import languages.GameText;
import main.Camera;
import mainGuis.ColourPalette;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import session.GameMode;
import speciesInformation.SpeciesInfoGui;
import textures.Texture;
import toolTips.ToolTipInfo;
import toolbar.SlideInPanel;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickableGroup;
import userInterfaces.Listener;
import userInterfaces.TabButtonUi;
import userInterfaces.TextButtonUi;

public class ControlUi
extends GuiComponent {
    private static final String TITLE = GameText.getText(28);
    private static final String FOLLOW_TITLE = GameText.getText(794);
    private static final String VIEW_TITLE = GameText.getText(995);
    private static final String CANCEL = GameText.getText(29);
    private static final String STATS_TIP_TITLE = GameText.getText(30);
    private static final String STATS_TIP_DESC = GameText.getText(31);
    private static final String CONTROLS_TIP_TITLE = GameText.getText(32);
    private static final String CONTROLS_TIP_DESC = GameText.getText(33);
    private static final String SPECIES_INFO_TAB = GameText.getText(34);
    private static final String SPECIES_TIP_DESC = GameText.getText(35);
    private static final float TITLE_Y = 0.025f;
    private static final int CANCEL_BUTTON_HEIGHT = 22;
    private static final int BUTTON_PAD = 8;
    private static final float X_PAD = 0.02f;
    private static final int TOP_PAD = 51;
    protected static final int LINE_HEIGHT = 20;
    private static final int BOTTOM_PAD = 38;
    private final Entity entity;
    private final InformationComponent info;
    private GuiTexture topBar;
    private GuiTexture separator1;
    private GuiTexture separator2;
    private List<PopUpInfoGui> statsInfo;
    private List<ControlBehaviour> behaviours;
    private GuiComponent currentPanel;
    private boolean alive = true;
    private boolean controlling;

    public ControlUi(Entity entity, List<PopUpInfoGui> statsInfo, boolean controlling) {
        this.entity = entity;
        this.controlling = controlling;
        this.info = (InformationComponent)entity.getComponent(ComponentType.INFO);
        this.statsInfo = statsInfo;
        this.behaviours = entity.getControlableBehaviour();
        this.initTextures();
    }

    @Override
    protected void init() {
        this.addTitle();
        this.addExitButton();
        this.addTabs();
        this.showPanel(new StatusPanelGui(this.statsInfo, this.statsInfo.size(), GameManager.getGameMode() == GameMode.BUILD));
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.topBar.setPosition(position.x, position.y, scale.x, 36.0f / (float)DisplayManager.getUiHeight());
        float pixelWidth = 1.0f / (float)DisplayManager.getUiWidth();
        float titleBarHeight = 36.0f / (super.getScale().y * (float)DisplayManager.getUiHeight());
        float separator1X = 1.0f - (0.1075f + super.pixelsToRelativeX(1.0f));
        this.separator1.setPosition(position.x + scale.x * separator1X, position.y, pixelWidth, titleBarHeight * scale.y);
        float separator2X = separator1X - (0.175f + super.pixelsToRelativeX(1.0f));
        this.separator2.setPosition(position.x + scale.x * separator2X, position.y, pixelWidth, titleBarHeight * scale.y);
    }

    @Override
    protected void updateSelf() {
        if (this.alive && this.entity.isDead()) {
            EquilinoxGuis.getToolBar().closeGuestPanel();
            this.alive = false;
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.topBar);
        if (this.controlling) {
            data.addTexture(this.getLevel(), this.separator1);
            data.addTexture(this.getLevel(), this.separator2);
        }
    }

    private void addTitle() {
        String title = this.controlling ? TITLE : (this.entity.getBlueprint().isAnimal() ? FOLLOW_TITLE : VIEW_TITLE);
        Text text = Text.newText(String.valueOf(title) + " - " + this.info.getName()).setFontSize(UiSettings.LARGE_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.02f, 0.025f, 1.0f);
    }

    private void initTextures() {
        this.topBar = new GuiTexture(GuiRepository.BLOCK);
        this.topBar.setOverrideColour(ColourPalette.MIDDLE_GREY);
        this.separator1 = new GuiTexture(GuiRepository.BLOCK);
        this.separator1.setOverrideColour(ColourPalette.LIGHT_GREY);
        this.separator2 = new GuiTexture(GuiRepository.BLOCK);
        this.separator2.setOverrideColour(ColourPalette.LIGHT_GREY);
    }

    private void addExitButton() {
        TextButtonUi cancelButton = new TextButtonUi(CANCEL, ColourPalette.FLAT_RED, SpeciesInfoGui.FONT_SIZE);
        cancelButton.setPreferredAspectRatio(8.0f);
        float heightPixels = super.getScale().y * (float)DisplayManager.getUiHeight();
        float buttonHeight = 22.0f / heightPixels;
        float y = 1.0f - (8.0f / heightPixels + buttonHeight);
        cancelButton.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    EquilinoxGuis.getToolBar().closeGuestPanel();
                }
            }
        });
        super.addCenteredComponentX(cancelButton, 0.5f, y, buttonHeight);
    }

    private void addTabs() {
        GuiClickableGroup group = new GuiClickableGroup(true);
        float titleBarHeight = 36.0f / (super.getScale().y * (float)DisplayManager.getUiHeight());
        float currentX = 0.90250003f;
        this.addInfoButton(titleBarHeight, currentX);
        currentX -= 0.0975f + super.pixelsToRelativeX(1.0f);
        if (this.controlling) {
            this.addControlsTab(group, titleBarHeight, currentX);
            currentX -= 0.0875f;
        }
        this.addStatsTab(group, titleBarHeight, currentX);
    }

    private void addStatsTab(GuiClickableGroup group, float titleBarHeight, float currentX) {
        TabButtonUi button = this.addButton(group, titleBarHeight, currentX, GuiRepository.STATS, true, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    ControlUi.this.showPanel(new StatusPanelGui(ControlUi.this.entity.getInfo(), ControlUi.this.statsInfo.size(), GameManager.getGameMode() == GameMode.BUILD));
                }
            }
        });
        button.setToolTip(ToolTipInfo.newInfo(STATS_TIP_TITLE, STATS_TIP_DESC, true, true));
    }

    private void addControlsTab(GuiClickableGroup group, float titleBarHeight, float currentX) {
        TabButtonUi button = this.addButton(group, titleBarHeight, currentX, GuiRepository.CONTROL, true, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    ControlUi.this.showPanel(new ControlsTabUi(ControlUi.this.behaviours));
                }
            }
        });
        button.setToolTip(ToolTipInfo.newInfo(CONTROLS_TIP_TITLE, CONTROLS_TIP_DESC, true, true));
    }

    private void addInfoButton(float titleBarHeight, float currentX) {
        final TabButtonUi button = new TabButtonUi(GuiRepository.INFO_ICON, 18);
        button.disableManualTurnOff();
        super.addComponent(button, currentX, 0.0f, 0.0875f, titleBarHeight);
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    SpeciesInfoGui.createSpeciesInfoGui(ControlUi.this.entity.getBlueprint());
                    EquilinoxGuis.getExtraInfoGui().addOneTimeCloseListener(new Listener(){

                        @Override
                        public void eventOccurred(boolean on) {
                            if (button.isToggledOn() && button.isShown()) {
                                button.toggle();
                            }
                        }
                    });
                }
            }
        });
        button.setToolTip(ToolTipInfo.newInfo(SPECIES_INFO_TAB, SPECIES_TIP_DESC, true, true));
    }

    private TabButtonUi addButton(GuiClickableGroup group, float titleBarHeight, float currentX, Texture icon, boolean selected, ClickListener listener) {
        TabButtonUi button = new TabButtonUi(icon, 18);
        group.addButton(button, selected);
        super.addComponent(button, currentX, 0.0f, 0.0875f, titleBarHeight);
        button.addListener(listener);
        return button;
    }

    private void showPanel(GuiComponent panel) {
        this.deleteCurrentContents();
        this.currentPanel = panel;
        float heightPixels = super.getScale().y * (float)DisplayManager.getUiHeight();
        float y = 51.0f / heightPixels;
        float height = (float)(this.statsInfo.size() * 20) / heightPixels;
        super.addComponent(this.currentPanel, 0.02f, y, 0.98f, height);
    }

    private void deleteCurrentContents() {
        if (this.currentPanel != null) {
            this.currentPanel.remove();
            this.currentPanel = null;
        }
    }

    public static void openControlUI(Entity entity, final AiComponent aiComponent) {
        List<PopUpInfoGui> statsInfo = entity.getInfo();
        float width = 320.0f / (float)DisplayManager.getUiWidth();
        int lines = GameManager.getGameMode() == GameMode.BUILD ? 4 : statsInfo.size();
        int heightPixels = 89 + lines * 20;
        float height = (float)heightPixels / (float)DisplayManager.getUiHeight();
        ControlUi content = new ControlUi(entity, statsInfo, aiComponent != null);
        SlideInPanel slidePanel = EquilinoxGuis.getToolBar().displayGuestPanel(content, width, height, true);
        slidePanel.addCloseListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (aiComponent != null) {
                    aiComponent.cancelAiProgram(aiComponent);
                }
                GameManager.gameState.endState(GameState.CONTROL);
                GameManager.getEntityPicker().deselect();
                Camera.getCamera().setTargetEntity(null);
                EquilinoxGuis.getExtraInfoGui().close();
            }
        });
    }
}


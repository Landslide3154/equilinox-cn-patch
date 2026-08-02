/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import basics.DisplayManager;
import basics.MasterRenderer;
import bottomBar.BottomBarUi;
import breedingTraits.Trait;
import breedingTrees.Node;
import componentArchitecture.Action;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityInfoGui.ActionPanelGui;
import entityInfoGui.BuffPanelUi;
import entityInfoGui.EntityPopUpPanel;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.StatusPanelGui;
import entityInfoGui.TabController;
import evolutionUi.EvolutionUi;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import health.LifeCompBlueprint;
import instances.Entity;
import java.util.List;
import languages.GameText;
import main.Camera;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import session.GameMode;
import speciesInformation.SpeciesInfoGui;
import textures.Texture;
import toolTips.ToolTipInfo;
import toolbar.Toolbar;
import toolbox.Maths;
import toolbox.MyKeyboard;
import traitGuis.TraitsPanelGui;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickableGroup;
import userInterfaces.TabButtonUi;
import visualFxDrivers.ConstantDriver;

public class EntityInfoGui
extends GuiComponent {
    private static final String SPECIES_TIP_TITLE = GameText.getText(34);
    private static final String SPECIES_TIP_DESC = GameText.getText(35);
    private static final String STATS_TIP_TITLE = GameText.getText(520);
    private static final String STATS_TIP_DESC = GameText.getText(521);
    private static final String EVOLVE_TIP_TITLE = GameText.getText(522);
    private static final String EVOLVE_TIP_DESC = GameText.getText(523);
    private static final String ACTIONS_TIP_TITLE = GameText.getText(524);
    private static final String ACTIONS_TIP_DESC = GameText.getText(525);
    private static final String TRAITS_TIP_TITLE = GameText.getText(526);
    private static final String TRAITS_TIP_DESC = GameText.getText(527);
    private static final String BUFFS_TIP_TITLE = GameText.getText(528);
    private static final String BUFFS_TIP_DESC = GameText.getText(529);
    public static final float WIDTH_PIXELS = 320.0f;
    private static final float OFFSET = 0.2f;
    private static final float OFF_SCREEN_VALUE = 0.3f;
    private static final int TITLE_BAR_HEIGHT = 36;
    private static final float TITLE_FONT_SIZE = UiSettings.LARGE_FONT;
    public static final float FONT_SIZE = UiSettings.NORM_FONT;
    public static final float BUTTON_WIDTH = 0.0875f;
    public static final float BUTTON_PAD = 0.01f;
    public static final int OUTER_PADDING = 5;
    public static final int THICK_PADDING = 13;
    private static final int TITLE_TEXT_Y = 6;
    private static final int Y_GAP = 22;
    public static final int PANEL_PADDING = 5;
    public static final float ICON_PADDING = 0.02f;
    private static final int GUI_PAD_PIXELS = 5;
    private static final float GUI_PAD_WIDTH = 5.0f / (float)DisplayManager.getUiWidth();
    private static final float GUI_PAD_HEIGHT = 5.0f / (float)DisplayManager.getUiHeight();
    private Entity entity;
    private InformationComponent mainInfo;
    private GuiTexture background;
    private GuiTexture titleBar;
    private GuiTexture separator1;
    private GuiTexture separator2;
    private int numberOfLines;
    private float totalHeightPixels;
    private List<Trait> traits;
    private float titleBarHeight;
    private GuiComponent currentPanel = null;
    private EntityPopUpPanel secondPanel = null;
    private GuiClickableGroup group = new GuiClickableGroup(true);
    private boolean breeder;
    private boolean hasChildren = false;
    private List<PopUpInfoGui> tempInfo;
    private List<Action> actions;
    private Vector3f anchorPosition;
    private float currentX = 1.0f;
    private int buttonCount = 0;
    private int initialTabIndex;

    public EntityInfoGui(Entity entity) {
        this.entity = entity;
        this.traits = entity.getTraits();
        this.actions = entity.getActions();
        this.mainInfo = (InformationComponent)entity.getComponent(ComponentType.INFO);
        this.breeder = this.isBreeder();
        if (this.breeder) {
            this.hasChildren = GameManager.isNormalMode() && GameManager.BREED_TREES.getNode(entity.getBlueprint()).getAllChildren().size() > 0;
        }
        this.initGuiTextures();
        this.tempInfo = entity.getInfo();
        this.determineSize(this.tempInfo);
        float sizeX = 320.0f / (float)DisplayManager.getUiWidth();
        float sizeY = this.totalHeightPixels / (float)DisplayManager.getUiHeight();
        GuiMaster.addComponent(this, 0.0f, 1.0f - sizeY, sizeX, sizeY);
    }

    public float getTitleBarHeight() {
        return this.titleBarHeight;
    }

    @Override
    protected void init() {
        this.titleBarHeight = super.pixelsToRelativeY(36.0f);
        this.buttonCount = this.calcButtonCount();
        this.initialTabIndex = GameManager.getGameMode() == GameMode.BUILD ? this.buttonCount - 1 : TabController.getTabIndex(this.entity.getBlueprint());
        this.addTitle();
        this.addInfoButton();
        int index = this.buttonCount - 1;
        this.addActionsButton(index--);
        if (GameManager.getGameMode() != GameMode.BUILD) {
            this.addBuffButton(index--);
        }
        if (this.breeder) {
            this.addTraitButton(index--);
            if (this.hasChildren) {
                this.addBreedButton(index--);
            }
        }
        if (!this.tempInfo.isEmpty() && GameManager.getGameMode() != GameMode.BUILD) {
            this.addStatusButton(index--);
        }
        this.addInfo(this.tempInfo);
        this.determineAnchorPosition();
        this.recalculatePosition();
    }

    private int calcButtonCount() {
        int count;
        int n = count = GameManager.getGameMode() == GameMode.BUILD ? 1 : 2;
        if (this.breeder) {
            ++count;
            if (this.hasChildren) {
                ++count;
            }
        }
        if (!this.tempInfo.isEmpty() && GameManager.getGameMode() != GameMode.BUILD) {
            ++count;
        }
        return count;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        float pixelWidth = 1.0f / (float)DisplayManager.getUiWidth();
        this.titleBar.setPosition(position.x, position.y, scale.x, this.titleBarHeight * scale.y);
        float separator1X = 1.0f - (0.0875f + super.pixelsToRelativeX(1.0f) + 0.02f);
        this.separator1.setPosition(position.x + scale.x * separator1X, position.y, pixelWidth, this.titleBarHeight * scale.y);
        float separator2X = 1.0f - ((float)(this.buttonCount + 1) * 0.0875f + super.pixelsToRelativeX(2.0f) + 0.02f);
        this.separator2.setPosition(position.x + scale.x * separator2X, position.y, pixelWidth, this.titleBarHeight * scale.y);
    }

    @Override
    protected void updateSelf() {
        this.recalculatePosition();
        this.background.update();
        if (MyKeyboard.getKeyboard().keyDownEventOccurred(211)) {
            this.entity.die(null, true);
        }
        if (MyKeyboard.getKeyboard().keyDownEventOccurred(46)) {
            GameManager.getShops().getPlacementManager().duplicateEntity(this.entity);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.titleBar);
        data.addTexture(this.getLevel(), this.separator1);
        data.addTexture(this.getLevel(), this.separator2);
    }

    public void removeSecondPanel() {
        if (this.secondPanel != null) {
            this.secondPanel.remove();
            this.secondPanel = null;
        }
    }

    public boolean isTopScreenHalf() {
        return super.getRelativeY() < 0.5f;
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        if (this.secondPanel == null) {
            return super.isMouseOverFocusIrrelevant();
        }
        return super.isMouseOverFocusIrrelevant() || this.secondPanel.isMouseOverFocusIrrelevant();
    }

    public void showSecondPanel(EntityPopUpPanel panel) {
        this.removeSecondPanel();
        this.secondPanel = panel;
        panel.addToParentPanel(this);
    }

    private void showPanel(GuiComponent panel, boolean pad) {
        if (this.currentPanel != null) {
            this.currentPanel.remove();
        }
        this.currentPanel = panel;
        float xPad = super.pixelsToRelativeX(pad ? 13 : 5);
        float y = super.pixelsToRelativeY(41.0f);
        float bottomPad = super.pixelsToRelativeY(5.0f);
        super.addComponent(this.currentPanel, xPad, y, 1.0f - 2.0f * xPad, 1.0f - (y + bottomPad));
    }

    private void determineAnchorPosition() {
        Vector3f entityPos = this.entity.getTransform().getPosition();
        float height = GameManager.getWorld().getHeightOfTerrain(entityPos.x, entityPos.z);
        this.anchorPosition = new Vector3f(entityPos);
        this.anchorPosition.y += this.entity.getBoundingBox().getHeight() + 0.2f;
    }

    public Entity getEntity() {
        return this.entity;
    }

    private void recalculatePosition() {
        Vector3f screenCoords = this.calculateScreenCoords();
        if (screenCoords != null) {
            this.updateGuiTransform(screenCoords);
        }
    }

    private Vector3f calculateScreenCoords() {
        Vector3f screenCoords = Maths.convertToScreenSpace(this.anchorPosition, Camera.getCamera().getViewMatrix(), MasterRenderer.getProjectionMatrix());
        return screenCoords;
    }

    private void updateGuiTransform(Vector3f screenCoords) {
        if (this.isTooFar(screenCoords)) {
            GameManager.getEntityPicker().deselect();
            return;
        }
        float yPos = this.clampYPos(screenCoords);
        float xPos = this.clampXPos(screenCoords);
        super.setRelativePosition(xPos - super.getRelativeScaleX(), yPos - super.getRelativeScaleY());
    }

    private float clampXPos(Vector3f screenCoords) {
        float rightSide = GUI_PAD_WIDTH;
        if (this.secondPanel != null) {
            rightSide += this.getScale().x * (this.secondPanel.getRelativeScaleX() + super.pixelsToRelativeX(5.0f));
        }
        float xPos = Math.min(1.0f - rightSide, screenCoords.x);
        xPos = Math.max(super.getRelativeScaleX() + GUI_PAD_WIDTH, xPos);
        return xPos;
    }

    private boolean isTooFar(Vector3f screenCoords) {
        return screenCoords.x > 1.3f || screenCoords.x < -0.3f || screenCoords.y > 1.3f || screenCoords.y < -0.120000005f;
    }

    private float clampYPos(Vector3f screenCoords) {
        float extraUp = 0.0f;
        float extraDown = 0.0f;
        if (this.secondPanel != null) {
            extraDown = Math.max(0.0f, this.secondPanel.getMaxY());
            extraUp = Math.max(0.0f, this.secondPanel.getMinY());
        }
        float maxY = 1.0f - (BottomBarUi.HEIGHT + GUI_PAD_HEIGHT + extraDown * this.getScale().y);
        float minY = super.getRelativeScaleY() + Toolbar.HEIGHT + GUI_PAD_HEIGHT + extraUp * this.getRelativeScaleY();
        return Math.max(minY, Math.min(screenCoords.y, maxY));
    }

    private void addInfo(List<PopUpInfoGui> info) {
        int check = this.buttonCount - 1;
        if (GameManager.getGameMode() == GameMode.BUILD || this.initialTabIndex == check--) {
            this.showPanel(new ActionPanelGui(this.actions, this.numberOfLines), true);
            return;
        }
        if (this.initialTabIndex == check--) {
            this.showPanel(new BuffPanelUi(this.entity.getPerformanceBuffInfo(), this.numberOfLines, this), false);
            return;
        }
        if (this.breeder) {
            if (this.initialTabIndex == check--) {
                this.showPanel(new TraitsPanelGui(this.entity, this.traits, this.numberOfLines, this), true);
                return;
            }
            if (this.hasChildren && this.initialTabIndex == check--) {
                this.showPanel(new EvolutionUi(this.entity, this, this.numberOfLines), false);
                return;
            }
        }
        this.showPanel(new StatusPanelGui(info, this.numberOfLines), true);
    }

    private void determineSize(List<PopUpInfoGui> info) {
        this.numberOfLines = Math.max(Math.round((float)this.actions.size() * 1.5f), Math.max(info.size(), this.traits.size() + 1));
        if (this.isBreeder()) {
            this.numberOfLines = Math.max(this.determineMaxReqSize(), this.numberOfLines);
        }
        this.totalHeightPixels = 36 + this.numberOfLines * 22 + 10;
    }

    private int determineMaxReqSize() {
        List<Node> allChildSpecies = GameManager.BREED_TREES.getNode(this.entity.getBlueprint()).getAllChildren();
        int maxReqs = 0;
        for (Node child : allChildSpecies) {
            LifeCompBlueprint lifeInfo = (LifeCompBlueprint)child.species.getComponent(ComponentType.LIFE);
            maxReqs = Math.max(lifeInfo.breedInfo.getRequirements().size(), maxReqs);
        }
        float maxSize = allChildSpecies.size() * 2;
        float reqSize = 2.5f + (float)maxReqs * 1.08f;
        return (int)Math.ceil(Math.max(maxSize, reqSize));
    }

    private void initGuiTextures() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setBlurry(true);
        this.background.setOverrideColour(ColourPalette.DARK_GREY);
        this.background.setAlphaDriver(new ConstantDriver(0.75f));
        this.separator1 = new GuiTexture(GuiRepository.BLOCK);
        this.separator1.setOverrideColour(ColourPalette.LIGHT_GREY);
        this.separator2 = new GuiTexture(GuiRepository.BLOCK);
        this.separator2.setOverrideColour(ColourPalette.LIGHT_GREY);
        this.titleBar = new GuiTexture(GuiRepository.BLOCK);
        this.titleBar.setOverrideColour(ColourPalette.MIDDLE_GREY);
    }

    private boolean isBreeder() {
        return this.entity.getComponent(ComponentType.LIFE) != null;
    }

    private void addTitle() {
        Text title = Text.newText(this.mainInfo.getName()).setFontSize(TITLE_FONT_SIZE).create();
        title.setColour(ColourPalette.WHITE);
        super.addText(title, super.pixelsToRelativeX(13.0f), super.pixelsToRelativeY(6.0f), 1.0f);
    }

    private void addStatusButton(final int index) {
        this.addButton(GuiRepository.STATS, index == this.initialTabIndex, STATS_TIP_TITLE, STATS_TIP_DESC, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    TabController.indicateTabSelected(EntityInfoGui.this.entity.getBlueprint(), index);
                    EntityInfoGui.this.showPanel(new StatusPanelGui(EntityInfoGui.this.entity.getInfo(), EntityInfoGui.this.numberOfLines), true);
                }
            }
        });
    }

    private void addActionsButton(final int index) {
        this.addButton(GuiRepository.HAND, index == this.initialTabIndex, ACTIONS_TIP_TITLE, ACTIONS_TIP_DESC, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    EntityInfoGui.this.showPanel(new ActionPanelGui(EntityInfoGui.this.actions, EntityInfoGui.this.numberOfLines), true);
                    TabController.indicateTabSelected(EntityInfoGui.this.entity.getBlueprint(), index);
                }
            }
        });
    }

    private void addBuffButton(final int index) {
        this.addButton(GuiRepository.STRONG, index == this.initialTabIndex, BUFFS_TIP_TITLE, BUFFS_TIP_DESC, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    TabController.indicateTabSelected(EntityInfoGui.this.entity.getBlueprint(), index);
                    EntityInfoGui.this.showPanel(new BuffPanelUi(EntityInfoGui.this.entity.getPerformanceBuffInfo(), EntityInfoGui.this.numberOfLines, EntityInfoGui.this), false);
                }
            }
        });
    }

    private void addBreedButton(final int index) {
        this.addButton(GuiRepository.BREED, index == this.initialTabIndex, EVOLVE_TIP_TITLE, EVOLVE_TIP_DESC, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    TabController.indicateTabSelected(EntityInfoGui.this.entity.getBlueprint(), index);
                    EntityInfoGui.this.showPanel(new EvolutionUi(EntityInfoGui.this.entity, EntityInfoGui.this, EntityInfoGui.this.numberOfLines), false);
                }
            }
        });
    }

    private void addTraitButton(final int index) {
        this.addButton(GuiRepository.DNA_ICON, index == this.initialTabIndex, TRAITS_TIP_TITLE, TRAITS_TIP_DESC, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    TabController.indicateTabSelected(EntityInfoGui.this.entity.getBlueprint(), index);
                    EntityInfoGui.this.showPanel(new TraitsPanelGui(EntityInfoGui.this.entity, EntityInfoGui.this.traits, EntityInfoGui.this.numberOfLines, EntityInfoGui.this), true);
                }
            }
        });
    }

    private void addInfoButton() {
        TabButtonUi button = new TabButtonUi(GuiRepository.INFO_ICON, 18);
        this.currentX -= 0.0975f;
        super.addComponent(button, this.currentX, 0.0f, 0.0875f, this.titleBarHeight);
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    SpeciesInfoGui.createSpeciesInfoGui(EntityInfoGui.this.entity.getBlueprint());
                    GameManager.getEntityPicker().clear();
                }
            }
        });
        button.setToolTip(ToolTipInfo.newInfo(SPECIES_TIP_TITLE, SPECIES_TIP_DESC, false, true));
        this.currentX -= super.pixelsToRelativeX(1.0f) + 0.01f;
    }

    private void addButton(Texture icon, boolean selected, String tipTitle, String tipDesc, ClickListener listener) {
        TabButtonUi button = new TabButtonUi(icon, 18);
        this.group.addButton(button, selected);
        this.currentX -= 0.0875f;
        button.setToolTip(ToolTipInfo.newInfo(tipTitle, tipDesc, false, true));
        super.addComponent(button, this.currentX, 0.0f, 0.0875f, this.titleBarHeight);
        button.addListener(listener);
    }
}


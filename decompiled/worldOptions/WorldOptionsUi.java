/*
 * Decompiled with CFR 0.152.
 */
package worldOptions;

import audio.SoundMaestro;
import dropDownBoxUi.ComboBoxUi;
import fontRendering.Text;
import gameManaging.GameManager;
import gameMenu.GameMenuBackground;
import gameMenu.GameMenuGui;
import gameMenu.LoadChecker;
import gameMenu.LoadingScreen;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import org.lwjgl.util.vector.Vector2f;
import session.GameMode;
import usefulUis.PaddedPanelUi;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.IconButtonUi;
import userInterfaces.Listener;
import userInterfaces.TextButtonUi;
import userInterfaces.TextFieldGui;
import world.WorldConfigs;
import worldOptions.ModeDescUi;
import worldOptions.TerrainType;
import worldOptions.WaterHeights;

public class WorldOptionsUi
extends GuiComponent {
    private static final String SIM_DESC = "In Simulation Mode you have unlimited DP and access to any species that you've previously unlocked in normal mode. Great for trying things out with your species.";
    private static final String BUILD_DESC = "Unlimited DP and all species are unlocked. Most simulation aspects are turned off (there's no health, growth, hunger, breeding, or environmental requirements). Good for creating nice looking low-poly scenes.";
    private static final String GEN_WORLD = GameText.getText(693);
    private static final String WORLD_NAME = GameText.getText(885);
    private static final float FONT_SIZE = 1.11111f;
    private static final float GEN_BUTTON_WIDTH = 0.3f;
    private static final float GEN_BUTTON_TEX_Y = 0.2f;
    private static final float OPTION_WIDTH = 0.2f;
    private static final float OPTION_LEFT_X = 0.25f;
    private static final float OPTION_GAP = 0.21f;
    private static final float OPTION_Y_START = 0.1f;
    private static final float DROP_BOX_HEIGHT = 0.08f;
    private static final float OPTION_RIGHT_X = 0.55f;
    private final GameMenuGui gameMenu;
    private final GameMenuBackground superMenu;
    private TextFieldGui nameField;
    private ComboBoxUi modeMenu;
    private ComboBoxUi terrainMenu;
    private ComboBoxUi waterMenu;
    private GuiComponent component;
    private boolean generated = false;

    public WorldOptionsUi(GameMenuGui gameMenu, GameMenuBackground superMenu) {
        this.gameMenu = gameMenu;
        this.superMenu = superMenu;
    }

    @Override
    protected void init() {
        super.init();
        this.createBackOption();
        this.addGenerateButton();
        float yPos = 0.1f;
        this.addNameOption(0.25f, yPos);
        final IconButtonUi button = this.addIconButton(yPos += 0.21f);
        this.modeMenu = this.addOption(0.25f, yPos, GameText.getText(1148), (Object[])GameMode.values(), 0);
        this.modeMenu.addSelectionListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                button.show(GameMode.values()[WorldOptionsUi.this.modeMenu.getSelectedIndex()] != GameMode.NORMAL);
            }
        });
        yPos = 0.1f;
        this.waterMenu = this.addOption(0.55f, yPos, GameText.getText(698), (Object[])WaterHeights.values(), 1);
        this.terrainMenu = this.addOption(0.55f, yPos += 0.21f, GameText.getText(702), (Object[])TerrainType.values(), 1);
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

    private IconButtonUi addIconButton(final float yPos) {
        IconButtonUi button = new IconButtonUi(GuiRepository.HELP_ICON_BIG);
        float xPos = 0.45999998f;
        float gap = 0.01f;
        float height = 0.08f - 2.0f * gap;
        super.addComponent(button, xPos, yPos + 0.08f + gap, super.getRelativeWidthCoords(height), height);
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    GameMode mode = GameMode.values()[WorldOptionsUi.this.modeMenu.getSelectedIndex()];
                    WorldOptionsUi.this.createPopUp(yPos, mode == GameMode.SIM ? WorldOptionsUi.SIM_DESC : WorldOptionsUi.BUILD_DESC);
                } else if (event.isMouseOff()) {
                    WorldOptionsUi.this.deletePopUp();
                }
            }
        });
        button.show(false);
        return button;
    }

    private void deletePopUp() {
        if (this.component != null) {
            this.component.remove();
            this.component = null;
        }
    }

    private void createPopUp(float yPos, String message) {
        this.deletePopUp();
        this.component = new ModeDescUi(message);
        super.addComponent(this.component, 0.25f, yPos + 0.21f, 0.2f, 0.25f);
    }

    private ComboBoxUi addOption(float xPos, float yPos, String name, Object[] options, int selected) {
        this.addName(xPos, yPos, name);
        return this.addMenu(xPos, yPos += 0.08f, options, selected);
    }

    private void addNameOption(float xPos, float yPos) {
        this.addName(xPos, yPos, WORLD_NAME);
        this.addTextField(xPos, yPos += 0.08f);
    }

    private void addName(float xPos, float yPos, String name) {
        Text text = Text.newText(String.valueOf(name) + ":").setFontSize(1.11111f).create();
        text.setColour(ColourPalette.DARK_GREY);
        super.addText(text, xPos, yPos, 1.0f);
    }

    private void addTextField(float xPos, float yPos) {
        String name = GameManager.sessionManager.getSaves().getWaitingSlot().getName();
        PaddedPanelUi panel = new PaddedPanelUi(ColourPalette.MIDDLE_GREY);
        panel.setPadding(8, 2, 2, 0);
        this.nameField = new TextFieldGui(name, 1.11111f, 20, false, true);
        panel.displayComponent(this.nameField);
        super.addComponent(panel, xPos, yPos, 0.2f, 0.08f);
    }

    private ComboBoxUi addMenu(float xPos, float yPos, Object[] options, int selected) {
        ComboBoxUi menu = new ComboBoxUi(options, selected);
        menu.setFontSize(1.11111f);
        super.addComponent(menu, xPos, yPos, 0.2f, 0.08f);
        return menu;
    }

    private void addGenerateButton() {
        TextButtonUi generateButton = new TextButtonUi(GEN_WORLD, ColourPalette.BASE_BLUE, 1.11111f, ColourPalette.WHITE, 0.2f);
        float yPos = GameMenuGui.BACK_BUTTON_POS.y;
        float xPos = 0.35f;
        super.addComponent(generateButton, xPos, yPos, 0.3f, 0.12f);
        generateButton.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    if (WorldOptionsUi.this.generated) {
                        return;
                    }
                    WorldOptionsUi.this.generated = true;
                    TerrainType terrainType = TerrainType.values()[WorldOptionsUi.this.terrainMenu.getSelectedIndex()];
                    WaterHeights waterHeight = WaterHeights.values()[WorldOptionsUi.this.waterMenu.getSelectedIndex()];
                    SoundMaestro.playSystemSound(GuiSounds.SELECT);
                    WorldOptionsUi.this.gameMenu.setNewTertiaryScreen(new LoadingScreen(new LoadChecker(){

                        @Override
                        public boolean isLoaded() {
                            return GameManager.sessionManager.hasWorldReady() && !GameManager.sessionManager.isLoading();
                        }
                    }, WorldOptionsUi.this.superMenu));
                    String name = WorldOptionsUi.this.nameField.getCurrentText();
                    if (name.isEmpty()) {
                        name = "No Name";
                    }
                    GameManager.sessionManager.startNewWorld(WorldConfigs.create(terrainType.smoothnessValue, waterHeight.waterHeight), name, GameMode.values()[WorldOptionsUi.this.modeMenu.getSelectedIndex()]);
                }
            }
        });
    }

    private void createBackOption() {
        IconButtonUi backButton = new IconButtonUi(GameMenuGui.BACK_ICON, ColourPalette.DARK_GREY, ColourPalette.MIDDLE_GREY);
        super.addComponentY(backButton, GameMenuGui.BACK_BUTTON_POS.x, GameMenuGui.BACK_BUTTON_POS.y, 0.12f);
        ClickListener listener = new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    WorldOptionsUi.this.gameMenu.closeSecondaryScreen();
                }
            }
        };
        backButton.addListener(listener);
    }
}


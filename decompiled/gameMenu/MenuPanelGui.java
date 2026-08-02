/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import audio.SoundMaestro;
import gameManaging.GameManager;
import gameManaging.GameState;
import gameMenu.ControlsScreenGui;
import gameMenu.CreditsGui;
import gameMenu.DnaButtonGui;
import gameMenu.GameMenuBackground;
import gameMenu.GameMenuGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import loadWorldScreen.LoadScreenGui;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import optionsMenu.OptionsPanelUi;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiImage;
import visualFxDrivers.ConstantDriver;
import worldOptions.WorldOptionsUi;

public class MenuPanelGui
extends GuiComponent {
    private static final int PLAY_TEXT_ID = 3;
    private static final int CONTROL_TEXT_ID = 4;
    private static final int OPTIONS_TEXT_ID = 5;
    private static final int QUIT_TEXT_ID = 6;
    private static final int NEW_WORLD_TEXT_ID = 7;
    private static final int SAVE_TEXT_ID = 19;
    private static final int LOAD_TEXT_ID = 20;
    private static final float ALPHA = 0.7f;
    private static final float BUTTON_Y_START = 0.171875f;
    private static final float BUTTON_GAP = 0.09375f;
    private static final float BUTTON_ASPECT = 12.0f;
    private static final float BUTTON_SIZE = 0.07f;
    private static final String CREDITS = GameText.getText(59);
    private static final String GAME_SAVED = GameText.getText(795);
    private static final String ERROR_SAVING = GameText.getText(1080);
    private GameMenuGui gameMenu;
    private GameMenuBackground superMenu;
    private DnaButtonGui saveButton;
    private DnaButtonGui newWorldButton;
    private List<DnaButtonGui> buttons = new ArrayList<DnaButtonGui>();
    private GuiImage dna;

    protected MenuPanelGui(GameMenuGui gameMenu, GameMenuBackground superMenu) {
        this.gameMenu = gameMenu;
        this.superMenu = superMenu;
    }

    protected void notifyOpening() {
        this.updateNewWorldButton();
        this.saveButton.block(false);
        this.saveButton.setText(GameText.getText(19));
        this.dna.getTexture().setOverrideColour(GameMenuBackground.getStandardColour());
        for (DnaButtonGui button : this.buttons) {
            button.notifyOpening();
        }
    }

    protected void updateNewWorldButton() {
        this.newWorldButton.block(!GameManager.sessionManager.getSaves().hasFreeSlots());
    }

    @Override
    protected void init() {
        this.addDna();
        this.buttons.add(this.addLine(0, 0, 4, GameText.getText(3), new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    MenuPanelGui.this.superMenu.display(false);
                }
            }
        }));
        this.newWorldButton = this.addLine(1, 3, 2, GameText.getText(7), new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    MenuPanelGui.this.gameMenu.setNewSecondaryScreen(new WorldOptionsUi(MenuPanelGui.this.gameMenu, MenuPanelGui.this.superMenu));
                }
            }
        });
        this.buttons.add(this.newWorldButton);
        this.buttons.add(this.addLine(2, 4, 2, GameText.getText(20), new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    MenuPanelGui.this.gameMenu.setNewSecondaryScreen(new LoadScreenGui(MenuPanelGui.this.superMenu, MenuPanelGui.this.gameMenu));
                }
            }
        }));
        this.saveButton = this.addLine(3, 6, 2, GameText.getText(19), new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    boolean success = GameManager.sessionManager.saveCurrentSession();
                    MenuPanelGui.this.saveButton.block(true);
                    MenuPanelGui.this.saveButton.setText(success ? GAME_SAVED : ERROR_SAVING);
                    if (!success) {
                        MenuPanelGui.this.saveButton.setColour(ColourPalette.BRIGHT_RED);
                    }
                }
            }
        });
        this.buttons.add(this.saveButton);
        this.buttons.add(this.addLine(4, 8, 2, GameText.getText(4), new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    MenuPanelGui.this.gameMenu.setNewSecondaryScreen(new ControlsScreenGui(MenuPanelGui.this.gameMenu));
                }
            }
        }));
        this.buttons.add(this.addLine(5, 10, 2, GameText.getText(5), new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    MenuPanelGui.this.gameMenu.setNewSecondaryScreen(new OptionsPanelUi(MenuPanelGui.this.gameMenu));
                }
            }
        }));
        this.buttons.add(this.addLine(6, 11, 4, CREDITS, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    MenuPanelGui.this.gameMenu.setNewSecondaryScreen(new CreditsGui(MenuPanelGui.this.gameMenu));
                }
            }
        }));
        this.buttons.add(this.addLine(7, 13, 2, GameText.getText(6), new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    SoundMaestro.getMusicPlayer().fadeOutAndStopPlayer();
                    MenuPanelGui.this.gameMenu.display(false);
                    MenuPanelGui.this.superMenu.quit();
                    GameManager.gameState.setState(GameState.SPLASH_SCREEN);
                }
            }
        }));
        this.updateNewWorldButton();
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

    private void addDna() {
        this.dna = new GuiImage(GuiRepository.DNA_MAIN);
        this.dna.getTexture().setOverrideColour(GameMenuBackground.NORM_COLOUR);
        this.dna.getTexture().setAlphaDriver(new ConstantDriver(0.7f));
        this.dna.setPreferredAspectRatio(0.0625f);
        super.addCenteredComponentYScaleY(this.dna, 0.125f, 0.0f, 2.0f);
    }

    private DnaButtonGui addLine(int index, int gridY, int heightY, String text, ClickListener listener) {
        float aspectRatio = 4.0f / (float)heightY;
        float yPos = 0.0625f * (float)gridY;
        float scaleY = 0.0625f * (float)heightY;
        GuiImage line = new GuiImage(GuiRepository.LINES[index]);
        GuiTexture lineTexture = line.getTexture();
        lineTexture.setOverrideColour(GameMenuBackground.getStandardColour());
        lineTexture.setAlphaDriver(new ConstantDriver(0.7f));
        line.setPreferredAspectRatio(aspectRatio);
        super.addComponentY(line, 0.0f, yPos, scaleY);
        return this.addButton(index, lineTexture, text, listener);
    }

    private DnaButtonGui addButton(int index, GuiTexture line, String text, ClickListener listener) {
        float yPos = 0.171875f + (float)index * 0.09375f;
        DnaButtonGui button = new DnaButtonGui(line, text);
        button.setPreferredAspectRatio(12.0f);
        button.addListener(listener);
        super.addCenteredComponentYScaleY(button, yPos, super.getRelativeWidthCoords(0.25f), 0.07f);
        return button;
    }
}


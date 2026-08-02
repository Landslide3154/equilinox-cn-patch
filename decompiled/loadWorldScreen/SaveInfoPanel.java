/*
 * Decompiled with CFR 0.152.
 */
package loadWorldScreen;

import audio.SoundMaestro;
import basics.DisplayManager;
import fontRendering.Text;
import gameManaging.GameManager;
import gameMenu.GameMenuBackground;
import gameMenu.GameMenuGui;
import gameMenu.LoadChecker;
import gameMenu.LoadingScreen;
import guis.GuiMaster;
import java.util.Calendar;
import languages.GameText;
import loadWorldScreen.ConfirmGui;
import loadWorldScreen.LoadScreenGui;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import saves.SaveSlot;
import session.GameMode;
import textures.Texture;
import toolbox.Maths;
import toolbox.MyMouse;
import userInterfaces.GuiButton;
import userInterfaces.GuiImage;
import userInterfaces.GuiPanel;
import userInterfaces.Listener;
import userInterfaces.TextFieldGui;
import utils.MyFile;

public class SaveInfoPanel
extends GuiPanel {
    public static final Texture LOAD_ICON = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "load.png")).noFiltering().create();
    private static final String CURRENT_SAVE_TEXT = GameText.getText(74);
    private static final String DAYS_AGO = GameText.getText(75);
    private static final String HOURS_AGO = GameText.getText(76);
    private static final String MINUTES_AGO = GameText.getText(77);
    private static final String CORRUPT = GameText.getText(1003);
    private static final String CORRUPT_MESSAGE = GameText.getText(1004);
    private static final float LOAD_START_TIME = 0.7f;
    private static final float X_MARGIN = 0.05f;
    private static final float CENTER = 0.4f;
    private static final float TITLE_Y = 0.0f;
    private static final float TITLE_HEIGHT = 0.17f;
    private static final float INFO_Y = 0.25f;
    private static final float INFO_HEIGHT = 0.09f;
    private static final float BUTTONS_Y = 0.86f;
    private static final float BUTTONS_WIDTH = 0.07f;
    private static final float BUTTONS_GAP = 0.04f;
    private static final float CONFIRM_SIZE = 0.25f;
    private static final float TITLE_FONT = 2.0f;
    protected static final float INFO_FONT = 1.0f;
    private final GameMenuBackground gameMenuMaster;
    private final GameMenuGui innerGameMenu;
    private final LoadScreenGui loadScreen;
    private ConfirmGui currentPopUp;
    private GuiButton deleteButton;
    private final SaveSlot slot;
    private final boolean currentSave;
    private float yPos = 0.25f;

    public SaveInfoPanel(SaveSlot slot, GameMenuBackground gameMenuMaster, GameMenuGui innerGameMenu, LoadScreenGui loadScreen) {
        super(GameMenuBackground.getStandardColour(), 0.65f);
        this.slot = slot;
        this.currentSave = slot == GameManager.getSession().getSave();
        this.gameMenuMaster = gameMenuMaster;
        this.innerGameMenu = innerGameMenu;
        this.loadScreen = loadScreen;
        this.addContent();
    }

    protected void turnOffDeleteButton() {
        this.deleteButton.toggle();
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        MyMouse mouse = MyMouse.getActiveMouse();
        if (!this.currentSave && mouse.isLeftClick() && !this.deleteButton.isMouseOver() && this.currentPopUp != null && !this.currentPopUp.isMouseOver()) {
            this.deleteButton.toggle();
        }
    }

    private void addContent() {
        this.clear();
        this.addTitleBarImage();
        if (this.slot.isCorrupt()) {
            this.addCorruptTitle();
        } else {
            this.addTitle();
        }
        if (this.currentSave) {
            this.addCurrentSaveText();
            this.addLoadButton(true);
        } else {
            this.addInfo();
            if (!this.slot.isCorrupt()) {
                this.addLoadButton(false);
            }
            this.addDeleteButton();
        }
    }

    private void addTitle() {
        final TextFieldGui text = new TextFieldGui(this.slot.getName(), 2.0f, 20, false);
        text.addAcceptListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                String newName = text.getCurrentText();
                if (!newName.isEmpty()) {
                    SaveInfoPanel.this.slot.setName(newName);
                    SaveInfoPanel.this.loadScreen.updateSlotsPanel(SaveInfoPanel.this.slot);
                }
            }
        });
        super.addComponent(text, 0.05f, 0.0f, 0.9f, 0.17f);
    }

    private void addCorruptTitle() {
        Text text = Text.newText(CORRUPT).setFontSize(2.0f).create();
        text.setColour(ColourPalette.BRIGHT_RED);
        super.addText(text, 0.05f, 0.0f, 1.0f);
    }

    private void addInfo() {
        if (this.slot.isCorrupt()) {
            this.addCorruptMessage();
            return;
        }
        if (this.slot.getInfo().getGameMode() != GameMode.NORMAL) {
            this.addTextInfo("Game Mode", this.slot.getInfo().getGameMode().toString(), true);
        }
        this.addTextInfo(GameText.getText(888), SaveInfoPanel.getCalendarString(this.slot.getInfo().getLastPlayedDate()));
        this.addTextInfo(GameText.getText(889), Integer.toString(this.slot.getInfo().getPopulation()));
        if (this.slot.getInfo().getGameMode() == GameMode.NORMAL) {
            this.addTextInfo(GameText.getText(890), Integer.toString(this.slot.getInfo().getTasksComplete()));
            this.addTextInfo(GameText.getText(891), String.valueOf(Maths.formatNumber(this.slot.getInfo().getDp())) + " dp");
        }
    }

    private void addCorruptMessage() {
        Text text = Text.newText(CORRUPT_MESSAGE).setFontSize(1.0f).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.05f, this.yPos, 0.9f);
    }

    private void addTextInfo(String name, String desc) {
        this.addTextInfo(name, desc, false);
    }

    private void addTextInfo(String name, String desc, boolean coloured) {
        Text text = Text.newText(String.valueOf(name) + ":").setFontSize(1.0f).create();
        text.setColour(coloured ? ColourPalette.BEIGE : ColourPalette.WHITE);
        super.addText(text, 0.05f, this.yPos, 1.0f);
        text = Text.newText(desc).setFontSize(1.0f).create();
        text.setColour(coloured ? ColourPalette.BEIGE : ColourPalette.GREEN);
        super.addText(text, 0.4f, this.yPos, 1.0f);
        this.yPos += 0.09f;
    }

    private void addCurrentSaveText() {
        Text text = Text.newText(CURRENT_SAVE_TEXT).setFontSize(1.0f).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.05f, 0.25f, 1.0f);
    }

    private void addLoadButton(boolean currentWorld) {
        GuiButton button = new GuiButton(LOAD_ICON);
        float xPos = 0.41f;
        super.addComponentX(button, xPos, 0.86f, 0.07f);
        if (currentWorld) {
            button.addListener(new Listener(){

                @Override
                public void eventOccurred(boolean on) {
                    SoundMaestro.playSystemSound(GuiSounds.SELECT);
                    SaveInfoPanel.this.gameMenuMaster.display(false);
                }
            });
        } else {
            button.addListener(new Listener(){

                @Override
                public void eventOccurred(boolean on) {
                    SoundMaestro.playSystemSound(GuiSounds.SELECT);
                    LoadingScreen loading = new LoadingScreen(new LoadChecker(){
                        private float time = 0.0f;
                        private boolean triggered = false;

                        @Override
                        public boolean isLoaded() {
                            if (!this.triggered) {
                                this.time += DisplayManager.getDeltaSeconds();
                                if (this.time > 0.7f) {
                                    GameManager.sessionManager.loadSaveSlot(SaveInfoPanel.this.slot.getNumber());
                                    this.triggered = true;
                                }
                            }
                            return this.triggered && GameManager.sessionManager.hasWorldReady() && !GameManager.sessionManager.isLoading() && GameManager.getSession().isLoaded();
                        }
                    }, SaveInfoPanel.this.gameMenuMaster);
                    SaveInfoPanel.this.innerGameMenu.setNewTertiaryScreen(loading);
                }
            });
        }
    }

    private void addDeleteButton() {
        this.deleteButton = new GuiButton(GuiRepository.DELETE, true);
        float xPos = 0.52f;
        final float centerX = xPos + 0.035f;
        super.addComponentX(this.deleteButton, xPos, 0.86f, 0.07f);
        this.deleteButton.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (on) {
                    SaveInfoPanel.this.currentPopUp = new ConfirmGui(SaveInfoPanel.this, SaveInfoPanel.this.loadScreen, SaveInfoPanel.this.slot.getNumber());
                    SaveInfoPanel.super.addCenteredComponentX(SaveInfoPanel.this.currentPopUp, centerX, 0.61f, 0.25f);
                } else {
                    SaveInfoPanel.this.currentPopUp.remove();
                    SaveInfoPanel.this.currentPopUp = null;
                }
            }
        });
    }

    public static String getCalendarString(Calendar cal) {
        long lastPlayedTime = cal.getTimeInMillis();
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long difference = currentTime - lastPlayedTime;
        long minutes = difference / 60000L;
        if (minutes < 60L) {
            return String.valueOf(minutes) + " " + MINUTES_AGO;
        }
        long hours = minutes / 60L;
        if (hours < 24L) {
            return String.valueOf(hours) + " " + HOURS_AGO;
        }
        long days = hours / 24L;
        return String.valueOf(days) + " " + DAYS_AGO;
    }

    private void addTitleBarImage() {
        GuiImage image = new GuiImage(GuiRepository.BLOCK);
        image.getTexture().setOverrideColour(ColourPalette.MIDDLE_GREY);
        super.addComponent(image, 0.0f, 0.0f, 1.0f, 0.17f);
    }
}


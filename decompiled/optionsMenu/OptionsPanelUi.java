/*
 * Decompiled with CFR 0.152.
 */
package optionsMenu;

import audio.SoundMaestro;
import basics.DisplayManager;
import dropDownBoxUi.ComboBoxUi;
import fontRendering.Text;
import gameManaging.UserConfigs;
import gameMenu.GameMenuGui;
import gameMenu.SecondPanelUi;
import graphicsOptions.DisplaySizes;
import graphicsOptions.GraphicsOptions;
import graphicsOptions.GraphicsPreset;
import guiRendering.GuiRenderData;
import languages.GameText;
import languages.Language;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import optionsMenu.CheckOptionUi;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.OpenGlError;
import userInterfaces.ChangeListener;
import userInterfaces.GuiImage;
import userInterfaces.GuiSlider;
import userInterfaces.Listener;

public class OptionsPanelUi
extends SecondPanelUi {
    private static final String SHADOWS = GameText.getText(60);
    private static final String WATER = GameText.getText(61);
    private static final String ANTIALIASING = GameText.getText(62);
    private static final String DOF = GameText.getText(63);
    private static final String SUN_SHAFTS = GameText.getText(64);
    private static final String LENS_FLARE = GameText.getText(65);
    private static final String DISPLAY_SIZE = GameText.getText(965);
    private static final String LANGUAGE = GameText.getText(966);
    private static final String RESTART_MESSAGE = GameText.getText(967);
    private static final String PRESETS = GameText.getText(969);
    private static final String CUSTOM = GameText.getText(970);
    private static final String MUSIC_VOLUME = GameText.getText(84);
    private static final String SOUND_VOLUME = GameText.getText(85);
    private static final String BORDERLESS = GameText.getText(1040);
    private static final String VSYNC = GameText.getText(1069);
    private static final String ENLARGE = GameText.getText(1059);
    private static final Colour ALERT = ColourPalette.BRIGHT_RED;
    private static final int COLUMN_COUNT = 4;
    private static final float SIDE_PAD = 0.05f;
    private static final float COLUMN_SIZE = 0.225f;
    private static final float BOX_SIZE = 0.08f;
    private static final float BOX_WIDTH = 0.18f;
    private static final float SLIDER_HEIGHT = 0.045f;
    private static final float SLIDER_Y = 0.09f;
    private static final float CONTENT_WIDTH = 0.17999999f;
    private static final float COLUMN_PAD = 0.0225f;
    private static final float COL_1_X = 0.072500005f;
    private static final float COL_2_X = 0.2975f;
    private static final float COL_3_X = 0.52250004f;
    private static final float COL_4_X = 0.74750006f;
    private static final float Y_START = 0.1f;
    private static final float ROWS = 8.0f;
    private static final float Y_PAD_BOTTOM = 0.15f;
    private static final float Y_GAP = 0.09375f;
    public static final float FONT_SIZE = 1.11111f;
    public static final float CHECK_HEIGHT = 0.07f;
    private CheckOptionUi[] checkBoxes = new CheckOptionUi[6];
    private ComboBoxUi presetMenu;
    private ComboBoxUi sizeMenu;
    private ComboBoxUi langMenu;
    private CheckOptionUi borderlessCheckBox;
    private CheckOptionUi vsyncCheckBox;
    private boolean settingPreset = false;
    private boolean enlargeUi = GraphicsOptions.UI_SIZE > 1.0f;
    private CheckOptionUi uiOption;
    private Text resetMessage;
    private Text secondColumnMessage;

    public OptionsPanelUi(GameMenuGui gameMenu) {
        super(gameMenu);
    }

    @Override
    protected void init() {
        super.init();
        this.addLeftColumn();
        this.addSecondColumn();
        this.addCheckBoxes();
        this.addPresetOptions();
    }

    private void addPresetOptions() {
        GraphicsPreset preset = GraphicsPreset.values()[UserConfigs.getPresetId()];
        int id = preset == GraphicsPreset.CUSTOM ? 0 : UserConfigs.getPresetId();
        this.presetMenu = this.addMenuComponent(0.52250004f, 0, PRESETS, (Object[])GraphicsPreset.getPresets(), id);
        this.presetMenu.addSelectionListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GraphicsPreset preset = (GraphicsPreset)((Object)OptionsPanelUi.this.presetMenu.getSelectedObject().getObject());
                UserConfigs.setPresetId(preset.ordinal());
                OptionsPanelUi.this.settingPreset = true;
                OptionsPanelUi.this.checkBoxes[0].getCheckBox().set(preset.isShadows());
                OptionsPanelUi.this.checkBoxes[1].getCheckBox().set(preset.isWater());
                OptionsPanelUi.this.checkBoxes[2].getCheckBox().set(preset.isAa());
                OptionsPanelUi.this.checkBoxes[3].getCheckBox().set(preset.isDof());
                OptionsPanelUi.this.checkBoxes[4].getCheckBox().set(preset.isShafts());
                OptionsPanelUi.this.checkBoxes[5].getCheckBox().set(preset.isFlare());
                OptionsPanelUi.this.settingPreset = false;
            }
        });
        if (preset == GraphicsPreset.CUSTOM) {
            this.presetMenu.setOverrideName(CUSTOM);
        }
    }

    private void addLeftColumn() {
        this.sizeMenu = this.addMenuComponent(0.072500005f, 0, DISPLAY_SIZE, (Object[])DisplaySizes.getUsableDisplaySizes(), GraphicsOptions.displaySize.ordinal());
        this.sizeMenu.addSelectionListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GraphicsOptions.displaySize = (DisplaySizes)((Object)OptionsPanelUi.this.sizeMenu.getSelectedObject().getObject());
                OptionsPanelUi.this.borderlessCheckBox.show(GraphicsOptions.displaySize != DisplaySizes.FULL_SCREEN);
                OptionsPanelUi.this.updateUiSize();
                OptionsPanelUi.this.updateFirstColumnAlerts();
            }
        });
        this.langMenu = this.addMenuComponent(0.072500005f, 2, LANGUAGE, (Object[])Language.values(), UserConfigs.getLanguage().ordinal());
        this.langMenu.addSelectionListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                UserConfigs.setLanguage((Language)((Object)OptionsPanelUi.this.langMenu.getSelectedObject().getObject()));
                OptionsPanelUi.this.updateFirstColumnAlerts();
            }
        });
        this.vsyncCheckBox = new CheckOptionUi(GraphicsOptions.VSYNC, VSYNC, 1.11111f).addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                try {
                    OpenGlError.check("Before vsync");
                    Display.setVSyncEnabled(on);
                    GraphicsOptions.VSYNC = on;
                }
                catch (Exception e) {
                    SoundMaestro.playSystemSound(GuiSounds.NEGATIVE);
                    OptionsPanelUi.this.vsyncCheckBox.getCheckBox().setSilently(!on);
                    e.printStackTrace();
                }
            }
        });
        super.addComponent(this.vsyncCheckBox, 0.072500005f, this.getPositionY(4), 0.17999999f, 0.07f);
        this.borderlessCheckBox = new CheckOptionUi(GraphicsOptions.BORDERLESS, BORDERLESS, 1.11111f).addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GraphicsOptions.BORDERLESS = on;
                OptionsPanelUi.this.updateFirstColumnAlerts();
            }
        });
        super.addComponent(this.borderlessCheckBox, 0.072500005f, this.getPositionY(5), 0.17999999f, 0.07f);
        this.borderlessCheckBox.show(GraphicsOptions.displaySize != DisplaySizes.FULL_SCREEN);
        this.updateFirstColumnAlerts();
        this.addLine(1);
        this.addLine(2);
    }

    private void updateFirstColumnAlerts() {
        boolean change = false;
        if (GraphicsOptions.displaySize != DisplaySizes.FULL_SCREEN && DisplayManager.isBorderless() != GraphicsOptions.BORDERLESS) {
            this.borderlessCheckBox.setTextColour(ALERT);
            change = true;
        } else {
            this.borderlessCheckBox.setTextColour(ColourPalette.DARK_GREY);
        }
        if (GraphicsOptions.displaySize != DisplayManager.getDisplaySize()) {
            change = true;
            this.sizeMenu.setNameBoxColour(ALERT);
        } else {
            this.sizeMenu.setNameBoxColour(ColourPalette.WHITE);
        }
        if (UserConfigs.getLanguage().ordinal() != GameText.getLanguageId()) {
            change = true;
            this.langMenu.setNameBoxColour(ALERT);
        } else {
            this.langMenu.setNameBoxColour(ColourPalette.WHITE);
        }
        if (change) {
            this.addResetMessage();
        } else {
            this.removeResetMessage();
        }
    }

    private void addResetMessage() {
        if (this.resetMessage == null) {
            this.resetMessage = this.addText(RESTART_MESSAGE, 0.072500005f, 6, 0.8f, ALERT);
        }
    }

    private void removeResetMessage() {
        if (this.resetMessage != null) {
            super.deleteText(this.resetMessage);
            this.resetMessage = null;
        }
    }

    private ComboBoxUi addMenuComponent(float xPos, int row, String name, Object[] options, int selected) {
        this.addText(name, xPos, row, 1.11111f, ColourPalette.DARK_GREY);
        return this.addDropMenu(xPos, row + 1, options, selected);
    }

    private ComboBoxUi addDropMenu(float xPos, int row, Object[] options, int selected) {
        ComboBoxUi menu = new ComboBoxUi(options, selected);
        menu.setFontSize(1.11111f);
        super.addComponent(menu, xPos, this.getPositionY(row) - 0.04f, 0.18f, 0.08f);
        return menu;
    }

    private void setCustomGraphics() {
        if (!this.settingPreset) {
            this.presetMenu.setOverrideName(CUSTOM);
            UserConfigs.setPresetId(GraphicsPreset.CUSTOM.ordinal());
        }
    }

    private void addCheckBoxes() {
        int row = 2;
        this.checkBoxes[0] = new CheckOptionUi(GraphicsOptions.SHADOWS, SHADOWS, 1.11111f).addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GraphicsOptions.SHADOWS = on;
                OptionsPanelUi.this.setCustomGraphics();
            }
        });
        super.addComponent(this.checkBoxes[0], 0.52250004f, this.getPositionY(row++), 0.17999999f, 0.07f);
        this.checkBoxes[1] = new CheckOptionUi(GraphicsOptions.HD_WATER, WATER, 1.11111f).addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GraphicsOptions.HD_WATER = on;
                OptionsPanelUi.this.setCustomGraphics();
            }
        });
        super.addComponent(this.checkBoxes[1], 0.52250004f, this.getPositionY(row++), 0.17999999f, 0.07f);
        this.checkBoxes[2] = new CheckOptionUi(GraphicsOptions.ANTI_ALIASING, ANTIALIASING, 1.11111f).addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GraphicsOptions.ANTI_ALIASING = on;
                OptionsPanelUi.this.setCustomGraphics();
            }
        });
        super.addComponent(this.checkBoxes[2], 0.52250004f, this.getPositionY(row++), 0.17999999f, 0.07f);
        this.checkBoxes[3] = new CheckOptionUi(GraphicsOptions.DOF_EFFECT, DOF, 1.11111f).addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GraphicsOptions.DOF_EFFECT = on;
                OptionsPanelUi.this.setCustomGraphics();
            }
        });
        super.addComponent(this.checkBoxes[3], 0.52250004f, this.getPositionY(row++), 0.17999999f, 0.07f);
        row = 2;
        this.checkBoxes[4] = new CheckOptionUi(GraphicsOptions.SUN_RAYS, SUN_SHAFTS, 1.11111f).addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GraphicsOptions.SUN_RAYS = on;
                OptionsPanelUi.this.setCustomGraphics();
            }
        });
        super.addComponent(this.checkBoxes[4], 0.74750006f, this.getPositionY(row++), 0.17999999f, 0.07f);
        this.checkBoxes[5] = new CheckOptionUi(GraphicsOptions.LENS_FLARE, LENS_FLARE, 1.11111f).addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GraphicsOptions.LENS_FLARE = on;
                OptionsPanelUi.this.setCustomGraphics();
            }
        });
        super.addComponent(this.checkBoxes[5], 0.74750006f, this.getPositionY(row++), 0.17999999f, 0.07f);
    }

    private void addSecondColumn() {
        int row = 0;
        this.addSlider(MUSIC_VOLUME, 0.2975f, row, SoundMaestro.getMusicPlayer().getVolume(), new ChangeListener(){

            @Override
            public void eventOccurred(float value) {
                SoundMaestro.getMusicPlayer().setVolume(value);
            }
        });
        this.addSlider(SOUND_VOLUME, 0.2975f, row += 2, SoundMaestro.SOUND_VOLUME, new ChangeListener(){

            @Override
            public void eventOccurred(float value) {
                SoundMaestro.SOUND_VOLUME = value;
            }
        });
        this.uiOption = new CheckOptionUi(this.enlargeUi, ENLARGE, 1.11111f).addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                OptionsPanelUi.this.enlargeUi = on;
                OptionsPanelUi.this.updateUiSize();
            }
        });
        this.updateUiSize();
        super.addComponent(this.uiOption, 0.2975f, this.getPositionY(row += 2), 0.17999999f, 0.07f);
    }

    private void updateUiSize() {
        this.uiOption.show(GraphicsOptions.displaySize.getWidth() > 1400);
        GraphicsOptions.UI_SIZE = this.enlargeUi ? (GraphicsOptions.displaySize.getWidth() > 3500 ? 2.0f : (GraphicsOptions.displaySize.getWidth() > 1950 ? 1.5f : (GraphicsOptions.displaySize.getWidth() > 1700 ? 1.3f : (GraphicsOptions.displaySize.getWidth() > 1400 ? 1.25f : 1.0f)))) : 1.0f;
        if (GraphicsOptions.displaySize.getWidth() > 1400 && DisplayManager.getUiScale() > 1.0f != this.enlargeUi) {
            this.uiOption.setTextColour(ALERT);
            this.addSecondColumMessage(RESTART_MESSAGE);
        } else {
            this.uiOption.setTextColour(ColourPalette.DARK_GREY);
            this.removeSecondColumMessage();
        }
    }

    private void addSecondColumMessage(String message) {
        if (this.secondColumnMessage == null) {
            this.secondColumnMessage = this.addText(message, 0.2975f, 5, 0.8f, ALERT);
        } else {
            this.secondColumnMessage.setText(message);
        }
    }

    private void removeSecondColumMessage() {
        if (this.secondColumnMessage != null) {
            super.deleteText(this.secondColumnMessage);
            this.secondColumnMessage = null;
        }
    }

    private void addSlider(String name, float xPos, int row, float value, ChangeListener listener) {
        this.addText(name, xPos, row, 1.11111f, ColourPalette.DARK_GREY);
        GuiSlider slider = new GuiSlider(value, 5, ColourPalette.WHITE);
        slider.addChangeListener(listener);
        float yPos = this.getPositionY(row) + 0.09f;
        super.addComponent(slider, xPos, yPos, 0.17999999f, 0.045f);
    }

    private void addLine(int column) {
        GuiImage image = new GuiImage(GuiRepository.BLOCK);
        image.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
        float width = super.pixelsToRelativeX(2.0f);
        float xPos = 0.05f + 0.225f * (float)column;
        super.addComponent(image, xPos, 0.1f, width, 0.8f);
    }

    private Text addText(String name, float xPos, int row, float font, Colour colour) {
        Text text = Text.newText(name).setFontSize(font).create();
        text.setColour(colour);
        super.addText(text, xPos, this.getPositionY(row) - 0.02f, 0.18f);
        return text;
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

    private float getPositionY(int row) {
        return 0.1f + (float)row * 0.09375f;
    }
}


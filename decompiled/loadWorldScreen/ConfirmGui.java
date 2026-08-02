/*
 * Decompiled with CFR 0.152.
 */
package loadWorldScreen;

import audio.SoundMaestro;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import languages.GameText;
import loadWorldScreen.LoadScreenGui;
import loadWorldScreen.SaveInfoPanel;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.GuiButton;
import userInterfaces.Listener;

public class ConfirmGui
extends GuiComponent {
    private static final String TITLE = GameText.getText(68);
    private static final float TITLE_Y = 0.15f;
    private static final float BUTTONS_Y = 0.5f;
    private static final float BUTTONS_HEIGHT = 0.3f;
    private static final float BUTTON_X = 0.35f;
    private GuiTexture background;
    private SaveInfoPanel panel;
    private int slotNum;
    private LoadScreenGui loadScreen;

    public ConfirmGui(SaveInfoPanel panel, LoadScreenGui loadScreen, int slotNumber) {
        this.slotNum = slotNumber;
        this.loadScreen = loadScreen;
        this.panel = panel;
        super.setPreferredAspectRatio(2.0f);
        this.background = new GuiTexture(GuiRepository.CONFIRM);
        this.background.setOverrideColour(ColourPalette.LIGHT_GREY);
        this.addTitle();
        this.addYes();
        this.addNo();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    private void addTitle() {
        Text text = Text.newText(TITLE).center().setFontSize(1.0f).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, 0.15f, 1.0f);
    }

    private void addYes() {
        GuiButton button = new GuiButton(GuiRepository.TICK);
        button.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GameManager.sessionManager.getSaves().deleteSave(ConfirmGui.this.slotNum);
                SoundMaestro.playSystemSound(GuiSounds.DELETE);
                ConfirmGui.this.loadScreen.reset();
            }
        });
        super.addCenteredComponentX(button, 0.35f, 0.5f, 0.3f);
    }

    private void addNo() {
        GuiButton button = new GuiButton(GuiRepository.CROSS);
        button.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                ConfirmGui.this.panel.turnOffDeleteButton();
            }
        });
        super.addCenteredComponentX(button, 0.65f, 0.5f, 0.3f);
    }
}


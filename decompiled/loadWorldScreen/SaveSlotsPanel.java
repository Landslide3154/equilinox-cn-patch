/*
 * Decompiled with CFR 0.152.
 */
package loadWorldScreen;

import fontRendering.Text;
import gameManaging.GameManager;
import gameMenu.GameMenuBackground;
import languages.GameText;
import loadWorldScreen.LoadScreenGui;
import mainGuis.ColourPalette;
import saves.SaveSlot;
import saves.Saves;
import session.GameMode;
import toolbox.Colour;
import userInterfaces.GuiPanel;
import userInterfaces.GuiTextButton;
import userInterfaces.GuiTextButtonGroup;
import userInterfaces.Listener;

public class SaveSlotsPanel
extends GuiPanel {
    public static final Colour TEXT_COLOUR = new Colour(0.55f, 0.55f, 0.55f);
    private static final float FONT_SIZE = 1.0f;
    private static final float BUTTONS_X_POS = 0.0f;
    private static final float BUTTON_Y_PAD = 0.05f;
    private static final float BUTTONS_Y_SIZE = 0.089999996f;
    private static final float BUTTONS_X_WIDTH = 1.0f;
    private static final String EMPTY = GameText.getText(78);
    private static final String CORRUPT = GameText.getText(1005);
    private float yPos = 0.05f;
    private LoadScreenGui superPanel;
    private GuiTextButtonGroup group = new GuiTextButtonGroup();
    private Text[] texts;

    public SaveSlotsPanel(LoadScreenGui superPanel) {
        super(GameMenuBackground.getStandardColour(), 0.65f);
        this.superPanel = superPanel;
        this.addSaveSlots();
        superPanel.addInfoPanel(GameManager.getSession().getSave());
    }

    protected void updateText(SaveSlot slot) {
        Text text = this.texts[slot.getNumber()];
        text.setText(slot.getName());
    }

    private void addSaveSlots() {
        Saves saves = GameManager.sessionManager.getSaves();
        this.texts = new Text[saves.getSlotCount()];
        int i = 0;
        while (i < saves.getSlotCount()) {
            SaveSlot slot = saves.getSaveSlot(i);
            if (!slot.isEmpty()) {
                this.createSaveOption(slot);
            } else {
                this.createTextSlot(slot);
            }
            ++i;
        }
    }

    private void createSaveOption(final SaveSlot slot) {
        Listener listener = new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (!slot.isEmpty()) {
                    SaveSlotsPanel.this.superPanel.addInfoPanel(slot);
                }
            }
        };
        String name = slot.isCorrupt() ? "<" + CORRUPT + ">" : slot.getName();
        this.texts[slot.getNumber()] = this.createButton(slot.getInfo().getGameMode() == GameMode.NORMAL, name, listener, slot == GameManager.getSession().getSave());
    }

    private void createTextSlot(SaveSlot slot) {
        String name = "<" + EMPTY + ">";
        Text text = Text.newText(name).center().setFontSize(1.0f).create();
        text.setColour(TEXT_COLOUR);
        this.addText(text, 0.0f, this.yPos, 1.0f);
        this.yPos += 0.089999996f;
    }

    private Text createButton(boolean normal, String textString, Listener listener, boolean current) {
        Text text = Text.newText(textString).center().setFontSize(1.0f).create();
        text.setColour(normal ? ColourPalette.WHITE : ColourPalette.BEIGE);
        GuiTextButton button = new GuiTextButton(text);
        if (current) {
            button.highlight(true);
        }
        button.addListener(listener);
        this.addComponent(button, 0.0f, this.yPos, 1.0f, 0.089999996f);
        this.yPos += 0.089999996f;
        this.group.addButton(button, current);
        return text;
    }
}


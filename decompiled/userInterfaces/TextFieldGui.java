/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import interpolation.Timer;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.MyKeyboard;
import userInterfaces.GuiButton;
import userInterfaces.GuiImage;
import userInterfaces.Listener;
import visualFxDrivers.SinWaveDriver;

public class TextFieldGui
extends GuiComponent {
    private static final float BUTTON_HEIGHT = 0.6f;
    private static final float CURSOR_HEIGHT = 0.6f;
    private static final float CURSOR_FLASH_TIME = 0.6f;
    private static final float PADDING_HEIGHT = 0.19999999f;
    private final Text text;
    private final int maxChars;
    private GuiButton editButton;
    private GuiImage cursor;
    private boolean alwaysOn = false;
    private int leftPadPixels = 0;
    private int topPadPixels = 0;
    private final boolean invertColour;
    private Timer deletingWait = Timer.createOneOffTimer(0.4f, false);
    private boolean deletingSpree = false;
    private Timer spreeTimer = Timer.createLoopingTimer(0.05f, false);
    private boolean editMode = false;
    private List<Listener> listeners = new ArrayList<Listener>();
    private List<Listener> acceptListeners = new ArrayList<Listener>();
    private GuiTexture background;

    public TextFieldGui(String initialText, float fontSize, int maxChars, boolean invertColours) {
        this.maxChars = maxChars;
        this.text = Text.newText(initialText).setFontSize(fontSize).create();
        this.invertColour = invertColours;
        this.text.setColour(this.invertColour ? ColourPalette.BEIGE : ColourPalette.WHITE);
    }

    public TextFieldGui(String initialText, float fontSize, int maxChars, boolean invertColours, boolean alwaysOn) {
        this.maxChars = maxChars;
        this.text = Text.newText(initialText).setFontSize(fontSize).create();
        this.invertColour = invertColours;
        this.alwaysOn = alwaysOn;
        this.text.setColour(this.invertColour ? ColourPalette.BEIGE : ColourPalette.WHITE);
    }

    public TextFieldGui(String initialText, float fontSize, int maxChars, boolean invertColours, Colour backCol, int leftPadPixels, int topPadPixels) {
        this.maxChars = maxChars;
        this.text = Text.newText(initialText).setFontSize(fontSize).create();
        this.invertColour = invertColours;
        this.alwaysOn = true;
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(backCol);
        this.text.setColour(this.invertColour ? ColourPalette.BEIGE : ColourPalette.WHITE);
        this.leftPadPixels = leftPadPixels;
        this.topPadPixels = topPadPixels;
    }

    public void addChangeListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void addAcceptListener(Listener listener) {
        this.acceptListeners.add(listener);
    }

    public String getCurrentText() {
        return this.text.getTextString();
    }

    @Override
    public void show(boolean visible) {
        super.show(visible);
        MyKeyboard.getKeyboard().block(visible);
    }

    @Override
    protected void delete() {
        MyKeyboard.getKeyboard().block(false);
        super.delete();
    }

    @Override
    protected void init() {
        if (!this.alwaysOn) {
            this.initButton();
        }
        this.initText();
        this.initCursor();
        if (this.alwaysOn) {
            this.startAlwaysEditMode();
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        if (this.background != null) {
            this.background.setPosition(position.x, position.y, scale.x, scale.y);
        }
    }

    @Override
    protected void updateSelf() {
        if (this.editMode) {
            MyKeyboard.getKeyboard().block(true);
            if (MyKeyboard.getKeyboard().keyDownEventOccurredIgnoreBlock(14)) {
                this.spreeTimer.reset();
                this.deletingWait.start();
                this.deletingSpree = false;
            }
            if (MyKeyboard.getKeyboard().keyUpEventOccurredIgnoreBlock(14)) {
                this.spreeTimer.reset();
                this.deletingWait.stop();
                this.deletingSpree = false;
            }
            if (!this.deletingSpree && MyKeyboard.getKeyboard().isKeyDownIgnoreBlock(14) && this.deletingWait.check()) {
                this.deletingSpree = true;
            }
            this.updateText();
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (this.background != null) {
            data.addTexture(this.getLevel(), this.background);
        }
    }

    private void initCursor() {
        this.cursor = new GuiImage(GuiRepository.BLOCK);
        float width = 1.0f / (super.getScale().x * (float)DisplayManager.getUiWidth());
        this.cursor.getTexture().setAlphaDriver(new SinWaveDriver(0.0f, 1.0f, 0.6f));
        super.addComponent(this.cursor, this.text.getActualWidth() / super.getScale().x + super.pixelsToRelativeX(this.leftPadPixels), 0.19999999f, width, 0.6f);
        this.cursor.show(false);
    }

    private void initButton() {
        this.editButton = new GuiButton(GuiRepository.EDIT, true);
        float buttonWidth = super.getRelativeWidthCoords(0.6f);
        float padWidth = super.getRelativeWidthCoords(0.19999999f);
        super.addCenteredComponentYScaleY(this.editButton, 0.5f, 1.0f - (padWidth + buttonWidth), 0.6f);
        this.editButton.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (!on) {
                    TextFieldGui.this.spreeTimer.reset();
                    TextFieldGui.this.deletingWait.stop();
                    TextFieldGui.this.deletingSpree = false;
                }
                TextFieldGui.this.editMode = on;
                TextFieldGui.this.cursor.show(on);
                TextFieldGui.this.text.setColour(TextFieldGui.this.invertColour != on ? ColourPalette.BEIGE : ColourPalette.WHITE);
                MyKeyboard.getKeyboard().block(on);
                if (!on) {
                    TextFieldGui.this.notifyAcceptListeners();
                }
            }
        });
    }

    private void startAlwaysEditMode() {
        this.editMode = true;
        this.cursor.show(true);
        this.text.setColour(!this.invertColour ? ColourPalette.BEIGE : ColourPalette.WHITE);
        if (super.isShown()) {
            MyKeyboard.getKeyboard().block(true);
        }
    }

    private void initText() {
        super.addText(this.text, super.pixelsToRelativeX(this.leftPadPixels), super.pixelsToRelativeY(this.topPadPixels), 2.0f);
    }

    public void clearText(boolean notifyListeners) {
        this.text.setText("");
        this.cursor.setRelativeX(this.text.getActualWidth() / super.getScale().x + super.pixelsToRelativeX(this.leftPadPixels));
        if (notifyListeners) {
            this.notifyListeners();
        }
    }

    private void updateText() {
        List<Integer> chars = MyKeyboard.getKeyboard().getChars();
        if (this.deletingSpree && this.spreeTimer.check()) {
            chars.add(8);
        }
        String newText = this.text.getTextString();
        for (int ascii : chars) {
            newText = this.applyChar(newText, ascii);
        }
        if (!chars.isEmpty()) {
            this.text.setText(newText);
            this.cursor.setRelativeX(this.text.getActualWidth() / super.getScale().x + super.pixelsToRelativeX(this.leftPadPixels));
            this.notifyListeners();
        }
    }

    private void notifyListeners() {
        for (Listener listener : this.listeners) {
            listener.eventOccurred(true);
        }
    }

    private void notifyAcceptListeners() {
        for (Listener listener : this.acceptListeners) {
            listener.eventOccurred(true);
        }
    }

    private String applyChar(String newText, int ascii) {
        if (ascii == 8) {
            if (newText.length() > 0 && this.text.getTextString().length() > 0) {
                newText = newText.substring(0, this.text.getTextString().length() - 1);
            }
        } else {
            if (ascii == 13) {
                if (!this.alwaysOn) {
                    this.editButton.toggle();
                }
                return newText;
            }
            if (newText.length() < this.maxChars && (ascii != 32 || newText.length() != 0)) {
                newText = newText.equals(".") ? "" + (char)ascii : String.valueOf(newText) + (char)ascii;
            }
        }
        return newText;
    }
}


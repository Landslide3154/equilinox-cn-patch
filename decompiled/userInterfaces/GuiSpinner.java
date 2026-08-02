/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.MyMouse;
import userInterfaces.GuiButton;
import userInterfaces.Listener;
import visualFxDrivers.SlideDriver;

public class GuiSpinner
extends GuiComponent {
    private static final float TEXT_PAD = 0.05f;
    private static final float FADE_TIME = 0.5f;
    private static final float SLIDE_SPEED = 1.3f;
    private int value;
    private int change;
    private GuiTexture background;
    private float arrowSectionWidth;
    private float fontSize;
    private Text text;
    private List<Listener> changeListeners = new ArrayList<Listener>();
    private float releasedTime = 0.0f;
    private Text releasedText;

    public GuiSpinner(int start, int change, float fontSize) {
        this.value = start;
        this.change = change;
        this.fontSize = fontSize;
        this.initTextures();
    }

    public int getValue() {
        return this.value;
    }

    public void switchOver() {
        if (this.releasedText != null) {
            super.deleteText(this.releasedText);
        }
        this.releasedTime = 0.0f;
        this.value = 0;
        this.releasedText = this.text;
        this.initText();
        this.releasedText.setAlphaDriver(new SlideDriver(1.0f, 0.0f, 0.5f));
    }

    public void setBackgroundColour(Colour colour) {
        this.background.setOverrideColour(colour);
    }

    public void addChangeListener(Listener listener) {
        this.changeListeners.add(listener);
    }

    public void setValue(int value) {
        this.value = value;
        this.text.setText(this.getTextString());
    }

    @Override
    protected void init() {
        this.calcArrowSectionWidth();
        this.addButtons();
        this.initText();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        MyMouse mouse = MyMouse.getActiveMouse();
        if (super.isMouseOver()) {
            this.change((int)Math.signum(mouse.getDWheel()));
        }
        this.updateReleasedText();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void updateReleasedText() {
        if (this.releasedText != null) {
            this.releasedTime += DisplayManager.getDeltaSeconds();
            this.releasedText.increaseRelativeX(-1.3f * DisplayManager.getDeltaSeconds());
            if (this.releasedTime > 0.5f) {
                super.deleteText(this.releasedText);
                this.releasedText = null;
            }
        }
    }

    private void initTextures() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.LIGHT_GREY);
    }

    private void calcArrowSectionWidth() {
        this.arrowSectionWidth = super.getRelativeWidthCoords(0.5f);
    }

    private void initText() {
        this.text = Text.newText(this.getTextString()).rightAlign().setFontSize(this.fontSize).create();
        this.text.setColour(ColourPalette.WHITE);
        super.addText(this.text, 0.0f, 0.0f, 1.0f - (this.arrowSectionWidth + 0.05f));
    }

    private void addButtons() {
        GuiButton up = new GuiButton(GuiRepository.PLUS);
        up.setPreferredPixelSize(8);
        up.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GuiSpinner.this.change(1);
            }
        });
        GuiButton down = new GuiButton(GuiRepository.MINUS);
        down.setPreferredPixelSize(8);
        down.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GuiSpinner.this.change(-1);
            }
        });
        super.addPixelCompCenterX(up, 1.0f - this.arrowSectionWidth / 2.0f, 0.0f);
        super.addPixelCompCenterX(down, 1.0f - this.arrowSectionWidth / 2.0f, 0.5f);
    }

    private String getTextString() {
        if (this.value == 0) {
            return "";
        }
        String textString = this.value > 0 ? "+" : "";
        textString = String.valueOf(textString) + this.value + "%";
        return textString;
    }

    private void notifyListeners() {
        for (Listener listener : this.changeListeners) {
            listener.eventOccurred(true);
        }
    }

    private void change(int amount) {
        if (amount == 0) {
            return;
        }
        this.value += this.change * amount;
        this.text.setText(this.getTextString());
        this.notifyListeners();
    }
}


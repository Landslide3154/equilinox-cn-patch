/*
 * Decompiled with CFR 0.152.
 */
package optionsMenu;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.GuiCheckBox;
import userInterfaces.Listener;

public class CheckOptionUi
extends GuiComponent {
    private static final float TEXT_X = 0.2f;
    private GuiCheckBox checkBox;
    private boolean on;
    private String name;
    private float fontSize;
    private Text text;
    private Colour textColour = ColourPalette.DARK_GREY;
    private List<Listener> listeners = new ArrayList<Listener>();

    public CheckOptionUi(boolean on, String name, float fontSize) {
        this.on = on;
        this.name = name;
        this.fontSize = fontSize;
    }

    @Override
    protected void init() {
        super.init();
        this.checkBox = new GuiCheckBox(this.on, GuiRepository.CHECK_FILLED, GuiRepository.CHECK_EMPTY, ColourPalette.LIGHT_GREY);
        super.addComponentY(this.checkBox, 0.0f, 0.0f, 1.0f);
        for (Listener listener : this.listeners) {
            this.checkBox.addListener(listener);
        }
        this.addName();
    }

    public GuiCheckBox getCheckBox() {
        return this.checkBox;
    }

    public CheckOptionUi addListener(Listener listener) {
        if (this.checkBox == null) {
            this.listeners.add(listener);
        } else {
            this.checkBox.addListener(listener);
        }
        return this;
    }

    public void setTextColour(Colour colour) {
        this.textColour = colour;
        if (this.text != null) {
            this.text.setColour(this.textColour);
        }
    }

    private void addName() {
        this.text = Text.newText(this.name).setFontSize(this.fontSize).create();
        this.text.setColour(this.textColour);
        super.addText(this.text, 0.2f, 0.0f, 1.0f);
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
}


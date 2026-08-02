/*
 * Decompiled with CFR 0.152.
 */
package dropDownBoxUi;

import basics.DisplayManager;
import dropDownBoxUi.ComboBoxObject;
import dropDownBoxUi.ComboBoxUi;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.MyMouse;

public class NameBoxUi
extends GuiComponent {
    private final ComboBoxUi comboBox;
    private GuiTexture background;
    private GuiTexture arrowBox;
    private GuiTexture arrow;
    private Text text;
    private GuiComponent currentComponent;
    private ComboBoxObject item;
    private boolean active = false;
    private boolean mouseOver = false;
    private String overrideName = null;
    private Colour nameColour = ComboBoxUi.TEXT_COLOUR;

    protected NameBoxUi(ComboBoxObject item, ComboBoxUi comboBox) {
        this.item = item;
        this.comboBox = comboBox;
        this.initTextures();
    }

    @Override
    protected void init() {
        if (this.item.hasUiComponent()) {
            this.addComponent(this.item.createUiComponent());
        } else {
            this.addText();
        }
    }

    protected void setCurrentItem(ComboBoxObject item) {
        this.item = item;
        if (item.hasUiComponent()) {
            this.addComponent(item.createUiComponent());
        } else {
            this.text.setText(String.valueOf(item.getExtraPrefix()) + item.toString());
        }
    }

    protected void setOverrideName(String name) {
        if (this.text != null) {
            this.text.setText(name);
        } else {
            this.overrideName = name;
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        float pixelHeight = scale.y * (float)DisplayManager.getUiHeight();
        float width = pixelHeight / (float)DisplayManager.getUiWidth();
        this.arrowBox.setPosition(position.x + scale.x - width, position.y, width, scale.y);
        this.arrow.setPosition(position.x + scale.x - width, position.y, width, scale.y);
    }

    protected void block(boolean blocked) {
        this.arrowBox.setOverrideColour(ComboBoxUi.TINT_COL);
        this.text.setColour(ComboBoxUi.TEXT_COLOUR);
        this.background.setOverrideColour(ComboBoxUi.BACK_COLOUR);
        this.arrow.setOverrideColour(ColourPalette.WHITE);
    }

    public void setNameColour(Colour colour) {
        this.nameColour = colour;
        if (this.text != null) {
            this.text.setColour(this.nameColour);
        }
    }

    @Override
    protected void updateSelf() {
        MyMouse mouse = MyMouse.getActiveMouse();
        if (super.isMouseOver()) {
            this.mouseOver = true;
            if (mouse.isLeftClick()) {
                this.activate(!this.active);
            }
        } else {
            this.mouseOver = false;
            if (this.active && mouse.isLeftClick()) {
                this.activate(false);
            }
        }
        this.setColour();
    }

    protected void activate(boolean active) {
        this.active = active;
        this.comboBox.openDropBox(active);
    }

    private void setColour() {
        if (this.active) {
            this.arrowBox.setOverrideColour(ComboBoxUi.TINT_COL);
            this.background.setOverrideColour(ColourPalette.LIGHT_GREY);
        } else if (this.mouseOver) {
            this.arrowBox.setOverrideColour(ComboBoxUi.TINT_COL_2);
            this.background.setOverrideColour(ComboBoxUi.BACK_COLOUR);
        } else {
            this.arrowBox.setOverrideColour(ColourPalette.LIGHT_GREY);
            this.background.setOverrideColour(ComboBoxUi.BACK_COLOUR);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.arrowBox);
        data.addTexture(this.getLevel(), this.arrow);
    }

    private void initTextures() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ComboBoxUi.BACK_COLOUR);
        this.arrowBox = new GuiTexture(GuiRepository.BLOCK);
        this.arrowBox.setOverrideColour(ColourPalette.LIGHT_GREY);
        this.arrow = new GuiTexture(GuiRepository.DOWN_ARROW);
        this.arrow.setOverrideColour(ComboBoxUi.TEXT_COLOUR);
    }

    private void addComponent(GuiComponent newComponent) {
        if (this.currentComponent != null) {
            this.currentComponent.remove();
        }
        float startX = 5.0f / ((float)DisplayManager.getUiWidth() * super.getScale().x);
        this.currentComponent = newComponent;
        super.addComponent(newComponent, startX, 0.0f, 1.0f - 2.0f * startX, 1.0f);
    }

    private void addText() {
        float startX = 5.0f / ((float)DisplayManager.getUiWidth() * super.getScale().x);
        float startY = 2.0f / ((float)DisplayManager.getUiHeight() * super.getScale().y);
        this.text = Text.newText(this.overrideName == null ? String.valueOf(this.item.getExtraPrefix()) + this.item.toString() : this.overrideName).setFontSize(this.comboBox.getFontSize()).create();
        this.text.setColour(this.nameColour);
        super.addText(this.text, startX, startY, 1.0f);
    }
}


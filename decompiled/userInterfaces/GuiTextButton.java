/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import audio.SoundMaestro;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.MyMouse;
import userInterfaces.Listener;
import visualFxDrivers.ConstantDriver;

public class GuiTextButton
extends GuiComponent {
    private Text text;
    private Colour blockedColour = new Colour(0.7f, 0.7f, 0.7f);
    private Colour textColour;
    private boolean mouseOver = false;
    private boolean blocked = false;
    private GuiTexture highlight;
    private boolean highlighted = false;
    private List<Listener> listeners = new ArrayList<Listener>();

    public GuiTextButton(Text text) {
        this.text = text;
        this.textColour = text.getColour();
        super.addText(text, 0.0f, 0.0f, 1.0f);
        this.highlight = new GuiTexture(GuiRepository.BLOCK);
        this.highlight.setOverrideColour(ColourPalette.GREEN);
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void block() {
        if (!this.blocked) {
            this.text.setColour(this.blockedColour);
            this.mouseOver = false;
            this.blocked = true;
            this.highlighted = false;
            this.text.setScaleDriver(new ConstantDriver(1.0f));
        }
    }

    public void unblock() {
        if (this.blocked) {
            this.blocked = false;
            this.text.setColour(this.textColour);
        }
    }

    public void highlight(boolean select) {
        this.highlighted = select;
        if (this.highlighted) {
            this.text.setColour(this.textColour);
        }
    }

    @Override
    protected void updateSelf() {
        if (this.blocked || this.highlighted) {
            return;
        }
        if (this.isMouseOver() && !this.mouseOver) {
            this.text.setColour(ColourPalette.GREEN);
            this.mouseOver = true;
        } else if (!this.isMouseOver() && this.mouseOver) {
            this.text.setColour(this.textColour);
            this.mouseOver = false;
        }
        if (this.isMouseOver() && MyMouse.getActiveMouse().isLeftClick()) {
            SoundMaestro.playSystemSound(GuiSounds.getClickSound());
            for (Listener listener : this.listeners) {
                listener.eventOccurred(true);
            }
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (this.highlighted) {
            data.addTexture(this.getLevel(), this.highlight);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.highlight.setPosition(position.x, position.y, scale.x, scale.y);
    }
}


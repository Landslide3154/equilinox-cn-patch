/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import componentArchitecture.Action;
import entityInfoGui.EntityInfoGui;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;

public class ActionButtonGui
extends GuiClickable {
    private static final float BOUNCE_TIME = 0.25f;
    private static final float BOUNCE_SIZE = 1.1f;
    private static final Colour MOUSE_OVER_COL = ColourPalette.BASE_BLUE.duplicate().scale(1.3f);
    private GuiTexture background;
    private GuiTexture foreground;
    private GuiTexture readyButton;
    private boolean ready;
    private final Action action;

    public ActionButtonGui(Action action) {
        super(1.0f);
        this.action = action;
        this.initTextures();
        this.addListener();
        this.ready = action.isReady();
        if (!this.ready) {
            super.block(true);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.addText();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        this.foreground.setPosition(position.x, position.y, this.getForgroundScaleX(), scale.y);
        this.readyButton.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        if (!this.ready && this.action.isReady()) {
            super.block(false);
            this.ready = true;
            super.bounce(0.25f, 1.1f);
        }
        this.foreground.setWidth(this.getForgroundScaleX());
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (this.action.isReady()) {
            data.addTexture(this.getLevel(), this.readyButton);
        } else {
            data.addTexture(this.getLevel(), this.background);
            data.addTexture(this.getLevel(), this.foreground);
        }
    }

    private void initTextures() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.MIDDLE_GREY);
        this.foreground = new GuiTexture(GuiRepository.BLOCK);
        this.foreground.setOverrideColour(ColourPalette.LIGHT_GREY);
        this.readyButton = new GuiTexture(GuiRepository.BLOCK);
        this.readyButton.setOverrideColour(ColourPalette.BASE_BLUE);
    }

    private void addText() {
        Text text = Text.newText(this.action.getName()).center().setFontSize(EntityInfoGui.FONT_SIZE).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, 0.0f, 1.0f);
    }

    private void addListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    ActionButtonGui.this.readyButton.setOverrideColour(MOUSE_OVER_COL);
                } else {
                    ActionButtonGui.this.readyButton.setOverrideColour(ColourPalette.BASE_BLUE);
                }
                if (event.isLeftClick()) {
                    ActionButtonGui.this.action.excecute();
                    ActionButtonGui.super.block(true);
                    ActionButtonGui.this.ready = false;
                }
            }
        });
    }

    private float getForgroundScaleX() {
        if (!this.action.isAffordable()) {
            return 0.0f;
        }
        return this.action.getProgression() * super.getScale().x;
    }
}


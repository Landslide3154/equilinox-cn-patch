/*
 * Decompiled with CFR 0.152.
 */
package colourSelector;

import colourSelector.ColourSelectorGui;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import userInterfaces.GuiImage;

public class SetColourUi
extends GuiClickable {
    private static final int BORDER = 2;
    private static final Colour BORDER_COL = ColourPalette.LIGHT_GREY;
    private GuiTexture background;
    private Colour colour;
    private final ColourSelectorGui mainUi;

    public SetColourUi(ColourSelectorGui mainUi, Colour colour) {
        super(1.0f);
        this.mainUi = mainUi;
        this.colour = colour;
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(BORDER_COL);
        this.addListener();
    }

    @Override
    protected void init() {
        super.init();
        this.addCenter();
    }

    private void addCenter() {
        GuiImage center = new GuiImage(GuiRepository.BLOCK);
        center.getTexture().setOverrideColour(this.colour);
        float borderWidth = super.pixelsToRelativeX(2.0f);
        float borderHeight = super.pixelsToRelativeY(2.0f);
        super.addComponent(center, borderWidth, borderHeight, 1.0f - 2.0f * borderWidth, 1.0f - 2.0f * borderHeight);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        if (super.isMouseOver()) {
            this.background.setOverrideColour(ColourPalette.WHITE);
        } else {
            this.background.setOverrideColour(BORDER_COL);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    private void addListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    SetColourUi.this.mainUi.setColour(SetColourUi.this.colour);
                }
            }
        });
    }
}


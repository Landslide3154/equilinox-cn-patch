/*
 * Decompiled with CFR 0.152.
 */
package evolutionUi;

import basics.DisplayManager;
import blueprints.Blueprint;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import evolutionUi.ChildState;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import speciesInformation.SpeciesInfoGui;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;

public class IconButtonUi
extends GuiClickable {
    private static final int BORDER = 2;
    private GuiTexture border;
    private GuiTexture background;
    private GuiTexture iconTexture;
    private ChildState state;

    public IconButtonUi(Blueprint species, ChildState state) {
        super(1.0f);
        this.state = state;
        Blueprint blueprint = species;
        this.initTextures(blueprint);
        this.addClickListener(blueprint);
    }

    public void changeState(ChildState state) {
        this.state = state;
        this.background.setOverrideColour(state.iconBackground);
        this.border.setOverrideColour(state.iconBorder);
        this.iconTexture.setOverrideColour(state.iconColour);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.border);
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.iconTexture);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        float borderWidth = 2.0f / ((float)DisplayManager.getUiWidth() * scale.x);
        float borderHeight = 2.0f / ((float)DisplayManager.getUiHeight() * scale.y);
        this.iconTexture.setPosition(position.x, position.y, scale.x, scale.y);
        this.border.setPosition(position.x, position.y, scale.x, scale.y);
        this.background.setPosition(position.x + borderWidth * scale.x, position.y + borderHeight * scale.y, (1.0f - borderWidth * 2.0f) * scale.x, (1.0f - borderHeight * 2.0f) * scale.y);
    }

    private void addClickListener(final Blueprint blueprint) {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    IconButtonUi.this.background.setOverrideColour(ColourPalette.BRIGHT_GREY);
                    IconButtonUi.this.border.setOverrideColour(ColourPalette.WHITE);
                } else if (event.isMouseOff()) {
                    IconButtonUi.this.background.setOverrideColour(((IconButtonUi)IconButtonUi.this).state.iconBackground);
                    IconButtonUi.this.border.setOverrideColour(((IconButtonUi)IconButtonUi.this).state.iconBorder);
                }
                if (event.isLeftClick()) {
                    SpeciesInfoGui.createSpeciesInfoGui(blueprint);
                    GameManager.getEntityPicker().clear();
                }
            }
        });
    }

    private void initTextures(Blueprint species) {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)species.getComponent(ComponentType.INFO);
        this.iconTexture = new GuiTexture(info.getIcon(), true);
        this.iconTexture.setOverrideColour(this.state.iconColour);
        this.background = new GuiTexture(GuiRepository.COOL);
        this.background.setOverrideColour(this.state.iconBackground);
        this.initBorder(species);
    }

    private void initBorder(Blueprint species) {
        this.border = new GuiTexture(GuiRepository.BLOCK);
        this.border.setOverrideColour(this.state.iconBorder);
    }
}


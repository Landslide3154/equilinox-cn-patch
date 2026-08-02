/*
 * Decompiled with CFR 0.152.
 */
package breedingTrees;

import audio.SoundMaestro;
import basics.DisplayManager;
import blueprints.Blueprint;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import org.lwjgl.util.vector.Vector2f;
import speciesInformation.SpeciesInfoGui;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;

public class GraphNodeGui
extends GuiClickable {
    private static final int BIG_BORDER = 3;
    private GuiTexture border;
    private GuiTexture background;
    private GuiTexture iconTexture;
    private boolean isBase;
    private boolean isHead;
    private int borderPixels;

    public GraphNodeGui(Blueprint species, boolean unlocked, boolean head) {
        super(head ? 1.0f : 1.15f);
        this.isHead = head;
        Blueprint blueprint = species;
        this.initTextures(unlocked, blueprint);
        this.addClickListener(unlocked, blueprint);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
    }

    @Override
    protected void setTextureClippingBounds(int[] bounds) {
        this.background.setClippingBounds(bounds);
        this.iconTexture.setClippingBounds(bounds);
        this.border.setClippingBounds(bounds);
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
        float borderWidth = (float)this.borderPixels / ((float)DisplayManager.getUiWidth() * scale.x);
        float borderHeight = (float)this.borderPixels / ((float)DisplayManager.getUiHeight() * scale.y);
        this.iconTexture.setPosition(position.x, position.y, scale.x, scale.y);
        this.border.setPosition(position.x, position.y, scale.x, scale.y);
        this.background.setPosition(position.x + borderWidth * scale.x, position.y + borderHeight * scale.y, (1.0f - borderWidth * 2.0f) * scale.x, (1.0f - borderHeight * 2.0f) * scale.y);
    }

    private void addClickListener(final boolean unlocked, final Blueprint blueprint) {
        if (!this.isHead) {
            super.addListener(new ClickListener(){

                @Override
                public void eventOccurred(GuiClickEvent event) {
                    if (event.isLeftClick()) {
                        if (unlocked) {
                            SpeciesInfoGui.createSpeciesInfoGui(blueprint);
                        } else {
                            SoundMaestro.playSystemSound(GuiSounds.NEGATIVE);
                        }
                    }
                }
            });
        }
    }

    private void initTextures(boolean unlocked, Blueprint species) {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)species.getComponent(ComponentType.INFO);
        this.iconTexture = new GuiTexture(info.getIcon(), true);
        this.background = new GuiTexture(GuiRepository.COOL);
        if (unlocked) {
            this.background.setOverrideColour(ColourPalette.DARKER_GREEN);
        } else {
            this.background.setOverrideColour(ColourPalette.LIGHT_GREY);
            this.iconTexture.setOverrideColour(ColourPalette.WHITE);
        }
        this.initBorder(species);
    }

    private void initBorder(Blueprint species) {
        this.border = new GuiTexture(GuiRepository.BLOCK);
        this.isBase = GameManager.BREED_TREES.isBaseSpecies(species);
        if (this.isBase) {
            this.border.setOverrideColour(ColourPalette.BASE_BLUE);
            this.borderPixels = 3;
        } else if (this.isHead) {
            this.border.setOverrideColour(ColourPalette.WHITE);
            this.borderPixels = 3;
        } else {
            this.border.setOverrideColour(ColourPalette.LIGHT_GREY);
            this.borderPixels = 1;
        }
    }
}


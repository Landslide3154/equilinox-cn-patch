/*
 * Decompiled with CFR 0.152.
 */
package traitGuis;

import breeding.BreedingComponent;
import breedingTraits.FloatTrait;
import breedingTraits.Trait;
import componentArchitecture.ComponentType;
import entityInfoGui.ComponentSwitchGui;
import entityInfoGui.EntityInfoGui;
import gameManaging.GameManager;
import geneticModificationUi.GeneticsPanelUi;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import health.LifeComponent;
import instances.Entity;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import session.GameMode;
import text3D.TraitDisplayOptionUi;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickableGroup;
import userInterfaces.Listener;
import userInterfaces.Tab2ButtonUi;

public class TraitsPanelGui
extends GuiComponent {
    private static final float TRAIT_DISPLAY_WIDTH = 0.75f;
    private static final float GENETICS_X = 0.7f;
    private static final float GENETICS_WIDTH = 0.3f;
    private static final float DISPLAY_BUTTON_X = 0.6f;
    private static final String SELECT_BREED = GameText.getText(175);
    private List<Trait> traits;
    private float yGap;
    private BreedingComponent breedComponent;
    private EntityInfoGui gui;
    private int lineCount;
    private GeneticsPanelUi geneticsPanel;
    private final Entity entity;
    private final GuiClickableGroup group = new GuiClickableGroup();

    public TraitsPanelGui(Entity entity, List<Trait> traits, int numberOfLines, EntityInfoGui gui) {
        this.traits = traits;
        this.lineCount = numberOfLines;
        this.yGap = 1.0f / (float)numberOfLines;
        this.gui = gui;
        this.entity = entity;
        LifeComponent lifeInfo = (LifeComponent)entity.getComponent(ComponentType.LIFE);
        this.breedComponent = lifeInfo.getBreedComponent();
    }

    @Override
    protected void init() {
        float yPos = 0.0f;
        for (Trait trait : this.traits) {
            super.addComponent(trait.getInfo(), 0.0f, yPos, 0.75f, this.yGap);
            if (trait instanceof FloatTrait) {
                this.addDisplayButton(yPos, this.yGap, trait);
            }
            yPos += this.yGap;
        }
        if (GameManager.getGameMode() != GameMode.BUILD) {
            this.addBreedSwitch(yPos);
        }
        this.geneticsPanel = new GeneticsPanelUi(this.gui, this.traits, this.yGap, this.lineCount);
        super.addComponent(this.geneticsPanel, 0.7f, 0.0f, 0.3f, 1.0f);
        if (GameManager.getGameMode() != GameMode.BUILD && !this.breedComponent.isBreedingBoosted()) {
            this.geneticsPanel.block(true);
        }
    }

    private void addDisplayButton(float yPos, float yGap, final Trait trait) {
        Tab2ButtonUi button = new Tab2ButtonUi(GuiRepository.DISPLAY_OFF, GuiRepository.DISPLAY_ON, ColourPalette.WHITE, true);
        button.setPreferredPixelSize(18);
        if (this.shouldBeSelected(trait)) {
            this.group.addButton(button, true);
        } else {
            this.group.addButton(button);
        }
        float buttonHeight = super.pixelsToRelativeY(18.0f);
        super.addPixelComp(button, 0.6f, yPos + (yGap - buttonHeight) * 0.5f);
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    EquilinoxGuis.showTraitDisplayOption(TraitsPanelGui.this.entity.getBlueprint(), trait.blueprint.getComponmentType(), trait.blueprint.getIndex());
                } else if (event.isToggleOff() && TraitsPanelGui.this.group.areAllOff()) {
                    EquilinoxGuis.hideTraitDisplayOption();
                }
            }
        });
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

    @Override
    public void remove() {
        this.gui.removeSecondPanel();
        super.remove();
    }

    private void addBreedSwitch(float yPos) {
        ComponentSwitchGui breedSwitch = new ComponentSwitchGui(SELECT_BREED, EntityInfoGui.FONT_SIZE, this.breedComponent.isBreedingBoosted(), new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                TraitsPanelGui.this.breedComponent.setBreedingBoost(on);
                TraitsPanelGui.this.geneticsPanel.block(!on);
            }
        });
        super.addComponent(breedSwitch, 0.0f, (float)(this.lineCount - 1) * this.yGap, 0.75f, this.yGap);
    }

    private boolean shouldBeSelected(Trait trait) {
        TraitDisplayOptionUi traitOption = EquilinoxGuis.getTraitDisplayOption();
        if (traitOption == null) {
            return false;
        }
        if (this.entity.getBlueprint() != traitOption.getSpecies()) {
            return false;
        }
        if (trait.blueprint.getComponmentType() != traitOption.getTraitType()) {
            return false;
        }
        return trait.blueprint.getIndex() == traitOption.getTraitIndex();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package evolutionUi;

import blueprints.Blueprint;
import breeding.BreedingComponent;
import breeding.EvolveProcess;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityInfoGui.EntityInfoGui;
import evolutionUi.ChildState;
import evolutionUi.EvolutionUi;
import evolutionUi.EvolveStatusUi;
import evolutionUi.IconButtonUi;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import instances.Entity;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickableGroup;
import userInterfaces.GuiImage;
import userInterfaces.Tab2ButtonUi;

public class ChooseSpeciesUi
extends GuiComponent {
    private static final float ICON_HEIGHT = 0.9f;
    private static final float TEXT_X = 0.18f;
    private static final float TEXT_Y = 0.2f;
    private static final float BUTTON_X = 0.9f;
    private final BreedingComponent breedComp;
    private final Entity entity;
    private final Blueprint childSpecies;
    private final int numberOfLines;
    private final InformationComponent.InformationCompBlueprint info;
    private final GuiClickableGroup tabGroup;
    private final EntityInfoGui mainUi;
    private final EvolutionUi evolveUi;
    private IconButtonUi iconButton;
    private Text nameText;
    private Text status;
    private Tab2ButtonUi tab;
    private ChildState state;

    protected ChooseSpeciesUi(Entity entity, Blueprint species, BreedingComponent breedComp, GuiClickableGroup group, int numberOfLines, EntityInfoGui mainUi, EvolutionUi evolveUi) {
        this.entity = entity;
        this.childSpecies = species;
        this.breedComp = breedComp;
        this.evolveUi = evolveUi;
        this.tabGroup = group;
        this.numberOfLines = numberOfLines;
        this.mainUi = mainUi;
        this.info = (InformationComponent.InformationCompBlueprint)this.childSpecies.getComponent(ComponentType.INFO);
    }

    @Override
    protected void init() {
        super.init();
        this.determineState();
        this.addIcon();
        this.addName(this.info.getName());
        if (this.state != ChildState.UNLOCKED) {
            this.addButton();
        } else {
            this.addTick();
        }
        this.addInfo(this.info);
    }

    public boolean isStateNormal() {
        return this.state == ChildState.NORMAL;
    }

    public boolean isStateBlocked() {
        return this.state == ChildState.BLOCKED;
    }

    public void changeState(ChildState state) {
        this.state = state;
        this.iconButton.changeState(state);
        this.status.setColour(state.statusColour);
        this.status.setText(state.getStatusString(this.info, this.getPercent()));
        state.updateTab(this.tab);
        this.nameText.setColour(state.getNameColour(this.breedComp.checkRequirementsMet(this.childSpecies)));
        if (state == ChildState.UNLOCKED) {
            this.tab.remove();
            this.addTick();
        }
    }

    public void toggleButton() {
        this.tab.toggle();
    }

    private void determineState() {
        this.state = GameManager.getSession().getStats().getLockStatus().isUnlocked(this.childSpecies) ? ChildState.UNLOCKED : (this.breedComp.getEvolveProcess() != null ? (this.breedComp.getEvolveProcess().getChildSpecies() == this.childSpecies ? ChildState.IN_PROGRESS : ChildState.BLOCKED) : (GameManager.getEvolvingStatus().getProcess(this.childSpecies) != null ? (GameManager.getEvolvingStatus().getProcess(this.childSpecies).isActive() ? ChildState.ELSEWHERE : ChildState.PAUSED) : ChildState.NORMAL));
    }

    private void addIcon() {
        this.iconButton = new IconButtonUi(this.childSpecies, this.state);
        float posX = super.pixelsToRelativeX(8.0f);
        super.addComponentY(this.iconButton, posX, 0.0f, 0.9f);
    }

    private void addName(String name) {
        this.nameText = Text.newText(name).setFontSize(UiSettings.LARGE_FONT).create();
        this.nameText.setColour(this.state.getNameColour(this.breedComp.checkRequirementsMet(this.childSpecies)));
        super.addText(this.nameText, 0.18f, 0.2f, 1.0f);
    }

    private void addTick() {
        GuiImage image = new GuiImage(GuiRepository.TICK_ICON);
        image.setPreferredPixelSize(20);
        image.getTexture().setOverrideColour(ColourPalette.DARKER_GREEN);
        super.addPixelCompCenterY(image, 0.9f, 0.5f);
    }

    private void addInfo(InformationComponent.InformationCompBlueprint info) {
        this.status = Text.newText(this.state.getStatusString(info, this.getPercent())).rightAlign().setFontSize(UiSettings.NORM_FONT).create();
        this.status.setColour(this.state.statusColour);
        super.addText(this.status, 0.0f, 0.2f, 0.84999996f);
    }

    private void addButton() {
        this.tab = new Tab2ButtonUi(GuiRepository.ARROW_OFF, GuiRepository.ARROW_ON);
        this.tab.setPreferredPixelSize(18);
        super.addPixelCompCenterY(this.tab, 0.9f, 0.5f);
        this.tabGroup.addButton(this.tab);
        this.addListener(this.tab, this.childSpecies, 10);
        if (this.state == ChildState.IN_PROGRESS) {
            this.tab.toggle();
        }
        this.state.updateTab(this.tab);
    }

    private void addListener(Tab2ButtonUi button, final Blueprint child, int cost) {
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    ChooseSpeciesUi.this.mainUi.showSecondPanel(new EvolveStatusUi(ChooseSpeciesUi.this.entity, child, ChooseSpeciesUi.this.breedComp, 1.0f / (float)ChooseSpeciesUi.this.numberOfLines, ChooseSpeciesUi.this, ChooseSpeciesUi.this.evolveUi));
                } else if (event.isToggleOff()) {
                    ChooseSpeciesUi.this.mainUi.removeSecondPanel();
                }
            }
        });
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        this.status.setText(this.state.getStatusString(this.info, this.getPercent()));
        if (this.state == ChildState.ELSEWHERE && GameManager.getSession().getStats().getLockStatus().isUnlocked(this.childSpecies)) {
            if (this.tab.isToggledOn()) {
                this.tab.toggle();
            }
            this.changeState(ChildState.UNLOCKED);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private int getPercent() {
        EvolveProcess process = GameManager.getEvolvingStatus().getProcess(this.childSpecies);
        int percent = 0;
        if (process != null) {
            percent = (int)(process.getCurrentPoints() / process.getRequiredPoints() * 100.0f);
        }
        return percent;
    }
}


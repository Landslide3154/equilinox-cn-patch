/*
 * Decompiled with CFR 0.152.
 */
package evolutionUi;

import audio.SoundMaestro;
import blueprints.Blueprint;
import breeding.BreedingComponent;
import breeding.EvolveProcess;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import evolutionUi.ChildState;
import evolutionUi.ChooseSpeciesUi;
import evolutionUi.EvolutionUi;
import evolutionUi.IconButtonUi;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Maths;
import usefulUis.PaddedPanelUi;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.LightButtonUi;
import userInterfaces.ProgressBarUi;
import userInterfaces.TextButtonUi;

public class EvolveProgressUi
extends GuiComponent {
    private static final String START_BREED = GameText.getText(685);
    private static final String REQS_NOT_MET = GameText.getText(686);
    private static final String NO_MONEY = GameText.getText(687);
    private static final String TAKE_OVER = GameText.getText(1042);
    private static final String CONTINUE = GameText.getText(1043);
    private static final float GAP = 0.035f;
    private static final float BAR_GAP_END = 0.0f;
    private static final float BAR_Y = 0.55f;
    private ProgressBarUi bar;
    private TextButtonUi textButton;
    private LightButtonUi closeButton;
    private final InformationComponent.InformationCompBlueprint info;
    private final Blueprint childSpecies;
    private final BreedingComponent breedComp;
    private final boolean reqsMet;
    private final EvolutionUi evolveUi;
    private final ChooseSpeciesUi chooseSpeciesUi;
    private final PaddedPanelUi display;
    private EvolveProcess evolveProcess;

    protected EvolveProgressUi(Blueprint child, BreedingComponent breedComp, boolean reqsMet, ChooseSpeciesUi chooseSpeciesUi, EvolutionUi evolveUi, PaddedPanelUi display) {
        this.breedComp = breedComp;
        this.display = display;
        this.childSpecies = child;
        this.evolveUi = evolveUi;
        this.reqsMet = reqsMet;
        this.chooseSpeciesUi = chooseSpeciesUi;
        this.info = (InformationComponent.InformationCompBlueprint)this.childSpecies.getComponent(ComponentType.INFO);
    }

    @Override
    protected void init() {
        super.init();
        float middleX = super.getRelativeWidthCoords(1.0f) + 0.035f;
        this.addIcon();
        this.addName(middleX);
        this.evolveProcess = this.breedComp.getEvolveProcess();
        if (this.evolveProcess != null) {
            this.addProgressBar(middleX);
        } else {
            this.addStartButton(middleX);
        }
        this.addCloseButton();
    }

    private void addIcon() {
        IconButtonUi iconButton = new IconButtonUi(this.childSpecies, ChildState.NORMAL);
        super.addComponentY(iconButton, 0.0f, 0.0f, 1.0f);
    }

    private void addCloseButton() {
        this.closeButton = new LightButtonUi(GuiRepository.EVOLVE_PAUSE, ColourPalette.LOCKED_BACKGROUND);
        this.closeButton.setPreferredPixelSize(17);
        this.closeButton.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    EvolveProgressUi.this.breedComp.pauseBreedProcess();
                    EvolveProgressUi.this.chooseSpeciesUi.changeState(ChildState.PAUSED);
                    EvolveProgressUi.this.evolveUi.notifyCompletion(false);
                    EvolveProgressUi.this.bar.remove();
                    EvolveProgressUi.this.bar = null;
                    EvolveProgressUi.this.closeButton.block(true);
                    EvolveProgressUi.this.addStartButton(EvolveProgressUi.this.getRelativeWidthCoords(1.0f) + 0.035f);
                }
            }
        });
        super.addPixelComp(this.closeButton, 1.0f - super.pixelsToRelativeX(17.0f), 0.0f);
        if (this.evolveProcess == null) {
            this.closeButton.block(true);
        }
    }

    private void addName(float middle) {
        Text text = Text.newText(this.info.getName()).setFontSize(UiSettings.LARGE_FONT).create();
        text.setColour(ColourPalette.BEIGE);
        super.addText(text, middle, 0.0f, 1.0f);
    }

    private void addStartButton(final float middleX) {
        this.textButton = new TextButtonUi(String.valueOf(START_BREED) + " (" + Maths.formatNumber(this.info.getPrice()) + " dp)", ColourPalette.BASE_BLUE, UiSettings.NORM_FONT, ColourPalette.WHITE, -0.01f);
        this.textButton.setBlockColours(ColourPalette.MIDDLE_GREY, ColourPalette.LIGHT_GREY);
        super.addComponent(this.textButton, middleX, 0.55f, 1.0f - (middleX + 0.0f), 0.45f);
        this.textButton.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    EvolveProgressUi.this.textButton.remove();
                    if (GameManager.getEvolvingStatus().getProcess(EvolveProgressUi.this.childSpecies) == null) {
                        SoundMaestro.playSystemSound(GuiSounds.CASH);
                        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)EvolveProgressUi.this.childSpecies.getComponent(ComponentType.INFO);
                        GameManager.getSession().getStats().increaseDp(-info.getPrice());
                    } else {
                        SoundMaestro.playSystemSound(GuiSounds.SELECT);
                    }
                    EvolveProgressUi.this.evolveProcess = EvolveProgressUi.this.breedComp.setBreedProcess(EvolveProgressUi.this.childSpecies);
                    EvolveProgressUi.this.addProgressBar(middleX);
                    EvolveProgressUi.this.chooseSpeciesUi.changeState(ChildState.IN_PROGRESS);
                    EvolveProgressUi.this.evolveUi.blockAvailableChoices();
                    EvolveProgressUi.this.closeButton.block(false);
                    EvolveProgressUi.this.display.setColour(ColourPalette.GOLD);
                }
            }
        });
    }

    private void addProgressBar(float middleX) {
        this.bar = new ProgressBarUi(this.evolveProcess.getCurrentPoints() / this.evolveProcess.getRequiredPoints());
        this.bar.flashArrows(this.reqsMet);
        this.bar.showCountingText((int)this.evolveProcess.getRequiredPoints(), ColourPalette.WHITE, UiSettings.NORM_FONT * 0.8f, 0.1f);
        this.bar.setBarColour(ColourPalette.GREEN);
        super.addComponent(this.bar, middleX, 0.55f, 1.0f - (middleX + 0.0f), 0.45f);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        boolean reqsMet = this.breedComp.checkRequirementsMet(this.childSpecies);
        this.display.setColour(reqsMet ? ColourPalette.GOLD : ColourPalette.LIGHT_GREY);
        if (this.bar != null) {
            this.updateProgressBar(reqsMet);
        } else {
            this.updateButton(reqsMet);
        }
    }

    private void updateButton(boolean reqsMet) {
        EvolveProcess currentProcess = GameManager.getEvolvingStatus().getProcess(this.childSpecies);
        if (!reqsMet) {
            this.textButton.block(true);
            this.textButton.setText(REQS_NOT_MET);
        } else {
            String buttonText;
            if (currentProcess == null) {
                if (this.info.getPrice() > GameManager.getSession().getStats().getDpCount()) {
                    this.textButton.block(true);
                    buttonText = NO_MONEY;
                } else {
                    buttonText = String.valueOf(START_BREED) + " (" + Maths.formatNumber(this.info.getPrice()) + " dp)";
                    this.textButton.block(false);
                }
            } else if (currentProcess.isActive()) {
                buttonText = TAKE_OVER;
                this.textButton.block(false);
            } else {
                buttonText = CONTINUE;
                this.textButton.block(false);
            }
            this.textButton.setText(buttonText);
        }
    }

    private void updateProgressBar(boolean reqsMet) {
        this.bar.flashArrows(reqsMet);
        float progress = this.evolveProcess.getCurrentPoints();
        if (this.evolveProcess.isComplete()) {
            this.chooseSpeciesUi.changeState(ChildState.UNLOCKED);
            this.evolveUi.notifyCompletion(true);
        }
        this.bar.setProgress(progress / this.evolveProcess.getRequiredPoints());
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }
}


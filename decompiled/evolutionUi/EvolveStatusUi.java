/*
 * Decompiled with CFR 0.152.
 */
package evolutionUi;

import blueprints.Blueprint;
import breeding.BreedingComponent;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.EntityPopUpPanel;
import evolutionUi.ChooseSpeciesUi;
import evolutionUi.EvolutionUi;
import evolutionUi.EvolveProgressUi;
import evolutionUi.EvolveRequirementUi;
import guiRendering.GuiRenderData;
import health.LifeCompBlueprint;
import instances.Entity;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import usefulUis.PaddedPanelUi;

public class EvolveStatusUi
extends EntityPopUpPanel {
    public static final float REQ_GAP = 0.08f;
    public static final float STATUS_HEIGHT = 2.3f;
    public static final float GAP = 0.2f;
    protected static final float ALPHA = 0.2f;
    private static final int PADDING = 4;
    private final Blueprint childSpecies;
    private final float yGap;
    private final BreedingComponent breedComp;
    private final Entity parentEntity;
    private final ChooseSpeciesUi chooseSpeciesUi;
    private final EvolutionUi evolveUi;

    protected EvolveStatusUi(Entity parentEntity, Blueprint breedSpecies, BreedingComponent breedComp, float yGap, ChooseSpeciesUi chooseSpeciesUi, EvolutionUi evolveUi) {
        super(ColourPalette.DARK_GREY, 0.75f);
        super.setBlurry();
        this.evolveUi = evolveUi;
        this.chooseSpeciesUi = chooseSpeciesUi;
        this.parentEntity = parentEntity;
        this.childSpecies = breedSpecies;
        this.breedComp = breedComp;
        this.yGap = yGap;
    }

    @Override
    public float getMaxY() {
        return 0.0f;
    }

    @Override
    public float getMinY() {
        return 0.0f;
    }

    @Override
    public void addToParentPanel(EntityInfoGui parentPanel) {
        float pad = 5.0f / parentPanel.getPixelWidth();
        float titleHeight = parentPanel.getTitleBarHeight();
        parentPanel.addComponent(this, 1.0f + pad, titleHeight, 1.0f, 1.0f - titleHeight);
    }

    @Override
    protected void init() {
        super.init();
        this.addProgressDisplay();
        LifeCompBlueprint lifeInfo = (LifeCompBlueprint)this.childSpecies.getComponent(ComponentType.LIFE);
        float yPos = this.yGap * 2.3f + super.pixelsToRelativeY(4.0f) + super.pixelsToRelativeY(4.0f);
        for (Requirement req : lifeInfo.breedInfo.getRequirements()) {
            this.addRequirementUi(req, yPos);
            yPos += this.yGap * 1.08f;
        }
    }

    private void addProgressDisplay() {
        boolean reqsMet = this.breedComp.checkRequirementsMet(this.childSpecies);
        PaddedPanelUi display = new PaddedPanelUi(this.breedComp.getEvolveProcess() != null && reqsMet ? ColourPalette.GOLD : ColourPalette.LIGHT_GREY, 0.2f);
        display.displayComponent(new EvolveProgressUi(this.childSpecies, this.breedComp, reqsMet, this.chooseSpeciesUi, this.evolveUi, display));
        display.setPadding(4);
        float padX = super.pixelsToRelativeX(4.0f);
        float padY = super.pixelsToRelativeY(4.0f);
        super.addComponent(display, padX, padY, 1.0f - 2.0f * padX, this.yGap * 2.3f);
    }

    private void addRequirementUi(Requirement req, float yPos) {
        PaddedPanelUi reqDisplay = new PaddedPanelUi(ColourPalette.LIGHT_GREY, 0.2f);
        reqDisplay.setPadding(4, 0, 0, 0);
        reqDisplay.displayComponent(new EvolveRequirementUi(req, this.parentEntity, reqDisplay));
        float padX = super.pixelsToRelativeX(4.0f);
        super.addComponent(reqDisplay, padX, yPos, 1.0f - 2.0f * padX, this.yGap);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        super.getGuiTextures(data);
    }
}


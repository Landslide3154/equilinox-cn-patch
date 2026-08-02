/*
 * Decompiled with CFR 0.152.
 */
package evolveStatusOverview;

import breeding.EvolveProcess;
import breedingTrees.Node;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import evolveStatusOverview.EvolveOverviewUi;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import userInterfaces.GuiImage;

public class EvolveOverviewTopUi
extends GuiComponent {
    private static final String UNSTARTED = GameText.getText(133);
    private static final String PAUSED = GameText.getText(1041);
    private static final String IN_PROGRESS = GameText.getText(688);
    private static final String UNLOCKED = GameText.getText(689);
    private static final String EVOLVES_FROM = GameText.getText(1064);
    public static final int PAD = 5;
    private final Node speciesNode;
    private Text progressText;
    private EvolveProcess process;

    public EvolveOverviewTopUi(Node speciesNode) {
        this.initTextures();
        this.speciesNode = speciesNode;
    }

    @Override
    protected void init() {
        super.init();
        this.addIcon();
        float xStart = super.getRelativeWidthCoords(1.0f) + super.pixelsToRelativeX(5.0f);
        float yPos = super.pixelsToRelativeY(2.0f);
        float gap = super.pixelsToRelativeY(EvolveOverviewUi.TEXT_HEIGHT);
        this.addName(xStart, yPos);
        this.addParentText(xStart, yPos += gap);
        this.addProgressText(xStart, yPos += gap);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        if (this.process == null) {
            this.process = GameManager.getEvolvingStatus().getProcess(this.speciesNode.species);
        }
        this.progressText.setText(this.getProgressString(this.process));
        this.progressText.setColour(this.getProgressColour(this.process));
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void initTextures() {
    }

    private void addName(float xStart, float yPos) {
        Text name = Text.newText(this.speciesNode.species.getName()).setFontSize(UiSettings.NORM_FONT).create();
        name.setColour(ColourPalette.WHITE);
        super.addText(name, xStart, yPos, 1.0f);
    }

    private void addParentText(float xStart, float yPos) {
        String parentName = String.valueOf(EVOLVES_FROM) + " " + this.speciesNode.parent.species.getName();
        Text text = Text.newText(parentName).setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.LIGHT_GREY);
        super.addText(text, xStart, yPos, 1.0f);
    }

    private void addProgressText(float xStart, float yPos) {
        EvolveProcess process = GameManager.getEvolvingStatus().getProcess(this.speciesNode.species);
        this.progressText = Text.newText(this.getProgressString(process)).setFontSize(UiSettings.NORM_FONT).create();
        this.progressText.setColour(this.getProgressColour(process));
        super.addText(this.progressText, xStart, yPos, 1.0f);
    }

    private String getProgressString(EvolveProcess process) {
        if (process == null) {
            return UNSTARTED;
        }
        if (process.isComplete()) {
            return UNLOCKED;
        }
        if (!process.isActive()) {
            return String.valueOf(PAUSED) + ": " + (int)(process.getProgressFactor() * 100.0f) + "%";
        }
        return String.valueOf(IN_PROGRESS) + ": " + (int)(process.getProgressFactor() * 100.0f) + "%";
    }

    private Colour getProgressColour(EvolveProcess process) {
        if (process == null) {
            return ColourPalette.BEIGE;
        }
        if (process.isComplete()) {
            return ColourPalette.GOLD;
        }
        if (!process.isActive()) {
            return ColourPalette.BRIGHT_RED;
        }
        return ColourPalette.BASE_BLUE;
    }

    private void addIcon() {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)this.speciesNode.species.getComponent(ComponentType.INFO);
        Texture icon = info.getIcon();
        GuiImage image = new GuiImage(icon);
        image.getTexture().flip(true);
        float xStart = 2.0f / super.getPixelWidth();
        float yStart = 2.0f / super.getPixelHeight();
        float yScale = 1.0f - 2.0f * yStart;
        float xScale = super.getRelativeWidthCoords(yScale);
        GuiImage back = new GuiImage(GuiRepository.COOL);
        back.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
        super.addComponent(back, xStart, yStart, xScale, yScale);
        super.addComponent(image, xStart, yStart, xScale, yScale);
    }
}


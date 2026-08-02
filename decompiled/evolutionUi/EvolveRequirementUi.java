/*
 * Decompiled with CFR 0.152.
 */
package evolutionUi;

import breedingTrees.ReqInfo;
import componentArchitecture.Requirement;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import instances.Entity;
import java.util.ArrayList;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import usefulUis.PaddedPanelUi;
import userInterfaces.GuiImage;

public class EvolveRequirementUi
extends GuiComponent {
    private static final float REQ_X = 0.19f;
    private static final float REQ_Y = 0.0f;
    private final Requirement requirement;
    private final Entity parent;
    private final PaddedPanelUi panel;
    private GuiImage tickImage;
    private GuiImage emptyImage;

    protected EvolveRequirementUi(Requirement requirement, Entity parent, PaddedPanelUi panel) {
        this.requirement = requirement;
        this.parent = parent;
        this.panel = panel;
    }

    @Override
    protected void init() {
        super.init();
        boolean complete = this.requirement.check(this.parent);
        this.panel.setColour(complete ? ColourPalette.GOLD : ColourPalette.LIGHT_GREY);
        ArrayList<ReqInfo> reqUi = new ArrayList<ReqInfo>();
        this.requirement.getGuiInfo(reqUi);
        this.addRequirement((ReqInfo)reqUi.get(0), 0.0f);
        this.addIcon(complete);
    }

    private void addIcon(boolean complete) {
        this.tickImage = new GuiImage(GuiRepository.CIRCLE_TICK);
        this.tickImage.setPreferredPixelSize(20);
        this.tickImage.getTexture().setOverrideColour(ColourPalette.GREEN);
        super.addPixelComp(this.tickImage, 0.0f, super.pixelsToRelativeY(1.0f));
        this.tickImage.show(complete);
        this.emptyImage = new GuiImage(GuiRepository.CIRCLE_TICK_EMPTY);
        this.emptyImage.setPreferredPixelSize(20);
        this.emptyImage.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
        super.addPixelComp(this.emptyImage, 0.0f, super.pixelsToRelativeY(1.0f));
        this.emptyImage.show(!complete);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        boolean complete = this.requirement.check(this.parent);
        this.panel.setColour(complete ? ColourPalette.GOLD : ColourPalette.LIGHT_GREY);
        this.tickImage.show(complete);
        this.emptyImage.show(!complete);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addRequirement(ReqInfo info, float yPos) {
        Text nameText = Text.newText(info.name).setFontSize(UiSettings.NORM_FONT).create();
        nameText.setColour(ColourPalette.WHITE);
        super.addText(nameText, 0.19f, yPos, 1.0f);
        Text dash = Text.newText("-").center().setFontSize(UiSettings.NORM_FONT).create();
        dash.setColour(info.valueColour);
        super.addText(dash, 0.0f, yPos, 1.0f);
        Text valueText = Text.newText(info.value).setFontSize(UiSettings.NORM_FONT).create();
        valueText.setColour(info.valueColour);
        super.addText(valueText, 0.6f, yPos, 1.0f);
    }
}


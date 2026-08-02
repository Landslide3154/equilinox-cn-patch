/*
 * Decompiled with CFR 0.152.
 */
package evolveStatusOverview;

import breedingTrees.Node;
import breedingTrees.ReqInfo;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import evolveStatusOverview.EvolveOverviewTopUi;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import health.LifeCompBlueprint;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.GuiImage;

public class EvolveOverviewUi
extends GuiComponent {
    public static final int BORDER = 2;
    private static int TOP_HEIGHT_PIXELS = 70;
    public static int TEXT_HEIGHT = 21;
    private final Node speciesNode;
    private List<Requirement> reqs;
    private GuiTexture background;

    public EvolveOverviewUi(Node speciesNode) {
        this.speciesNode = speciesNode;
        LifeCompBlueprint life = (LifeCompBlueprint)speciesNode.species.getComponent(ComponentType.LIFE);
        this.reqs = life.breedInfo.getRequirements();
        this.initTextures();
    }

    public int getRequirementCount() {
        return this.reqs.size();
    }

    public int getHeightInPixels() {
        int total = TOP_HEIGHT_PIXELS + 10;
        return total += this.reqs.size() * TEXT_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();
        EvolveOverviewTopUi topSection = new EvolveOverviewTopUi(this.speciesNode);
        super.addComponent(topSection, 0.0f, 0.0f, 1.0f, (float)TOP_HEIGHT_PIXELS / super.getPixelHeight());
        this.addBackgroundSquare();
        this.addReqs();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        this.background.update();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    @Override
    protected void setTextureClippingBounds(int[] bounds) {
        this.background.setClippingBounds(bounds);
    }

    private void initTextures() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.DARK_GREY.duplicate().scale(1.3f));
    }

    private void addBackgroundSquare() {
        GuiImage image = new GuiImage(GuiRepository.BLOCK);
        image.getTexture().setOverrideColour(ColourPalette.DARK_GREY.duplicate().scale(1.7f));
        float xPos = super.pixelsToRelativeX(2.0f);
        float yPos = super.pixelsToRelativeY(TOP_HEIGHT_PIXELS);
        float xScale = 1.0f - 2.0f * xPos;
        float yScale = 1.0f - (yPos + super.pixelsToRelativeY(2.0f));
        super.addComponent(image, xPos, yPos, xScale, yScale);
    }

    private void addReqs() {
        ArrayList<ReqInfo> infos = new ArrayList<ReqInfo>();
        for (Requirement req : this.reqs) {
            req.getGuiInfo(infos);
        }
        float yPos = super.pixelsToRelativeY(TOP_HEIGHT_PIXELS + 5);
        for (ReqInfo info : infos) {
            this.addRequirement(info, yPos);
            yPos += super.pixelsToRelativeY(TEXT_HEIGHT);
        }
    }

    private void addRequirement(ReqInfo info, float yPos) {
        Text nameText = Text.newText(info.name).setFontSize(UiSettings.NORM_FONT).create();
        nameText.setColour(ColourPalette.WHITE);
        super.addText(nameText, super.pixelsToRelativeX(TOP_HEIGHT_PIXELS + 5), yPos, 1.0f);
        Text dash = Text.newText("-").center().setFontSize(UiSettings.NORM_FONT).create();
        dash.setColour(info.valueColour);
        super.addText(dash, 0.0f, yPos, 1.0f);
        Text valueText = Text.newText(info.value).setFontSize(UiSettings.NORM_FONT).create();
        valueText.setColour(info.valueColour);
        super.addText(valueText, 0.6f, yPos, 1.0f);
    }
}


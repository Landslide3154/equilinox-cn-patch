/*
 * Decompiled with CFR 0.152.
 */
package speciesInformation;

import basics.DisplayManager;
import blueprints.Blueprint;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import extraInfoGui.ExtraInfoContent;
import extraInfoGui.ExtraToolbarGui;
import guiRendering.GuiRenderData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import profile3d.Profile3D;
import speciesInformation.SpeciesDescriptionGui;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import speciesInformation.StatsGui;
import textures.Texture;
import userInterfaces.BorderPanelGui;
import userInterfaces.TabContent;
import userInterfaces.TabPanelUi;

public class InfoPanelGui
extends ExtraInfoContent {
    private static final float X_PADDING = ExtraToolbarGui.X_START;
    private static final float ICON_DESC_PAD = 0.02f;
    private static final float Y_PADDING = 0.035f;
    private static final float TOP_SECTION_HEIGHT = 0.25f;
    private static final float BOTTOM_SECTION_HEIGHT = 0.645f;
    private static final float SECTION_WIDTH = 1.0f - 2.0f * X_PADDING;
    private static final float BOTTOM_START_Y = 0.32f;
    private Blueprint blueprint;

    protected InfoPanelGui(Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    protected void init() {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)this.blueprint.getComponent(ComponentType.INFO);
        if (!DisplayManager.isMinitureWidth()) {
            this.createIcon(info.getIcon());
        }
        this.addDescription(info.getDescription());
        this.addStats(this.blueprint);
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

    private void createIcon(Texture iconTexture) {
        Profile3D icon = new Profile3D(this.blueprint);
        BorderPanelGui panel = new BorderPanelGui(1, ColourPalette.BEIGE);
        panel.addComponent(icon, 0.0f, 0.0f, 1.0f, 1.0f);
        super.addComponentY(panel, X_PADDING, 0.035f, 0.25f);
    }

    private void addDescription(String description) {
        float iconWidth = super.getRelativeWidthCoords(0.25f);
        float descX = X_PADDING + (DisplayManager.isMinitureWidth() ? 0.0f : iconWidth + 0.02f);
        float descWidth = 1.0f - (descX + X_PADDING);
        SpeciesDescriptionGui descGui = new SpeciesDescriptionGui(description);
        super.addComponent(descGui, descX, 0.035f, descWidth, 0.25f);
    }

    private void addStats(Blueprint blueprint) {
        TabContent[] contents = this.createContents(blueprint.getInfo());
        TabPanelUi panel = new TabPanelUi(contents);
        super.addComponent(panel, X_PADDING, 0.32f, SECTION_WIDTH, 0.645f);
    }

    private TabContent[] createContents(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        ArrayList<TabContent> contents = new ArrayList<TabContent>(info.size());
        for (Map.Entry<SpeciesInfoType, List<SpeciesInfoLine>> entry : info.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            contents.add(this.createContent(entry));
        }
        return contents.toArray(new TabContent[contents.size()]);
    }

    private TabContent createContent(Map.Entry<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        List<SpeciesInfoLine> list = info.getValue();
        StatsGui stats = new StatsGui(list, info.getKey() != SpeciesInfoType.ABILITIES);
        return new TabContent(info.getKey().getName(), stats);
    }
}


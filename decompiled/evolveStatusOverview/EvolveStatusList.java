/*
 * Decompiled with CFR 0.152.
 */
package evolveStatusOverview;

import blueprints.Blueprint;
import breeding.EvolveProcess;
import breedingTrees.Node;
import componentArchitecture.ComponentType;
import evolveStatusOverview.EvolveOverviewUi;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import health.LifeCompBlueprint;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.lwjgl.util.vector.Vector2f;
import resourceManagement.BlueprintRepository;

public class EvolveStatusList
extends GuiComponent {
    private static final int GAP = 12;
    private static final int TOP_PAD = 10;
    private static final int SIDE_PAD = 10;
    private List<EvolveOverviewUi> evolveOverviews = new ArrayList<EvolveOverviewUi>();
    private int pixelHeight = 0;

    public EvolveStatusList() {
        List<Node> nodes = this.getListOfSpecies();
        this.pixelHeight = 10;
        for (Node node : nodes) {
            this.createOverview(node.species);
            this.pixelHeight += 12;
        }
    }

    public int getHeightInPixels() {
        return this.pixelHeight;
    }

    @Override
    protected void init() {
        super.init();
        float yPos = super.pixelsToRelativeY(10.0f);
        for (EvolveOverviewUi overview : this.evolveOverviews) {
            yPos = this.addEvolveOverview(overview, yPos);
            yPos += super.pixelsToRelativeY(12.0f);
        }
    }

    private void createOverview(Blueprint species) {
        Node node = GameManager.BREED_TREES.getNode(species);
        EvolveProcess process = GameManager.getEvolvingStatus().getProcess(species);
        EvolveOverviewUi overview = new EvolveOverviewUi(node);
        this.pixelHeight += overview.getHeightInPixels();
        if (process == null) {
            this.evolveOverviews.add(overview);
        } else {
            this.evolveOverviews.add(0, overview);
        }
    }

    private float addEvolveOverview(EvolveOverviewUi overview, float yPos) {
        float xPad = super.pixelsToRelativeX(10.0f);
        float rightPad = super.pixelsToRelativeX(20.0f);
        float yScale = super.pixelsToRelativeY(overview.getHeightInPixels());
        super.addComponent(overview, xPad, yPos, 1.0f - (rightPad + xPad), yScale);
        return yPos + yScale;
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

    private List<Node> getListOfSpecies() {
        Set<Integer> speciesInWorld = GameManager.getSession().getStats().getLockStatus().getUnlockedSpecies();
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Integer species : speciesInWorld) {
            Blueprint parent = BlueprintRepository.getBlueprint(species);
            this.addChildren(nodes, parent);
        }
        return nodes;
    }

    private void addChildren(List<Node> nodes, Blueprint parent) {
        LifeCompBlueprint life = (LifeCompBlueprint)parent.getComponent(ComponentType.LIFE);
        if (life == null) {
            return;
        }
        Node parentNode = GameManager.BREED_TREES.getNode(parent);
        if (parentNode == null) {
            return;
        }
        for (Node child : parentNode.getAllChildren()) {
            if (GameManager.getSession().getStats().getLockStatus().isUnlocked(child.species)) continue;
            nodes.add(child);
        }
    }
}


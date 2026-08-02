/*
 * Decompiled with CFR 0.152.
 */
package evolutionUi;

import breeding.BreedingComponent;
import breedingTrees.Node;
import componentArchitecture.ComponentType;
import entityInfoGui.EntityInfoGui;
import evolutionUi.ChildState;
import evolutionUi.ChooseSpeciesUi;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import health.LifeComponent;
import instances.Entity;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.GuiClickableGroup;

public class EvolutionUi
extends GuiComponent {
    private final EntityInfoGui mainUi;
    private final Entity entity;
    private final List<Node> allChildSpecies;
    private final BreedingComponent breedComp;
    private final float yGap;
    private final int numberOfLines;
    private List<ChooseSpeciesUi> chooseUis = new ArrayList<ChooseSpeciesUi>();
    private GuiClickableGroup tabGroup;

    public EvolutionUi(Entity entity, EntityInfoGui mainUi, int numberOfLines) {
        Node node = GameManager.BREED_TREES.getNode(entity.getBlueprint());
        this.numberOfLines = numberOfLines;
        this.breedComp = ((LifeComponent)entity.getComponent(ComponentType.LIFE)).getBreedComponent();
        this.mainUi = mainUi;
        this.yGap = 2.0f / (float)numberOfLines;
        this.allChildSpecies = node.getAllChildren();
        this.entity = entity;
        this.tabGroup = new GuiClickableGroup();
    }

    @Override
    protected void init() {
        super.init();
        float yPos = 0.0f;
        for (Node node : this.allChildSpecies) {
            ChooseSpeciesUi chooseUi = new ChooseSpeciesUi(this.entity, node.species, this.breedComp, this.tabGroup, this.numberOfLines, this.mainUi, this);
            this.chooseUis.add(chooseUi);
            super.addComponent(chooseUi, 0.0f, yPos, 1.0f, this.yGap);
            yPos += this.yGap;
        }
    }

    @Override
    public void remove() {
        super.remove();
        this.mainUi.removeSecondPanel();
    }

    public void notifyCompletion(boolean removePanel) {
        if (removePanel) {
            this.mainUi.removeSecondPanel();
        }
        for (ChooseSpeciesUi choice : this.chooseUis) {
            if (!choice.isStateBlocked()) continue;
            choice.changeState(ChildState.NORMAL);
        }
    }

    public void blockAvailableChoices() {
        for (ChooseSpeciesUi choice : this.chooseUis) {
            if (!choice.isStateNormal()) continue;
            choice.changeState(ChildState.BLOCKED);
        }
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
}


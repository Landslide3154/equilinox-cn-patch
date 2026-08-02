/*
 * Decompiled with CFR 0.152.
 */
package breedingTrees;

import basics.DisplayManager;
import breedingTrees.GraphGui;
import breedingTrees.Node;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import fontRendering.Text;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import userInterfaces.GuiClippingPanel;

public class BreedingTreeGui
extends GuiClippingPanel {
    private static final String UP_TEXT = GameText.getText(1046);
    private static final String DOWN_TEXT = GameText.getText(1047);
    private GraphGui graph;
    private Node head;
    private boolean upwards;
    private final boolean showTitle;

    public BreedingTreeGui(Node headNode, boolean upwards) {
        this.graph = new GraphGui(headNode, upwards, this);
        super.addComponent(this.graph, 0.0f, 0.0f, 1.0f, 1.0f);
        this.head = headNode;
        this.upwards = upwards;
        this.showTitle = true;
    }

    public BreedingTreeGui(Node headNode, boolean upwards, boolean showTitle) {
        this.graph = new GraphGui(headNode, upwards, this, showTitle);
        super.addComponent(this.graph, 0.0f, 0.0f, 1.0f, 1.0f);
        this.head = headNode;
        this.upwards = upwards;
        this.showTitle = showTitle;
    }

    @Override
    protected void init() {
        super.init();
        if (this.showTitle) {
            this.addText(this.head, this.upwards);
        }
    }

    @Override
    public void remove() {
        this.graph.notifyClose();
        super.remove();
    }

    private void addText(Node headNode, boolean upwards) {
        String name = ((InformationComponent.InformationCompBlueprint)headNode.species.getComponent(ComponentType.INFO)).getName();
        String fullText = String.valueOf(name) + " - " + (upwards ? UP_TEXT : DOWN_TEXT);
        Text text = Text.newText(fullText).setFontSize(UiSettings.TITLE_FONT).center().create();
        text.setColour(ColourPalette.WHITE);
        float startY = 9.0f / ((float)DisplayManager.getUiHeight() * super.getScale().y);
        super.addText(text, 0.0f, startY, 1.0f);
    }
}


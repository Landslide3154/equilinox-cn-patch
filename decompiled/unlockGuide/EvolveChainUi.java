/*
 * Decompiled with CFR 0.152.
 */
package unlockGuide;

import blueprints.Blueprint;
import breedingTrees.GraphNode;
import breedingTrees.GraphNodeGui;
import breedingTrees.Node;
import breedingTrees.RequirementPopUpGui;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import interpolation.Timer;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiImage;

public class EvolveChainUi
extends GuiComponent {
    private static final float MOUSE_OVER_WAIT = 0.16f;
    private static final int NODE_WIDTH = 60;
    private static final int LINE_WIDTH = 16;
    private final GraphNode headNode;
    private float xPos;
    private int nodeCount;
    private RequirementPopUpGui currentPopUp = null;
    private GraphNode currentlyMousedOver;
    private Timer timer = Timer.createOneOffTimer(0.16f, false);

    public EvolveChainUi(Blueprint species) {
        Node leafNode = GameManager.BREED_TREES.getNode(species);
        this.headNode = GraphNode.createNewUpwardsGraph(leafNode);
        this.nodeCount = leafNode.tier;
    }

    @Override
    protected void init() {
        super.init();
        this.calcStartX();
        this.displayLines(this.headNode);
        this.calcStartX();
        this.displayChain(this.headNode);
    }

    private void calcStartX() {
        int pixelWidth = 60 * this.nodeCount + 16 * (this.nodeCount - 1);
        float width = super.pixelsToRelativeX(pixelWidth);
        this.xPos = (1.0f - width) * 0.5f;
    }

    private void displayChain(GraphNode node) {
        this.addNode(node);
        if (!node.getChildren().isEmpty()) {
            this.xPos += super.pixelsToRelativeX(16.0f);
            this.displayChain(node.getChildren().get(0));
        }
    }

    private void displayLines(GraphNode node) {
        this.xPos += super.pixelsToRelativeX(60.0f);
        if (!node.getChildren().isEmpty()) {
            this.addLine();
            this.displayLines(node.getChildren().get(0));
        }
    }

    private void addNode(GraphNode node) {
        GraphNodeGui nodeGui = new GraphNodeGui(node.getNode().species, node.isUnlocked(), false);
        super.addComponent(nodeGui, this.xPos, 0.0f, super.pixelsToRelativeX(60.0f), super.pixelsToRelativeY(60.0f));
        this.addMouseOverListener(nodeGui, node);
        this.xPos += super.pixelsToRelativeX(60.0f);
    }

    private void addLine() {
        GuiImage line = new GuiImage(GuiRepository.EVOLVE_ARROW);
        float center = super.pixelsToRelativeY(60.0f) * 0.5f;
        float yPos = center - super.pixelsToRelativeY(16.0f) * 0.5f;
        super.addComponent(line, this.xPos, yPos, super.pixelsToRelativeX(16.0f), super.pixelsToRelativeY(16.0f));
        this.xPos += super.pixelsToRelativeX(16.0f);
    }

    @Override
    protected void delete() {
        this.deletePopUp();
        super.delete();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        this.updateMouseOver();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addMouseOverListener(GraphNodeGui icon, final GraphNode node) {
        icon.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    EvolveChainUi.this.deletePopUp();
                    EvolveChainUi.this.timer.start();
                    EvolveChainUi.this.currentlyMousedOver = node;
                } else if (event.isMouseOff() && node == EvolveChainUi.this.currentlyMousedOver) {
                    EvolveChainUi.this.timer.stop();
                    EvolveChainUi.this.currentlyMousedOver = null;
                    EvolveChainUi.this.deletePopUp();
                }
            }
        });
    }

    private void deletePopUp() {
        if (this.currentPopUp != null) {
            this.currentPopUp.remove();
            this.currentPopUp = null;
        }
    }

    private void updateMouseOver() {
        if (this.currentlyMousedOver != null && this.timer.check()) {
            this.currentPopUp = new RequirementPopUpGui(this.currentlyMousedOver.getNode().species);
        }
    }
}


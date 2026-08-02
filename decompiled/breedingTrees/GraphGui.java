/*
 * Decompiled with CFR 0.152.
 */
package breedingTrees;

import breedingTrees.GraphNode;
import breedingTrees.GraphNodeGui;
import breedingTrees.Node;
import breedingTrees.RequirementPopUpGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import interpolation.Timer;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.MyCursor;
import org.lwjgl.util.vector.Vector2f;
import toolbox.MyMouse;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClippingPanel;
import userInterfaces.GuiImage;

public class GraphGui
extends GuiComponent {
    private static final boolean HIDE_UNKNOWN = false;
    private static final int LINE_THICKNESS = 2;
    private static final float NODE_WIDTH = 0.1f;
    private static final float PADDING_X = 0.06f;
    private static final float PADDING_Y = 0.06f;
    private static final float CELL_WIDTH = 0.16f;
    private static final float MOUSE_OVER_WAIT = 0.16f;
    private float lineWidth;
    private float lineHeight;
    private float cellHeight;
    private float graphOffsetX;
    private float headGridY;
    private Node headNode;
    private boolean upwards;
    private GuiClippingPanel panel;
    private boolean mousedOver = false;
    private boolean grabbed = false;
    private boolean canMove = false;
    private RequirementPopUpGui currentPopUp = null;
    private GraphNode currentlyMousedOver;
    private GraphNode topNode;
    private Timer timer = Timer.createOneOffTimer(0.16f, false);
    private final boolean paddingTop;

    public GraphGui(Node headNode, boolean upwards, GuiClippingPanel panel) {
        this.headNode = headNode;
        this.upwards = upwards;
        this.panel = panel;
        this.paddingTop = true;
    }

    public GraphGui(Node headNode, boolean upwards, GuiClippingPanel panel, boolean topPad) {
        this.headNode = headNode;
        this.upwards = upwards;
        this.panel = panel;
        this.paddingTop = topPad;
    }

    @Override
    protected void init() {
        this.calculateDimensions();
        if (this.upwards) {
            this.topNode = GraphNode.createNewUpwardsGraph(this.headNode);
            this.graphOffsetX = 0.5f;
        } else {
            this.topNode = GraphNode.createNewGraph(this.headNode);
            float graphWidth = (float)this.headNode.getWeight() * 0.16f;
            this.graphOffsetX = -((graphWidth - 1.0f) / 2.0f);
            if (graphWidth > 1.0f) {
                this.canMove = true;
            }
        }
        this.headGridY = this.topNode.getGridY();
        this.displayTree(this.topNode);
    }

    protected void notifyClose() {
        this.deletePopUp();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void delete() {
        super.delete();
        MyCursor.setCursor(MyCursor.NORMAL);
    }

    @Override
    protected void updateSelf() {
        MyMouse mouse = MyMouse.getActiveMouse();
        this.updateMouseOver();
        if (this.canMove) {
            this.checkMouseOver();
            this.checkGrabbed(mouse);
            if (this.grabbed) {
                this.moveGraphWithMouse(mouse);
            }
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void updateMouseOver() {
        if (this.currentlyMousedOver != null && this.timer.check()) {
            this.currentPopUp = new RequirementPopUpGui(this.currentlyMousedOver.getNode().species);
        }
    }

    private void checkMouseOver() {
        if (!this.mousedOver && this.panel.isMouseOver()) {
            MyCursor.setCursor(MyCursor.GRABBED_LIGHT);
            this.mousedOver = true;
        } else if (this.mousedOver && !this.panel.isMouseOver()) {
            if (!this.grabbed) {
                MyCursor.setCursor(MyCursor.NORMAL);
            }
            this.mousedOver = false;
        }
    }

    private void checkGrabbed(MyMouse mouse) {
        if (this.mousedOver && mouse.isLeftClick()) {
            this.grabbed = true;
        } else if (mouse.isLeftClickRelease()) {
            if (!this.mousedOver) {
                MyCursor.setCursor(MyCursor.NORMAL);
            }
            this.grabbed = false;
        }
    }

    private void displayTree(GraphNode headNode) {
        this.displayNode(headNode);
        for (GraphNode child : headNode.getChildren()) {
            this.displayTree(child);
        }
    }

    private void displayNode(GraphNode node) {
        Vector2f nodeCenter = this.getRelativeCenter(node);
        this.createLines(node, nodeCenter);
        this.addIcon(nodeCenter, node);
    }

    private void createLines(GraphNode node, Vector2f center) {
        if (node.hasLine()) {
            this.createHorizontalLine(node, center);
        }
        if (node.getChildren().size() > 0) {
            for (GraphNode child : node.getChildren()) {
                this.createChildLine(child);
            }
            this.createParentLine(center);
        }
    }

    private Vector2f getRelativeCenter(GraphNode node) {
        Vector2f center = new Vector2f();
        float gridX = node.getGridX();
        float topPad = this.paddingTop ? this.cellHeight : this.cellHeight * 0.5f;
        center.x = gridX * 0.16f + this.graphOffsetX;
        float gridY = node.getGridY() - this.headGridY;
        center.y = gridY * this.cellHeight + topPad;
        if (center.y + this.cellHeight / 2.0f > 1.0f) {
            this.canMove = true;
        }
        return center;
    }

    private void calculateDimensions() {
        this.cellHeight = super.getRelativeHeightCoords(0.1f) + 0.06f;
        this.lineWidth = super.pixelsToRelativeX(2.0f);
        this.lineHeight = super.pixelsToRelativeY(2.0f);
    }

    private void addIcon(Vector2f center, GraphNode node) {
        boolean headNode = node.isImportant() && !this.upwards;
        GraphNodeGui nodeGui = new GraphNodeGui(node.getNode().species, node.isUnlocked(), headNode);
        if (!headNode) {
            this.addMouseOverListener(nodeGui, node);
        }
        super.addCenteredComponent(nodeGui, center.x, center.y, 0.1f);
    }

    private void createHorizontalLine(GraphNode node, Vector2f center) {
        float lineY = center.y + this.cellHeight / 2.0f;
        float lineX = node.getLineMin() * 0.16f + this.graphOffsetX;
        float lineLength = (node.getLineMax() - node.getLineMin()) * 0.16f;
        GuiImage line = new GuiImage(GuiRepository.BLOCK);
        line.getTexture().setOverrideColour(ColourPalette.WHITE);
        super.addComponent(line, lineX, lineY, lineLength, this.lineHeight);
    }

    private void createChildLine(GraphNode child) {
        Vector2f childCenter = this.getRelativeCenter(child);
        float xPos = childCenter.x - this.lineWidth / 2.0f;
        float yPos = childCenter.y - this.cellHeight / 2.0f;
        GuiImage line = new GuiImage(GuiRepository.BLOCK);
        line.getTexture().setOverrideColour(ColourPalette.WHITE);
        super.addComponent(line, xPos, yPos, this.lineWidth, this.cellHeight / 2.0f);
    }

    private void createParentLine(Vector2f center) {
        float xPos = center.x - this.lineWidth / 2.0f;
        float ySize = this.cellHeight / 2.0f;
        GuiImage line = new GuiImage(GuiRepository.BLOCK);
        line.getTexture().setOverrideColour(ColourPalette.WHITE);
        super.addComponent(line, xPos, center.y, this.lineWidth, ySize);
    }

    private void moveGraphWithMouse(MyMouse mouse) {
        float moveX = super.pixelsToRelativeX(mouse.getDX());
        float moveY = -super.pixelsToRelativeY(mouse.getDY());
        super.setRelativeX(super.getRelativeX() + moveX);
        super.setRelativeY(super.getRelativeY() + moveY);
        if (mouse.isLeftClickRelease()) {
            this.grabbed = false;
        }
    }

    private void addMouseOverListener(GraphNodeGui icon, final GraphNode node) {
        icon.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    GraphGui.this.deletePopUp();
                    GraphGui.this.timer.start();
                    GraphGui.this.currentlyMousedOver = node;
                } else if (event.isMouseOff() && node == GraphGui.this.currentlyMousedOver) {
                    GraphGui.this.timer.stop();
                    GraphGui.this.currentlyMousedOver = null;
                    GraphGui.this.deletePopUp();
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
}


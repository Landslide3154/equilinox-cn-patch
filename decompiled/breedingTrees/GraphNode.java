/*
 * Decompiled with CFR 0.152.
 */
package breedingTrees;

import breedingTrees.Node;
import gameManaging.GameManager;
import java.util.ArrayList;
import java.util.List;
import sessionStats.LockStatus;

public class GraphNode {
    private int start;
    private float posX;
    private float lineMin;
    private float lineMax;
    private Node node;
    private List<GraphNode> children = new ArrayList<GraphNode>();
    private boolean important = false;
    private boolean unlocked = false;
    private boolean unknown = false;

    private GraphNode(Node node, int start) {
        this.node = node;
        this.start = start;
        this.lineMin = this.posX = (float)start + (float)node.getWeight() * 0.5f;
        this.lineMax = this.posX;
        this.checkLockStatus();
    }

    public float getGridX() {
        return this.posX;
    }

    public float getGridY() {
        return this.node.tier;
    }

    public List<GraphNode> getChildren() {
        return this.children;
    }

    public boolean hasLine() {
        return this.children.size() > 1;
    }

    public Node getNode() {
        return this.node;
    }

    protected float getLineMin() {
        return this.lineMin;
    }

    public boolean isImportant() {
        return this.important;
    }

    protected float getLineMax() {
        return this.lineMax;
    }

    public boolean isUnlocked() {
        return this.unlocked;
    }

    protected boolean isUnknown() {
        return this.unknown;
    }

    private void checkLockStatus() {
        LockStatus lockStatus = GameManager.getSession().getStats().getLockStatus();
        this.unlocked = lockStatus.isUnlocked(this.node.species);
        if (!this.unlocked) {
            this.unknown = this.node.parent == null ? false : !lockStatus.isUnlocked(this.node.parent.species);
        }
    }

    private void createAllChildren() {
        int current = this.start;
        for (Node child : this.node.getNormalChildren()) {
            GraphNode childGraphNode = new GraphNode(child, current);
            current += child.getWeight();
            this.children.add(childGraphNode);
            childGraphNode.createAllChildren();
            this.checkMinMax(childGraphNode.posX);
        }
    }

    private void checkMinMax(float xPos) {
        this.lineMin = Math.min(xPos, this.lineMin);
        this.lineMax = Math.max(xPos, this.lineMax);
    }

    public static GraphNode createNewGraph(Node headNode) {
        GraphNode headGraphNode = new GraphNode(headNode, 0);
        headGraphNode.important = true;
        headGraphNode.createAllChildren();
        return headGraphNode;
    }

    public static GraphNode createNewUpwardsGraph(Node leafNode) {
        GraphNode leaf = new GraphNode(leafNode, 0);
        leaf.important = true;
        GraphNode head = GraphNode.addAllParents(leaf);
        return head;
    }

    private static GraphNode addAllParents(GraphNode leaf) {
        leaf.posX = 0.0f;
        if (leaf.node.parent != null) {
            GraphNode parentNode = new GraphNode(leaf.node.parent, 0);
            parentNode.children.add(leaf);
            return GraphNode.addAllParents(parentNode);
        }
        return leaf;
    }
}


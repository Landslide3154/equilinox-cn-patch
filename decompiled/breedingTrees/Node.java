/*
 * Decompiled with CFR 0.152.
 */
package breedingTrees;

import blueprints.Blueprint;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public final Blueprint species;
    public final Node parent;
    public final int tier;
    private int weight;
    private final int key;
    private List<Node> children = new ArrayList<Node>();
    private List<Node> specialChildren = new ArrayList<Node>();

    protected Node(Blueprint species, int key, Node parent) {
        this.species = species;
        this.key = key;
        this.parent = parent;
        this.tier = parent != null ? parent.tier + 1 : 1;
    }

    public int getWeight() {
        return this.weight;
    }

    public List<Node> getNormalChildren() {
        return this.children;
    }

    public List<Node> getAllChildren() {
        if (this.specialChildren.isEmpty()) {
            return this.children;
        }
        ArrayList<Node> allChildren = new ArrayList<Node>(this.children.size() + this.specialChildren.size());
        allChildren.addAll(this.children);
        allChildren.addAll(this.specialChildren);
        return this.children;
    }

    protected Node connectChild(Blueprint species) {
        int childKey = this.children.size();
        Node child = new Node(species, childKey, this);
        this.children.add(child);
        return child;
    }

    protected Node connectSpecialChild(Blueprint species) {
        int childKey = -(this.specialChildren.size() + 1);
        Node child = new Node(species, childKey, this);
        this.specialChildren.add(child);
        return child;
    }

    protected Node getChild(int key) {
        if (key >= 0) {
            return this.children.get(key);
        }
        int index = Math.abs(key + 1);
        return this.specialChildren.get(index);
    }

    protected int calculateWeight() {
        this.weight = 0;
        for (Node child : this.children) {
            this.weight += child.calculateWeight();
        }
        this.weight = this.weight == 0 ? 1 : this.weight;
        return this.weight;
    }

    protected int[] getKey() {
        int[] fullKey = new int[this.tier];
        if (this.parent != null) {
            int[] parentKey = this.parent.getKey();
            int i = 0;
            while (i < parentKey.length) {
                fullKey[i] = parentKey[i];
                ++i;
            }
            fullKey[fullKey.length - 1] = this.key;
        } else {
            fullKey[0] = this.key;
        }
        return fullKey;
    }
}


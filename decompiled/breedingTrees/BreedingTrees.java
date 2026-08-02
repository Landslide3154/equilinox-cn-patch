/*
 * Decompiled with CFR 0.152.
 */
package breedingTrees;

import blueprints.Blueprint;
import breedingTrees.Node;
import componentArchitecture.ComponentType;
import health.LifeCompBlueprint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BreedingTrees {
    public static final int NO_TIER = -1;
    public static final int BASE_TIER = 1;
    private Map<Blueprint, int[]> blueprintKeys = new HashMap<Blueprint, int[]>();
    private List<Node> breedingTrees = new ArrayList<Node>();
    private int totalSpeciesCount = 0;

    public void initBreedingTrees(List<Blueprint> allBlueprints) {
        for (Blueprint species : allBlueprints) {
            LifeCompBlueprint lifeComp = (LifeCompBlueprint)species.getComponent(ComponentType.LIFE);
            if (lifeComp == null) continue;
            ++this.totalSpeciesCount;
            if (this.blueprintKeys.containsKey(species)) continue;
            this.createNewTreeNode(species);
        }
        this.calculateWeights();
    }

    public int getTotalSpeciesCount() {
        return this.totalSpeciesCount;
    }

    public Node getNode(Blueprint blueprint) {
        int[] key = this.blueprintKeys.get(blueprint);
        if (key == null) {
            return null;
        }
        return this.getNodeWithKey(key);
    }

    public int getTier(Blueprint species) {
        Node node = this.getNode(species);
        if (node == null) {
            return -1;
        }
        return node.tier;
    }

    public boolean isBaseSpecies(Blueprint species) {
        return this.getTier(species) == 1;
    }

    public List<Blueprint> getBaseSpecies() {
        ArrayList<Blueprint> bases = new ArrayList<Blueprint>();
        for (Node baseNode : this.breedingTrees) {
            bases.add(baseNode.species);
        }
        return bases;
    }

    public List<Blueprint> getAllSpecies() {
        ArrayList<Blueprint> species = new ArrayList<Blueprint>();
        for (Node baseNode : this.breedingTrees) {
            this.getAllNodeSpecies(baseNode, species);
        }
        return species;
    }

    private void getAllNodeSpecies(Node baseNode, List<Blueprint> species) {
        species.add(baseNode.species);
        for (Node node : baseNode.getAllChildren()) {
            this.getAllNodeSpecies(node, species);
        }
    }

    private Node getNodeWithKey(int[] key) {
        Node current = this.breedingTrees.get(key[0]);
        int i = 1;
        while (i < key.length) {
            current = current.getChild(key[i]);
            ++i;
        }
        return current;
    }

    private Node createNewTreeNode(Blueprint blueprint) {
        LifeCompBlueprint lifeComp = (LifeCompBlueprint)blueprint.getComponent(ComponentType.LIFE);
        Blueprint parent = lifeComp.breedInfo.getParent();
        if (parent == null) {
            return this.createBaseSpeciesNode(blueprint);
        }
        return this.addSpeciesIntoTree(blueprint, parent);
    }

    private Node createBaseSpeciesNode(Blueprint blueprint) {
        Node baseNode = new Node(blueprint, this.breedingTrees.size(), null);
        this.breedingTrees.add(baseNode);
        this.blueprintKeys.put(blueprint, baseNode.getKey());
        return baseNode;
    }

    private Node addSpeciesIntoTree(Blueprint species, Blueprint parent) {
        Node parentNode = this.getNode(parent);
        if (parentNode == null) {
            parentNode = this.createNewTreeNode(parent);
        }
        Node newNode = species.isSecret() ? parentNode.connectSpecialChild(species) : parentNode.connectChild(species);
        this.blueprintKeys.put(species, newNode.getKey());
        return newNode;
    }

    private void calculateWeights() {
        for (Node baseNode : this.breedingTrees) {
            baseNode.calculateWeight();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package classification;

import classification.ClassNode;
import classification.LeafClassNode;
import classification.NormalClassNode;
import classification.SpeciesClassification;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Classification {
    private final Classification parent;
    private final Map<Character, Classification> subClassifications;
    private Character id;
    private String key = "";
    private String name = "Everything";
    private boolean superPlacement = false;
    private int tier;

    private Classification(char id, String name, Classification parent) {
        this.id = Character.valueOf(id);
        this.tier = parent.tier + 1;
        this.name = name;
        this.parent = parent;
        this.subClassifications = new LinkedHashMap<Character, Classification>();
        this.key = String.valueOf(parent.key) + id;
    }

    private Classification() {
        this.parent = null;
        this.tier = 0;
        this.subClassifications = new LinkedHashMap<Character, Classification>();
    }

    protected Classification(String speciesId, Classification parent) {
        this.parent = parent;
        this.tier = parent.tier + 1;
        this.key = String.valueOf(parent.key) + speciesId;
        this.subClassifications = null;
    }

    public boolean needsSuperPlacement() {
        return this.superPlacement;
    }

    public int getCategoryTier() {
        return this.tier;
    }

    public Classification getTier(int tier) {
        if (tier >= this.tier) {
            return this;
        }
        return this.parent.getTier(tier);
    }

    public Collection<Classification> getChildren() {
        return this.subClassifications.values();
    }

    public String getName() {
        return this.name;
    }

    public String getFullClassification() {
        if (this.parent == null || this.parent.parent == null) {
            return this.name;
        }
        return String.valueOf(this.name) + " - " + this.parent.getFullClassification();
    }

    public boolean isTypeOf(Classification classification) {
        return this.key.startsWith(classification.key);
    }

    public String getKey() {
        return this.key;
    }

    public String[] getKeyAsArray() {
        return this.getKeyArray(0);
    }

    private String[] getKeyArray(int index) {
        String[] keyArray;
        if (this.parent == null) {
            keyArray = new String[index];
        } else {
            keyArray = this.parent.getKeyArray(index + 1);
            keyArray[keyArray.length - 1 - index] = this.getIdentificationString();
        }
        return keyArray;
    }

    public Classification getParent() {
        return this.parent;
    }

    public Classification createSpeciesClassification(int speciesId) {
        if (this.isLeaf()) {
            return this.createSpeciesClassification(Integer.toString(speciesId));
        }
        System.err.println("Can't add species to non-leaf node!");
        return null;
    }

    public boolean isSpecies() {
        return false;
    }

    protected Classification createChild(char id, String name) {
        Classification classification = new Classification(id, name, this);
        this.subClassifications.put(Character.valueOf(id), classification);
        return classification;
    }

    protected Classification createChild(char id, String name, boolean superPlacement) {
        Classification classification = new Classification(id, name, this);
        classification.superPlacement = superPlacement;
        this.subClassifications.put(Character.valueOf(id), classification);
        return classification;
    }

    protected ClassNode createChildNode(NormalClassNode parent, Character childId) {
        Classification childClassification = this.subClassifications.get(childId);
        if (childClassification.isLeaf()) {
            return new LeafClassNode(parent, childClassification);
        }
        return new NormalClassNode(parent, childClassification);
    }

    protected Classification getClassification(String key) {
        if (this.isLeaf()) {
            return this.createSpeciesClassification(key);
        }
        Classification child = this.subClassifications.get(Character.valueOf(key.charAt(0)));
        if (key.length() == 1) {
            return child;
        }
        return child.getClassification(key.substring(1));
    }

    protected String getIdentificationString() {
        return Character.toString(this.id.charValue());
    }

    protected Character getId() {
        return this.id;
    }

    private boolean isLeaf() {
        return this.subClassifications.isEmpty();
    }

    private Classification createSpeciesClassification(String speciesId) {
        return new SpeciesClassification(speciesId, this);
    }

    protected static Classification createHeadNode() {
        return new Classification();
    }
}


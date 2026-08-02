/*
 * Decompiled with CFR 0.152.
 */
package classification;

import classification.ClassNode;
import classification.Classification;
import entityBundle.EntityBundle;
import instances.Entity;
import java.util.HashMap;
import java.util.Map;
import toolbox.Maths;
import world.GridSection;

public class NormalClassNode
extends ClassNode {
    private final Map<Character, ClassNode> nodes = new HashMap<Character, ClassNode>();

    protected NormalClassNode(NormalClassNode parent, Classification classification) {
        super(parent, classification);
    }

    @Override
    protected EntityBundle get(String classKey, EntityBundle bundle) {
        ClassNode childNode = this.nodes.get(Character.valueOf(classKey.charAt(0)));
        if (childNode == null) {
            return null;
        }
        if (classKey.length() == 1) {
            if (bundle == null) {
                bundle = new EntityBundle();
            }
            childNode.get(bundle);
            return bundle;
        }
        return childNode.get(classKey.substring(1), bundle);
    }

    @Override
    protected void get(EntityBundle entityBundle) {
        for (ClassNode childNode : this.nodes.values()) {
            childNode.get(entityBundle);
        }
    }

    @Override
    protected void add(Entity entity, String key, GridSection section) {
        Character childId = Character.valueOf(key.charAt(0));
        ClassNode childNode = this.nodes.get(childId);
        if (childNode == null) {
            childNode = this.addChildNode(childId);
        }
        childNode.add(entity, key.substring(1), section);
        super.increaseEntityCount();
    }

    @Override
    protected void remove(Entity entity, String key, GridSection section) {
        ClassNode childNode = this.nodes.get(Character.valueOf(key.charAt(0)));
        childNode.remove(entity, key.substring(1), section);
        super.decreaseEntityCount();
    }

    @Override
    protected Entity getRandomEntity(String key) {
        ClassNode child = this.nodes.get(Character.valueOf(key.charAt(0)));
        if (child == null) {
            return null;
        }
        if (key.length() == 1) {
            return child.getRandomEntity();
        }
        return child.getRandomEntity(key.substring(1));
    }

    @Override
    protected int getEntityCount(String key) {
        ClassNode child = this.nodes.get(Character.valueOf(key.charAt(0)));
        if (child == null) {
            return 0;
        }
        if (key.length() == 1) {
            return child.getEntityCount();
        }
        return child.getEntityCount(key.substring(1));
    }

    @Override
    protected Entity getRandomEntity() {
        int index = Maths.RANDOM.nextInt(this.getEntityCount());
        int current = 0;
        for (ClassNode child : this.nodes.values()) {
            if ((current += child.getEntityCount()) <= index) continue;
            return child.getRandomEntity();
        }
        return null;
    }

    protected void removeChild(ClassNode child) {
        this.nodes.remove(child.getId());
        if (this.nodes.isEmpty() && super.getParent() != null) {
            super.getParent().removeChild(this);
        }
    }

    private ClassNode addChildNode(Character childId) {
        ClassNode childNode = this.getClassification().createChildNode(this, childId);
        this.nodes.put(childId, childNode);
        return childNode;
    }
}


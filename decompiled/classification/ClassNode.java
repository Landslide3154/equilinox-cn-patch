/*
 * Decompiled with CFR 0.152.
 */
package classification;

import classification.Classification;
import classification.NormalClassNode;
import entityBundle.EntityBundle;
import instances.Entity;
import world.GridSection;

public abstract class ClassNode {
    private final Classification classification;
    private final NormalClassNode parent;
    private int entityCount;

    protected ClassNode(NormalClassNode parent, Classification classification) {
        this.classification = classification;
        this.parent = parent;
    }

    protected abstract void add(Entity var1, String var2, GridSection var3);

    protected abstract void remove(Entity var1, String var2, GridSection var3);

    protected abstract EntityBundle get(String var1, EntityBundle var2);

    protected abstract void get(EntityBundle var1);

    protected abstract Entity getRandomEntity(String var1);

    protected abstract Entity getRandomEntity();

    protected abstract int getEntityCount(String var1);

    protected int getEntityCount() {
        return this.entityCount;
    }

    protected NormalClassNode getParent() {
        return this.parent;
    }

    protected Classification getClassification() {
        return this.classification;
    }

    protected Character getId() {
        return this.classification.getId();
    }

    protected void increaseEntityCount() {
        ++this.entityCount;
    }

    protected void decreaseEntityCount() {
        --this.entityCount;
    }
}


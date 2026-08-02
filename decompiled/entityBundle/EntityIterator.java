/*
 * Decompiled with CFR 0.152.
 */
package entityBundle;

import instances.Entity;
import java.util.Iterator;
import java.util.List;

public class EntityIterator
implements Iterator<Entity> {
    private Iterator<List<Entity>> mainIterator;
    private Iterator<Entity> currentListIterator;

    protected EntityIterator(List<List<Entity>> entities) {
        this.mainIterator = entities.iterator();
        if (this.mainIterator.hasNext()) {
            this.currentListIterator = this.mainIterator.next().iterator();
        }
    }

    @Override
    public boolean hasNext() {
        return this.currentListIterator != null && (this.mainIterator.hasNext() || this.currentListIterator.hasNext());
    }

    @Override
    public Entity next() {
        if (!this.currentListIterator.hasNext()) {
            this.currentListIterator = this.mainIterator.next().iterator();
        }
        return this.currentListIterator.next();
    }
}


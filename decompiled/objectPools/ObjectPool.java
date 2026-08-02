/*
 * Decompiled with CFR 0.152.
 */
package objectPools;

import java.util.Stack;

public abstract class ObjectPool<T> {
    private Stack<T> pool = new Stack();

    public T get() {
        if (this.pool.isEmpty()) {
            return this.createNewObject();
        }
        return this.pool.pop();
    }

    public void release(T unusedObject) {
        this.pool.push(unusedObject);
    }

    protected abstract T createNewObject();
}


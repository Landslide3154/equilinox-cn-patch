/*
 * Decompiled with CFR 0.152.
 */
package objectPools;

import objectPools.ObjectPool;
import org.lwjgl.util.vector.ReadableVector2f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Vec2Pool {
    private static final ObjectPool<Vector2f> pool = new ObjectPool<Vector2f>(){

        @Override
        protected Vector2f createNewObject() {
            return new Vector2f();
        }
    };

    public static synchronized Vector2f get(float x, float y) {
        Vector2f vec = pool.get();
        vec.set(x, y);
        return vec;
    }

    public static synchronized Vector2f getXZ(Vector3f vector) {
        Vector2f vec = pool.get();
        vec.set(vector.x, vector.z);
        return vec;
    }

    public static synchronized Vector2f get(ReadableVector2f duplicate) {
        Vector2f vec = pool.get();
        vec.set(duplicate);
        return vec;
    }

    public static synchronized Vector2f get() {
        Vector2f vec = pool.get();
        vec.set(0.0f, 0.0f);
        return vec;
    }

    public static synchronized void release(Vector2f old) {
        pool.release(old);
    }

    public static synchronized void release(Vector2f ... oldVectors) {
        Vector2f[] vector2fArray = oldVectors;
        int n = oldVectors.length;
        int n2 = 0;
        while (n2 < n) {
            Vector2f oldVector = vector2fArray[n2];
            pool.release(oldVector);
            ++n2;
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package objectPools;

import objectPools.ObjectPool;
import org.lwjgl.util.vector.ReadableVector4f;
import org.lwjgl.util.vector.Vector4f;

public class Vec4Pool {
    private static final ObjectPool<Vector4f> pool = new ObjectPool<Vector4f>(){

        @Override
        protected Vector4f createNewObject() {
            return new Vector4f();
        }
    };

    public static synchronized Vector4f get(float x, float y, float z, float w) {
        Vector4f vec = pool.get();
        vec.set(x, y, z, w);
        return vec;
    }

    public static synchronized Vector4f get(ReadableVector4f duplicate) {
        Vector4f vec = pool.get();
        vec.set(duplicate);
        return vec;
    }

    public static synchronized Vector4f get() {
        Vector4f vec = pool.get();
        vec.set(0.0f, 0.0f, 0.0f, 0.0f);
        return vec;
    }

    public static synchronized void release(Vector4f old) {
        pool.release(old);
    }

    public static synchronized void release(Vector4f ... oldVectors) {
        Vector4f[] vector4fArray = oldVectors;
        int n = oldVectors.length;
        int n2 = 0;
        while (n2 < n) {
            Vector4f oldVector = vector4fArray[n2];
            pool.release(oldVector);
            ++n2;
        }
    }
}


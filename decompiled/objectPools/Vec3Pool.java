/*
 * Decompiled with CFR 0.152.
 */
package objectPools;

import objectPools.ObjectPool;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

public class Vec3Pool {
    private static final ObjectPool<Vector3f> pool = new ObjectPool<Vector3f>(){

        @Override
        protected Vector3f createNewObject() {
            return new Vector3f();
        }
    };

    public static synchronized Vector3f get(float x, float y, float z) {
        Vector3f vec = pool.get();
        vec.set(x, y, z);
        return vec;
    }

    public static synchronized Vector3f get(ReadableVector3f duplicate) {
        Vector3f vec = pool.get();
        vec.set(duplicate);
        return vec;
    }

    public static synchronized Vector3f get() {
        Vector3f vec = pool.get();
        vec.set(0.0f, 0.0f, 0.0f);
        return vec;
    }

    public static synchronized void release(Vector3f old) {
        pool.release(old);
    }

    public static synchronized void release(Vector3f ... oldVectors) {
        Vector3f[] vector3fArray = oldVectors;
        int n = oldVectors.length;
        int n2 = 0;
        while (n2 < n) {
            Vector3f oldVector = vector3fArray[n2];
            pool.release(oldVector);
            ++n2;
        }
    }
}


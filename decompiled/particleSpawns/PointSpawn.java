/*
 * Decompiled with CFR 0.152.
 */
package particleSpawns;

import org.lwjgl.util.vector.Vector4f;
import particleSpawns.ParticleSpawn;

public class PointSpawn
implements ParticleSpawn {
    @Override
    public Vector4f getBaseSpawnPosition() {
        return new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
    }
}


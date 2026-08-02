/*
 * Decompiled with CFR 0.152.
 */
package particleSpawns;

import java.util.Random;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import particleSpawns.ParticleSpawn;

public class LineSpawn
implements ParticleSpawn {
    private float length;
    private Vector3f axis;
    private Random random = new Random();

    public LineSpawn(float length, Vector3f axis) {
        this.length = length;
        this.axis = (Vector3f)axis.normalise();
    }

    @Override
    public Vector4f getBaseSpawnPosition() {
        float actualLength = this.length;
        Vector3f actualAxis = new Vector3f(this.axis.x * actualLength, this.axis.y * actualLength, this.axis.z * actualLength);
        actualAxis.scale(this.random.nextFloat() - 0.5f);
        return new Vector4f(actualAxis.x, actualAxis.y, actualAxis.z, 1.0f);
    }
}


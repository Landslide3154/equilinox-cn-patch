/*
 * Decompiled with CFR 0.152.
 */
package particleSpawns;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import particleSpawns.ParticleSpawn;
import toolbox.Maths;

public class CircleSpawn
implements ParticleSpawn {
    private Vector3f heading;
    private float radius;

    public CircleSpawn(Vector3f heading, float radius) {
        this.heading = (Vector3f)heading.normalise();
        this.radius = radius;
    }

    @Override
    public Vector4f getBaseSpawnPosition() {
        Vector3f point = Maths.randomPointOnCircle(this.heading, this.radius);
        return new Vector4f(point.x, point.y, point.z, 1.0f);
    }
}


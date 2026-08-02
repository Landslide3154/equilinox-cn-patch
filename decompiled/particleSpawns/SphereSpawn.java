/*
 * Decompiled with CFR 0.152.
 */
package particleSpawns;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import particleSpawns.ParticleSpawn;
import toolbox.Maths;

public class SphereSpawn
implements ParticleSpawn {
    private float radius;

    public SphereSpawn(float radius) {
        this.radius = radius;
    }

    @Override
    public Vector4f getBaseSpawnPosition() {
        Vector3f spherePoint = Maths.generateRandomUnitVector();
        spherePoint.scale(this.radius);
        float a = Maths.RANDOM.nextFloat();
        float b = Maths.RANDOM.nextFloat();
        if (a > b) {
            float temp = a;
            a = b;
            b = temp;
        }
        float randX = (float)((double)b * Math.cos(Math.PI * 2 * (double)(a / b)));
        float randY = (float)((double)b * Math.sin(Math.PI * 2 * (double)(a / b)));
        float distance = new Vector2f(randX, randY).length();
        spherePoint.scale(distance);
        return new Vector4f(spherePoint.x, spherePoint.y, spherePoint.z, 1.0f);
    }
}


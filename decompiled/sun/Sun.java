/*
 * Decompiled with CFR 0.152.
 */
package sun;

import environment.EnvironmentVariables;
import main.Camera;
import org.lwjgl.util.vector.Vector3f;
import particles.Particle;
import particles.ParticleTexture;

public class Sun
extends Particle {
    private static final float DISTANCE = 180.0f;
    private Vector3f offset;

    public Sun(ParticleTexture texture, float scale, Vector3f direction) {
        super(texture, new Vector3f(), new Vector3f(), 0.0f, Float.MAX_VALUE, 0.0f, scale);
        this.offset = new Vector3f(direction);
        this.offset.negate();
        this.offset.normalise();
        this.offset.scale(180.0f);
        super.setManualStages(true);
    }

    public void update(Vector3f direction) {
        this.offset.set(direction);
        this.offset.negate();
        this.offset.normalise();
        this.offset.scale(180.0f);
        this.setStages(0, 1, 1.0f - EnvironmentVariables.getSunEffectBrightness());
    }

    @Override
    protected Vector3f getPosition() {
        Vector3f camPos = Camera.getCamera().getPosition();
        return Vector3f.add(camPos, this.offset, null);
    }
}


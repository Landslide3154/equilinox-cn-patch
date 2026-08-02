/*
 * Decompiled with CFR 0.152.
 */
package dayNightCycle;

import environment.EnvironmentVariables;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector3f;
import particles.ParticleTexture;
import sun.Sun;

public class SunCycle {
    private static final float STEEPNESS = 0.8f;
    private static final float DAY_BIAS = 0.9f;
    private static final float MIDNIGHT = 0.041666668f;
    private static final float MIN_LIGHT_HEIGHT = 0.37f;
    private Vector3f lightSourcePos = new Vector3f();
    private Sun sun;

    public void addSun() {
        this.sun = new Sun(new ParticleTexture(GuiRepository.SUN, 2, false).setGlowy(), 100.0f, new Vector3f(0.0f, -1.0f, 0.0f));
    }

    public void update(float time) {
        float diff;
        double theta = (double)((time - 0.041666668f) * 2.0f) * Math.PI;
        this.lightSourcePos.x = -((float)Math.sin(theta));
        this.lightSourcePos.y = -((float)Math.cos(theta)) * 0.8f + 0.9f;
        this.lightSourcePos.z = (float)Math.cos(theta);
        Vector3f sunDir = new Vector3f(this.lightSourcePos);
        sunDir.negate();
        sunDir.y += 0.65f;
        sunDir.y /= 1.7f;
        sunDir.normalise();
        EnvironmentVariables.VISIBLE_SUN_DIR = sunDir;
        if (this.sun != null) {
            this.sun.update(sunDir);
        }
        if ((diff = 0.37f - this.lightSourcePos.y) > 0.0f) {
            this.lightSourcePos.y = 0.37f + diff * 0.4f;
        }
        this.lightSourcePos.negate();
        this.lightSourcePos.normalise();
        EnvironmentVariables.getVariables().setLightDirection(this.lightSourcePos);
    }
}


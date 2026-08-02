/*
 * Decompiled with CFR 0.152.
 */
package environment;

import basics.EngineMaster;
import basics.MasterRenderer;
import dayNightCycle.DayNightCycle;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Colour;
import toolbox.Maths;

public class EnvironmentVariables {
    private static EnvironmentVariables variables = new EnvironmentVariables();
    public static final float GRAVITY = -10.0f;
    public static float starBrightness = 0.0f;
    private static final float LARGE_NUMBER = 1000000.0f;
    private static Colour lightColour = new Colour(0.8f, 0.6f, 0.6f);
    private static Vector3f lightDirection = new Vector3f(0.4f, -1.0f, 0.2f);
    public static final Colour VOID_COLOUR = new Colour(1.0f, 0.87f, 0.6f);
    private static float ambientWeighting = 0.6f;
    private static float diffuseWeighting = 0.6f;
    public static Colour horizonColour = new Colour(1.0f, 0.87f, 0.6f);
    public static Colour skyColour = new Colour(0.6f, 0.9f, 1.0f);
    public static Vector3f VISIBLE_SUN_DIR = new Vector3f(1.0f, -0.08f, 0.5f);
    public static Vector2f MIST_VALS = new Vector2f(20.0f, 120.0f);
    public static Colour MIST_COL = new Colour(255.0f, 222.0f, 210.0f, true);
    public static float SHADOW_DARKNESS = 0.2f;
    private float skyRotateSpeed = 1.0f;
    private Vector2f sunScreenCoords;
    public static DayNightCycle cycle = new DayNightCycle();

    public static EnvironmentVariables getVariables() {
        return variables;
    }

    public static float getSunEffectBrightness() {
        return 1.0f - Maths.smoothStep(0.0f, 0.2f, EnvironmentVariables.VISIBLE_SUN_DIR.y);
    }

    private EnvironmentVariables() {
    }

    public void setLightColour(Colour colour) {
        lightColour = colour;
    }

    public void update() {
        this.sunScreenCoords = this.getSunScreenCoords();
        cycle.update();
    }

    public void setLightDirection(Vector3f lightDir) {
        lightDirection.set(lightDir);
    }

    public Vector2f getSunScreenPosition() {
        return this.sunScreenCoords;
    }

    public boolean isSunVisible() {
        if (this.sunScreenCoords == null) {
            return false;
        }
        return this.sunScreenCoords.x < 1.3f && this.sunScreenCoords.x > -0.3f && this.sunScreenCoords.y < 1.3f && this.sunScreenCoords.y > -0.3f;
    }

    public Colour getLightColour() {
        return lightColour;
    }

    public float getSkyRotateSpeed() {
        return this.skyRotateSpeed;
    }

    public float getAmbientWeighting() {
        return ambientWeighting;
    }

    public float getDiffuseWeighting() {
        return diffuseWeighting;
    }

    public Vector3f getLightDirection() {
        return lightDirection;
    }

    private Vector3f getSunPosition(Vector3f sunDirection) {
        Vector3f sunPos = new Vector3f(sunDirection);
        sunPos.negate();
        sunPos.scale(1000000.0f);
        return sunPos;
    }

    private Vector2f getSunScreenCoords() {
        Vector3f screenCoords = Maths.convertToScreenSpace(this.getSunPosition(VISIBLE_SUN_DIR), EngineMaster.getCamera().getViewMatrix(), MasterRenderer.getProjectionMatrix());
        if (screenCoords == null) {
            return null;
        }
        return new Vector2f(screenCoords);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package dayNightCycle;

import dayNightCycle.HorizonColourCycle;
import dayNightCycle.LightColourCycle;
import dayNightCycle.MistColourCycle;
import dayNightCycle.SkyColourCycle;
import dayNightCycle.SunCycle;
import environment.EnvironmentVariables;
import gameManaging.GameManager;
import session.Session;
import toolbox.Colour;
import toolbox.Maths;

public class DayNightCycle {
    private SunCycle sunCycle = new SunCycle();
    private LightColourCycle lightCycle = new LightColourCycle();
    private SkyColourCycle skyColCycle = new SkyColourCycle();
    private HorizonColourCycle horizonColCycle = new HorizonColourCycle();
    private MistColourCycle mistCycle = new MistColourCycle();
    private static final float DARK = 0.25f;
    private static final float SOFT = 0.06f;
    private static final float MID_START = 0.5f;
    private static final float MID_END = 0.6875f;
    private static final float NIGHT_START = 0.8541667f;
    private static final float NIGHT_END = 0.14583333f;

    public void addSun() {
        this.sunCycle.addSun();
    }

    public void update() {
        float time = this.getTime();
        Colour colour = this.lightCycle.getColour(time);
        EnvironmentVariables.skyColour = this.horizonColCycle.getColour(time);
        EnvironmentVariables.MIST_COL = this.mistCycle.getColour(time);
        EnvironmentVariables.horizonColour = this.skyColCycle.getColour(time);
        EnvironmentVariables.getVariables().setLightColour(colour);
        this.updateMistAmount(time);
        this.calculateStarBrightness(time);
        this.updateShadowDarkness(time);
        this.sunCycle.update(time);
    }

    private float getTime() {
        Session session = GameManager.getSession();
        float time = 0.0f;
        if (session != null) {
            time = session.getStats().getCalendar().getRawTime();
        }
        return time;
    }

    private void updateShadowDarkness(float time) {
        if (time > 0.5f && time < 0.6875f) {
            EnvironmentVariables.SHADOW_DARKNESS = 0.25f;
        } else if (time > 0.8541667f || time < 0.14583333f) {
            EnvironmentVariables.SHADOW_DARKNESS = 0.06f;
        } else if (time >= 0.14583333f && time <= 0.5f) {
            float blend = Maths.smoothStep(0.14583333f, 0.5f, time);
            EnvironmentVariables.SHADOW_DARKNESS = Maths.interpolate(0.06f, 0.25f, blend);
        } else if (time >= 0.6875f && time <= 0.8541667f) {
            float blend = Maths.smoothStep(0.6875f, 0.8541667f, time);
            EnvironmentVariables.SHADOW_DARKNESS = Maths.interpolate(0.25f, 0.06f, blend);
        }
    }

    private void updateMistAmount(float time) {
        float mistyness = time > 0.5f ? Maths.smoothStep(0.7083333f, 0.875f, time) : 1.0f - Maths.smoothStep(0.20833333f, 0.375f, time);
        mistyness = 1.0f - mistyness;
        EnvironmentVariables.MIST_VALS.x = 15.0f + mistyness * 20.0f;
        EnvironmentVariables.MIST_VALS.y = 75.0f + mistyness * 50.0f;
    }

    private void calculateStarBrightness(float time) {
        EnvironmentVariables.starBrightness = time > 0.5f ? Maths.smoothStep(0.9166667f, 1.0f, time) : 1.0f - Maths.smoothStep(0.125f, 0.20833333f, time);
    }
}


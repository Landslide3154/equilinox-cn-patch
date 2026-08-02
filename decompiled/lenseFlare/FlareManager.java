/*
 * Decompiled with CFR 0.152.
 */
package lenseFlare;

import environment.EnvironmentVariables;
import lenseFlare.FlareRenderer;
import lenseFlare.FlareTexture;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FlareManager {
    private static final float BRIGHTNESS = 0.5f;
    protected static final float FIRST_SIZE = 0.4f;
    protected static final float MIDDLE_SIZE = 0.07f;
    protected static final float GAP = 0.33f;
    private static final float MAX_SCREEN_DIS = 0.6f;
    private static final Vector2f CENTER_SCREEN = new Vector2f(0.5f, 0.5f);
    private final FlareTexture[] flareTextures;
    private final int middleIndex;
    private Vector3f[] flareCoordinates;
    private FlareRenderer renderer;

    protected FlareManager(int middleIndex, FlareTexture ... flares) {
        this.middleIndex = middleIndex;
        this.flareTextures = flares;
        this.renderer = new FlareRenderer();
        this.initCoords();
    }

    public void doOcclusionTest() {
        this.renderer.doOcclusionTest(EnvironmentVariables.getVariables().getSunScreenPosition());
    }

    protected void render(Vector3f sunDirection) {
        Vector2f sunCoords = EnvironmentVariables.getVariables().getSunScreenPosition();
        if (sunCoords == null) {
            return;
        }
        Vector2f sunScreenCoords = new Vector2f(sunCoords);
        Vector2f toCenter = Vector2f.sub(CENTER_SCREEN, new Vector2f(sunScreenCoords), null);
        float brightness = 1.0f - toCenter.length() / 0.6f;
        float dirBrightness = EnvironmentVariables.getSunEffectBrightness();
        if ((brightness *= dirBrightness) <= 0.0f) {
            return;
        }
        this.updateFlarePositions(toCenter, sunScreenCoords);
        this.renderer.render(this.flareTextures, this.flareCoordinates, brightness * 0.5f);
    }

    protected void cleanUp() {
        this.renderer.cleanUp();
    }

    private void initCoords() {
        int count = this.flareTextures.length;
        this.flareCoordinates = new Vector3f[count];
        int i = 0;
        while (i < count) {
            int absDifference = i - this.middleIndex;
            float sizeFactor = (float)absDifference / (float)this.middleIndex;
            this.flareCoordinates[i] = new Vector3f(0.0f, 0.0f, sizeFactor);
            ++i;
        }
    }

    private void updateFlarePositions(Vector2f toCenter, Vector2f sunScreenCoords) {
        int i = 0;
        while (i < this.flareCoordinates.length) {
            this.calculatePosition(new Vector2f(toCenter), sunScreenCoords, this.flareCoordinates[i]);
            ++i;
        }
    }

    private void calculatePosition(Vector2f toCenter, Vector2f sunPos, Vector3f flareData) {
        toCenter.scale(flareData.z);
        Vector2f flarePosition = Vector2f.add(CENTER_SCREEN, toCenter, null);
        flareData.x = flarePosition.x;
        flareData.y = flarePosition.y;
    }
}


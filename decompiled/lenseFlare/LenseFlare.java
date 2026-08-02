/*
 * Decompiled with CFR 0.152.
 */
package lenseFlare;

import lenseFlare.FlareManager;
import lenseFlare.FlareTexture;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import textures.Texture;
import utils.FileUtils;
import utils.MyFile;

public class LenseFlare {
    private static final MyFile textureFolder = new MyFile(FileUtils.RES_FOLDER, "lenseFlare");
    private static final int MIDDLE_INDEX = 4;
    private static Texture circleRings = LenseFlare.loadTexture("flare2.png");
    private static Texture smallGreen = LenseFlare.loadTexture("flare3.png");
    private static Texture rainbowRing = LenseFlare.loadTexture("flare4.png");
    private static Texture smallDot = LenseFlare.loadTexture("flare5.png");
    private static Texture sunStar = LenseFlare.loadTexture("flare6b.png");
    private static Texture redDot = LenseFlare.loadTexture("flare7.png");
    private static Texture rainbow2 = LenseFlare.loadTexture("flare8.png");
    private final FlareManager flareManager = new FlareManager(4, new FlareTexture(sunStar, 1.3f), new FlareTexture(rainbowRing), new FlareTexture(circleRings, 0.8f), new FlareTexture(redDot, 0.6f), new FlareTexture(smallGreen, 0.6f), new FlareTexture(smallDot, 0.5f), new FlareTexture(redDot), new FlareTexture(smallGreen, 0.9f), new FlareTexture(smallDot, 0.3f), new FlareTexture(rainbowRing), new FlareTexture(rainbow2, 1.2f));

    public LenseFlare(Matrix4f projectionMat, Matrix4f viewMat) {
    }

    public void render(Vector3f lightDirection, boolean sunVisible) {
        if (sunVisible) {
            this.flareManager.render(lightDirection);
        }
    }

    public void doOcclusionTest() {
        this.flareManager.doOcclusionTest();
    }

    public void cleanUp() {
        this.flareManager.cleanUp();
    }

    private static Texture loadTexture(String fileName) {
        return Texture.newTexture(new MyFile(textureFolder, fileName)).noFiltering().clampEdges().create();
    }
}


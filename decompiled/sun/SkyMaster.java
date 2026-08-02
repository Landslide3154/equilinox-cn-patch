/*
 * Decompiled with CFR 0.152.
 */
package sun;

import environment.EnvironmentVariables;
import graphicsOptions.GraphicsOptions;
import lenseFlare.LenseFlare;
import org.lwjgl.util.vector.Matrix4f;
import skybox.SkyboxRenderer;
import sunShafts.SunShaftMaster;
import world.Chunk;

public class SkyMaster {
    private LenseFlare lensFlare;
    private SkyboxRenderer skybox;
    private SunShaftMaster sunShafts;

    public SkyMaster(Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        this.lensFlare = new LenseFlare(projectionMatrix, viewMatrix);
        this.sunShafts = new SunShaftMaster();
        this.skybox = new SkyboxRenderer();
    }

    public int getSunShaftTexture() {
        return this.sunShafts.getTexture();
    }

    public void updateSunShaftTexture(Chunk[] chunks) {
        if (GraphicsOptions.SUN_RAYS) {
            this.sunShafts.renderSunShafts(chunks);
        }
    }

    public void doOcclusionTest() {
        this.lensFlare.doOcclusionTest();
    }

    public void renderSkyBox() {
        this.skybox.render();
    }

    public void applyLensFlare() {
        if (GraphicsOptions.LENS_FLARE) {
            this.lensFlare.render(EnvironmentVariables.VISIBLE_SUN_DIR, EnvironmentVariables.getVariables().isSunVisible());
        }
    }

    public void cleanUp() {
        this.lensFlare.cleanUp();
        this.skybox.cleanUp();
        this.sunShafts.cleanUp();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package water;

import basics.CameraInterface;
import batches.DynamicBatch;
import entityRenderers.EntityRenderer;
import entityRenderers.StaticRenderer;
import environment.EnvironmentVariables;
import graphicsOptions.GraphicsOptions;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import sun.SkyMaster;
import terrainRenderer.TerrainRenderer;
import toolbox.OpenglUtils;
import water.Water;
import water.WaterFrameBuffers;
import water.WaterHdRenderer;
import water.WaterRenderer;
import world.Chunk;

public class WaterMasterRenderer {
    private static final float REFRACTION_BIAS = 0.3f;
    private WaterFrameBuffers waterFbos;
    private WaterRenderer waterRenderer;
    private WaterHdRenderer waterHdRenderer;
    private TerrainRenderer terrainRenderer;
    private EntityRenderer entityRenderer;
    private StaticRenderer staticRenderer;
    private SkyMaster sky;
    private CameraInterface camera;

    public WaterMasterRenderer(CameraInterface camera, StaticRenderer staticRenderer, EntityRenderer entityRenderer, TerrainRenderer terrainRenderer, SkyMaster sky) {
        this.camera = camera;
        this.waterFbos = new WaterFrameBuffers();
        this.waterHdRenderer = new WaterHdRenderer(this.waterFbos, camera);
        this.waterRenderer = new WaterRenderer();
        this.staticRenderer = staticRenderer;
        this.terrainRenderer = terrainRenderer;
        this.entityRenderer = entityRenderer;
        this.sky = sky;
    }

    public void renderWater(Water water) {
        if (GraphicsOptions.HD_WATER) {
            this.waterHdRenderer.render(water, this.camera);
        } else {
            this.waterRenderer.render(water, this.camera);
        }
    }

    public void updateWaterTextures(Chunk[] chunks, DynamicBatch batch, Water water) {
        if (GraphicsOptions.HD_WATER) {
            GL11.glEnable(12288);
            this.renderWaterRefractionPass(this.camera, chunks, batch, water);
            this.renderWaterReflectionPass(this.camera, chunks, batch, water);
            GL11.glDisable(12288);
        }
    }

    public void cleanUp() {
        this.waterFbos.cleanUp();
        this.waterRenderer.cleanUp();
        this.waterHdRenderer.cleanUp();
    }

    private void renderWaterRefractionPass(CameraInterface camera, Chunk[] chunks, DynamicBatch batch, Water water) {
        Vector4f clipPlane = new Vector4f(0.0f, -1.0f, 0.0f, water.height + 0.3f);
        this.waterFbos.bindRefractionFrameBuffer();
        OpenglUtils.prepareNewRenderPassOnlyDepth();
        this.terrainRenderer.render(chunks, clipPlane, true);
        this.waterFbos.unbindCurrentFrameBuffer();
    }

    private void renderWaterReflectionPass(CameraInterface camera, Chunk[] chunks, DynamicBatch batch, Water water) {
        float height = water.height;
        camera.reflect(height);
        Vector4f clipPlane = new Vector4f(0.0f, 1.0f, 0.0f, -height);
        this.waterFbos.bindReflectionFrameBuffer();
        OpenglUtils.prepareNewRenderPass(EnvironmentVariables.skyColour);
        this.sky.renderSkyBox();
        this.staticRenderer.renderNormalBatches(chunks, clipPlane, false);
        this.entityRenderer.render(batch, camera, clipPlane, false);
        this.terrainRenderer.render(chunks, clipPlane, true);
        this.waterFbos.unbindCurrentFrameBuffer();
        camera.reflect(height);
    }
}


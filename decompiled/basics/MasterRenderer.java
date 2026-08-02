/*
 * Decompiled with CFR 0.152.
 */
package basics;

import basics.CameraInterface;
import basics.EngineMaster;
import batches.DynamicBatch;
import entityRenderers.EntityRenderer;
import entityRenderers.StaticRenderer;
import environment.EnvironmentVariables;
import fontRendering.FontRenderer;
import graphicsOptions.GraphicsOptions;
import guiRendering.GuiRenderData;
import guiRendering.GuiRenderer;
import guis.GuiMaster;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import particles.ParticleMaster;
import postProcessing.EquilinoxPipeline;
import postProcessing.Fbo;
import postProcessing.FboBuilder;
import postProcessing.GaussianBlur;
import postProcessing.PostProcessingPipeline;
import shadows.ShadowBox;
import shadows.ShadowMapMasterRenderer;
import sun.SkyMaster;
import terrainRenderer.TerrainHDRenderer;
import terrainRenderer.TerrainRenderer;
import toolbox.MyKeyboard;
import toolbox.OpenglUtils;
import toolbox.ScreenshotTaker;
import water.Water;
import water.WaterMasterRenderer;
import world.Chunk;

public class MasterRenderer {
    private static final int BLUR_TEXTURE_WIDTH = 384;
    private static final int BLUR_TEXTURE_HEIGHT = 216;
    private static final Matrix4f projectionMatrix = new Matrix4f();
    private static final Matrix4f projectionViewMatrix = new Matrix4f();
    private static EntityRenderer entityRenderer;
    private static StaticRenderer staticRenderer;
    private static TerrainHDRenderer terrainHDRenderer;
    private static TerrainRenderer terrainRenderer;
    private static ShadowMapMasterRenderer shadowMapRenderer;
    private static FontRenderer fontRenderer;
    private static GuiRenderer guiRenderer;
    private static Fbo multisamplingFbo;
    private static Fbo postProcessingFbo;
    private static GaussianBlur gaussianBlur;
    private static PostProcessingPipeline postProcessing;
    private static WaterMasterRenderer waterRenderer;
    private static SkyMaster sky;

    public static void init(CameraInterface camera) {
        MasterRenderer.createProjectionMatrix(camera);
        shadowMapRenderer = new ShadowMapMasterRenderer(camera);
        terrainHDRenderer = new TerrainHDRenderer(shadowMapRenderer);
        terrainRenderer = new TerrainRenderer();
        staticRenderer = new StaticRenderer();
        entityRenderer = new EntityRenderer();
        fontRenderer = new FontRenderer();
        sky = new SkyMaster(projectionMatrix, camera.getViewMatrix());
        guiRenderer = new GuiRenderer();
        gaussianBlur = new GaussianBlur(384, 216);
        postProcessing = new EquilinoxPipeline(sky);
        waterRenderer = new WaterMasterRenderer(camera, staticRenderer, entityRenderer, terrainRenderer, sky);
        multisamplingFbo = Fbo.newFbo(Display.getWidth(), Display.getHeight()).antialias(GraphicsOptions.MSAA_SAMPLES).create();
        postProcessingFbo = Fbo.newFbo(Display.getWidth(), Display.getHeight()).setDepthBuffer(FboBuilder.DepthBufferType.TEXTURE).create();
    }

    public static void render(Chunk[] chunks, DynamicBatch dynamicBatch, Water water, boolean updateScreenshot) {
        CameraInterface camera = EngineMaster.getCamera();
        EnvironmentVariables.getVariables().update();
        Matrix4f.mul(projectionMatrix, camera.getViewMatrix(), projectionViewMatrix);
        sky.updateSunShaftTexture(chunks);
        if (GraphicsOptions.SHADOWS) {
            shadowMapRenderer.render(chunks, dynamicBatch);
        }
        waterRenderer.updateWaterTextures(chunks, dynamicBatch, water);
        MasterRenderer.renderMainRenderPass(camera, chunks, dynamicBatch, water, updateScreenshot);
        MasterRenderer.updateScreenShot(true);
    }

    public static int getOutputTexture() {
        return gaussianBlur.getOutputTexture();
    }

    public static int getOutputNonBlurTexture() {
        return postProcessingFbo.getColourTexture();
    }

    public static void cleanUp() {
        entityRenderer.cleanUp();
        terrainHDRenderer.cleanUp();
        staticRenderer.cleanUp();
        shadowMapRenderer.cleanUp();
        fontRenderer.cleanUp();
        guiRenderer.cleanUp();
        gaussianBlur.cleanUp();
        waterRenderer.cleanUp();
        multisamplingFbo.cleanUp();
        postProcessing.cleanUp();
        postProcessingFbo.cleanUp();
        sky.cleanUp();
    }

    public static Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static Matrix4f getProjectionViewMatrix() {
        return projectionViewMatrix;
    }

    public static void renderGuis() {
        GuiRenderData renderData = GuiMaster.getRenderData();
        GuiRenderData.GuiRenderLevelData[] guiRenderLevelDataArray = renderData.getRenderData();
        int n = guiRenderLevelDataArray.length;
        int n2 = 0;
        while (n2 < n) {
            GuiRenderData.GuiRenderLevelData renderLevel = guiRenderLevelDataArray[n2];
            if (!renderLevel.isEmpty()) {
                guiRenderer.render(renderLevel.getTextures());
                fontRenderer.render(renderLevel.getTexts());
            }
            ++n2;
        }
    }

    protected static ShadowBox getShadowBox() {
        return shadowMapRenderer.getShadowBox();
    }

    protected static void updateShadowBox() {
        shadowMapRenderer.updateShadowBox();
    }

    private static void renderMainRenderPass(CameraInterface camera, Chunk[] chunks, DynamicBatch batch, Water water, boolean updateGuiTexture) {
        MasterRenderer.bindReleventFbo(updateGuiTexture);
        OpenglUtils.prepareNewRenderPass(EnvironmentVariables.horizonColour);
        staticRenderer.renderNormalBatches(chunks, new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), true);
        if (GraphicsOptions.SHADOWS) {
            terrainHDRenderer.render(chunks, camera);
        } else {
            terrainRenderer.render(chunks, new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), false);
        }
        entityRenderer.render(batch, camera, new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), true);
        waterRenderer.renderWater(water);
        staticRenderer.renderClutterBatches(chunks);
        sky.renderSkyBox();
        ParticleMaster.renderParticles(camera);
        sky.doOcclusionTest();
        MasterRenderer.postProcess(updateGuiTexture);
        sky.applyLensFlare();
        if (MyKeyboard.getKeyboard().keyDownEventOccurred(25)) {
            ScreenshotTaker.takeScreenshot(postProcessingFbo);
        }
    }

    private static void bindReleventFbo(boolean updateGuiTexture) {
        if (GraphicsOptions.ANTI_ALIASING) {
            multisamplingFbo.bindFrameBuffer();
        } else if (GraphicsOptions.needsPostProcessing() || updateGuiTexture) {
            postProcessingFbo.bindFrameBuffer();
        }
    }

    private static void postProcess(boolean updateGuiTexture) {
        if (GraphicsOptions.needsPostProcessing()) {
            if (GraphicsOptions.ANTI_ALIASING) {
                multisamplingFbo.unbindFrameBuffer();
                multisamplingFbo.resolveMultisampledFbo(postProcessingFbo);
            } else {
                postProcessingFbo.unbindFrameBuffer();
            }
            postProcessing.carryOutProcessing(postProcessingFbo.getColourTexture(), postProcessingFbo.getDepthTexture(), true);
        } else if (GraphicsOptions.ANTI_ALIASING) {
            if (updateGuiTexture) {
                multisamplingFbo.resolveMultisampledFbo(postProcessingFbo);
                postProcessingFbo.blitToScreen();
            } else {
                multisamplingFbo.blitToScreen();
            }
        } else if (updateGuiTexture) {
            postProcessingFbo.blitToScreen();
        }
    }

    private static void updateScreenShot(boolean update) {
        if (update) {
            gaussianBlur.carryOutProcessing(postProcessingFbo.getColourTexture(), 0, false);
        }
    }

    private static void createProjectionMatrix(CameraInterface camera) {
        float farPlane = camera.getFarPlane();
        float nearPlane = camera.getNearPlane();
        float aspectRatio = (float)Display.getWidth() / (float)Display.getHeight();
        float y_scale = (float)(1.0 / Math.tan(Math.toRadians(camera.getFOV() / 2.0f)));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane;
        MasterRenderer.projectionMatrix.m00 = x_scale;
        MasterRenderer.projectionMatrix.m11 = y_scale;
        MasterRenderer.projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
        MasterRenderer.projectionMatrix.m23 = -1.0f;
        MasterRenderer.projectionMatrix.m32 = -(2.0f * nearPlane * farPlane / frustum_length);
        MasterRenderer.projectionMatrix.m33 = 0.0f;
    }
}


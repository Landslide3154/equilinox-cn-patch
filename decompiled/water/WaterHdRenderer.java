/*
 * Decompiled with CFR 0.152.
 */
package water;

import basics.CameraInterface;
import basics.MasterRenderer;
import environment.EnvironmentVariables;
import gameManaging.GameManager;
import org.lwjgl.opengl.GL11;
import toolbox.OpenglUtils;
import water.Water;
import water.WaterFrameBuffers;
import water.WaterHdShader;

public class WaterHdRenderer {
    private static final int REFLECTION_TEX_UNIT = 1;
    private static final int DEPTH_TEX_UNIT = 2;
    private static final float WAVE_PERIOD = 3.9f;
    private WaterFrameBuffers fbos;
    private WaterHdShader shader;
    private float waveTime = 0.0f;

    public WaterHdRenderer(WaterFrameBuffers fbos, CameraInterface camera) {
        this.fbos = fbos;
        this.shader = new WaterHdShader();
        this.shader.start();
        this.initShader(camera);
        this.shader.stop();
    }

    public void render(Water water, CameraInterface camera) {
        this.prepare(water, camera);
        GL11.glDrawArrays(4, 0, water.getVertexCount());
        this.stopRendering();
    }

    public void cleanUp() {
        this.shader.cleanUp();
    }

    private void prepare(Water water, CameraInterface camera) {
        OpenglUtils.cullBackFaces(true);
        OpenglUtils.antialias(true);
        GL11.glDepthMask(false);
        OpenglUtils.bindVAO(water.getVao(), 0);
        OpenglUtils.enableAlphaBlending();
        this.shader.start();
        this.bindTextures();
        this.shader.viewMatrix.loadMatrix(camera.getViewMatrix());
        this.shader.mistColour.loadVec3(EnvironmentVariables.MIST_COL.getVector());
        this.shader.mistValues.loadVec2(EnvironmentVariables.MIST_VALS);
        this.shader.skyColour.loadVec3(EnvironmentVariables.horizonColour.getVector());
        EnvironmentVariables atmosphere = EnvironmentVariables.getVariables();
        this.shader.lightDirection.loadVec3(atmosphere.getLightDirection());
        this.shader.lightColour.loadVec3(atmosphere.getLightColour().getVector());
        this.shader.cameraPosition.loadVec3(camera.getPosition());
        this.shader.waterHeight.loadFloat(water.height);
        this.updateWaveTime();
    }

    private void stopRendering() {
        OpenglUtils.disableBlending();
        OpenglUtils.unbindVAO(0);
        GL11.glDepthMask(true);
        this.shader.stop();
    }

    private void updateWaveTime() {
        this.waveTime += GameManager.getGameSeconds() * 0.9f;
        this.waveTime %= 3.9f;
        this.shader.waveTime.loadFloat(this.waveTime / 3.9f);
    }

    private void bindTextures() {
        OpenglUtils.bindTextureToBank(this.fbos.getReflectionTexture(), 1);
        OpenglUtils.bindTextureToBank(this.fbos.getRefractionDepthTexture(), 2);
    }

    private void bindTextureUnits() {
        this.shader.reflectionTexture.loadTexUnit(1);
        this.shader.depthTexture.loadTexUnit(2);
    }

    private void initShader(CameraInterface camera) {
        this.shader.projectionMatrix.loadMatrix(MasterRenderer.getProjectionMatrix());
        this.shader.amplitude.loadFloat(0.06f);
        this.shader.worldRadius.loadFloat(50.0f);
        this.shader.fadeOutPeriod.loadFloat(10.0f);
        this.shader.frustumNearFar.loadVec2(camera.getNearPlane(), camera.getFarPlane());
        this.shader.worldCenter.loadVec2(50.0f, 50.0f);
        this.bindTextureUnits();
    }
}


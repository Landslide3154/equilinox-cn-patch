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
import water.WaterShader;

public class WaterRenderer {
    private static final float WAVE_PERIOD = 3.5f;
    private WaterShader shader = new WaterShader();
    private float waveTime = 0.0f;

    public WaterRenderer() {
        this.shader.start();
        this.initShader();
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

    private void stopRendering() {
        OpenglUtils.disableBlending();
        OpenglUtils.unbindVAO(0);
        GL11.glDepthMask(true);
        this.shader.stop();
    }

    private void prepare(Water water, CameraInterface camera) {
        OpenglUtils.cullBackFaces(true);
        OpenglUtils.antialias(true);
        OpenglUtils.bindVAO(water.getVao(), 0);
        OpenglUtils.enableAlphaBlending();
        GL11.glDepthMask(false);
        this.shader.start();
        this.shader.viewMatrix.loadMatrix(camera.getViewMatrix());
        this.shader.mistColour.loadVec3(EnvironmentVariables.MIST_COL.getVector());
        this.shader.mistValues.loadVec2(EnvironmentVariables.MIST_VALS);
        EnvironmentVariables atmosphere = EnvironmentVariables.getVariables();
        this.shader.lightDirection.loadVec3(atmosphere.getLightDirection());
        this.shader.lightColour.loadVec3(atmosphere.getLightColour().getVector());
        this.shader.cameraPosition.loadVec3(camera.getPosition());
        this.shader.waterHeight.loadFloat(water.height);
        this.updateWaveTime();
    }

    private void updateWaveTime() {
        this.waveTime += GameManager.getGameSeconds() * 0.9f;
        this.waveTime %= 3.5f;
        this.shader.waveTime.loadFloat(this.waveTime / 3.5f);
    }

    private void initShader() {
        this.shader.projectionMatrix.loadMatrix(MasterRenderer.getProjectionMatrix());
        this.shader.amplitude.loadFloat(0.06f);
        this.shader.worldCenter.loadVec2(50.0f, 50.0f);
        this.shader.worldRadius.loadFloat(50.0f);
        this.shader.fadeOutPeriod.loadFloat(10.0f);
    }
}


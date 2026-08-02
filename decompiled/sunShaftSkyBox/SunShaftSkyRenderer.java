/*
 * Decompiled with CFR 0.152.
 */
package sunShaftSkyBox;

import basics.EngineMaster;
import basics.Loader;
import basics.MasterRenderer;
import environment.EnvironmentVariables;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import sunShaftSkyBox.SunShaftSkyShader;
import toolbox.Maths;
import toolbox.OpenglUtils;

public class SunShaftSkyRenderer {
    private static final float SIZE = 200.0f;
    private static final float[] VERTICES = new float[]{-200.0f, 200.0f, -200.0f, -200.0f, 200.0f, 200.0f, 200.0f, -200.0f};
    private int vao;
    private SunShaftSkyShader shader;
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f pvMatrix = new Matrix4f();

    public SunShaftSkyRenderer() {
        this.initShader();
        this.vao = Loader.createInterleavedVAO(VERTICES.length / 2, new float[][]{VERTICES});
    }

    public void render(Vector2f sunScreenPos) {
        this.prepare(sunScreenPos);
        GL11.glDrawArrays(5, 0, VERTICES.length / 2);
        this.finishRendering();
    }

    public void cleanUp() {
        this.shader.cleanUp();
    }

    private void prepare(Vector2f sunScreenPos) {
        OpenglUtils.bindVAO(this.vao, 0);
        this.shader.start();
        Maths.createViewMatrix(this.viewMatrix, new Vector3f(0.0f, 0.0f, 0.0f), EngineMaster.getCamera().getPitch(), 0.0f);
        Matrix4f.mul(MasterRenderer.getProjectionMatrix(), this.viewMatrix, this.pvMatrix);
        this.shader.pvMatrix.loadMatrix(this.pvMatrix);
        this.shader.sunPosition.loadVec2(sunScreenPos.x * 2.0f - 1.0f, (sunScreenPos.y * 2.0f - 1.0f) * -1.0f);
    }

    private void finishRendering() {
        OpenglUtils.unbindVAO(0);
        this.shader.stop();
    }

    private void initShader() {
        this.shader = new SunShaftSkyShader();
        this.shader.start();
        this.shader.horizonColour.loadVec3(EnvironmentVariables.horizonColour.getVector());
        this.shader.skyboxSize.loadFloat(200.0f);
        this.shader.stop();
    }
}


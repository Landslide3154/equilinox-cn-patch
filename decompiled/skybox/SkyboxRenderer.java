/*
 * Decompiled with CFR 0.152.
 */
package skybox;

import basics.EngineMaster;
import basics.MasterRenderer;
import environment.EnvironmentVariables;
import gameManaging.GameManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import skybox.CurvedMeshGenerator;
import skybox.SkyboxShader;
import textures.Texture;
import toolbox.Maths;
import toolbox.OpenglUtils;
import utils.FileUtils;
import utils.MyFile;

public class SkyboxRenderer {
    private static final float SIZE = 550.0f;
    private static final float STAR_SPEED = 6.0E-4f;
    private static final int SEG_COUNT = 25;
    private static final MyFile STAR_FILE = new MyFile(FileUtils.RES_FOLDER, "nightSky.png");
    private int vao;
    private SkyboxShader shader;
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f pvMatrix = new Matrix4f();
    private Texture skyTexture;
    private float time = 0.0f;

    public SkyboxRenderer() {
        this.initShader();
        this.vao = new CurvedMeshGenerator(25, 1.5707964f).generateMeshVao();
        this.skyTexture = Texture.newTexture(STAR_FILE).create();
        this.shader.start();
        this.shader.segCount.loadFloat(25.0f);
        this.shader.stop();
    }

    public void render() {
        this.prepare();
        GL11.glDrawArrays(5, 0, 52);
        this.finishRendering();
    }

    public void cleanUp() {
        this.skyTexture.delete();
        this.shader.cleanUp();
    }

    private void prepare() {
        OpenglUtils.bindVAO(this.vao, 0);
        GL11.glDepthMask(false);
        OpenglUtils.bindTextureToBank(this.skyTexture.getID(), 0);
        this.shader.start();
        this.time += GameManager.getGameSeconds() * 6.0E-4f;
        this.time %= 1.0f;
        this.shader.time.loadFloat(this.time);
        this.shader.starBrightness.loadFloat(EnvironmentVariables.starBrightness);
        this.shader.horizonColour.loadVec3(EnvironmentVariables.horizonColour.getVector());
        this.shader.skyColour.loadVec3(EnvironmentVariables.skyColour.getVector());
        this.shader.scroll.loadFloat(EngineMaster.getCamera().getYaw() / (EngineMaster.getCamera().getFOV() * 2.0f));
        Maths.createViewMatrix(this.viewMatrix, new Vector3f(0.0f, 0.0f, 0.0f), EngineMaster.getCamera().getPitch(), 0.0f);
        Matrix4f.mul(MasterRenderer.getProjectionMatrix(), this.viewMatrix, this.pvMatrix);
        this.shader.pvMatrix.loadMatrix(this.pvMatrix);
    }

    private void finishRendering() {
        OpenglUtils.unbindVAO(0);
        GL11.glDepthMask(true);
        this.shader.stop();
    }

    private void initShader() {
        this.shader = new SkyboxShader();
        this.shader.start();
        this.shader.skyboxSize.loadFloat(550.0f);
        this.shader.stop();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package sunShafts;

import basics.EngineMaster;
import basics.MasterRenderer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import postProcessing.Fbo;
import postProcessing.FboBuilder;
import sunShaftSkyBox.SunShaftSkyRenderer;
import sunShafts.ShapeShader;
import sunShafts.ShapeStaticsRenderer;
import sunShafts.ShapeTerrainRenderer;
import world.Chunk;

public class ShapeMapMasterRenderer {
    private Fbo shapeFbo;
    private ShapeShader shader;
    private Matrix4f projectionViewMatrix = new Matrix4f();
    private ShapeStaticsRenderer staticRenderer;
    private ShapeTerrainRenderer terrainRenderer;
    private SunShaftSkyRenderer skybox;

    public ShapeMapMasterRenderer() {
        this.shader = new ShapeShader();
        this.skybox = new SunShaftSkyRenderer();
        this.shapeFbo = Fbo.newFbo(Display.getWidth() / 2, Display.getHeight() / 2).setDepthBuffer(FboBuilder.DepthBufferType.NONE).create();
        this.staticRenderer = new ShapeStaticsRenderer(this.shader, this.projectionViewMatrix);
        this.terrainRenderer = new ShapeTerrainRenderer(this.projectionViewMatrix, this.shader);
    }

    public void render(Chunk[] chunks, Vector2f sunScreenPos) {
        this.prepare();
        this.skybox.render(sunScreenPos);
        this.shader.start();
        this.terrainRenderer.render(chunks);
        this.staticRenderer.render(chunks);
        this.shader.stop();
        this.finish();
    }

    public void cleanUp() {
        this.shader.cleanUp();
        this.skybox.cleanUp();
        this.shapeFbo.cleanUp();
    }

    public int getShapeTexture() {
        return this.shapeFbo.getColourTexture();
    }

    private void prepare() {
        Matrix4f.mul(MasterRenderer.getProjectionMatrix(), EngineMaster.getCamera().getViewMatrix(), this.projectionViewMatrix);
        this.shapeFbo.bindFrameBuffer();
        GL11.glClear(16384);
        GL11.glDisable(2929);
    }

    private void finish() {
        GL11.glEnable(2929);
        this.shapeFbo.unbindFrameBuffer();
    }
}


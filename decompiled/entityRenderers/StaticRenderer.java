/*
 * Decompiled with CFR 0.152.
 */
package entityRenderers;

import basics.EngineMaster;
import basics.MasterRenderer;
import batches.StaticBatch;
import entityRenderers.StaticShader;
import environment.EnvironmentVariables;
import gameManaging.GameManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import toolbox.OpenglUtils;
import world.Chunk;

public class StaticRenderer {
    private static final float WIND_SPEED = 0.12f;
    private float time = 0.0f;
    private StaticShader shader = new StaticShader();

    public StaticRenderer() {
        this.shader.start();
        this.initShader();
        this.shader.stop();
    }

    public void renderNormalBatches(Chunk[] chunks, Vector4f clipPlane, boolean hdRender) {
        this.prepare(clipPlane, hdRender);
        this.shader.alpha.loadFloat(1.0f);
        Chunk[] chunkArray = chunks;
        int n = chunks.length;
        int n2 = 0;
        while (n2 < n) {
            Chunk chunk = chunkArray[n2];
            StaticBatch staticBatch = chunk.getStaticBatch();
            if (chunk.isVisible() && !staticBatch.isEmpty()) {
                this.renderStaticBatch(staticBatch);
            }
            ++n2;
        }
        this.shader.stop();
    }

    public void renderClutterBatches(Chunk[] chunks) {
        this.prepare(new Vector4f(), true);
        OpenglUtils.enableAlphaBlending();
        Chunk[] chunkArray = chunks;
        int n = chunks.length;
        int n2 = 0;
        while (n2 < n) {
            Chunk chunk = chunkArray[n2];
            if (chunk.isClutterVisible() && !chunk.getClutterBatch().isEmpty()) {
                this.shader.alpha.loadFloat(chunk.getClutterAlpha());
                this.renderStaticBatch(chunk.getClutterBatch());
            }
            ++n2;
        }
        OpenglUtils.disableBlending();
        this.shader.stop();
    }

    public void cleanUp() {
        this.shader.cleanUp();
    }

    private void renderStaticBatch(StaticBatch batch) {
        OpenglUtils.bindVAO(batch.getVao(), 0, 1, 2);
        GL11.glDrawArrays(4, 0, batch.getVertexCount());
        OpenglUtils.unbindVAO(0, 1, 2);
    }

    private void initShader() {
        this.shader.projectionMatrix.loadMatrix(MasterRenderer.getProjectionMatrix());
        this.shader.worldCenter.loadVec2(50.0f, 50.0f);
        this.shader.worldRadius.loadFloat(50.0f);
        this.shader.fadeOutPeriod.loadFloat(10.0f);
    }

    private void prepare(Vector4f clipPlane, boolean hdRender) {
        this.time += GameManager.getGameSeconds() * 0.12f;
        this.time %= 1.0f;
        OpenglUtils.cullBackFaces(true);
        OpenglUtils.antialias(hdRender);
        this.shader.start();
        this.shader.time.loadFloat(this.time);
        this.shader.mistColour.loadVec3(EnvironmentVariables.MIST_COL.getVector());
        this.shader.mistValues.loadVec2(EnvironmentVariables.MIST_VALS);
        this.shader.clipPlane.loadVec4(clipPlane);
        this.shader.viewMatrix.loadMatrix(EngineMaster.getCamera().getViewMatrix());
        EnvironmentVariables atmosphere = EnvironmentVariables.getVariables();
        this.shader.lightDirection.loadVec3(atmosphere.getLightDirection());
        this.shader.lightColour.loadVec3(atmosphere.getLightColour().getVector());
        this.shader.lightBias.loadVec2(atmosphere.getAmbientWeighting(), atmosphere.getDiffuseWeighting());
    }
}


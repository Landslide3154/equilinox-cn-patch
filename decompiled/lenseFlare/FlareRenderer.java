/*
 * Decompiled with CFR 0.152.
 */
package lenseFlare;

import basics.DisplayManager;
import basics.Loader;
import guiRendering.GuiShader;
import lenseFlare.FlareTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.OpenglUtils;
import toolbox.Query;

public class FlareRenderer {
    private static final float[] POSITIONS = new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    private static final float TEST_BOX_WIDTH = 0.04f;
    private static final float TEST_BOX_HEIGHT = 0.04f * (float)Display.getWidth() / (float)Display.getHeight();
    private static final float TOTAL_SAMPLES = (float)Math.pow(0.04f * (float)Display.getWidth(), 2.0) * 4.0f;
    private GuiShader shader;
    private int vao;
    private Query query = new Query(35092);
    private float coverage = 1.0f;

    protected FlareRenderer() {
        this.shader = new GuiShader();
        this.vao = Loader.createInterleavedVAO(POSITIONS, 2);
        this.initShader();
    }

    protected void render(FlareTexture[] flares, Vector3f[] data, float brightness) {
        this.prepare(brightness);
        this.shader.alpha.loadFloat(brightness * this.coverage);
        int i = 0;
        while (i < flares.length) {
            this.renderFlare(flares[i], data[i]);
            ++i;
        }
        this.endRendering();
    }

    protected void cleanUp() {
        this.query.delete();
        this.shader.cleanUp();
    }

    private void prepare(float brightness) {
        OpenglUtils.antialias(false);
        OpenglUtils.enableAdditiveBlending();
        OpenglUtils.disableDepthTesting();
        this.shader.start();
        OpenglUtils.bindVAO(this.vao, 0);
    }

    private void renderFlare(FlareTexture flare, Vector3f data) {
        if (!flare.texture.isLoaded()) {
            return;
        }
        OpenglUtils.bindTextureToBank(flare.texture.getID(), 0);
        float scale = this.getActualSize(data.z) * flare.relativeScale;
        float yScale = scale * DisplayManager.getAspectRatio();
        this.shader.transform.loadVec4(data.x - scale / 2.0f, data.y - yScale / 2.0f, scale, yScale);
        GL11.glDrawArrays(5, 0, 4);
    }

    public void doOcclusionTest(Vector2f pos) {
        if (pos == null) {
            this.coverage = 0.0f;
            return;
        }
        OpenglUtils.antialias(false);
        this.shader.start();
        OpenglUtils.bindVAO(this.vao, 0);
        OpenglUtils.enableDepthTesting();
        GL11.glDepthMask(false);
        GL11.glColorMask(false, false, false, false);
        OpenglUtils.bindTextureToBank(0, 0);
        this.shader.transform.loadVec4(pos.x - 0.02f, pos.y - TEST_BOX_HEIGHT / 2.0f, 0.04f, TEST_BOX_HEIGHT);
        if (this.query.isResultReady()) {
            int result = this.query.getResult();
            this.coverage = Math.min((float)result / TOTAL_SAMPLES, 1.0f);
        }
        if (!this.query.isInUse()) {
            this.query.start();
            GL11.glDrawArrays(5, 0, 4);
            this.query.end();
        }
        GL11.glDepthMask(true);
        GL11.glColorMask(true, true, true, true);
        OpenglUtils.unbindVAO(0);
        this.shader.stop();
    }

    private void endRendering() {
        OpenglUtils.unbindVAO(0);
        this.shader.stop();
        OpenglUtils.disableBlending();
        OpenglUtils.enableDepthTesting();
    }

    private void initShader() {
        this.shader.start();
        this.shader.overrideColour.loadVec3(1.0f, 1.0f, 1.0f);
        this.shader.useOverrideColour.loadBoolean(false);
        this.shader.flipTexture.loadBoolean(false);
        this.shader.alpha.loadFloat(1.0f);
        this.shader.stop();
    }

    private float getActualSize(float sizeFactor) {
        return 0.07f + 0.33f * Math.abs(sizeFactor);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package postProcessing;

import basics.Loader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import postProcessing.Fbo;
import postProcessing.FboBuilder;
import shaders.ShaderProgram;
import toolbox.Colour;
import toolbox.OpenglUtils;

public class PostProcessingFilter {
    private static final float[] POSITIONS = new float[]{-1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f};
    private static final int VERTEX_COUNT = POSITIONS.length / 2;
    private static final int vao = Loader.createInterleavedVAO(POSITIONS, 2);
    private Fbo fbo;
    private ShaderProgram shader;

    public PostProcessingFilter(ShaderProgram shader, int width, int height, boolean alpha) {
        this.shader = shader;
        this.fbo = Fbo.newFbo(width, height).withAlphaChannel(alpha).setDepthBuffer(FboBuilder.DepthBufferType.NONE).create();
    }

    public PostProcessingFilter(ShaderProgram shader) {
        this.shader = shader;
        this.fbo = Fbo.newFbo(Display.getWidth(), Display.getHeight()).create();
    }

    public int applyFilter(boolean renderToScreen, int ... inputTextures) {
        this.prepare(renderToScreen, inputTextures);
        GL11.glDrawArrays(5, 0, VERTEX_COUNT);
        this.finishRendering(renderToScreen);
        return this.fbo.getColourTexture();
    }

    public int getOutputTexture() {
        return this.fbo.getColourTexture();
    }

    public void blitToScreen() {
        this.fbo.blitToScreen();
    }

    public void cleanUp() {
        this.fbo.cleanUp();
        this.shader.cleanUp();
    }

    protected void prepareShader(ShaderProgram shader) {
    }

    private void prepare(boolean renderToScreen, int ... inputTextures) {
        if (!renderToScreen) {
            this.fbo.bindFrameBuffer();
        }
        OpenglUtils.prepareNewRenderPass(new Colour(1.0f, 1.0f, 1.0f));
        this.shader.start();
        OpenglUtils.antialias(false);
        OpenglUtils.disableDepthTesting();
        OpenglUtils.bindVAO(vao, 0);
        int i = 0;
        while (i < inputTextures.length) {
            OpenglUtils.bindTextureToBank(inputTextures[i], i);
            ++i;
        }
        this.prepareShader(this.shader);
    }

    private void finishRendering(boolean renderToScreen) {
        OpenglUtils.unbindVAO(0);
        this.shader.stop();
        OpenglUtils.disableBlending();
        OpenglUtils.enableDepthTesting();
        if (!renderToScreen) {
            this.fbo.unbindFrameBuffer();
        }
    }
}


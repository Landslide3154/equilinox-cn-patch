/*
 * Decompiled with CFR 0.152.
 */
package postProcessing;

import postProcessing.PostProcessingFilter;
import postProcessing.PostProcessingPipeline;
import shaders.ShaderProgram;
import shaders.UniformFloat;
import utils.MyFile;

public class GaussianBlur
implements PostProcessingPipeline {
    private final PostProcessingFilter verticalBlur;
    private final PostProcessingFilter horizontalBlur;

    public GaussianBlur(int width, int height) {
        this.verticalBlur = new PostProcessingFilter(new VerticalBlurShader(height), width, height, false);
        this.horizontalBlur = new PostProcessingFilter(new HorizontalBlurShader(width), width, height, false);
    }

    @Override
    public int getOutputTexture() {
        return this.horizontalBlur.getOutputTexture();
    }

    @Override
    public void carryOutProcessing(int colourTexture, int depthTexture, boolean renderToScreen) {
        int vertBlurred = this.verticalBlur.applyFilter(false, colourTexture);
        this.horizontalBlur.applyFilter(false, vertBlurred);
        if (renderToScreen) {
            this.horizontalBlur.blitToScreen();
        }
    }

    @Override
    public void cleanUp() {
        this.verticalBlur.cleanUp();
        this.horizontalBlur.cleanUp();
    }

    private static class HorizontalBlurShader
    extends ShaderProgram {
        private static final MyFile HBLUR_V_SHADER = new MyFile("postProcessing", "horizontalBlurVertex.glsl");
        private static final MyFile HBLUR_F_SHADER = new MyFile("postProcessing", "blurFragment.glsl");
        private UniformFloat width = new UniformFloat("width");

        public HorizontalBlurShader(float widthValue) {
            super(HBLUR_V_SHADER, HBLUR_F_SHADER, new String[0]);
            super.storeAllUniformLocations(this.width);
            this.start();
            this.width.loadFloat(widthValue);
            this.stop();
        }
    }

    private static class VerticalBlurShader
    extends ShaderProgram {
        private static final MyFile VBLUR_V_SHADER = new MyFile("postProcessing", "verticalBlurVertex.glsl");
        private static final MyFile VBLUR_F_SHADER = new MyFile("postProcessing", "blurFragment.glsl");
        private UniformFloat height = new UniformFloat("height");

        public VerticalBlurShader(float heightValue) {
            super(VBLUR_V_SHADER, VBLUR_F_SHADER, new String[0]);
            super.storeAllUniformLocations(this.height);
            this.start();
            this.height.loadFloat(heightValue);
            this.stop();
        }
    }
}


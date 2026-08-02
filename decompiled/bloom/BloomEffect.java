/*
 * Decompiled with CFR 0.152.
 */
package bloom;

import bloom.BloomCombineFilter;
import postProcessing.GaussianBlur;
import postProcessing.PostProcessingFilter;
import postProcessing.PostProcessingPipeline;
import shaders.ShaderProgram;
import utils.MyFile;

public class BloomEffect
implements PostProcessingPipeline {
    private static final MyFile BRIGHT_V_SHADER = new MyFile("postProcessing", "basicVertex.glsl");
    private static final MyFile BRIGHT_F_SHADER = new MyFile("bloom", "brightPassFragment.glsl");
    private PostProcessingFilter brightFilter;
    private GaussianBlur gaussian;
    private BloomCombineFilter combineFilter;

    public BloomEffect(int blurWidth, int blurHeight) {
        this.brightFilter = new PostProcessingFilter(new ShaderProgram(BRIGHT_V_SHADER, BRIGHT_F_SHADER, new String[0]), blurWidth, blurHeight, false);
        this.gaussian = new GaussianBlur(blurWidth, blurHeight);
        this.combineFilter = new BloomCombineFilter();
    }

    @Override
    public void carryOutProcessing(int colourTexture, int depthTexture, boolean renderToScreen) {
        this.brightFilter.applyFilter(false, colourTexture);
        this.gaussian.carryOutProcessing(this.brightFilter.getOutputTexture(), depthTexture, false);
        this.combineFilter.applyFilter(renderToScreen, colourTexture, this.gaussian.getOutputTexture());
    }

    @Override
    public int getOutputTexture() {
        return this.combineFilter.getOutputTexture();
    }

    @Override
    public void cleanUp() {
        this.brightFilter.cleanUp();
    }
}


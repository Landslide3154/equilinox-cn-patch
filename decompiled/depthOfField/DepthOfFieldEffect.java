/*
 * Decompiled with CFR 0.152.
 */
package depthOfField;

import depthOfField.CombineFilter;
import postProcessing.GaussianBlur;
import postProcessing.PostProcessingFilter;
import postProcessing.PostProcessingPipeline;

public class DepthOfFieldEffect
implements PostProcessingPipeline {
    private PostProcessingPipeline gaussianBlur;
    private PostProcessingFilter combineFilter;

    public DepthOfFieldEffect(int blurWidth, int blurHeight) {
        this.gaussianBlur = new GaussianBlur(blurWidth, blurHeight);
        this.combineFilter = new CombineFilter();
    }

    @Override
    public void carryOutProcessing(int colourTexture, int depthTexture, boolean renderToScreen) {
        this.gaussianBlur.carryOutProcessing(colourTexture, depthTexture, false);
        this.combineFilter.applyFilter(renderToScreen, colourTexture, this.gaussianBlur.getOutputTexture(), depthTexture);
    }

    @Override
    public void cleanUp() {
        this.gaussianBlur.cleanUp();
        this.combineFilter.cleanUp();
    }

    @Override
    public int getOutputTexture() {
        return this.combineFilter.getOutputTexture();
    }

    public int getBlurredTexture() {
        return this.gaussianBlur.getOutputTexture();
    }
}


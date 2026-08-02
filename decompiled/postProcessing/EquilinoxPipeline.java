/*
 * Decompiled with CFR 0.152.
 */
package postProcessing;

import depthOfField.DepthOfFieldEffect;
import environment.EnvironmentVariables;
import graphicsOptions.GraphicsOptions;
import postProcessing.PostProcessingFilter;
import postProcessing.PostProcessingPipeline;
import sun.SkyMaster;
import sunShafts.ShaftCombineFilter;

public class EquilinoxPipeline
implements PostProcessingPipeline {
    private static final int DOF_BLUR_WIDTH = 960;
    private static final int DOF_BLUR_HEIGHT = 540;
    private DepthOfFieldEffect depthOfField;
    private PostProcessingFilter shaftCombineFilter;
    private SkyMaster sky;
    private int blurredImage;

    public EquilinoxPipeline(SkyMaster sky) {
        this.sky = sky;
        this.depthOfField = new DepthOfFieldEffect(960, 540);
        this.shaftCombineFilter = new ShaftCombineFilter();
    }

    @Override
    public void carryOutProcessing(int colourTexture, int depthTexture, boolean renderToScreen) {
        boolean renderRays;
        int currentOutput = colourTexture;
        boolean bl = renderRays = EnvironmentVariables.getVariables().isSunVisible() && GraphicsOptions.SUN_RAYS;
        if (renderRays) {
            this.shaftCombineFilter.applyFilter(!GraphicsOptions.DOF_EFFECT, currentOutput, this.sky.getSunShaftTexture());
            currentOutput = this.shaftCombineFilter.getOutputTexture();
        }
        if (GraphicsOptions.DOF_EFFECT) {
            this.depthOfField.carryOutProcessing(currentOutput, depthTexture, true);
            currentOutput = this.depthOfField.getOutputTexture();
            this.blurredImage = this.depthOfField.getBlurredTexture();
        }
    }

    @Override
    public int getOutputTexture() {
        return this.blurredImage;
    }

    @Override
    public void cleanUp() {
        this.depthOfField.cleanUp();
    }
}


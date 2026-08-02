/*
 * Decompiled with CFR 0.152.
 */
package motionBlur;

import postProcessing.PostProcessingPipeline;

public class MotionBlurEffect
implements PostProcessingPipeline {
    @Override
    public void carryOutProcessing(int colourTexture, int depthTexture, boolean renderToScreen) {
    }

    @Override
    public int getOutputTexture() {
        return 0;
    }

    @Override
    public void cleanUp() {
    }
}


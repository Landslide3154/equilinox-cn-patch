/*
 * Decompiled with CFR 0.152.
 */
package bloom;

import postProcessing.PostProcessingFilter;
import shaders.ShaderProgram;
import shaders.UniformSampler;
import utils.MyFile;

public class BloomCombineFilter
extends PostProcessingFilter {
    public BloomCombineFilter() {
        super(new BloomCombineShader());
    }

    private static class BloomCombineShader
    extends ShaderProgram {
        private static final MyFile COMBINE_V_SHADER = new MyFile("postProcessing", "basicVertex.glsl");
        private static final MyFile COMBINE_F_SHADER = new MyFile("bloom", "combineFragment.glsl");
        private UniformSampler originalTexture = new UniformSampler("originalTexture");
        private UniformSampler blurredTexture = new UniformSampler("bloomTexture");

        public BloomCombineShader() {
            super(COMBINE_V_SHADER, COMBINE_F_SHADER, new String[0]);
            super.storeAllUniformLocations(this.originalTexture, this.blurredTexture);
            super.start();
            this.originalTexture.loadTexUnit(0);
            this.blurredTexture.loadTexUnit(1);
            super.stop();
        }
    }
}


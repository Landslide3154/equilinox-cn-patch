/*
 * Decompiled with CFR 0.152.
 */
package depthOfField;

import basics.EngineMaster;
import postProcessing.PostProcessingFilter;
import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformSampler;
import utils.MyFile;

public class CombineFilter
extends PostProcessingFilter {
    public CombineFilter() {
        super(new CombineShader());
    }

    @Override
    protected void prepareShader(ShaderProgram shader) {
        float focusDistance = EngineMaster.getCamera().getAimDistance();
        ((CombineShader)shader).aimDistance.loadFloat(focusDistance);
    }

    private static class CombineShader
    extends ShaderProgram {
        private static final MyFile COMBINE_V_SHADER = new MyFile("postProcessing", "basicVertex.glsl");
        private static final MyFile COMBINE_F_SHADER = new MyFile("depthOfField", "combineFragment.glsl");
        private UniformSampler originalTexture = new UniformSampler("originalTexture");
        private UniformSampler blurredTexture = new UniformSampler("blurredTexture");
        private UniformSampler depthTexture = new UniformSampler("depthTexture");
        private UniformFloat aimDistance = new UniformFloat("aimDistance");

        public CombineShader() {
            super(COMBINE_V_SHADER, COMBINE_F_SHADER, new String[0]);
            super.storeAllUniformLocations(this.originalTexture, this.blurredTexture, this.depthTexture, this.aimDistance);
            super.start();
            this.originalTexture.loadTexUnit(0);
            this.blurredTexture.loadTexUnit(1);
            this.depthTexture.loadTexUnit(2);
            super.stop();
        }
    }
}


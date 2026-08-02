/*
 * Decompiled with CFR 0.152.
 */
package sunShafts;

import environment.EnvironmentVariables;
import postProcessing.PostProcessingFilter;
import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformSampler;
import utils.MyFile;

public class ShaftCombineFilter
extends PostProcessingFilter {
    public ShaftCombineFilter() {
        super(new ShaftCombineShader());
    }

    @Override
    protected void prepareShader(ShaderProgram shader) {
        ((ShaftCombineShader)shader).brightness.loadFloat(EnvironmentVariables.getSunEffectBrightness());
    }

    private static class ShaftCombineShader
    extends ShaderProgram {
        private static final MyFile V_SHADER = new MyFile("postProcessing", "basicVertex.glsl");
        private static final MyFile F_SHADER = new MyFile("sunShafts", "shaftCombineFragment.glsl");
        private UniformSampler originalTexture = new UniformSampler("originalTexture");
        private UniformSampler shaftTexture = new UniformSampler("shaftTexture");
        private UniformFloat brightness = new UniformFloat("brightness");

        public ShaftCombineShader() {
            super(V_SHADER, F_SHADER, new String[0]);
            super.storeAllUniformLocations(this.originalTexture, this.shaftTexture, this.brightness);
            super.start();
            this.originalTexture.loadTexUnit(0);
            this.shaftTexture.loadTexUnit(1);
            super.stop();
        }
    }
}


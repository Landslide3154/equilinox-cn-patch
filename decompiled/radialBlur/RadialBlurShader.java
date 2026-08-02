/*
 * Decompiled with CFR 0.152.
 */
package radialBlur;

import shaders.ShaderProgram;
import shaders.UniformVec2;
import utils.MyFile;

public class RadialBlurShader
extends ShaderProgram {
    private static final MyFile VBLUR_V_SHADER = new MyFile("radialBlur", "radialBlurVertex.glsl");
    private static final MyFile VBLUR_F_SHADER = new MyFile("radialBlur", "radialBlurFragment.glsl");
    protected UniformVec2 lightPositionOnScreen = new UniformVec2("lightPositionOnScreen");

    public RadialBlurShader() {
        super(VBLUR_V_SHADER, VBLUR_F_SHADER, new String[0]);
        super.storeAllUniformLocations(this.lightPositionOnScreen);
    }
}


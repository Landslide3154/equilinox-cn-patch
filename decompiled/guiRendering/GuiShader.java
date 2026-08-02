/*
 * Decompiled with CFR 0.152.
 */
package guiRendering;

import shaders.ShaderProgram;
import shaders.UniformBoolean;
import shaders.UniformFloat;
import shaders.UniformSampler;
import shaders.UniformVec3;
import shaders.UniformVec4;
import utils.MyFile;

public class GuiShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("guiRendering", "guiVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("guiRendering", "guiFragment.glsl");
    public UniformVec4 transform = new UniformVec4("transform");
    public UniformVec3 overrideColour = new UniformVec3("overrideColour");
    public UniformBoolean useOverrideColour = new UniformBoolean("useOverrideColour");
    public UniformFloat alpha = new UniformFloat("alpha");
    public UniformBoolean flipTexture = new UniformBoolean("flipTexture");
    public UniformBoolean usesBlur = new UniformBoolean("usesBlur");
    public UniformSampler blurTexture = new UniformSampler("blurTexture");
    public UniformSampler guiTexture = new UniformSampler("guiTexture");

    public GuiShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.transform, this.alpha, this.flipTexture, this.overrideColour, this.useOverrideColour, this.blurTexture, this.guiTexture, this.usesBlur);
        super.start();
        this.guiTexture.loadTexUnit(0);
        this.blurTexture.loadTexUnit(1);
        super.stop();
    }
}


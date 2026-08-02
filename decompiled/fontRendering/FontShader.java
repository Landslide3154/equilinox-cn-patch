/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

import shaders.ShaderProgram;
import shaders.UniformSampler;
import shaders.UniformVec2;
import shaders.UniformVec3;
import shaders.UniformVec4;
import utils.MyFile;

public class FontShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("fontRendering", "fontVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("fontRendering", "fontFragment.glsl");
    protected UniformVec4 colour = new UniformVec4("colour");
    protected UniformSampler fontTexture = new UniformSampler("fontTexture");
    protected UniformVec3 transform = new UniformVec3("transform");
    protected UniformVec3 borderColour = new UniformVec3("borderColour");
    protected UniformVec2 borderSizes = new UniformVec2("borderSizes");
    protected UniformVec2 edgeData = new UniformVec2("edgeData");

    public FontShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.colour, this.fontTexture, this.transform, this.borderColour, this.borderSizes, this.edgeData);
    }
}


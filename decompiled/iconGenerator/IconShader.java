/*
 * Decompiled with CFR 0.152.
 */
package iconGenerator;

import shaders.ShaderProgram;
import shaders.UniformMatrix;
import shaders.UniformVec2;
import shaders.UniformVec3;
import utils.MyFile;

public class IconShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("profile3d", "profileVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("profile3d", "profileFragment.glsl");
    protected UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
    protected UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
    protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
    protected UniformVec3 lightColour = new UniformVec3("lightColour");
    protected UniformVec2 lightBias = new UniformVec2("lightingBias");
    protected UniformVec3 material = new UniformVec3("material");

    public IconShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.modelMatrix, this.projectionViewMatrix, this.lightDirection, this.lightColour, this.lightBias, this.material);
    }
}


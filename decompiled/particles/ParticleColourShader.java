/*
 * Decompiled with CFR 0.152.
 */
package particles;

import shaders.ShaderProgram;
import shaders.UniformMatrix;
import shaders.UniformVec3;
import utils.MyFile;

public class ParticleColourShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("particles", "particleColourVShader.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("particles", "particleColourFShader.glsl");
    protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    protected UniformVec3 lighting = new UniformVec3("lighting");

    public ParticleColourShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.projectionMatrix, this.lighting);
    }
}


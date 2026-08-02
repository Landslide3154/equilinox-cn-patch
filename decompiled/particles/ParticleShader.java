/*
 * Decompiled with CFR 0.152.
 */
package particles;

import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformVec3;
import utils.MyFile;

public class ParticleShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("particles", "particleVShader.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("particles", "particleFShader.glsl");
    protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    protected UniformFloat numberOfRows = new UniformFloat("numberOfRows");
    protected UniformVec3 lighting = new UniformVec3("lighting");

    public ParticleShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.projectionMatrix, this.numberOfRows, this.lighting);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package shadows;

import shaders.ShaderProgram;
import shaders.UniformMatrix;
import utils.MyFile;

public class ShadowShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("shadows", "shadowVertexShader.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("shadows", "shadowFragmentShader.glsl");
    protected UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
    protected UniformMatrix projectionView = new UniformMatrix("projectionView");

    public ShadowShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position");
        super.storeAllUniformLocations(this.modelMatrix, this.projectionView);
    }
}


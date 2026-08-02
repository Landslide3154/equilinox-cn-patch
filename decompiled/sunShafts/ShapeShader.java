/*
 * Decompiled with CFR 0.152.
 */
package sunShafts;

import shaders.ShaderProgram;
import shaders.UniformMatrix;
import utils.MyFile;

public class ShapeShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("sunShafts", "shapeVertexShader.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("sunShafts", "shapeFragmentShader.glsl");
    protected UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
    protected UniformMatrix projectionView = new UniformMatrix("projectionView");

    public ShapeShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.modelMatrix, this.projectionView);
    }
}


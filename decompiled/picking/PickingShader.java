/*
 * Decompiled with CFR 0.152.
 */
package picking;

import shaders.ShaderProgram;
import shaders.Uniform;
import utils.MyFile;

public class PickingShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("picking", "pickingVertex.txt");
    private static final MyFile FRAGMENT_SHADER = new MyFile("picking", "pickingFragment.txt");

    public PickingShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(new Uniform[0]);
    }
}


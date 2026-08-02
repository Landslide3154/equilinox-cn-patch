/*
 * Decompiled with CFR 0.152.
 */
package sunShaftSkyBox;

import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformVec2;
import shaders.UniformVec3;
import utils.MyFile;

public class SunShaftSkyShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("sunShaftSkyBox", "SunShaftSkyVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("sunShaftSkyBox", "SunShaftSkyFragment.glsl");
    protected UniformMatrix pvMatrix = new UniformMatrix("pvMatrix");
    protected UniformVec2 sunPosition = new UniformVec2("sunPosition");
    protected UniformVec3 horizonColour = new UniformVec3("horizonColour");
    protected UniformFloat skyboxSize = new UniformFloat("skyboxSize");

    public SunShaftSkyShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.pvMatrix, this.horizonColour, this.skyboxSize, this.sunPosition);
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package skybox;

import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import shaders.UniformVec3;
import utils.MyFile;

public class SkyboxShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("skybox", "skyVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("skybox", "skyFragment.glsl");
    protected UniformMatrix pvMatrix = new UniformMatrix("pvMatrix");
    protected UniformVec3 horizonColour = new UniformVec3("horizonColour");
    protected UniformVec3 skyColour = new UniformVec3("skyColour");
    protected UniformFloat skyboxSize = new UniformFloat("skyboxSize");
    protected UniformFloat starBrightness = new UniformFloat("starBrightness");
    protected UniformFloat scroll = new UniformFloat("scroll");
    protected UniformFloat time = new UniformFloat("time");
    protected UniformSampler nightSky = new UniformSampler("nightSky");
    protected UniformFloat segCount = new UniformFloat("segCount");

    public SkyboxShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.pvMatrix, this.horizonColour, this.skyboxSize, this.skyColour, this.starBrightness, this.nightSky, this.scroll, this.time, this.segCount);
    }
}

